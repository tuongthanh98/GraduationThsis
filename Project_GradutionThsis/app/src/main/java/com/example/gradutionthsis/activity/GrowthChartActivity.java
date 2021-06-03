package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;


import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.adapter.ChartDataAdapter;
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.listviewitems.ChartItem;
import com.example.gradutionthsis.listviewitems.LineChartItem;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GrowthChartActivity extends AppCompatActivity {
    private static final String TAG = "GrowthChartActivity";

    private Relative relative;
    private List<Health> healthList = new ArrayList<>();

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_growth_chart);

        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.statistical)); //Chỉnh sửa title trên ActionBar
        }

//        lineChart.setDrawBorders(false);    //Tắt đường viền bao quanh biểu đồ
//        lineChart.getAxisRight().setDrawLabels(false);  //Xóa cột bên phải của biểu đồ
//        lineChart.getXAxis().setDrawAxisLine(false);// Xóa cột dưới cùng của biểu đồ

        dbHelper = new DBHelper(this);
        relative = reciveObject();
        healthList = getListHealth();
        sortList();

        ListView lv = findViewById(R.id.listView1);
        ArrayList<ChartItem> list = new ArrayList<>();
        list.add(new LineChartItem(generateDataLine(1), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine(2), getApplicationContext()));
        ChartDataAdapter chartDataAdapter = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(chartDataAdapter);
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


    private LineData generateDataLine(int cnt) {
        ArrayList<Entry> values;

        if (cnt == 1) {
            values = lineChartDataSet(cnt);
            LineDataSet lineDataSet = new LineDataSet(values, getResources().getString(R.string.weight_of_child));
            lineDataSet.setLineWidth(2f);           //Chỉnh độ rộng của của đường
            lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
            lineDataSet.setCircleRadius(4f);        //Chỉnh độ to nhỏ của vòng tròn
            lineDataSet.setDrawCircleHole(false);   //Đặt vẽ lỗ cho Vòng tròn
//            lineDataSet.setDrawValues(true);       //Đặt giá trị của các entry/items

            ArrayList<ILineDataSet> iLineDataSet = new ArrayList<>();
            iLineDataSet.add(lineDataSet);

            return new LineData(iLineDataSet);
        } else {
            LineDataSet lineDataSet = new LineDataSet(lineChartDataSet(cnt), getResources().getString(R.string.height_of_child));
            lineDataSet.setLineWidth(2f);           //Chỉnh độ rộng của của đường
            lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
            lineDataSet.setCircleRadius(4f);        //Chỉnh độ to nhỏ của vòng tròn
            lineDataSet.setDrawCircleHole(false);   //Đặt vẽ lỗ cho Vòng tròn
//            lineDataSet.setDrawValues(true);       //Đặt giá trị của các entry/items
            lineDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            lineDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);

            ArrayList<ILineDataSet> iLineDataSet = new ArrayList<>();
            iLineDataSet.add(lineDataSet);

            return new LineData(iLineDataSet);
        }
    }


    private ArrayList<Entry> lineChartDataSet(int cnt) {
        ArrayList<Entry> dataSet = new ArrayList<>();

        if (healthList != null)
            if (cnt == 1)
                for (Health health : healthList) {
                    dataSet.add(new Entry(getTime(health.getTime()), (float) health.getWeight()));
                }
            else
                for (Health health : healthList) {
                    dataSet.add(new Entry(getTime(health.getTime()), (float) health.getHeight()));
                }
        return dataSet;
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 07/5/2021 1h44p
     */
    //Phương thức lấy từng tháng của items trong danh sách health
    // [START getTime]
    private int getTime(String time) {
        String pattern = "dd/MM/yyyy";
        try {
            @SuppressLint("SimpleDateFormat")
            Date date = new SimpleDateFormat(pattern).parse(time);
            Calendar c = Calendar.getInstance();
            if (date != null) {
                c.setTime(date);
                Log.d(TAG, "getMonth: " + c.get(Calendar.MONTH));
                return c.get(Calendar.MONTH) + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // [END getTime]


    /**
     * @author Nguyễn Thanh Tường
     * date: 02/5/2021 : 14h43p
     */
    //Lấy danh sách thân nhân từ cơ sở dữ liệu - Get a list of relatives from database
    // [START getListHealth]
    private List<Health> getListHealth() {
        if (relative == null) {
            Log.d(TAG, "getListHealth: The object Relative is null!");
            return null;
        } else {
            healthList = dbHelper.getAllHealthsById(relative.getIdRelative());
            if (healthList.size() > 0) {
                ArrayList<String> list_string = new ArrayList<>();
                for (Health health : healthList) {
                    list_string.add(String.valueOf(health.getWeight()));
                }
                return healthList;
            } else {
                Log.d(TAG, "getListHealth: null");
                return null;
            }
        }
    }
    // [END getListHealth]

    /**
     * @author Nguyễn Thanh Tường
     * date: 02/5/2021 : 14h43p
     */
    //Sắp xếp danh sách cân nặng/chiều cao của bé tăng dần theo tháng - Sort your baby's weight/height list in ascending order by month
    // [START sortList]
    private void sortList() {
        if (healthList != null) {
            //Sắp xếp danh sách tăng dần theo tháng
//            Collections.sort(healthList, (h1, h2) -> Integer.compare(getTime(h1.getTime()), getTime(h2.getTime())));
            healthList.sort((h1, h2) -> Integer.compare(getTime(h1.getTime()), getTime(h2.getTime())));
        } else {
            Log.d(TAG, "getListWeight: null");
        }
    }
    // [END sortList]

    /**
     * @return trả về đồi tượng relative được gửi từ ProfileFragment
     * @author: Nguyễn Thanh Tường
     * date: 01/5/2021 : 11h52p
     */
    //Nhận dữ liệu thân nhân dược gửi đến - reciveObject
    // [START reciveObject]
    private Relative reciveObject() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.d(TAG, "reciveObject: Nhận được object");
            return (Relative) bundle.getSerializable("object");
        } else {
            Log.d(TAG, "reciveObject: null");
        }
        return null;
    }
    // [END reciveObject]
}