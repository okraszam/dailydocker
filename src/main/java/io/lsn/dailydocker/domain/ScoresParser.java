package io.lsn.dailydocker.domain;

import io.lsn.dailydocker.dao.ScoresMapper;
import io.lsn.dailydocker.dao.SearchesMapper;
import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.dictionary.SearchParameter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScoresParser {

    private static final Logger logger = Logger.getLogger(ScoresParser.class);
    private static final String lottoArchivalScoresPath = "http://www.mbnet.com.pl/dl.txt";
    private static final String lottoLatestScorePath = "http://app.lotto.pl/wyniki/?type=dl";

    @Autowired
    private ScoresMapper scoresMapper;

    @Autowired
    private SearchesMapper searchesMapper;

    public List<Score> parseURLScores() {
        logger.info("method parseURLScores");
        List<Score> scores = new ArrayList<>();
        parseArchivalScores(scores);
        parseLatestScore(scores);
        scoresMapper.truncateScoresTable();
        scoresMapper.insertURLScores(scores);
        return scores;
    }

    public List<Number> parseURLNumbers() {
        logger.info("method parseURLNumbers");
        List<Number> numbers = new ArrayList<>();
        parseListOfNumbersWithOccurrence(buildListOfScoresArrays(parseURLScores()), numbers);
        return numbers;
    }

    private void parseArchivalScores(List<Score> lottoScores) {
        try (BufferedReader scoresDataStream = new BufferedReader(new InputStreamReader(new URL(lottoArchivalScoresPath).openStream()))) {
            String tempString;
            while((tempString = scoresDataStream.readLine()) != null) {
                String date = tempString.split(" ")[1].split("\\.")[2] + "."
                        + tempString.split(" ")[1].split("\\.")[1] + "."
                        + tempString.split(" ")[1].split("\\.")[0];
                lottoScores.add(new Score(Integer.valueOf(tempString.replaceAll("\\D.*", "")),
                        date,
                        tempString.replaceAll("[\\d]*. [\\d]{2}.[\\d]{2}.[\\d]{4} ","").trim().split(",")));
            }
        } catch (IOException e) {
            logger.error("Something went wrong with Lotto archival scores file.\n" + e.toString());
        }
    }

    private void parseLatestScore(List<Score> scores) {
        try (BufferedReader scoresDataStream = new BufferedReader(new InputStreamReader(new URL(lottoLatestScorePath).openStream()))) {
            Score score = new Score();
            String tempString;
            String[] numbers = new String[6];
            int i = 0;
            while((tempString = scoresDataStream.readLine()) != null) {
                if (tempString.length() > 2) {
                    String[] date = tempString.split("-");
                    score.setIndex(scores.size() + 1);
                    score.setDate(date[0] + "." + date[1] + "." + date[2]);
                } else {
                    numbers[i] = tempString;
                    i++;
                }
            }
            score.setNumbers(numbers);
            if(!doesListContainsThisScore(scores, score)) {
                scores.add(score);
            }
        } catch (IOException e) {
            logger.error("Something went wrong with Lotto latest score file.\n" + e.toString());
        }
    }

    private void parseListOfNumbersWithOccurrence(List<String[]> scoresArrays, List<Number> numbers) {
        List<Integer> duplicatedNumbers = new ArrayList<>();
        List<Integer> numbersOccurrence = new ArrayList<>();

        for(String[] array : scoresArrays) {
            for(String number : array) {
                duplicatedNumbers.add(Integer.valueOf(number));
            }
        }

        for(int i = 0; i < 49; i++) {
            numbersOccurrence.add(i, Collections.frequency(duplicatedNumbers, i+1));
        }

        for(int i = 0; i < 49; i++) {
            numbers.add(i, new Number(i+1, numbersOccurrence.get(i)));
        }
    }

    public List<String[]> buildListOfScoresArrays(List<Score> scores) {
        return scores.stream().map(score -> score.getNumbers()).collect(Collectors.toList());
    }

    private boolean doesListContainsThisScore(List<Score> scores, Score score) {
        return scores.stream().anyMatch(s -> s.getDate().equalsIgnoreCase(score.getDate()));
    }

    public List<String> getListOfArchivedScoreFiles() {
        logger.info("method getListOfArchivedScoreFiles");
        List<String> listOfFiles = new ArrayList<>();
        try {
            Files.newDirectoryStream(Paths.get("/usr/local/tomcat/scores"), path -> path.toFile().isFile())
                    .forEach(file -> listOfFiles.add(file.getFileName().toString()));
        } catch (Exception e) {
            logger.error("Something went wrong with listing files in app resources.\n" + e.toString());
            return new ArrayList<>();
        }
        searchesMapper.saveSearchParameter(new SearchParameter("parser.getListOfArchivedScoreFiles", null, null, false));
        return listOfFiles;
    }

    public boolean checkIfFileExistInResources(String fileNameWithExtension) {
        logger.info("method checkIfFileExistInResources");
        searchesMapper.saveSearchParameter(new SearchParameter("parser.checkIfFileExistInResources", null, null, false));
        return getListOfArchivedScoreFiles().stream().anyMatch(file -> file.equalsIgnoreCase(fileNameWithExtension));
    }

    public void saveURLScoresToFile(List<Score> scores) throws IOException {
        if (scores == null || scores.isEmpty()) {
            scores = parseURLScores();
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formatted = now.format(formatter);

        Path path = Paths.get("/usr/local/tomcat/scores/urlScores_" + formatted + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            scores.stream()
                    .forEach(score -> {
                        try {
                            writer.write(String.valueOf(score.getIndex())
                                    + ". " + score.getDate()
                                    + " " + Arrays.asList(score.getNumbers()).stream().collect(Collectors.joining(",")));
                            writer.newLine();
                        } catch (Exception e) {
                            logger.error("Something went wrong with saving latest scores to file.\n" + e.toString());
                        }
                    });
        }
        searchesMapper.saveSearchParameter(new SearchParameter("parser.saveURLScoresToFile", null, null, false));
    }

    public void cleanResourcesFolder() {
        logger.info("method cleanResourcesFolder");
        List<String> fileNames = getListOfArchivedScoreFiles();
        List<File> files = new ArrayList<>();

        fileNames.stream()
                .forEach(fileName -> files.add(new File("/usr/local/tomcat/scores/" + fileName)));

        files.stream().forEach(file -> file.delete());
        searchesMapper.saveSearchParameter(new SearchParameter("parser.cleanResourcesFolder", null, null, false));
    }

    public List<Score> getScoresFromDB() {
        logger.info("method getScoresFromDB");
        return scoresMapper.getScoresFromDB();
    }

    public void truncateScoresTable() {
        logger.info("method truncateScoresTable");
        scoresMapper.truncateScoresTable();
    }
}
