package io.lsn.dailydocker.dictionary;

public class Number {

    private int number;
    private Integer occurrence;

    public Number() {
    }

    public Number(int number, int occurency) {
        this.number = number;
        this.occurrence = occurency;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Integer occurrence) {
        this.occurrence = occurrence;
    }
}
