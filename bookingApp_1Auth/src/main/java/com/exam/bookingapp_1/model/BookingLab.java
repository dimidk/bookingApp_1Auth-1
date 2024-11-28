package com.exam.bookingapp_1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name ="bookinglab")
public class BookingLab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Column(name="id")
    private int id;

//    @Column(columnDefinition = "bTitle")
    @Column(name = "title")
    @JsonProperty("title")
    private String title;

//    private Timestamp startDate1;

//    @JsonFormat(shape=JsonFormat.Shape.STRING)
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    @Column(name="startdate")
    @JsonProperty("start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;

    @Column(name = "enddate")
    @JsonProperty("end")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;


    //    @Column(columnDefinition = "bpeople")
//    private String bpeople;
//    @Column(columnDefinition = "bslotidstart")
//    private int bslotidstart;
//
//    @Column(columnDefinition = "bslotidend")
//    private int bslotidend;



//    @JoinTable(name = "labuser",
//            joinColumns = @JoinColumn(name = "id", referencedColumnName = "uid"))

    @Column(name = "username")
    @JsonProperty("username")
    private String username;


    @Column(name = "labname")
    @JsonProperty("labname")
    private String labname;

//    @Builder
//    public static BookingLab of (String title, Timestamp start, Timestamp end, String username, String labname) {
//
//        BookingLab bookingLab = new BookingLab();
//        bookingLab.title = title;
//        bookingLab.start = convertTime(start);
//        bookingLab.end = convertTime(end);
//        bookingLab.username = username;
//        bookingLab.labname = labname;
//
//        return bookingLab;
//    }

    private static Date convertTime(Timestamp timestamp) {

        return new Date(timestamp.getTime());
    }

}
