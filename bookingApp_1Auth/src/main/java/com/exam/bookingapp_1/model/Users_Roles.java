package com.exam.bookingapp_1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Users_Roles {

    @Id
    private int uid;

    @Id
    private int rid;
}
