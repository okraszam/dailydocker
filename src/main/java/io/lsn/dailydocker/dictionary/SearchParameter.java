package io.lsn.dailydocker.dictionary;

public class SearchParameter {

    private String searchType;
    private String beginning;
    private String end;
    private String searchDate;
    private boolean asc;

    public SearchParameter() {
    }

    public SearchParameter(String searchType, String beginning, String end, String searchDate, boolean asc) {
        this.searchType = searchType;
        this.beginning = beginning;
        this.end = end;
        this.searchDate = searchDate;
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
