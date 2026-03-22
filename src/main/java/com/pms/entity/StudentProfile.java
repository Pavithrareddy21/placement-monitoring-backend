package com.pms.entity;

import com.pms.enums.PlacementStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile extends BaseEntity {

    private String name;
    private String usn;
    private Double cgpa;

    @Enumerated(EnumType.STRING)
    private PlacementStatus placementStatus;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}