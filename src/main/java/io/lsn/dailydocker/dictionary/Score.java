package io.lsn.dailydocker.dictionary;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Score {

    private int index;
    private String date;
    private String[] numbers;
    private String numbersString;

    public Score() {}

    public Score(int index, String date, String[] numbers) {
        this.index = index;
        this.date = date;
        this.numbers = numbers;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int position) {
        this.index = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getNumbers() {
        return numbers;
    }

    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    public String getNumbersString() {
        return Arrays.asList(this.numbers).stream().collect(Collectors.joining(","));
    }

    public void setNumbersString(String numbersString) {
        this.numbersString = numbersString;
        if (this.numbers == null || this.numbers.length == 0) {
            this.numbers = numbersString.split(",");
        }
    }
}
