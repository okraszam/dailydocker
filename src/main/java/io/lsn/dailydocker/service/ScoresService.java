package io.lsn.dailydocker.service;

import io.lsn.dailydocker.dictionary.LottoScore;
import io.lsn.dailydocker.domain.ScoresParser;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoresService {

    private static int smallestNumber = 1;
    private static int biggestNumber = 49;
    private static List<Integer> collectionOfNumbers = new ArrayList<>();
    private static List<Integer> scoresProbability = new ArrayList<>();
    private static List<String> sortedProbabilityTemp = new ArrayList<>();
    private ScoresParser scoresParser = new ScoresParser();
    private static List<LottoScore> lottoScores;

    public ScoresService() {
    }

//    @Autowired
//    public ScoresService(ScoresParser scoresParser) {
//        this.scoresParser = scoresParser;
//        this.lottoScores = scoresParser.parseScores();
//    }

    public List<String> showMostFrequentNumbers (int numberBetween1and49) {

        lottoScores = scoresParser.parse();
        List<String> sortedProbability = new ArrayList<>();
        List<String> bestOfTheBest = new ArrayList<>();

        sortedProbability.addAll(numbersProbabilityOrganizer(buildScoresCollection(lottoScores)));
        if(numberBetween1and49 > biggestNumber || numberBetween1and49 < smallestNumber) {
            bestOfTheBest.add("Please choose different number.");
            return bestOfTheBest;
        } else {
            for (int i = 0; i < numberBetween1and49; i++) {
                bestOfTheBest.add(sortedProbabilityTemp.get(i));
            }
            return bestOfTheBest;
        }
    }

    private List<String> numbersProbabilityOrganizer (List<String[]> scoresCollection) {

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

    public String showLatestLottoScores (int numberOfPastScores) {

        StringBuilder latestScoresBuilder = new StringBuilder();
        for(int i = 0; i < numberOfPastScores; i++) {
            latestScoresBuilder.append(lottoScores.get(lottoScores.size() - 1 - i) + "\n");
        }

        return latestScoresBuilder.toString();
    }

    private List<String[]> buildScoresCollection (List<LottoScore> lottoScores) {

        List<String[]> scoresCollection = new ArrayList<>();
        lottoScores.forEach(lottoScore -> scoresCollection.add(lottoScore.getNumbers()));

        return scoresCollection;
    }

}
