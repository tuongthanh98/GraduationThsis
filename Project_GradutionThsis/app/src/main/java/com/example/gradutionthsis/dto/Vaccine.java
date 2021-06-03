package com.example.gradutionthsis.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vaccine implements Serializable {

    @SerializedName("id")
    private int idVaccine;//mã vaccine
    @SerializedName("vaccineName")
    private String nameVaccine;//tên vaccine - tên bệnh
    @SerializedName("vaccination")
    private String vaccination;     //tên tiêm chủng

    @SerializedName("disease")
    private String disease; // căn bệnh

    @SerializedName("description")
    private String description; // mô tả, nội dụng

    @SerializedName("note")
    private String note;//lưu ý

    public int getIdVaccine() {
        return idVaccine;
    }

    public void setIdVaccine(int idVaccine) {
        this.idVaccine = idVaccine;
    }

    public String getNameVaccine() {
        return nameVaccine;
    }

    public void setNameVaccine(String nameVaccine) {
        this.nameVaccine = nameVaccine;
    }

    public String getVaccination() {
        return vaccination;
    }

    public void setVaccination(String vaccination) {
        this.vaccination = vaccination;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Vaccine(int idVaccine, String nameVaccine, String vaccination, String disease, String description, String note) {
        this.idVaccine = idVaccine;
        this.nameVaccine = nameVaccine;
        this.vaccination = vaccination;
        this.disease = disease;
        this.description = description;
        this.note = note;
    }

    public Vaccine() {
    }

    @Override
    public String toString() {
        return "Vaccine{" +
                "idVaccine=" + idVaccine +
                ", nameVaccine='" + nameVaccine + '\'' +
                ", vaccination='" + vaccination + '\'' +
                ", disease='" + disease + '\'' +
                ", description='" + description + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
