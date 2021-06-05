package com.example.gradutionthsis.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailSchedulePresenter {
    private static final String TAG = DetailSchedulePresenter.class.getSimpleName();
    private final Context context;

    DBHelper dbHelper;
    DetailScheduleDAO dao;

    public DetailSchedulePresenter(Context context) {
        this.context = context;
    }

    public DetailSchedulePresenter(Context context, DetailScheduleDAO dao) {
        this.context = context;
        this.dao = dao;
    }

    public void createDetailSchedule(int idRelative, String birthDate) {
        dbHelper = new DBHelper(context);
        List<Injection> injections = dbHelper.getAllInjections(); //list dbHelper

        for (Injection injection : injections) {
            String injectionTime = calTime(birthDate, injection.getinjectionMonth()); //Tạo thời gian tiêm phòng (injectionTime)
            DetailSchedule detailSchedule = new DetailSchedule(idRelative, injection.getIdInjection(), injectionTime, 0, 0);
            if (dbHelper.insertDetailSchedule(detailSchedule) > 0)
                Log.d(TAG, "insertInjection: success!" + detailSchedule.toString());
            else
                dao.createFail();
        }
    }


    public void updateDetailSchedule(DetailSchedule detailSchedule) {
        dbHelper = new DBHelper(context);

        if (dbHelper.updateDetailSchedule(detailSchedule) > 0)
            dao.updateSuccess();
        else dao.updateFail();
    }


    public List<DetailSchedule> getListByIdRelative(int idRelative) {
        dbHelper = new DBHelper(context);
        List<DetailSchedule> detailSchedules = dbHelper.getDetailSchedulesById(idRelative);

        if (detailSchedules.size() == 0)
            return null;
        return detailSchedules;
    }

    public DetailSchedule getDetailSchedule(int idRelative, int idInjection){
        dbHelper = new DBHelper(context);
        return dbHelper.getDetailScheduleById(idRelative, idInjection);
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 23/5/2021 16h58p
     */
    //Phương thức tính thời gian
    // [START calTime]
    private String calTime(String birthDay, int injectionMonth) {
        String pattern = "dd/MM/yyyy";
        int month;//Tháng tiêm phòng
        try {
            @SuppressLint("SimpleDateFormat")
            Date date = new SimpleDateFormat(pattern).parse(birthDay);
            Calendar c = Calendar.getInstance();
            if (date != null) {
                c.setTime(date);
                Log.d(TAG, "calTime: " + c.get(Calendar.MONTH));
                c.add(Calendar.MONTH, injectionMonth);
                Log.d(TAG, "Ngày cộng thêm: " + c.get(Calendar.MONTH));
                month = c.get(Calendar.MONTH) + 1;
                return (c.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + c.get(Calendar.YEAR));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    // [END calTime]
}
