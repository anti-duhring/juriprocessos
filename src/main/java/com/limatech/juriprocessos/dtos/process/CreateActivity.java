package com.limatech.juriprocessos.dtos.process;

import com.limatech.juriprocessos.models.process.Activity;
import com.limatech.juriprocessos.models.process.Process;

import java.time.LocalDateTime;

public class CreateActivity {

    private Process process;

    private String name;

    private String description;

    private String type;

    private LocalDateTime date;

    public CreateActivity(Process process, String name, String description, String type, LocalDateTime date) {
        this.process = process;
        this.name = name;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public Activity toEntity() {
        return new Activity(process, name, description, type, date.toString());
    }
}
