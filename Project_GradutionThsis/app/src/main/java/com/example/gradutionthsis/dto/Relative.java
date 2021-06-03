package com.example.gradutionthsis.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Relative implements Serializable {

    private int idRelative;
    private String fullName;
    private String nickName;
    private String gender;
    private String birthDate;
    private byte[] avatar;

    public void setIdRelative(int idRelative) {
        this.idRelative = idRelative;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getIdRelative() {
        return idRelative;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relative)) return false;
        Relative relative = (Relative) o;
        return getIdRelative() == relative.getIdRelative();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdRelative());
    }

    public Relative(int idRelative, String fullName, String nickName, String gender, String birthDate, byte[] avatar) {
        this.idRelative = idRelative;
        this.fullName = fullName;
        this.nickName = nickName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.avatar = avatar;
    }
    public Relative(String fullName, String nickName, String gender, String birthDate) {
        this.fullName = fullName;
        this.nickName = nickName;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public Relative() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Relative{" +
                "idRelative=" + idRelative +
                ", fullName='" + fullName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", avatar=" + Arrays.toString(avatar) +
                '}';
    }
}
