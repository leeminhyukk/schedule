package com.sparta.mhproject.dto;


import com.sparta.mhproject.object.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseDto {
    private int id;
    private String manager;
    private String contents;
    private LocalDateTime firstday;
    private LocalDateTime updateday;


    public ResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.manager = schedule.getManager();
        this.contents = schedule.getContents();
        this.firstday = schedule.getFirstday();
        this.updateday = schedule.getUpdateday();
    }
}
