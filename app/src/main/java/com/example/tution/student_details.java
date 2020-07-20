package com.example.tution;

public class student_details {

    public String year;
    public String stuname;
    public String stuschool;
    public String stuclass;
    public String stufather;
    public String stumother;
    public String stuphno;
    public String fees;
    public String impath;

    public student_details(String nam, String scl, String cl, String fnam, String mnam, String ph, String yea, String ref, String f) {
        year=yea;
        stuname=nam;
        stuschool=scl;
        stuclass=cl;
        stufather=fnam;
        stumother=mnam;
        stuphno=ph;
        fees=f;
        impath=ref;
    }

    public student_details() {

    }
}
