package io.lsn.dailydocker.controller;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.dictionary.Score;
import io.lsn.dailydocker.dictionary.SearchParameter;
import io.lsn.dailydocker.domain.ScoresParser;
import io.lsn.dailydocker.service.ScoresService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/default")
public class DefaultController {

    private static final Logger logger = Logger.getLogger(DefaultController.class);

    @Autowired
    private ScoresParser parser;

    @Autowired
    private ScoresService service;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String greeter() {
        logger.info("method greeter");
        return "Greetings Dear User";
    }

    @RequestMapping(value = "/getURLListOfDatesOfDraws", method = RequestMethod.GET)
    public List<String> getURLListOfDatesOfDraws() {
        List<String> list = new ArrayList<>();
        try {
            list = service.getURLListOfDatesOfDraws();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/getURLScoreForSpecificDate", method = RequestMethod.GET)
    public String[] getURLScoreForSpecificDate(@RequestParam("date") String date) {
        String[] array;
        try {
            array = service.getURLScoreForSpecificDate(date);
        } catch (java.lang.Throwable e) {
            logger.error(e);
            return new String[0];
        }
        return array;
    }

    @RequestMapping(value = "/getURLListOfNumbersWithOccurrence", method = RequestMethod.GET)
    public List<Number> getURLListOfNumbersWithOccurrence(@RequestParam("number") Integer number, @RequestParam("asc") boolean asc) {
        List<Number> list = new ArrayList<>();
        try {
            list = service.getURLListOfNumbersWithOccurrence(number, asc);
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/getURLListOfNumbersWithOccurrenceForSpecificDates", method = RequestMethod.GET)
    public List<Number> getURLListOfNumbersWithOccurrenceForSpecificDates(@RequestParam("beginning") String beginning, @RequestParam("end") String end, @RequestParam("asc") boolean asc) {
        List<Number> list = new ArrayList<>();
        try {
            list = service.getURLListOfNumbersWithOccurrenceForSpecificDates(beginning, end, asc);
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/getURLListOfScoresForSpecificDates", method = RequestMethod.GET)
    public List<Score> getURLListOfScoresForSpecificDates(@RequestParam("beginning") String beginning, @RequestParam("end") String end) {
        List<Score> list = new ArrayList<>();
        try {
            list = service.getURLListOfScoresForSpecificDates(beginning, end);
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/saveURLScoresForSpecificDateToFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveURLScoresForSpecificDateToFile(@RequestBody SearchParameter parameter) throws IOException {
        try {
            service.saveURLScoresForSpecificDateToFile(parameter.getBeginning(), parameter.getEnd());
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
    }

    @RequestMapping(value = "/getSearchesFromDB", method = RequestMethod.GET)
    public List<SearchParameter> getSearchesFromDB() {
        List<SearchParameter> list = new ArrayList<>();
        try {
            list = service.getSearchesFromDB();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/truncateSearchesTable", method = RequestMethod.POST)
    public void truncateSearchesTable() {
        try {
            service.truncateSearchesTable();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
    }



    @RequestMapping(value = "/parseURLScores", method = RequestMethod.GET)
    public List<Score> parseURLScores() {
        List<Score> list = new ArrayList<>();
        try {
            list = parser.parseURLScores();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/parseURLNumbers", method = RequestMethod.GET)
    public List<Number> parseURLNumbers() {
        List<Number> list = new ArrayList<>();
        try {
            list = parser.parseURLNumbers();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/getListOfArchivedScoreFiles", method = RequestMethod.GET)
    public List<String> getListOfArchivedScoreFiles() {
        List<String> list = new ArrayList<>();
        try {
            list = parser.getListOfArchivedScoreFiles();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/checkIfFileExistInResources", method = RequestMethod.GET)
    public boolean checkIfFileExistInResources(@RequestParam("fileNameWithExtension") String fileNameWithExtension) {
        boolean value;
        try {
            value = parser.checkIfFileExistInResources(fileNameWithExtension);
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return false;
    }

    @RequestMapping(value = "/cleanResourcesFolder", method = RequestMethod.POST)
    public void cleanResourcesFolder() {
        try {
            parser.cleanResourcesFolder();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
    }

    @RequestMapping(value = "/getScoresFromDB", method = RequestMethod.GET)
    public List<Score> getScoresFromDB() {
        List<Score> list = new ArrayList<>();
        try {
            list = parser.getScoresFromDB();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
        return list;
    }

    @RequestMapping(value = "/truncateScoresTable", method = RequestMethod.POST)
    public void truncateScoresTable() {
        try {
            parser.truncateScoresTable();
        } catch (java.lang.Throwable e) {
            logger.error(e);
        }
    }

}
