package com.example.gradutionthsis.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationTask implements Serializable {

    @SerializedName("idNotify")
    private int idNotify;
    private int status;
    private int day;
    private int hour;
    private int minute;

    public int getIdNotify() {
        return idNotify;
    }

    public void setIdNotify(int idNotify) {
        this.idNotify = idNotify;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public NotificationTask(int idNotify, int status, int day, int hour, int minute) {
        this.idNotify = idNotify;
        this.status = status;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationTask{" +
                "idNotify=" + idNotify +
                ", status=" + status +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }
}
