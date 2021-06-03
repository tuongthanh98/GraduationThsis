package com.example.gradutionthsis.listviewitems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.example.gradutionthsis.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.Utils;

public class LineChartItem extends ChartItem{
    private final Typeface mTf;

    public LineChartItem(ChartData<?> mcChartData, Context c) {
        super(mcChartData);
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }
    
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context context) {
        ViewHolder holder;
//        Utils.init(context);

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.getDescription().setEnabled(false);    //Tắt mô tả hóc dưới trong biểu đồ
        holder.chart.setDrawGridBackground(false);          //Tắt background chữ nhật nền đặt phía sau biểu đồ

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setTypeface(mTf);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        holder.chart.invalidate();              //Làm tươi biểu đồ - refresh
        holder.chart.animateX(750);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }
}
