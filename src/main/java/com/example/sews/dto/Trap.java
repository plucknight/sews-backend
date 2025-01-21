package com.example.sews.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "traps")
public class Trap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trap_id")
    private Integer trapId;



    @Column(name = "device_id")
    private Integer deviceId;



    @Column(name = "trap_name", nullable = false)
    private String trapName;

    @Column(name = "trap_type", nullable = false)
    private String trapType;

}
