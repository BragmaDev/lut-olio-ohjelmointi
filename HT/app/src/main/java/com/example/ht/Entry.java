package com.example.ht;

import java.io.Serializable;
import java.util.Date;

public abstract class Entry implements Serializable {

    protected Date date;

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

}
