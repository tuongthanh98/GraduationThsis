package com.example.gradutionthsis.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Injection implements Serializable {

    private int idInjection;

    @SerializedName("injectionMonth")
    private int injectionMonth;//Thời gian tiêm

    @SerializedName("distance")
    private int distance;//khoảng cách với mũi tiêm trước

    @SerializedName("injectionName")
    private String injectionName;//mũi tiêm số

    @SerializedName("idVaccine")
    private int idVaccine;//id vaccine

    public int getIdInjection() {
        return idInjection;
    }

    public void setIdInjection(int idInjection) {
        this.idInjection = idInjection;
    }

    public int getinjectionMonth() {
        return injectionMonth;
    }

    public void setinjectionMonth(int injectionMonth) {
        this.injectionMonth = injectionMonth;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getInjectionName() {
        return injectionName;
    }

    public void setInjectionName(String injectionName) {
        this.injectionName = injectionName;
    }

    public int getIdVaccine() {
        return idVaccine;
    }

    public void setIdVaccine(int idVaccine) {
        this.idVaccine = idVaccine;
    }

    public Injection() {
    }

    public Injection(int idInjection, int injectionMonth, int distance, String injectionName, int idVaccine) {
        this.idInjection = idInjection;
        this.injectionMonth = injectionMonth;
        this.distance = distance;
        this.injectionName = injectionName;
        this.idVaccine = idVaccine;
    }

    public Injection(int injectionMonth, int distance, String injectionName, int idVaccine) {
        this.injectionMonth = injectionMonth;
        this.distance = distance;
        this.injectionName = injectionName;
        this.idVaccine = idVaccine;
    }

    @Override
    public String toString() {
        return "Injection{" +
                "idInjection=" + idInjection +
                ", injectionMonth=" + injectionMonth +
                ", distance=" + distance +
                ", injectionName='" + injectionName + '\'' +
                ", idVaccine=" + idVaccine +
                '}';
    }
}