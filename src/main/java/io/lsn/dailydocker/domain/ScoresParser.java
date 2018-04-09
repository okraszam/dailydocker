package io.lsn.dailydocker.domain;

import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.dictionary.Number;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScoresParser {

    private static final Logger logger = Logger.getLogger(ScoresParser.class);
    private static final String lottoArchivalScoresPath = "http://www.mbnet.com.pl/dl.txt";
    private static final String lottoLatestScorePath = "http://app.lotto.pl/wyniki/?type=dl";

    /**
     *
     * @return Zwracana jest kolekcja wyników losowań wraz z datami i id
     */
    public List<Score> parseScores() {
        List<Score> scores = new ArrayList<>();
        parseArchivalScores(scores);
        parseLatestScore(scores);
        return scores;
    }

    /**
     *
     * @return Zwracana jest kolekcja liczb od 1 do 49 wraz z ilością ich wystąpień
     */
    public List<Number> parseNumbers() {
        List<Number> numbers = new ArrayList<>();
        parseListOfNumbersWithOccurrence(buildListOfScoresArrays(parseScores()), numbers);
        return numbers;
    }

    private void parseArchivalScores(List<Score> lottoScores) {
        try (BufferedReader scoresDataStream = new BufferedReader(new InputStreamReader(new URL(lottoArchivalScoresPath).openStream()))) {
            String tempString;
            while((tempString = scoresDataStream.readLine()) != null) {
                lottoScores.add(new Score(Integer.valueOf(tempString.replaceAll("\\D.*", "")),
                        tempString.split(" ")[1],
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
                    score.setDate(date[2] + "." + date[1] + "." + date[0]);
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

    private boolean doesListContainsThisScore(List<Score> scores, Score score) {
        return scores.stream().allMatch(s -> s.getDate().equalsIgnoreCase(score.getDate()));
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

    private List<String[]> buildListOfScoresArrays(List<Score> scores) {
        return scores.stream().map(score -> score.getNumbers()).collect(Collectors.toList());
    }

}
