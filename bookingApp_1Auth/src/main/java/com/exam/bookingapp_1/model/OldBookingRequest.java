package com.exam.bookingapp_1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="oldbookingrequest")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OldBookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Column(name="id")
    private int Id;

    @Column(name="username")
    @JsonProperty("username")
    private String username;

    @Column(name="labname")
    @JsonProperty("labname")
    private String labname;


}
