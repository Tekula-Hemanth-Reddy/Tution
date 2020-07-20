package com.example.tution;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    public String name;
    public String mail;
    public String place;
    public String phno;
    public String CP;
    public String imp;


    UserProfile()
    {
    }
    UserProfile(String n, String m, String d, String p, String c , String ref)
    {
        name = n;
        mail = m;
        place = d;
        phno = p;
        CP = c;
        imp=ref;


    }

}
