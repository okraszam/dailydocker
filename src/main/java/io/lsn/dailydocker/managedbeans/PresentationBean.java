package io.lsn.dailydocker.managedbeans;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.domain.ScoresParser;
import io.lsn.dailydocker.service.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class PresentationBean extends SpringBeanAutowiringSupport implements Serializable {

    @Autowired
    private ScoresParser parser = new ScoresParser();

    @Autowired
    private ScoresService service;

    private String startDate;

    private String endDate;

    private List<Number> numbers;

    public PresentationBean() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Number> numbers) {
        this.numbers = numbers;
    }

    public List<String> getURLListOfDatesOfDraws() {
        return service.getURLListOfDatesOfDraws();
    }

    public void assignNumbers(String startDate, String endtDate) {
        numbers = service.getURLListOfNumbersWithOccurrenceForSpecificDates(startDate, endtDate, false);
    }
}
