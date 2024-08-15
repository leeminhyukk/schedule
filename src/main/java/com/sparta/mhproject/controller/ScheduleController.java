package com.sparta.mhproject.controller;

import com.sparta.mhproject.dto.RequestDto;
import com.sparta.mhproject.dto.ResponseDto;
import com.sparta.mhproject.object.Schedule;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedule")
    public ResponseDto addschedule(@RequestBody RequestDto requestDto ){
       Schedule schedule = new Schedule(requestDto);

        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (manager, password, contents, firstday, updateday) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getManager());
                    preparedStatement.setString(2, schedule.getPassword());
                    preparedStatement.setString(3, schedule.getContents());
                    preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                    preparedStatement.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                    return preparedStatement;
                },
                keyHolder);
        int id = keyHolder.getKey().intValue();
        schedule.setId(id);




       ResponseDto responseDto = new ResponseDto(schedule);
       return responseDto;

    }

    @GetMapping("/schedule/{id}")
    //단계 끝.
    public ResponseDto checkschedule (@PathVariable int id){
        Schedule schedule = findById(id);
        if(schedule != null){
            return new ResponseDto(schedule);
        }else{
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");
        }

    }

    @GetMapping("/schedule")
    public List<ResponseDto> getschedule(@PathVariable int id, String manager,LocalDateTime updateday){

        Schedule schedule = findById(id);
        if(schedule != null){
            //담당자로 조회하거나, 수정일로 조회 하거나!
            if(manager.equals(schedule.getManager()) || schedule.getUpdateday() != null){

            }return (List<ResponseDto>) new ResponseDto(schedule);
            }else{
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");

        }

    }






    public Schedule findById(int id){
        // sql 그대로 가져다 쓰는 느낌.
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {

            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setManager(resultSet.getString("manager"));
                schedule.setContents(resultSet.getString("contents"));
                schedule.setFirstday(resultSet.getTimestamp("firstday").toLocalDateTime());
                schedule.setUpdateday(resultSet.getTimestamp("updateday").toLocalDateTime());

                return schedule;
            } else {
                return null;
            }
        },id);
    }

}

