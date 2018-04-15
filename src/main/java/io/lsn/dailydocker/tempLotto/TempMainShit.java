package io.lsn.dailydocker.tempLotto;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.domain.ScoresParser;
import io.lsn.dailydocker.service.ScoresService;


import java.io.IOException;
import java.util.List;

public class TempMainShit {

    public static void main(String[] args) throws IOException {
        ScoresParser parser = new ScoresParser();
        List<Number> numbers = parser.parseURLNumbers();
        numbers.get(2);

        parser.getListOfArchivedScoreFiles();
        parser.saveURLScoresToFile(null);


        ScoresService service = new ScoresService();
        service.getURLListOfDatesOfDraws();
        service.getURLListOfNumbersWithOccurrenceForSpecificDates(service.getURLListOfDatesOfDraws().get(5), service.getURLListOfDatesOfDraws().get(1000), false);

        String beginning = service.getURLListOfDatesOfDraws().get(0);
        String end = service.getURLListOfDatesOfDraws().get(4);

        service.saveURLScoresForSpecificDateToFile(beginning, end);


    }

}
