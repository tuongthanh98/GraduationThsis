package com.example.gradutionthsis.presenter;

import android.content.Context;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.dto.Relative;

import java.util.List;

public class RelativePresenter {
    private final Context context;

    DBHelper dbHelper;
    RelativeDAO relativeDAO;

    public RelativePresenter(Context context, RelativeDAO relativeDAO) {
        this.context = context;
        this.relativeDAO = relativeDAO;
    }

    public void create(Relative relative) {
        dbHelper = new DBHelper(context);

        if (dbHelper.insertRelative(relative) > 0)
            relativeDAO.createSuccess();
        else relativeDAO.createFail();
    }

    public void update(Relative relative) {
        dbHelper = new DBHelper(context);

        if (dbHelper.updateRelative(relative) > 0)
            relativeDAO.updateSuccess();
        else relativeDAO.updateFail();
    }

    public void delete(int id) {
        dbHelper = new DBHelper(context);

        if (dbHelper.deleteRelative(id))
            relativeDAO.deleteSuccess();
        else relativeDAO.deleteFail();
    }

    public Relative getRelativeFinal() {
        List<Relative> list = dbHelper.getAllRelatives();  //Lấy ra danh sách trẻ em/thân nhân - Get all relatives

        //Lấy vị trí trẻ em cuối cùng trong danh sách - Get relavtive's last position in the list
        for (int i = list.size() - 1; i >= 0; ) {
            Relative relative = list.get(i);
            return relative;
        }
        return null;
    }
}
