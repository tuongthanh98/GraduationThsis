package com.example.gradutionthsis.presenter;

import android.content.Context;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.dto.Health;

public class HealthPresenter {
    private final Context context;

    DBHelper dbHelper;
    HealthDAO healthDAO;

    public HealthPresenter(Context context, HealthDAO healthDAO) {
        this.context = context;
        this.healthDAO = healthDAO;
    }

    public void createHealth(Health health, int idRelative) {
        dbHelper = new DBHelper(context);

        health.setIdRelative(idRelative);
        if (dbHelper.insertHealth(health) > 0)
            healthDAO.createSuccess();
        else healthDAO.createFail();
    }

    public void updateHealth(Health health) {
        dbHelper = new DBHelper(context);

        if (dbHelper.updateHealth(health) > 0)
            healthDAO.updateSuccess();
        else healthDAO.updateFail();
    }

    public void deleteHealth(int id) {
        dbHelper = new DBHelper(context);

        if (dbHelper.deleteHealth(id))
            healthDAO.deleteSuccess();
        else healthDAO.deleteFail();
    }
}
