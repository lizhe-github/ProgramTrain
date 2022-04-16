package com.miniprogramlearn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Organizer {
    private int oid;
    private String unit;
    private Date authtime;
    private String authway;
    private String authinfo;
    private String mode;
    private int userid;
}
