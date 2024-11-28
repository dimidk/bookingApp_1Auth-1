package com.exam.bookingapp_1.model;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Bslot {


    private int bsid;


    private String bsstart;


    private String bsend;

}
