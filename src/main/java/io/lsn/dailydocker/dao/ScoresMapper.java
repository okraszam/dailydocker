package io.lsn.dailydocker.dao;

import io.lsn.dailydocker.dictionary.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface ScoresMapper {

    @Insert({"<script>" +
            "insert into scores(`index`,date,numbersString)" +
            "VALUES <foreach item='score' collection='list' separator=','>" +
            "(#{score.index},#{score.date},#{score.numbersString})" +
            "</foreach>" +
            "</script>"})
    void insertURLScores(List<Score> scores);

    @Select("select * from scores")
    List<Score> getScoresFromDB();

    @Delete("truncate table scores")
    void truncateScoresTable();

}
