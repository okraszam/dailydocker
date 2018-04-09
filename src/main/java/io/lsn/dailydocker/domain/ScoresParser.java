package io.lsn.dailydocker.domain;

import io.lsn.dailydocker.dictionary.LottoScore;
import io.lsn.dailydocker.dictionary.SingleNumber;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Component
public class ScoresParser {

    private static final Logger logger = Logger.getLogger(ScoresParser.class);
    private static final String lottoArchivalScoresPath = "http://www.mbnet.com.pl/dl.txt";
    private static final String lottoLatestScorePath = "http://app.lotto.pl/wyniki/?type=dl";

    public List<LottoScore> parse() {
        List<LottoScore> lottoScores = new ArrayList<>();
        parseArchivalScores(lottoScores);
        parseLatestScore(lottoScores);
        return lottoScores;
    }

    public List<SingleNumber> getNumbers() {
        List<SingleNumber> singleNumbers = new ArrayList<>();
        numbersProbabilityOrganizer(buildScoresCollection(parse()));
        return singleNumbers;
    }

    private void parseArchivalScores(List<LottoScore> lottoScores) {
        try (BufferedReader scoresDataStream = new BufferedReader(new InputStreamReader(new URL(lottoArchivalScoresPath).openStream()))) {
            String tempString;
            while((tempString = scoresDataStream.readLine()) != null) {
                lottoScores.add(new LottoScore(Integer.valueOf(tempString.replaceAll("\\D.*", "")),
                        tempString.split(" ")[1],
                        tempString.replaceAll("[\\d]*. [\\d]{2}.[\\d]{2}.[\\d]{4} ","").trim().split(",")));
            }
        } catch (IOException e) {
            logger.error("Something went wrong with Lotto archival scores file.\n" + e.toString());
        }
    }

    private void parseLatestScore(List<LottoScore> lottoScores) {
        try (BufferedReader scoresDataStream = new BufferedReader(new InputStreamReader(new URL(lottoLatestScorePath).openStream()))) {
            LottoScore score = new LottoScore();
            String tempString;
            String[] numbers = new String[6];
            int i = 0;
            while((tempString = scoresDataStream.readLine()) != null) {
                if (tempString.length() > 2) {
                    String[] date = tempString.split("-");
                    score.setIndex(lottoScores.size() + 1);
                    score.setDate(date[2] + "." + date[1] + "." + date[0]);
                } else {
                    numbers[i] = tempString;
                    i++;
                }
            }
            score.setNumbers(numbers);
            if(!doesListContainsThisScore(lottoScores, score)) {
                lottoScores.add(score);
            }
        } catch (IOException e) {
            logger.error("Something went wrong with Lotto latest score file.\n" + e.toString());
        }
    }

    private boolean doesListContainsThisScore(List<LottoScore> lottoScores, LottoScore lottoScore) {
        return lottoScores.stream().allMatch(score -> score.getDate().equalsIgnoreCase(lottoScore.getDate()));
    }

    private List<String> numbersProbabilityOrganizer (List<String[]> scoresCollection) {
        List<Integer> collectionOfNumbers = new ArrayList<>();
        List<Integer> scoresProbability = new ArrayList<>();
        List<String> sortedProbabilityTemp = new ArrayList<>();

        for(String[] scoreArray : scoresCollection) {
            for(String number : scoreArray) {
                collectionOfNumbers.add(Integer.valueOf(number));
            }
        }

        for(int i = 0; i < 49; i++) {
            scoresProbability.add(i, Collections.frequency(collectionOfNumbers, i+1));
        }

        for(int i = 0; i < 49; i++) {
            sortedProbabilityTemp.add(i, i+1 + " -> " + String.valueOf(scoresProbability.get(i)));
        }

        sortedProbabilityTemp.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                int firstOccurency = Integer.valueOf(o1.replaceAll("[\\d]* -> ", ""));
                int secondOccurency = Integer.valueOf(o2.replaceAll("[\\d]* -> ", ""));

                if(firstOccurency < secondOccurency) {
                    return 1;
                } else if (firstOccurency > secondOccurency) {
                    return -1;
                }
                return 0;
            }
        });

        return sortedProbabilityTemp;
    }

    private List<String[]> buildScoresCollection (List<LottoScore> lottoScores) {

        List<String[]> scoresCollection = new ArrayList<>();
        lottoScores.forEach(lottoScore -> scoresCollection.add(lottoScore.getNumbers()));

        return scoresCollection;
    }

}
