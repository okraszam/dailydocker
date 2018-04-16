package io.lsn.dailydocker.controller;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.dictionary.SearchParameter;
import io.lsn.dailydocker.domain.ScoresParser;
import io.lsn.dailydocker.service.ScoresService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/default")
public class DefaultController {

    private static final Logger logger = Logger.getLogger(DefaultController.class);

    @Autowired
    private ScoresParser parser;

    @Autowired
    private ScoresService service;

    @RequestMapping(value = "/getURLListOfDatesOfDraws", method = RequestMethod.GET)
    public List<String> getURLListOfDatesOfDraws() {
        return service.getURLListOfDatesOfDraws();
    }

    @RequestMapping(value = "/getURLScoreForSpecificDate", method = RequestMethod.GET)
    public String[] getURLScoreForSpecificDate(@RequestParam("date") String date) {
        return service.getURLScoreForSpecificDate(date);
    }

    @RequestMapping(value = "/getURLListOfNumbersWithOccurrence", method = RequestMethod.GET)
    public List<Number> getURLListOfNumbersWithOccurrence(@RequestParam("number") Integer number, @RequestParam("asc") boolean asc) {
        return service.getURLListOfNumbersWithOccurrence(number, asc);
    }

    @RequestMapping(value = "/getURLListOfNumbersWithOccurrenceForSpecificDates", method = RequestMethod.GET)
    public List<Number> getURLListOfNumbersWithOccurrenceForSpecificDates(@RequestParam("beginning") String beginning, @RequestParam("end") String end, @RequestParam("asc") boolean asc) {
        return service.getURLListOfNumbersWithOccurrenceForSpecificDates(beginning, end, asc);
    }

    @RequestMapping(value = "/getURLListOfScoresForSpecificDates", method = RequestMethod.GET)
    public List<Score> getURLListOfScoresForSpecificDates(@RequestParam("beginning") String beginning, @RequestParam("end") String end) {
        return service.getURLListOfScoresForSpecificDates(beginning, end);
    }

    @RequestMapping(value = "/saveURLScoresForSpecificDateToFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveURLScoresForSpecificDateToFile(@RequestBody SearchParameter parameter) throws IOException {
        service.saveURLScoresForSpecificDateToFile(parameter.getBeginning(), parameter.getEnd());
    }



    @RequestMapping(value = "/parseURLScores", method = RequestMethod.GET)
    public List<Score> parseURLScores() {
        return parser.parseURLScores();
    }

    @RequestMapping(value = "/parseURLNumbers", method = RequestMethod.GET)
    public List<Number> parseURLNumbers() {
        return parser.parseURLNumbers();
    }

    @RequestMapping(value = "/getListOfArchivedScoreFiles", method = RequestMethod.GET)
    public List<String> getListOfArchivedScoreFiles() {
        return parser.getListOfArchivedScoreFiles();
    }

    @RequestMapping(value = "/checkIfFileExistInResources", method = RequestMethod.GET)
    public boolean checkIfFileExistInResources(@RequestParam("fileNameWithExtension") String fileNameWithExtension) {
        return parser.checkIfFileExistInResources(fileNameWithExtension);
    }
}
