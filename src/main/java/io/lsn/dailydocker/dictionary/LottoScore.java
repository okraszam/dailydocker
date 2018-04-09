package io.lsn.dailydocker.dictionary;

public class LottoScore {

    private int index;
    private String date;
    private String[] numbers;

    public LottoScore() {}

    public LottoScore(int index, String date, String[] numbers) {
        this.index = index;
        this.date = date;
        this.numbers = numbers;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
}
