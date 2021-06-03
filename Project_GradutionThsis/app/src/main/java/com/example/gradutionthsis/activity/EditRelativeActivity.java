package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Relative;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditRelativeActivity extends AppCompatActivity {

//    private static final String TAG = "EditRelativeActivity";

    private TextView txtBirthDate;
    private EditText edtFullName, edtNickName;
    private CircleImageView ivAvatar;
    private RadioGroup radioGroup;
    private RadioButton radSelect;

    Relative relative;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_relative);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.edit_info));   //Chỉnh sửa title trên ActionBar
        }

        dbHelper = new DBHelper(this);
        relative = reciveObject();

        edtFullName = findViewById(R.id.inputFullName);
        edtNickName = findViewById(R.id.inputNickName);
        ivAvatar = findViewById(R.id.imageView);
        txtBirthDate = findViewById(R.id.textBirthdate);

        radioGroup = findViewById(R.id.radioGroup);
        Button imgCalendar = findViewById(R.id.imageCalendar);

        Button btnBack = findViewById(R.id.buttonBack);
        Button btnEdit = findViewById(R.id.buttonEdit);

        RadioButton radFemale = findViewById(R.id.radioFemale);
        radFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                ivAvatar.setImageResource(R.drawable.female);
            else
                ivAvatar.setImageResource(R.drawable.male);
        });

        setData();

        imgCalendar.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            @SuppressLint("SetTextI18n")
            DatePickerDialog.OnDateSetListener callBack = (DatePicker datePicker, int year, int month, int dayOfMonth) -> (txtBirthDate = findViewById(R.id.textBirthdate)).setText((dayOfMonth) + "/" + (month + 1) + "/" + year);
            DatePickerDialog pic = new DatePickerDialog(EditRelativeActivity.this, callBack, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            pic.show();
        });

        //Sự kiện click button back - Handle back click
        btnBack.setOnClickListener(view -> {
            finish(); //Đóng activity hiện tại - close current activity
        });

        //Sự kiện click button edit - Handle edit click
        btnEdit.setOnClickListener(view -> {
            if (valid()) {
                updateRelative();
                finish();
            }
        });
    }


    /*
     * @author: Nguyễn Thanh Tường
     * date: 19/4/2021 : 19h06p
     * */
    //Phương thức xử lý sự kiện menu - Method of handling the menu event
    // [START onOptionsItemSelected]
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Xử lý sự kiện của nút back trên action bar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
     * @author: Nguyễn Thanh Tường
     * date: 07/4/2021 : 16h33p
     * */
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
        return true;
    }
    // [END valid]


    /*
     * @author: Nguyễn Thanh Tường
     * date: 07/4/2021 : 17h03p
     * */
    //Chuyển ImageView sang byte để lưu xuống database
    // [START ImageViewToByte]
    public byte[] imageViewToByte(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    // [END ImageViewToByte]


    /*
     * @author: Nguyễn Thanh Tường
     * date: 14/4/2021 : 10h42p
     * @id mã của thân nhân
     * */
    //Sửa thân nhân - update Relative
    // [START updateRelative]
    public void updateRelative() {
        if (relative != null) {
            relative.setFullName(edtFullName.getText().toString());
            relative.setNickName(edtNickName.getText().toString());
            relative.setGender(radSelect.getText().toString());
            relative.setBirthDate(txtBirthDate.getText().toString());
            relative.setAvatar(imageViewToByte(ivAvatar));

            if (dbHelper.updateRelative(relative) > 0) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END updateRelative]

    /*
     * @author: Nguyễn Thanh Tường
     * date: 16/4/2021 : 16h34p
     * */
    //Nhận dữ liệu thân nhân dược gửi đến - reciveObject
    // [START reciveObject]
    private Relative reciveObject() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            return (Relative) bundle.getSerializable("object");
        } else {
            Toast.makeText(this, "Dữ liệu rỗng ", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    // [END reciveObject]


    /*
     * @author: Nguyễn Thanh Tường
     * date: 16/4/2021 : 16h39p
     * */
    //Đặt dữ liệu cho các ô thông tin
    // [START setData]
    private void setData() {
        edtFullName.setText(relative.getFullName());
        edtNickName.setText(relative.getNickName());
        txtBirthDate.setText(relative.getBirthDate());

        if (relative.getGender().trim().equalsIgnoreCase(getResources().getString(R.string.male))) {
            radSelect = findViewById(R.id.radioMale);
        } else {
            radSelect = findViewById(R.id.radioFemale);
        }
        radSelect.setChecked(true);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(relative.getAvatar(), 0, relative.getAvatar().length, options));
    }
    // [END setData]


    //Kiểm tra trạng thái hoàn tất của mũi tiêm
    private int compareSatusInject(){
        List<Injection> injections = dbHelper.getTheSameDistance(0);

        for (Injection injection : injections){
            DetailSchedule detailSchedule = dbHelper.getDetailScheduleById(relative.getIdRelative(), injection.getIdInjection());
            if (detailSchedule.getStatus() == 1)
                return DetailInjectionActivity.STATUS_CODE;
        }

        return DetailInjectionActivity.CHECK_STATUS_CODE;
    }
}