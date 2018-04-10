package io.lsn.dailydocker.service;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.domain.ScoresParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoresService {

    private ScoresParser parser;

    public ScoresService() {
    }

    @Autowired
    public ScoresService(ScoresParser parser) {
        this.parser = parser;
    }

    public List<String> getURLListOfDatesOfDraws() {
        return parser.parseScores().stream().map(score -> score.getDate()).collect(Collectors.toList());
    }

    public List<Number> getURLListOfNumbersWithOccurrence(Integer number, boolean isLowest) {
        List<Number> numbers = parser.parseNumbers();
        if(isLowest) {
            parser.sortNumbersByOccurrenceAsc(numbers);
        } else {
            parser.sortNumbersByOccurrenceDesc(numbers);
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
        List<Score> scores = parser.parseScores();
        if (beginning == null && end == null) {
            return scores;
        }
        if (beginning == null && end != null && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(end))) {
            return scores.subList(0, getIndexOfScore(scores, end));
        }
        if (beginning != null && end == null && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(beginning))) {
            return scores.subList(getIndexOfScore(scores, beginning), scores.size()-1);
        }
        if (beginning != null && end != null && !beginning.equalsIgnoreCase(end)
                && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(beginning))
                && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(end))) {
            return scores.subList(getIndexOfScore(scores, beginning)-1, getIndexOfScore(scores, end)-1);
        }

        return new ArrayList<>();
    }

    public List<Number> getURLListOfNumbersWithOccurrenceForSpecificDates(String beginning, String end, boolean isLowest) {
        List<Score> scores = getURLListOfScoresForSpecificDates(beginning, end);
        List<String[]> scoresArrays = scores.stream().map(score -> score.getNumbers()).collect(Collectors.toList());
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

        numbers = numbers.stream().filter(number -> number.getOccurrence() > 0).collect(Collectors.toList());

        if (isLowest) {
            parser.sortNumbersByOccurrenceAsc(numbers);
        } else {
            parser.sortNumbersByOccurrenceDesc(numbers);
        }

        return numbers;
    }

    private int getIndexOfScore(List<Score> scores, String date) {
        return scores.stream()
                     .filter(score -> score.getDate().equalsIgnoreCase(date))
                     .collect(Collectors.toList())
                     .get(0).getIndex();
    }

    public boolean checkChosenDates(String beggining, String end) {
        int begginingValue = 0;
        int endValue = 0;
        if (beggining == null && end != null) {
            return true;
        }
        if (beggining != null && end == null) {
            return true;
        }
        if (beggining != null && end != null && beggining.equalsIgnoreCase(end)) {
            return false;
        }
        begginingValue = Integer.parseInt(beggining.replace(".", ""));
        endValue = Integer.parseInt(end.replace(".", ""));
        if (begginingValue > endValue) {
            return false;
        }

        return true;
    }
}
