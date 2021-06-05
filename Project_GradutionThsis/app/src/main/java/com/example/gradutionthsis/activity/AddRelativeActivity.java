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

import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.presenter.DetailScheduleDAO;
import com.example.gradutionthsis.presenter.DetailSchedulePresenter;
import com.example.gradutionthsis.presenter.HealthDAO;
import com.example.gradutionthsis.presenter.HealthPresenter;
import com.example.gradutionthsis.presenter.RelativeDAO;
import com.example.gradutionthsis.presenter.RelativePresenter;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddRelativeActivity extends AppCompatActivity implements RelativeDAO, HealthDAO, DetailScheduleDAO {

    private final static String TAG = "RelativesActivity";

    private TextView txtBirthDate;
    private EditText edtFullName, edtNickName, edtWeight, edtHeight;
    private CircleImageView ivAvatar;
    private RadioGroup radioGroup;
    private RadioButton radSelect;
    private ProgressBar progressBar;

    RelativePresenter relativePresenter;
    HealthPresenter healthPresenter;
    DetailSchedulePresenter dsPresenter;

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

        relativePresenter = new RelativePresenter(this, this);
        healthPresenter = new HealthPresenter(this, this);
        dsPresenter = new DetailSchedulePresenter(this, this);

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

        relativePresenter.create(relative);
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
        int id = relativePresenter.getRelativeFinal().getIdRelative();

        health.setWeight(Double.parseDouble(edtWeight.getText().toString().trim()));
        health.setHeight(Double.parseDouble(edtHeight.getText().toString().trim()));
        health.setTime(txtBirthDate.getText().toString());

        healthPresenter.createHealth(health, id);


    }
    // [END insertHealth]


    /**
     * @author Nguyễn Thanh Tường
     * date: 24/05/2021 : 3h03p
     */
    //Thêm mũi tiêm trẻ em
    // [START insertInjection]
    private void insertInjection() {

        int id = relativePresenter.getRelativeFinal().getIdRelative();
        String birthDate = relativePresenter.getRelativeFinal().getBirthDate();

        dsPresenter.createDetailSchedule(id, birthDate);

    }
    // [END insertInjection]


    @Override
    public void createSuccess() {
        Toast.makeText(this, "Thành công!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createFail() {
        Toast.makeText(this, "Thất bại!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSuccess() {

    }

    @Override
    public void updateFail() {

    }

    @Override
    public void deleteSuccess() {

    }

    @Override
    public void deleteFail() {

    }
}