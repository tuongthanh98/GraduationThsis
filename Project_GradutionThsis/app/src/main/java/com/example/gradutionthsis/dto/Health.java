package com.example.gradutionthsis.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Health implements Serializable {
    private int idHealth;
    private double weight;
    private double height;
    private String time;
    private int idRelative;

    public int getIdRelative() {
        return idRelative;
    }

    public void setIdRelative(int idRelative) {
        this.idRelative = idRelative;
    }

    public int getIdHealth() {
        return idHealth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Health(double weight, double height, String time) {
        this.weight = weight;
        this.height = height;
        this.time = time;
    }

    public Health(int idHealth, double weight, double height, String time, int idRelative) {
        this.idHealth = idHealth;
        this.weight = weight;
        this.height = height;
        this.time = time;
        this.idRelative = idRelative;
    }

    public Health() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Health)) return false;
        Health health = (Health) o;
        return getIdHealth() == health.getIdHealth();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdHealth());
    }

    @NonNull
    @Override
    public String toString() {
        return "Health{" +
                "idHealth=" + idHealth +
                ", weight=" + weight +
                ", height=" + height +
                ", time='" + time + '\'' +
                ", idRelative=" + idRelative +
                '}';
    }
}
