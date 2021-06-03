package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddRelativeActivity extends AppCompatActivity {

    private final static String TAG = "RelativesActivity";

    private TextView txtBirthDate;
    private EditText edtFullName, edtNickName, edtWeight, edtHeight;
    private CircleImageView ivAvatar;
    private RadioGroup radioGroup;
    private RadioButton radSelect;
    private ProgressBar progressBar;

    List<Relative> list = new ArrayList<>();
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relative);

        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.add_new)); //Chỉnh sửa title trên ActionBar
        }


        String pattern = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat(pattern);

        dbHelper = new DBHelper(this);

        edtFullName = findViewById(R.id.inputFullName);
        edtNickName = findViewById(R.id.inputNickName);
        edtWeight = findViewById(R.id.inputWeight);
        edtHeight = findViewById(R.id.inputHeight);
        ivAvatar = findViewById(R.id.imageView);

        progressBar = findViewById(R.id.progressBar);

        txtBirthDate = findViewById(R.id.textBirthdate);
        txtBirthDate.setText(df.format(Calendar.getInstance().getTime()));

        radioGroup = findViewById(R.id.radioGroup);
        ImageButton imgCalendar = findViewById(R.id.imageCalendar);

        Button btnNext = findViewById(R.id.buttonNext);

        RadioButton radFemale = findViewById(R.id.radioFemale);
        //Sự kiện click radio button - Handle add click
        radFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                ivAvatar.setImageResource(R.drawable.female);
            else
                ivAvatar.setImageResource(R.drawable.male);
        });


        //Sự kiện click button next - Handle add click
        btnNext.setOnClickListener(view -> {
            if (valid()) {
                Log.i(TAG, "Button next is clicked!");
                insertRelative();
                insertHealth();
                insertInjection();
                progressBar.setVisibility(View.VISIBLE);
                finish(); //Đóng activity hiện tại - close current activity
            }
        });

        imgCalendar.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            @SuppressLint("SetTextI18n")
            DatePickerDialog.OnDateSetListener callBack = (DatePicker datePicker, int year, int month, int dayOfMonth) -> (txtBirthDate = findViewById(R.id.textBirthdate)).setText((dayOfMonth) + "/" + (month + 1) + "/" + year);
            DatePickerDialog pic = new DatePickerDialog(AddRelativeActivity.this, callBack, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            pic.show();
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Xử lý sự kiện của nút back trên action bar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @author Nguyễn Thanh Tường
     * date: 07/4/2021 : 16h33p
     */
    //Kiểm tra tính hợp lệ của dữ liệu nhập vào - Check the validity of the input data
    // [START valid]
    private boolean valid() {
        radSelect = findViewById(radioGroup.getCheckedRadioButtonId());
        if (edtFullName.getText().toString().isEmpty()) {
            Toast.makeText(this, "The baby's fullname is not emtpy!", Toast.LENGTH_SHORT).show();
            edtFullName.requestFocus();
            return false;
        }
        if (edtNickName.getText().toString().isEmpty()) {
            Toast.makeText(this, "The baby's nick name is not emtpy!", Toast.LENGTH_SHORT).show();
            edtNickName.requestFocus();
            return false;
        }
        if (edtWeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "The baby's weight is not empty!", Toast.LENGTH_SHORT).show();
            edtWeight.requestFocus();
            return false;
        }
        if (edtHeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "The baby's height is not empty!", Toast.LENGTH_SHORT).show();
            edtHeight.requestFocus();
            return false;
        }
        return true;
    }
    // [END valid]


    /**
     * @author Nguyễn Thanh Tường
     * date: 07/4/2021 : 17h03p
     */
    //Chuyển ImageView sang byte để lưu xuống database
    // [START ImageViewToByte]
    private byte[] imageViewToByte(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }
    // [END ImageViewToByte]


    /**
     * @author Nguyễn Thanh Tường
     * date: 07/4/2021 : 17h03p
     */
    //Thêm trẻ em mới
    // [START insertRelative]
    private void insertRelative() {
        Relative relative = new Relative();
        relative.setFullName(edtFullName.getText().toString());
        relative.setNickName(edtNickName.getText().toString());
        relative.setGender(radSelect.getText().toString());
        relative.setBirthDate(txtBirthDate.getText().toString());
        relative.setAvatar(imageViewToByte(ivAvatar));

        if (dbHelper.insertRelative(relative) > 0) {
            Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Fail!!!", Toast.LENGTH_SHORT).show();

    }
    // [END insertRelative]


    /**
     * @author Nguyễn Thanh Tường
     * date: 07/4/2021 : 17h03p
     */
    //Thêm sức khỏe trẻ em
    // [START insertHealth]
    private void insertHealth() {
        Health health = new Health();

        health.setWeight(Double.parseDouble(edtWeight.getText().toString().trim()));
        health.setHeight(Double.parseDouble(edtHeight.getText().toString().trim()));
        health.setTime(txtBirthDate.getText().toString());

        list = dbHelper.getAllRelatives();  //Lấy ra danh sách trẻ em/thân nhân - Get all relatives
        Log.i(TAG, "insertHealth: " + list.size());

        //Lấy vị trí trẻ em cuối cùng trong danh sách - Get relavtive's last position in the list
        for (int i = list.size() - 1; i >= 0; ) {
            Relative relative = list.get(i);
            health.setIdRelative(relative.getIdRelative());  //Đặt id mã trẻ em cho đối tượng sức khỏe - Set idRelative for object's Health
            Log.i(TAG, "insertHealth: " + relative.getIdRelative());
            break;
        }
        //Lưu health xuống SQLite
        if (dbHelper.insertHealth(health) > 0) {
            Log.d(TAG, "insertHealth: Success!!!");
        } else
            Log.d(TAG, "insertHealth: Failed!!!");
    }
    // [END insertHealth]


    /**
     * @author Nguyễn Thanh Tường
     * date: 24/05/2021 : 3h03p
     */
    //Thêm mũi tiêm trẻ em
    // [START insertInjection]
    private void insertInjection() {
        List<Injection> injections = dbHelper.getAllInjections(); //list dbHelper

        //Lấy vị trí trẻ em cuối cùng trong danh sách - Get relavtive's last position in the list
        for (int i = list.size() - 1; i >= 0; ) {
            Relative relative = list.get(i);
            for (Injection injection : injections) {
                String injectionTime = calTime(relative.getBirthDate(), injection.getinjectionMonth()); //Tạo thời gian tiêm phòng (injectionTime)
                DetailSchedule detailSchedule = new DetailSchedule(relative.getIdRelative(), injection.getIdInjection(), injectionTime, 0, 0);
                if (dbHelper.insertDetailSchedule(detailSchedule) > 0)
                    Log.d(TAG, "insertInjection: success!" + detailSchedule.toString());
            }
            break;
        }
    }
    // [END insertInjection]


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