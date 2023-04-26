package com.zakaria.hospital.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity(name = "patients")
@AllArgsConstructor @NoArgsConstructor
@Data
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    // @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date birthdate;
    private boolean sick;
    private Integer score;
}
