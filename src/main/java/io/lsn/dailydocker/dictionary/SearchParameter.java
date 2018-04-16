package io.lsn.dailydocker.dictionary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SearchParameter {

    private String searchType;
    private String beginning;
    private String end;
    private String searchDate;
    private boolean asc;

    public SearchParameter() {
    }

    public SearchParameter(String searchType, String beginning, String end, boolean asc) {
        if (searchType == null) {
            this.searchType = "";
        } else {
            this.searchType = searchType;
        }

        if (beginning == null) {
            this.beginning = "";
        } else {
            this.beginning = beginning;
        }

        if (end == null) {
            this.end = "";
        } else {
            this.end = end;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        this.searchDate = now.format(formatter);

        this.asc = asc;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getBeginning() {
        return beginning;
    }

    public void setBeginning(String beginning) {
        this.beginning = beginning;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public boolean getAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

}
