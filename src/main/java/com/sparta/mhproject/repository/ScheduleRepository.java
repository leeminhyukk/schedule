package com.sparta.mhproject.repository;

import com.sparta.mhproject.dto.RequestDto;
import com.sparta.mhproject.dto.ResponseDto;
import com.sparta.mhproject.object.Schedule;
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

public class ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }
    //일정 등록
    public Schedule save(Schedule schedule) {
        //DB 저장
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
        return schedule;
    }
    //일정 조회
    public ResponseDto find(int id) {
        Schedule schedule = findById(id);
        if (schedule != null) {
            schedule.setPassword(null);
            return new ResponseDto(schedule);
        } else {
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");
        }
    }
    //일정 목록 조회
    public List<ResponseDto> findAll(String manager,LocalDateTime updateday) {
        String sql = "SELECT * FROM schedule order by updateday DESC";
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
        }return responseDtos;
    }

    //일정 수정
    public Schedule updateSchedule(int id, String password, RequestDto requestDto) {
        Schedule schedule = findById(id);

        if (schedule != null) {
            String sql = "UPDATE schedule SET manager = ?, contents = ?, updateday =? WHERE id = ?";
            if(schedule.getPassword().equals(password)){
                jdbcTemplate.update(sql,
                        requestDto.getManager(),requestDto.getContents(), LocalDateTime.now(), id);

                return  schedule;
            }else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }


        } else {
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");
        }
    }

    //일정 삭제
    public Schedule deleteSchedule(int id, String password) {
        Schedule schedule = findById(id);

        if (schedule != null) {
            String sql = "DELETE FROM schedule WHERE id = ?";
            if(schedule.getPassword().equals(password)){
                jdbcTemplate.update(sql, id);

                return  schedule;
            }else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }


        } else {
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");
        }
    }
    // 공통으로 사용하는 조회 - 공통으로 사용하는건 맨 밑에 위치.
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
                    schedule.setPassword(resultSet.getString("password"));
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
