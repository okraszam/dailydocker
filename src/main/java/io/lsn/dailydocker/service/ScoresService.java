package io.lsn.dailydocker.service;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.domain.ScoresParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoresService {

    private ScoresParser parser = new ScoresParser();

    public ScoresService() {
    }

    @Autowired
    public ScoresService(ScoresParser parser) {
        this.parser = parser;
    }

    private int getIdOfScore(List<Score> scores, String date) {
        return scores.stream()
                .filter(score -> score.getDate().equalsIgnoreCase(date))
                .findFirst().get()
                .getIndex();
    }

    public void sortNumbersByOccurrenceDesc(List<Number> numbers) {
        numbers.sort(new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return o1.getOccurrence().compareTo(o2.getOccurrence());
            }
        });
    }

    public void sortNumbersByOccurrenceAsc(List<Number> numbers) {
        numbers.sort(new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return o2.getOccurrence().compareTo(o1.getOccurrence());
            }
        });
    }

    public List<String> getURLListOfDatesOfDraws() {
        return parser.parseURLScores().stream()
                .map(score -> score.getDate()).collect(Collectors.toList());
    }

    public String[] getURLScoreForSpecificDate(String date) {
        return parser.parseURLScores().stream()
                .filter(score -> score.getDate().equalsIgnoreCase(date))
                .findFirst().get()
                .getNumbers();
    }

    public List<Number> getURLListOfNumbersWithOccurrence(Integer number, boolean asc) {
        List<Number> numbers = parser.parseURLNumbers();
        if(asc) {
            this.sortNumbersByOccurrenceAsc(numbers);
        } else {
            this.sortNumbersByOccurrenceDesc(numbers);
        }
        if (number == null || number == 0) {
            return numbers;
        }
        if (number < 1 && number > 49) {
            return new ArrayList<>();
        }

        return numbers.stream().limit(number-1).collect(Collectors.toList());
    }

    public List<Score> getURLListOfScoresForSpecificDates(String beginning, String end) {
        List<Score> scores = parser.parseURLScores();
        if (beginning != null && end != null && !beginning.equalsIgnoreCase(end)
                && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(beginning))
                && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(end))
                && getIdOfScore(scores, beginning) < getIdOfScore(scores, end)) {
            return scores.stream()
                    .filter(score -> score.getIndex() >= getIdOfScore(scores, beginning) && score.getIndex() <= getIdOfScore(scores, end))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public void saveURLScoresForSpecificDateToFile(String beginning, String end) throws IOException {
        List<Score> scores = getURLListOfScoresForSpecificDates(beginning, end);
        parser.saveURLScoresToFile(scores);
    }

    public List<Number> getURLListOfNumbersWithOccurrenceForSpecificDates(String beginning, String end, boolean asc) {
        List<Score> scores = getURLListOfScoresForSpecificDates(beginning, end);
        List<String[]> scoresArrays = parser.buildListOfScoresArrays(scores);
        List<Number> numbers = new ArrayList<>();
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

        numbers = numbers.stream()
                .filter(number -> number.getOccurrence() > 0)
                .collect(Collectors.toList());

        if (asc) {
            this.sortNumbersByOccurrenceAsc(numbers);
        } else {
            this.sortNumbersByOccurrenceDesc(numbers);
        }

        return numbers;
    }
}