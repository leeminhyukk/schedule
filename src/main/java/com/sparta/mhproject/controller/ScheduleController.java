package com.sparta.mhproject.controller;

import com.sparta.mhproject.dto.RequestDto;
import com.sparta.mhproject.dto.ResponseDto;
import com.sparta.mhproject.object.Schedule;
import com.sparta.mhproject.service.Scheduleservice;
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

    //일정 등록
    @PostMapping("/schedule")
    public ResponseDto addschedule(@RequestBody RequestDto requestDto) {
        // 3개로 쪼개기 위한 인스턴트화
        // 1.객체 생성. 2. 리턴값 수정. 3. service 에 클래스 생성. 4. 옮기기 5. 생성자 변경 내용 확인
        Scheduleservice scheduleservice = new Scheduleservice(jdbcTemplate);
        return scheduleservice.addschedule(requestDto);
    }

    //일정 1개 조회
    @GetMapping("/schedule/{id}")
    public ResponseDto checkschedule(@PathVariable int id) {
        Scheduleservice scheduleservice = new Scheduleservice(jdbcTemplate);
        return scheduleservice.checkschedule(id);
    }

    //일정 여러개 조회
    @GetMapping("/schedule")
    public List<ResponseDto> getschedule(@RequestParam(required = false) String manager, @RequestParam(required = false) LocalDateTime updateday) {
        Scheduleservice scheduleservice = new Scheduleservice(jdbcTemplate);
        return scheduleservice.getschedule(manager, updateday);
    }

    //일정 수정
    @PutMapping("/schedule/{id}")
    public Schedule update(@PathVariable int id, @RequestParam String password,@RequestBody RequestDto requestDto){
        Scheduleservice scheduleservice = new Scheduleservice(jdbcTemplate);
        return scheduleservice.update(id, password, requestDto);
    }

    //일정 삭제
    @DeleteMapping("/schedule/{id}")
    public Schedule delete(@PathVariable int id, @RequestParam String password){
        Scheduleservice scheduleservice = new Scheduleservice(jdbcTemplate);
        return scheduleservice.delete(id, password);
    }
}