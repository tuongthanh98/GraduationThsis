package com.example.gradutionthsis.presenter;

import android.content.Context;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.dto.Injection;

public class InjectionPresenter {
    private static final String TAG = InjectionPresenter.class.getSimpleName();

    private final Context context;

    DBHelper dbHelper;
    InjectionDAO injectionDAO;

    public InjectionPresenter(Context context) {
        this.context = context;
    }

    public InjectionPresenter(Context context, InjectionDAO injectionDAO) {
        this.context = context;
        this.injectionDAO = injectionDAO;
    }

    public void createInjection (Injection injection){
        dbHelper = new DBHelper(context);

        if (dbHelper.insertInjection(injection) > 0)
            injectionDAO.createSuccess();
        else injectionDAO.createFail();
    }

    public boolean getAllInjections (){
        dbHelper = new DBHelper(context);
        return dbHelper.getAllInjections().size() == 0;//return true if list.size == 0, return false if list.size != 0
    }

    public Injection getInjectionById (int idInjection){
        dbHelper = new DBHelper(context);
        return dbHelper.getInjectionById(idInjection);
    }
}
