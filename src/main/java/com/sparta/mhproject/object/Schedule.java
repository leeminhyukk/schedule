package com.sparta.mhproject.object;
import com.sparta.mhproject.dto.RequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class Schedule {
    private int id;
    private String manager;
    private String password;
    private String contents;
    private LocalDateTime firstday;
    private LocalDateTime updateday;

    public Schedule (RequestDto requestDto){
        this.manager = requestDto.getManager();
        this.password =requestDto.getPassword();
        this.contents = requestDto.getContents();
        this.firstday=LocalDateTime.now();
        this.updateday=LocalDateTime.now();

    }

}
