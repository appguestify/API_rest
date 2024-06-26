package org.iesvdm.api_rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "task",
        indexes = @Index(name = "task_description_index", columnList = "description"))
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private String description;
    private LocalDate deadline;
    private Boolean completed;

    @JsonBackReference
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade =  CascadeType.PERSIST)
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}