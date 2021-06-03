package com.example.gradutionthsis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.NotificationTask;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.dto.Vaccine;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_App = "GradutionThsis.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_App, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCrTableRealatives = "CREATE TABLE RELATIVES (" +
                "idRelative integer PRIMARY KEY autoincrement," +
                "fullName TEXT(150) NOT NULL," +
                "nickName TEXT," +
                "gender TEXT," +
                "birthDate TEXT," +
                "avatar BLOB" +
                ");";

        String queryCrTableDetailSchedules = "CREATE TABLE DETAILSCHEDULES (" +
                "idRelative integer," +
                "idInjection integer," +
                "injectionTime text," +
                "status integer," +
                "notification integer," +
                "PRIMARY KEY (idRelative, idInjection)," +
                "FOREIGN KEY (idRelative) REFERENCES RELATIVES(idRelative) " +
                "ON DELETE CASCADE ON UPDATE CASCADE," +
                "FOREIGN KEY (idInjection) REFERENCES INJECTIONS(idInjection) " +
                "ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        String queryCrTableHealths = "CREATE TABLE HEALTHS (" +
                "idHealth integer PRIMARY KEY autoincrement," +
                "weight double NOT NULL," +
                "height doulbe," +
                "time TEXT ," +
                "id_relative integer," +
                "FOREIGN KEY  (id_relative) REFERENCES RELATIVES(idRelative) " +
                "ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        String queryCrTableNotifyTask = "CREATE TABLE NOTIFICATIONTASK (" +
                "idNotify integer primary key autoincrement," +
                "status integer," +
                "day integer," +
                "hour integer ," +
                "minute integer" +
                ");";
        String queryCrTableVaccines = "CREATE TABLE VACCINES (" +
                "idVaccine integer primary key autoincrement," +
                "nameVaccine TEXT," +
                "vaccination TEXT," +
                "disease TEXT ," +
                "description TEXT," +
                "note TEXT" +
                ");";
        String queryCrTableInjections = "CREATE TABLE INJECTIONS (" +
                "idInjection integer primary key autoincrement," +
                "injectionMonth TEXT," +
                "distance integer," +
                "injectionName TEXT ," +
                "idVaccine integer," +
                "FOREIGN KEY (idVaccine) REFERENCES VACCINES(idVaccine) " +
                "ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";

        sqLiteDatabase.execSQL(queryCrTableRealatives);
        sqLiteDatabase.execSQL(queryCrTableHealths);
        sqLiteDatabase.execSQL(queryCrTableVaccines);
        sqLiteDatabase.execSQL(queryCrTableNotifyTask);
        sqLiteDatabase.execSQL(queryCrTableInjections);
        sqLiteDatabase.execSQL(queryCrTableDetailSchedules);
    }


    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS RELATIVES");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS HEALTHS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS VACCINES");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NOTIFICATIONTASK");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS INJECTIONS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DETAILSCHEDULES");
        onCreate(sqLiteDatabase);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onOpen(@NonNull SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onOpen(db);
    }

    /**
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Thêm nhân thân
    //[START insertRelative]
    public int insertRelative(Relative relative) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put("fullName", relative.getFullName());
        con.put("nickName", relative.getNickName());
        con.put("gender", relative.getGender());
        con.put("birthDate", relative.getBirthDate());
        con.put("avatar", relative.getAvatar());
        int result = (int) db.insert("RELATIVES", null, con);
        db.close();
        return result;
    }
    //[END insertRelative]


    /**
     * @return list of relative
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Lấy danh sách nhân thân - Get list of relative
    //[START getAllRelatives]
    public ArrayList<Relative> getAllRelatives() {
        ArrayList<Relative> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RELATIVES", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Relative(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //[END getAllRelatives]

    /**
     * @return relative
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Lấy nhân thân theo id - Get relative by ID
    //[START getRelativeById]
    public Relative getRelativeById(int idRelative) {
        Relative relative = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RELATIVES WHERE idRelative = " + idRelative, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                relative = new Relative(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return relative;
    }
    //[END getRelativeById]


    /**
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Xóa nhân thân theo id - Delete relative by id
    //[START deleteRelative]
    public boolean deleteRelative(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete("RELATIVES", "idRelative" + "=?", new String[]{String.valueOf(id)}) > 0) {
            db.close();
            return true;
        }
        return false;
    }
    //[END deleteRelative]


    /**
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Cập nhật nhân thân - Update relative
    //[START updateRelative]
    public int updateRelative(Relative relative) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullName", relative.getFullName());
        contentValues.put("nickName", relative.getNickName());
        contentValues.put("gender", relative.getGender());
        contentValues.put("birthDate", relative.getBirthDate());
        contentValues.put("avatar", relative.getAvatar());
        int result = db.update("RELATIVES", contentValues, "idRelative = ?", new String[]{String.valueOf(relative.getIdRelative())});
        db.close();
        return result;
    }
    //[END updateRelative]


    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    // Thêm sức khỏe của trẻ em - insert health for relative
    //[START insertHealth]
    public int insertHealth(Health health) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put("weight", health.getWeight());
        con.put("height", health.getHeight());
        con.put("time", health.getTime());
        con.put("id_relative", health.getIdRelative());
        int result = (int) db.insert("HEALTHS", null, con);
        db.close();
        return result;
    }
    //[END insertHealth]


    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    // Lấy danh sách sức khỏe của trẻ em theo id trẻ - Get all health for relative by id this relative
    //[START getAllHealthsById]
    public ArrayList<Health> getAllHealthsById(int idRelative) {
        ArrayList<Health> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HEALTHS WHERE id_relative = " + idRelative, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Health(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //[END getAllHealthsById]


    /**
     * @param id Mã sức khỏe cần xóa
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Xóa sức khỏe trẻ theo idHealth - Delete health
    //[START deleteHealth]
    public boolean deleteHealth(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete("HEALTHS", "idHealth" + "=?", new String[]{String.valueOf(id)}) > 0) {
            db.close();
            return true;
        }
        return false;
    }
    //[END deleteHealth]


    /**
     * @param health đối tượng trẻ cần sửa thông tin
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Cập nhật sức khỏe trẻ theo idHealth - Update health
    //[START updateHealth]
    public int updateHealth(Health health) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("weight", health.getWeight());
        contentValues.put("height", health.getHeight());
        contentValues.put("time", health.getTime());
        contentValues.put("id_relative", health.getIdRelative());
        int result = db.update("HEALTHS", contentValues, "idHealth = ?", new String[]{String.valueOf(health.getIdHealth())});
        db.close();
        return result;
    }
    //[END updateHealth]


    /**
     * @param task đối tượng NotificationTask
     * @author Nguyễn Thanh Tường
     * @date 14/05/2021 : 16h
     */
    // Thêm mới nhiêm vụ thông báo - Insert new notification task
    //[START insertNotifyTask]
    public int insertNotifyTask(NotificationTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put("status", task.getStatus());
        con.put("day", task.getDay());
        con.put("hour", task.getHour());
        con.put("minute", task.getMinute());
        int result = (int) db.insert("NOTIFICATIONTASK", null, con);
        db.close();
        return result;
    }
    //[END insertNotifyTask]


    /**
     * @author Nguyễn Thanh Tường
     * @date 14/05/2021 : 16h06p
     */
    // Lấy danh sách nhiệm vụ thông báo - Get all notification task list
    //[START getAllTask]
    public ArrayList<NotificationTask> getAllTask() {
        ArrayList<NotificationTask> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NOTIFICATIONTASK", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new NotificationTask(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        return list;
    }
    //[END getAllTask]


    /**
     * @param task nhiệm vụ thông báo cần sửa
     * @author Nguyễn Thanh Tường
     * @date 14/05/2021 : 16h08p
     */
    //Cập nhật nhiệm vụ thông báo - Update notification task
    //[START updateTask]
    public int updateTask(NotificationTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", task.getStatus());
        contentValues.put("day", task.getDay());
        contentValues.put("hour", task.getHour());
        contentValues.put("minute", task.getMinute());
        contentValues.put("idNotify", task.getIdNotify());
//        int result = (int) db.update("NOTIFICATIONTASK", contentValues, "idNotify = ?", new String[]{String.valueOf(task.getIdNotify())});
        int result = (int) db.update("NOTIFICATIONTASK", contentValues, "idNotify = " + task.getIdNotify(), null);
        db.close();
        return result;
    }
    //[END updateTask]


    /**
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Thêm vaccine
    //[START insertVaccine]
    public int insertVaccine(Vaccine vaccine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put("nameVaccine", vaccine.getNameVaccine());
        con.put("vaccination", vaccine.getVaccination());
        con.put("disease", vaccine.getDisease());
        con.put("description", vaccine.getDescription());
        con.put("note", vaccine.getNote());
        int result = (int) db.insert("VACCINES", null, con);
        db.close();
        return result;
    }
    //[END insertVaccine]


    /**
     * @return list of relative
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Lấy danh sách Vaccines - Get list of Vaccines
    //[START getAllVaccines]
    public ArrayList<Vaccine> getAllVaccines() {
        ArrayList<Vaccine> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM VACCINES", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Vaccine(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //[END getAllVaccines]

    /**
     * @return list of relative
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Lấy danh sách nhân thân - Get list of relative
    //[START getVaccineById]
    public Vaccine getVaccineById(int id) {
        Vaccine vaccine = new Vaccine();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM VACCINES WHERE idVaccine = " + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                vaccine.setIdVaccine(cursor.getInt(0));
                vaccine.setNameVaccine(cursor.getString(1));
                vaccine.setVaccination(cursor.getString(2));
                vaccine.setDisease(cursor.getString(3));
                vaccine.setDescription(cursor.getString(4));
                vaccine.setNote(cursor.getString(5));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return vaccine;
    }
    //[END getVaccineById]


    /**
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Thêm mữi tiêm dành cho trẻ
    //[START insertInjection]
    public int insertInjection(Injection injection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put("injectionMonth", injection.getinjectionMonth());
        con.put("distance", injection.getDistance());
        con.put("injectionName", injection.getInjectionName());
        con.put("idVaccine", injection.getIdVaccine());
        int result = (int) db.insert("INJECTIONS", null, con);
        db.close();
        return result;
    }
    //[END insertInjection]

    /**
     * @param idInjection mã mũi tiêm
     * @author: Nguyễn Thanh Tường
     * @date
     */
    //Lấy mũi tiêm theo id
    //[START getInjectionById]
    public Injection getInjectionById(int idInjection) {
        Injection injection = new Injection();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM INJECTIONS WHERE idInjection = " + idInjection, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                injection.setIdInjection(cursor.getInt(0));
                injection.setinjectionMonth(cursor.getInt(1));
                injection.setDistance(cursor.getInt(2));
                injection.setInjectionName(cursor.getString(3));
                injection.setIdVaccine(cursor.getInt(4));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return injection;
    }
    //[END getInjectionById]

    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Lấy các mũi tiêm cùng 1 vaccine thông qua id vaccine
    //[START getTheSameInjection]
    public List<Injection> getTheSameInjections(int idVaccine) {
        List<Injection> injections = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM INJECTIONS WHERE idVaccine =  " + idVaccine, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                injections.add(new Injection(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();

        }
        db.close();
        return injections;
    }
    //[END getTheSameInjection]



    /**
     * @param distance Khoảng cách giữa 2 mũi tiêm
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Lấy danh sách các mũi tiêm cùng một khoảng cách tiêm
    //[START getTheSameDistance]
    public List<Injection> getTheSameDistance(int distance) {
        List<Injection> injections = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM INJECTIONS WHERE distance =  " + distance, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                injections.add(new Injection(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return injections;
    }
    //[END getTheSameDistance]



    /**
     * @return list of relative
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Lấy danh sách mũi tiêm - Get list of injection
    //[START getAllInjection]
    public ArrayList<Injection> getAllInjections() {
        ArrayList<Injection> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM INJECTIONS", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Injection(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //[END getAllInjection]


    /**
     * @author Nguyễn Thanh Tường
     * @date
     */
    //Tạo chi tiết lịch tiêm cho trẻ
    //[START insertInjection]
    public int insertDetailSchedule(DetailSchedule detailSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put("idRelative", detailSchedule.getIdRelative());
        con.put("idInjection", detailSchedule.getIdInjection());
        con.put("injectionTime", detailSchedule.getInjectionTime());
        con.put("status", detailSchedule.getStatus());
        con.put("notification", detailSchedule.getNotification());
        int result = (int) db.insert("DETAILSCHEDULES", null, con);
        db.close();
        return result;
    }
    //[END insertInjection]

    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Lấy lịch tiêm thông qua id trẻ
    //[START getScheduleById]
    public DetailSchedule getDetailScheduleById(int idRelative, int idInjection) {
        DetailSchedule detailSchedule = new DetailSchedule();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DETAILSCHEDULES WHERE idRelative = " + idRelative + "  AND idInjection = " + idInjection, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                detailSchedule.setIdRelative(cursor.getInt(0));
                detailSchedule.setIdInjection(cursor.getInt(1));
                detailSchedule.setInjectionTime(cursor.getString(2));
                detailSchedule.setStatus(cursor.getInt(3));
                detailSchedule.setNotification(cursor.getInt(4));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return detailSchedule;
    }
    //[END getScheduleById]


    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Lấy danh sách lịch tiêm thông qua id trẻ
    //[START getDetailSchedulesById]
    public List<DetailSchedule> getDetailSchedulesById(int idRelative) {
        List<DetailSchedule> detailSchedules = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DETAILSCHEDULES WHERE idRelative = " + idRelative, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                detailSchedules.add(new DetailSchedule(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return detailSchedules;
    }
    //[END getDetailSchedulesById]

    /**
     * @author Nguyễn Thanh Tường
     * @date 24/05/2021
     */
    //Cập nhật sức khỏe trẻ theo idHealth - Update health
    //[START updateDetailSchedule]
    public int updateDetailSchedule(DetailSchedule detailSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("injectionTime", detailSchedule.getInjectionTime());
        contentValues.put("status", detailSchedule.getStatus());
        contentValues.put("notification", detailSchedule.getNotification());
        int result = db.update("DETAILSCHEDULES", contentValues, "idRelative = ? AND idInjection = ?", new String[]{String.valueOf(detailSchedule.getIdRelative()), String.valueOf(detailSchedule.getIdInjection())});
        db.close();
        return result;
    }
    //[END updateDetailSchedule]


    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Lấy lịch tiêm thông qua id trẻ
    //[START getScheduleById]
    public List<DetailSchedule> getDSchedulesByTime(String injectionTime) {
        List<DetailSchedule> detailSchedules = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DETAILSCHEDULES WHERE injectionTime LIKE '" + injectionTime + "'", null);
//        Cursor cursor = db.rawQuery("SELECT * FROM DETAILSCHEDULES", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                detailSchedules.add(new DetailSchedule(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return detailSchedules;
    }
    //[END getScheduleById]

    /**
     * @author Nguyễn Thanh Tường
     * @date 29/04/2021
     */
    //Lấy lịch tiêm thông qua id trẻ
    //[START getScheduleById]
    public List<DetailSchedule> getDSchedulesByNotify() {
        List<DetailSchedule> detailSchedules = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DETAILSCHEDULES WHERE notification = " + 0, null);
//        Cursor cursor = db.rawQuery("SELECT * FROM DETAILSCHEDULES", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                detailSchedules.add(new DetailSchedule(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return detailSchedules;
    }
    //[END getScheduleById]
}
