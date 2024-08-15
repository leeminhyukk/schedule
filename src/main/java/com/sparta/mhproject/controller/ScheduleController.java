package com.sparta.mhproject.controller;

import com.sparta.mhproject.dto.RequestDto;
import com.sparta.mhproject.dto.ResponseDto;
import com.sparta.mhproject.object.Schedule;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.*;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedule")
    public ResponseDto addschedule(@RequestBody RequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO schedule (manager, password, contents, firstday, updateday) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
    public ResponseDto checkschedule(@PathVariable int id) {
        Schedule schedule = findById(id);
        if (schedule != null) {
            return new ResponseDto(schedule);
        } else {
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");
        }
    }

    @GetMapping("/schedule")
    public List<ResponseDto> getschedule(@RequestParam(required = false) String manager, @RequestParam(required = false) LocalDateTime updateday) {
        String sql = "SELECT * FROM schedule";
        List<Schedule> schedules = jdbcTemplate.query(sql, new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getInt("id"));
                schedule.setManager(resultSet.getString("manager"));
                schedule.setContents(resultSet.getString("contents"));
                schedule.setFirstday(resultSet.getTimestamp("firstday").toLocalDateTime());
                schedule.setUpdateday(resultSet.getTimestamp("updateday").toLocalDateTime());
                return schedule;
            }
        });

        List<ResponseDto> responseDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if ((manager == null || manager.equals(schedule.getManager())) &&
                    (updateday == null || updateday.equals(schedule.getUpdateday()))) {
                responseDtos.add(new ResponseDto(schedule));
            }
        }
        return responseDtos;
    }

    public Schedule findById(int id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new RowMapper<Schedule>() {
                @Override
                public Schedule mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    Schedule schedule = new Schedule();
                    schedule.setId(resultSet.getInt("id"));
                    schedule.setManager(resultSet.getString("manager"));
                    schedule.setContents(resultSet.getString("contents"));
                    schedule.setFirstday(resultSet.getTimestamp("firstday").toLocalDateTime());
                    schedule.setUpdateday(resultSet.getTimestamp("updateday").toLocalDateTime());
                    return schedule;
                }
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}