package com.exam.bookingapp_1.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.users.GenericRole;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="lab")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "labname")
    private String labname;
}
