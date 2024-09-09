package com.hibernate_concepts.spring_jpa_mapping.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tutorial_details")
public class TutorialDetails {

    @Id
    private Long id;

    @Column
    private LocalDateTime createdOn;

    @Column
    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tutorial_id")
    private Tutorial tutorial;

}
