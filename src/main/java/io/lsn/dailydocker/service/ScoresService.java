package io.lsn.dailydocker.service;

import io.lsn.dailydocker.controller.DefaultController;
import io.lsn.dailydocker.dao.SearchesMapper;
import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.dictionary.SearchParameter;
import io.lsn.dailydocker.domain.ScoresParser;
import org.apache.log4j.Logger;
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

    @Autowired
    private ScoresParser parser;

    @Autowired
    private SearchesMapper searchesMapper;

    private static final Logger logger = Logger.getLogger(DefaultController.class);

    public ScoresService() {
    }

    @Autowired
    public ScoresService(ScoresParser parser, SearchesMapper searchesMapper) {
        this.parser = parser;
        this.searchesMapper = searchesMapper;
    }

    private int getIdOfScore(List<Score> scores, String date) {
        return scores.stream()
                .filter(score -> score.getDate().equalsIgnoreCase(date))
                .findFirst().get()
                .getIndex();
    }

    private void sortNumbersByOccurrenceDesc(List<Number> numbers) {
        numbers.sort(new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return o2.getOccurrence().compareTo(o1.getOccurrence());
            }
        });
    }

    private void sortNumbersByOccurrenceAsc(List<Number> numbers) {
        numbers.sort(new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return o1.getOccurrence().compareTo(o2.getOccurrence());
            }
        });
    }

    public List<String> getURLListOfDatesOfDraws() {
        logger.info("method getURLListOfDatesOfDraws");
        searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfDatesOfDraws", null, null, false));
        return parser.parseURLScores().stream()
                .map(score -> score.getDate()).collect(Collectors.toList());
    }

    public String[] getURLScoreForSpecificDate(String date) {
        logger.info("method getURLScoreForSpecificDate");
        searchesMapper.saveSearchParameter(new SearchParameter("service.getURLScoreForSpecificDate", "requestFor", date, false));
        return parser.parseURLScores().stream()
                .filter(score -> score.getDate().equalsIgnoreCase(date))
                .findFirst().get()
                .getNumbers();
    }

    public List<Number> getURLListOfNumbersWithOccurrence(Integer number, boolean asc) {
        logger.info("method getURLListOfNumbersWithOccurrence");
        List<Number> numbers = parser.parseURLNumbers();
        if(asc) {
            this.sortNumbersByOccurrenceAsc(numbers);
        } else {
            this.sortNumbersByOccurrenceDesc(numbers);
        }
        if (number == null || number == 0) {
            searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfNumbersWithOccurrence", "badRequest", "badRequest", asc));
            return numbers;
        }
        if (number < 1 || number > 49) {
            searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfNumbersWithOccurrence", "badRequest", "badRequest", asc));
            return new ArrayList<>();
        }

        searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfNumbersWithOccurrence", null, null, asc));
        return numbers.stream().limit(number).collect(Collectors.toList());
    }

    public List<Number> getURLListOfNumbersWithOccurrenceForSpecificDates(String beginning, String end, boolean asc) {
        logger.info("method getURLListOfNumbersWithOccurrenceForSpecificDates");
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

        searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfNumbersWithOccurrenceForSpecificDates", beginning, end, asc));
        return numbers;
    }

    public List<Score> getURLListOfScoresForSpecificDates(String beginning, String end) {
        logger.info("method getURLListOfScoresForSpecificDates");
        List<Score> scores = parser.parseURLScores();
        if (beginning != null && end != null && !beginning.equalsIgnoreCase(end)
                && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(beginning))
                && scores.stream().anyMatch(score -> score.getDate().equalsIgnoreCase(end))
                && getIdOfScore(scores, beginning) < getIdOfScore(scores, end)) {

            searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfScoresForSpecificDates", beginning, end, false));
            return scores.stream()
                    .filter(score -> score.getIndex() >= getIdOfScore(scores, beginning) && score.getIndex() <= getIdOfScore(scores, end))
                    .collect(Collectors.toList());
        }

        searchesMapper.saveSearchParameter(new SearchParameter("service.getURLListOfScoresForSpecificDates", "badRequest", "badRequest", false));
        return new ArrayList<>();
    }

    public void saveURLScoresForSpecificDateToFile(String beginning, String end) throws IOException {
        logger.info("method saveURLScoresForSpecificDateToFile");
        List<Score> scores = getURLListOfScoresForSpecificDates(beginning, end);
        parser.saveURLScoresToFile(scores);
        searchesMapper.saveSearchParameter(new SearchParameter("service.saveURLScoresForSpecificDateToFile", beginning, end, false));
    }

    public List<SearchParameter> getSearchesFromDB() {
        logger.info("method getSearchesFromDB");
        return searchesMapper.getSearches();
    }

    public void truncateSearchesTable() {
        logger.info("method truncateSearchesTable");
        searchesMapper.truncateSearchesTable();
    }
}