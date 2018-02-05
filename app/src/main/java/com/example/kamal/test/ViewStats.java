package com.example.kamal.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.kamal.test.Constants.categoryType;


/**
 * Created by KAMAL on 2/5/2018.
 */

public class ViewStats extends AppCompatActivity {

    private DbHelper db;
    private Switch switchChart;
    private TextView chartType;
    private BarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stats);
        db = new DbHelper(this);

        initialiseViews();
        getBarChartDataForRange();
    }

    private void getBarChartDataForRange() {
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        ArrayList<String> dates = new ArrayList<>();
        dates.add(Constants.DATEFORMATTER.format(calendar.getTime()));
        for (int i = 1; i <= 6; i++) {
            calendar.add(Calendar.DATE, 1);
            dates.add(Constants.DATEFORMATTER.format(calendar.getTime()));
        }
        for (String date : dates) {
            double total = db.getExpenseForParticularDate(date);
            if (total != 0) {
                map.put(date, total);
                Log.w("DATE", "" + date + "----" + total);
            }
        }


        BarData data = new BarData(getBarChartXAxisValues(map), getBarChartDataSet(map));
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setData(data);
        barChart.animateXY(2000, 2000);
        barChart.invalidate();


    }

    private ArrayList<String> getBarChartXAxisValues(HashMap<String, Double> map) {
        ArrayList<String> xAxis = new ArrayList<>();
        for (Map.Entry<String, Double> stringDoubleEntry : map.entrySet()) {
            xAxis.add(stringDoubleEntry.getKey());
        }
        return xAxis;
    }

    private ArrayList<BarDataSet> getBarChartDataSet(HashMap<String, Double> map) {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet = new ArrayList<>();

        int pos = 0;
        for (Map.Entry<String, Double> stringDoubleEntry : map.entrySet()) {
            valueSet.add(new BarEntry(Float.parseFloat(String.valueOf(stringDoubleEntry.getValue())), pos));
            pos++;
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet, "Bar Chart");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();

        dataSets.add(barDataSet1);
        return dataSets;
    }

    private void initialiseViews() {
        switchChart = (Switch) findViewById(R.id.switchChart);
        chartType = (TextView) findViewById(R.id.chartType);
        barChart = (BarChart) findViewById(R.id.chart);
        pieChart = (PieChart) findViewById(R.id.piechart);

        switchChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getPieChartDataByCategory();
                } else {
                    getBarChartDataForRange();
                }

            }
        });
    }

    private void getPieChartDataByCategory() {
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
        for (String s : categoryType) {
            double total = db.getExpenseForParticularCategory(s);
            if (total != 0) {
                map.put(s, total);
                Log.w("CATEGORY", "" + s + "----" + total);
            }
        }

        pieChart.setUsePercentValues(true);
        PieDataSet dataSet = new PieDataSet(getPieChartYValues(map), "Pie Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(getPieChartXValues(map), dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
    }

    private List<String> getPieChartXValues(LinkedHashMap<String, Double> map) {
        ArrayList<String> xVals = new ArrayList<String>();

        for (Map.Entry<String, Double> stringDoubleEntry : map.entrySet()) {
            xVals.add(stringDoubleEntry.getKey());
        }

        return xVals;
    }

    private List<Entry> getPieChartYValues(LinkedHashMap<String, Double> map) {
        ArrayList<Entry> yvalues = new ArrayList<Entry>();

        int pos = 0;
        for (Map.Entry<String, Double> stringDoubleEntry : map.entrySet()) {
            yvalues.add(new Entry(Float.parseFloat(String.valueOf(stringDoubleEntry.getValue())), pos));
            pos++;
        }

        return yvalues;
    }
}
