package com.example.gradutionthsis.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class DetailSchedule implements Serializable {

    //Mã lịch tiêm phòng
    private int idRelative;

    //Mã mũi tiêm
    private int idInjection;

    public int getIdRelative() {
        return idRelative;
    }

    public void setIdRelative(int idRelative) {
        this.idRelative = idRelative;
    }

    public int getIdInjection() {
        return idInjection;
    }

    public void setIdInjection(int idInjection) {
        this.idInjection = idInjection;
    }

    public String getInjectionTime() {
        return injectionTime;
    }

    public void setInjectionTime(String injectionTime) {
        this.injectionTime = injectionTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    //Thời gian tiêm phòng
    private String injectionTime;

    //Trạng thái
    private int status;

    //Thông báo
    private int notification;


    public DetailSchedule() {
    }

    public DetailSchedule(int idRelative, int idInjection, String injectionTime, int status, int notification) {
        this.idRelative = idRelative;
        this.idInjection = idInjection;
        this.injectionTime = injectionTime;
        this.status = status;
        this.notification = notification;
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailSchedule{" +
                "idRelative=" + idRelative +
                ", idInjection=" + idInjection +
                ", injectionTime='" + injectionTime + '\'' +
                ", status=" + status +
                ", notification=" + notification +
                '}';
    }
}
