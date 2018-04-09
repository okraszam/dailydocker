package io.lsn.dailydocker.tempLotto;

import io.lsn.dailydocker.dictionary.Number;
import io.lsn.dailydocker.domain.ScoresParser;


import java.util.List;

public class TempMainShit {

    public static void main(String[] args) {
        ScoresParser parser = new ScoresParser();
        List<Number> numbers = parser.parseNumbers();
        numbers.get(2);
    }

}
