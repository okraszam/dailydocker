package io.lsn.dailydocker.tempLotto;

import io.lsn.dailydocker.service.ScoresService;

public class TempMainShit {

    public static void main(String[] args) {
        ScoresService scoresService = new ScoresService();
        scoresService.showMostFrequentNumbers(7);
    }

}
