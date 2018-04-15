package io.lsn.dailydocker.dao;

import io.lsn.dailydocker.dictionary.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface ScoresMapper {

    @Insert("insert into scores(index,date,numbersString)" +
            "values" +
            "<foreach collection=\"list\" item=\"score\" index=\"index\" separator=\",\">" +
            "(#{score.index},#{score.date},#{score.numbersString})" +
            "</foreach>")
    void insertURLScores(List<Score> scores);

    @Select("select * from scores")
    List<Score> getScoresFromDB();

    @Delete("truncate table scores")
    void truncateScoresTable();

}
