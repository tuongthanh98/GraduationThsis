package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.MainActivity;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.presenter.DetailScheduleDAO;
import com.example.gradutionthsis.presenter.DetailSchedulePresenter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailInjectionActivity extends AppCompatActivity implements DetailScheduleDAO {
    private static final String TAG = DetailInjectionActivity.class.getSimpleName();

    public static final int STATUS_CODE = 1;
    public static final int CHECK_STATUS_CODE = 0;
    public static final int COMPARE_CODE = -1;

    private TextView txtInjectionName, txtVaccineName, txtInjectionTime;
    private CheckBox chkStatus;

    DBHelper dbHelper;
    DetailSchedule detailSchedule;
    DetailSchedulePresenter mPresenter;


    String requestCode;
    Relative relative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_injection);

        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.injection_detail)); //Chỉnh sửa title trên ActionBar
        }

        dbHelper = new DBHelper(this);
        mPresenter = new DetailSchedulePresenter(this, this);


        txtInjectionName = findViewById(R.id.textInjectionName);
        txtVaccineName = findViewById(R.id.textVaccineName);
        txtInjectionTime = findViewById(R.id.textInjectionTime);
        chkStatus = findViewById(R.id.checkbox);
        Button btnSave = findViewById(R.id.buttonSave);
        Button btnBack = findViewById(R.id.buttonBack);
        Button imgCalendar = findViewById(R.id.imageCalendar);

        detailSchedule = new DetailSchedule();

        DetailSchedule dsRecieve = reciveObject();

        if (dsRecieve != null) {
            detailSchedule = dbHelper.getDetailScheduleById(Objects.requireNonNull(dsRecieve).getIdRelative(),
                    dsRecieve.getIdInjection());
            relative = dbHelper.getRelativeById(detailSchedule.getIdRelative());
        } else {
            int idRelative = getIntent().getExtras().getInt("idRelative");
            int idInjection = getIntent().getExtras().getInt("idInjection");
            requestCode = getIntent().getStringExtra("requestCode");
            detailSchedule = dbHelper.getDetailScheduleById(idRelative, idInjection);
            relative = dbHelper.getRelativeById(detailSchedule.getIdRelative());
            Log.d(TAG, "onCreate: " + detailSchedule.toString());
        }

        if (detailSchedule.getIdRelative() == 0) {
            Toast.makeText(this, "Trẻ em không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        setTextView();

        btnBack.setOnClickListener(v -> eventBack());
        btnSave.setOnClickListener(v -> save());

        imgCalendar.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            @SuppressLint("SetTextI18n")
            DatePickerDialog.OnDateSetListener callBack = (DatePicker datePicker, int year, int month, int dayOfMonth) -> (txtInjectionTime = findViewById(R.id.textInjectionTime)).setText((dayOfMonth) + "/" + (month + 1) + "/" + year);
            DatePickerDialog pic = new DatePickerDialog(DetailInjectionActivity.this, callBack, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            pic.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Xử lý sự kiện của nút back trên action bar
        if (item.getItemId() == android.R.id.home) {
            eventBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Cài đặt sự kiện nút quay lại của thiết bị áp dụng cho API 5 trở lên
    @Override
    public void onBackPressed() {
        eventBack();
        super.onBackPressed();
    }

    private void eventBack() {
        finish();
        if (requestCode != null)                                                    // Kiểm tra mã gửi đến nếu != null
            sendObject(dbHelper.getRelativeById(detailSchedule.getIdRelative()));   //Mở TabRelative ứng với relative của DetailInjection hiện tại
    }


    /**
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author Nguyễn Thanh Tường
     * date: 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Object object) {
        Intent intent = new Intent(getApplicationContext(), TabRelativeActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object", (Serializable) object);
        intent.putExtras(bundle);
        Log.i(TAG, "sendObject: Sender!");
        startActivity(intent);
    }


    /**
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 : 3h56p
     */
    //Sự kiện nút save
    //[START save]
    private void save() {
        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());
        setData();

        //Kiểm tra xem có phải vị trí phần tử thứ 0
        if (checkStatusInjectBefore() == 3) {
            //Kiểm tra ngày tiêm và ngày sinh có hợp lệ hay không
            if (compareInjectionTime()) {
                mPresenter.updateDetailSchedule(detailSchedule);
                finish();
                return;
            } else {
                Toast.makeText(this, "Ngày tiêm phải bằng hoặc sau ngày sinh!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Kiểm tra khoảng cách giữa 2 mũi tiêm (trước và hiện tại) && kiểm tra trạng thái mũi tiêm trước đó: true && false == -1
        if (checkStatusInjectBefore() == CHECK_STATUS_CODE) {
            if (chkStatus.isChecked()) {
                Toast.makeText(this, "Khoảng cách giữa mũi trước và mũi sau là " + injection.getDistance() + " tháng!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mPresenter.updateDetailSchedule(detailSchedule);
                finish();
            }
            return;
        }


        //Kiểm tra khoảng cách giữa 2 mũi tiêm (trước và hiện tại) && kiểm tra trạng thái mũi tiêm trước đó: true && true == 1
        if (checkStatusInjectBefore() == STATUS_CODE) {
            mPresenter.updateDetailSchedule(detailSchedule);
            finish();
            return;
        }

        //Kiểm tra khoảng cách giữa 2 mũi tiêm (trước và hiện tại) && kiểm tra trạng thái mũi tiêm trước đó: true && true == 1
        if (checkStatusInjectBefore() == COMPARE_CODE) {
            Toast.makeText(this, "Khoảng cách giữa mũi trước và mũi sau là " + injection.getDistance() + " tháng!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Trạng thái mũi tiêm false
        Toast.makeText(this, "Hãy cập nhật mũi tiêm trước đó!", Toast.LENGTH_SHORT).show();
    }
    //[END save]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 : 3h56p
     */
    //Kiểm tra và đặt dữ liệu
    //[START setData]
    private void setData() {
        //Kiểm tra trạng thái check box
        if (!chkStatus.isChecked()) {
            detailSchedule.setStatus(0);//Đặt trạng thái chưa tiêm
            detailSchedule.setNotification(0);//Đặt trạng thái thông báo mũi tiêm
            detailSchedule.setInjectionTime(txtInjectionTime.getText().toString());
            return;
        }
        detailSchedule.setStatus(1); //Đặt trạng thái đã tiêm
        detailSchedule.setNotification(1);
        detailSchedule.setInjectionTime(txtInjectionTime.getText().toString());
    }
    //[END setData]


    /**
     * @param dayBefore Ngày trước đó
     * @param dayAfter  Ngày sau đó - ngày cập nhật
     * @author: Nguyễn Thanh Tường
     * @date 27/05/2021 15h3p
     */
    //So sánh 2 ngày
    //[START compareDate]
    public static int compareDate(String dayBefore, String dayAfter) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = df.parse(dayBefore);   //Ngày trước đó (Ngày lưu trong SQLite)
            Date date2 = df.parse(dayAfter);    //Ngày sau đó (Ngày chọn từ txtInjectionTime)
            if (date1 != null && date2 != null) {
                return date2.compareTo(date1);  //trả về -1 Nếu date2<date1
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //[END compareDate]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 27/05/2021 15h9p
     */
    //Kiểm tra ngày tiêm với ngày sinh
    //[START compareInjectionTime]
    private boolean compareInjectionTime() {
        int check = compareDate(relative.getBirthDate(), txtInjectionTime.getText().toString());
        //Khác check - 1: ngày sau>ngày trước
        if (check != COMPARE_CODE) {
            Log.d(TAG, "checkStatusInjectBefore: " + check);
            return true;
        }
        return false;
    }
    //[END compareInjectionTime]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 25/05/2021 : 19h18p
     */
    //Kiểm tra trạng thái mũi tiêm trước đó
    //[START checkStatus]
    private int checkStatusInjectBefore() {
        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());//Lấy thông tin mũi tương ứng với vaccine
        List<Injection> injections = dbHelper.getTheSameInjections(injection.getIdVaccine());//Lấy danh sách mũi tiêm có cùng vaccine
        //Lấy ngược danh sách mũi tiêm cùng vaccie
        for (int i = injections.size() - 1; i >= 0; i--) {
            if (i != CHECK_STATUS_CODE)                                                                                                                             //so sánh vị trí khác 0
                if (injection.getIdInjection() == injections.get(i).getIdInjection()) {                                                             //so sách mũi tiêm hiện tại với mũi tiêm trong danh sách tiêm
                    DetailSchedule dtBefore = dbHelper.getDetailScheduleById(detailSchedule.getIdRelative(), injections.get(i - 1).getIdInjection()); // lấy chi tiết mũi tiêm của bé trước đó
                    String newDate = calTime(detailSchedule.getInjectionTime(), injection.getDistance());

                    //kiểm tra trạng thái mũi trước đó đã hoàn thành chưa
                    if (dtBefore.getStatus() != CHECK_STATUS_CODE) {
                        //kiểm tra ngày tiêm trước với ngày của mũi tiêm hiện tại
//                        Log.d(TAG, "checkStatusInjectBefore: " + compareDate(dtBefore.getInjectionTime(), newDate));
                        if (compareDate(dtBefore.getInjectionTime(), newDate) != COMPARE_CODE) {
                            //kiểm tra trạng thái ô checkbox
                            if (chkStatus.isChecked())
                                return 1;
                            return 0;
                        }
                        return -1;
                    }
                    break;
                }
            if (i == 0) {
                return 3;
            }
        }
        return 2;
    }
    //[END checkStatus]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 : 3h55p
     */
    //[START setTextView]
    private void setTextView() {
        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());
        txtInjectionName.setText(injection.getInjectionName());
        txtVaccineName.setText(dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination());
        txtInjectionTime.setText(detailSchedule.getInjectionTime());
        chkStatus.setChecked(detailSchedule.getStatus() != 0);
    }
    //[END setTextView]


    /*
     * @author: Nguyễn Thanh Tường
     * date: 16/4/2021 : 16h34p
     * */
    //Nhận dữ liệu thân nhân dược gửi đến - reciveObject
    // [START reciveObject]
    private DetailSchedule reciveObject() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            return (DetailSchedule) bundle.getSerializable("object");
        } else {
            Toast.makeText(this, "Dữ liệu rỗng ", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    // [END reciveObject]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 23/5/2021 16h58p
     */
    //Phương thức tính thời gian
    // [START calTime]
    private String calTime(String injectionMonth, int distance) {
        String pattern = "dd/MM/yyyy";
        int month;//Tháng tiêm phòng
        try {
            @SuppressLint("SimpleDateFormat")
            Date date = new SimpleDateFormat(pattern).parse(injectionMonth);
            Calendar c = Calendar.getInstance();
            if (date != null) {
                c.setTime(date);
                Log.d(TAG, "calTime: " + c.get(Calendar.MONTH));
                c.add(Calendar.MONTH, -distance);
                Log.d(TAG, "Tháng cộng thêm: " + c.get(Calendar.MONTH));
                month = c.get(Calendar.MONTH) + 1;
                return (c.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + c.get(Calendar.YEAR));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createSuccess() {

    }

    @Override
    public void createFail() {

    }

    @Override
    public void updateSuccess() {
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFail() {
        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
    }
    // [END calTime]
}