package com.example.gradutionthsis.listviewitems;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

public abstract class ChartItem {
    static final int TYPE_BARCHART = 0;
    static final int TYPE_LINECHART = 1;
    static final int TYPE_PIECHART = 2;

    ChartData<?> mChartData;

    public ChartItem(ChartData<?> mcChartData) {
        this.mChartData = mcChartData;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View view, Context context);
}
