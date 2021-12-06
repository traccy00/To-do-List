package com.example.todolist;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.common.Enums;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {

    private PieChart pieChart;
    private BarChart barChart;

    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 100;
    private static final int MIN_Y_VALUE = 0;
    private static final String SET_LABEL = "Task Complete %";
    private static final String[] DAYS = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private int LAST_7_DAYS = -7;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //pieChart
        pieChart = view.findViewById(R.id.piechart);
        setupPieChart();
        loadPieChartData();
        //barChart
        barChart = view.findViewById(R.id.barchart);
        BarData data = getBarChartData();
        configureChartAppearance();
        prepareChartData(data);
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
//        pieChart.setCenterText("Spending by Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    private void loadPieChartData() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.7f, Enums.TaskStatus.COMPLETED.getTaskStatus()));
        entries.add(new PieEntry(0.3f, Enums.TaskStatus.NOT_COMPLETED.getTaskStatus()));

        List<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void configureChartAppearance() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        //XAxis bottom
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
//                return DAYS[(int) value];
//                List<String> dateList = getLast7Days();
                return getLast7Days().get((int) value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-90);
        //YAxis left
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);
        //YAxis Right
        barChart.getAxisRight().setEnabled(false);
    }

    private BarData getBarChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        //find last 7 days
        List<String> dateList = getLast7Days();
        //get task info in 7 days
        AppDatabase db = Room.databaseBuilder(this.getContext(), AppDatabase.class, Constant.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        TaskDAO taskDAO = db.taskDAO();
        float i = 0;
        for (String date : dateList) {
            //task percent completed
            int countAllTaskInDate = taskDAO.countAllTasksInDate(date);
            int countCompletedTaskInDate = taskDAO.countIsDoneTasksInDate(date, 1);
            float percentCompleted = 0f;
            if (countAllTaskInDate != 0) {
                percentCompleted = ((float) (countCompletedTaskInDate) / (float) countAllTaskInDate) * 100;
            }
            values.add(new BarEntry(i, percentCompleted));
            i++;
        }
        BarDataSet set1 = new BarDataSet(values, SET_LABEL);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        barChart.animateY(1400, Easing.EaseInOutQuad);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setData(data);
        barChart.invalidate();
    }

    private List<String> getLast7Days() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, LAST_7_DAYS);
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInLast7Days = "";
        for (int i = 0; i <= 6; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dateInLast7Days = sdf.format(calendar.getTime());
            if (dateInLast7Days.startsWith("0")) {
                dateInLast7Days = dateInLast7Days.substring(1);
            }
            dateList.add(dateInLast7Days);
        }
        return dateList;
    }
}