package com.sparta.mhproject.service;

import com.sparta.mhproject.dto.RequestDto;
import com.sparta.mhproject.dto.ResponseDto;
import com.sparta.mhproject.object.Schedule;
import com.sparta.mhproject.repository.ScheduleRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Scheduleservice {
    private final JdbcTemplate jdbcTemplate;

    public Scheduleservice(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    //일정 등록
    public ResponseDto addschedule(RequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        //DB 저장
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule saveschedule = scheduleRepository.save(schedule);

        //Entity -> ReponseDto
        ResponseDto responseDto = new ResponseDto(schedule);
        return responseDto;
    }

    //일정 조회
    public ResponseDto checkschedule(int id) {
        // RequestDto -> Entity
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);

        // Entity -> ResponseDto
        return scheduleRepository.find(id);
    }

    //일정 목록 조회
    public List<ResponseDto> getschedule(String manager, LocalDateTime updateday) {
        ScheduleRepository scheduleRepository =new ScheduleRepository(jdbcTemplate);



        return scheduleRepository.findAll(manager, updateday);
    }

    //일정 수정
    public Schedule update(int id, String password, RequestDto requestDto) {
        ScheduleRepository scheduleRepository =new ScheduleRepository(jdbcTemplate);

        return scheduleRepository.updateSchedule(id, password, requestDto);

    }

    //일정 삭제
    public Schedule delete(int id, String password) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);

        return scheduleRepository.deleteSchedule(id, password);

    }
}
