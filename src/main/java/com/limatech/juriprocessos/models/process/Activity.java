package com.limatech.juriprocessos.models.process;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;


@Entity
@Table(name = "_activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private Process process;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String type;

    @NotEmpty
    private String date;

    public Activity(Process process, String name, String description, String type, String date) {
        this.process = process;
        this.name = name;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public Activity() {

    }

}
