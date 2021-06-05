package com.example.gradutionthsis.presenter;

import android.content.Context;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.dto.Vaccine;

public class VaccinePresenter {
    private static final String TAG = VaccinePresenter.class.getSimpleName();

    private final Context context;
    DBHelper dbHelper;
    VaccineDAO vaccineDAO;

    public VaccinePresenter(Context context, VaccineDAO vaccineDAO) {
        this.context = context;
        this.vaccineDAO = vaccineDAO;
    }

    public void createVaccine(Vaccine vaccine){
        dbHelper = new DBHelper(context);

        if (dbHelper.insertVaccine(vaccine) > 0)
            vaccineDAO.createSuccess();
        else vaccineDAO.createFail();
    }

    public boolean getAllVaccine(){
        dbHelper = new DBHelper(context);
        return dbHelper.getAllVaccines().size() == 0; //return true if list.size == 0, return false if list.size != 0
    }
}
