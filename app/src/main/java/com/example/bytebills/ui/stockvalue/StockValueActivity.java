package com.example.bytebills.ui.stockvalue;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.R;
import com.example.bytebills.controller.StockInfoWorker;
import com.example.bytebills.ui.addTransaction.AddTransactionFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StockValueActivity extends AppCompatActivity {
    private RecyclerView rvVertical, rvHorizontal;
    private ArrayList<String> dividendDateList;
    private ArrayList<String> dividendValueList;
    private ArrayList<String> dividendPercentageList;
    private ArrayList<String> purchaseDateList;
    private ArrayList<String> purchasePriceList;
    private ArrayList<String> purchaseValueList;
    private ArrayList<String> currentValueList;
    private ArrayList<String> differenceList;
    private ArrayList<String> opStateList;
    private RecyclerView.LayoutManager mLayoutManagerHorizontal;
    private RecyclerView.LayoutManager mLayoutManagerVertical;
    private PurchaseAdapter verticalAdapter;
    private DividenAdapter horizontalAdapter;
    LineChart volumeReportChart;

    public static String stock_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_stockvalue);

        FloatingActionButton add = findViewById(R.id.fab);
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Double> allAmounts = new ArrayList<>();

        stock_id = getIntent().getStringExtra("stockSymbol");
        System.out.println("STOCKID");
        System.out.println(stock_id);
        TextView title = findViewById(R.id.tvSesionGraph);
        title.setText(stock_id);
        Data data = new Data.Builder()
                .putString("stock_id", stock_id)
                .build();

        OneTimeWorkRequest stockInfoWork =
                new OneTimeWorkRequest.Builder(StockInfoWorker.class)
                        .setInputData(data)
                        .build();


        WorkManager.getInstance().getWorkInfoByIdLiveData(stockInfoWork.getId())
                .observe(StockValueActivity.this, status -> {
                    if (status != null && status.getState().isFinished()) {
                        String queryStatus = status.getOutputData().getString("value");
                        try {
                            String dataValues =status.getOutputData().getString("value");

                            System.out.println("GOO");
                            System.out.println(dataValues);
                            String [] valueList = dataValues.replace("{","").replace("}","").split(",");
                            for (String value: valueList){
                                dates.add(value.split("\":\"")[0].replace("\"",""));
                                allAmounts.add(Double.valueOf(value.split("\":\"")[1].replace("\"","")));
                            }
                            System.out.println(allAmounts);
                            renderData(dates, allAmounts);
                        } catch (NullPointerException e){
                            System.out.println("NO DATA");
                        }

                    }
                });

        WorkManager.getInstance().enqueue(stockInfoWork);



        volumeReportChart = findViewById(R.id.reportingChart);

        rvVertical = findViewById(R.id.rvVertical);
        rvHorizontal = findViewById(R.id.rvHorizontal);
        dividendDateList = new ArrayList<>();
        dividendValueList = new ArrayList<>();
        dividendPercentageList = new ArrayList<>();
        purchaseDateList = new ArrayList<>();
        purchasePriceList = new ArrayList<>();
        purchaseValueList = new ArrayList<>();
        currentValueList = new ArrayList<>();
        differenceList = new ArrayList<>();
        opStateList = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dividendDateList.add(LocalDateTime.now().minusDays(i).toString());
                dividendValueList.add(String.valueOf(40.3*i) + "€");
                dividendPercentageList.add(String.valueOf(i) + "%");

                purchaseDateList.add(LocalDateTime.now().minusDays(i).toString());
                purchasePriceList.add(String.valueOf(0.67*i) + "€");
                purchaseValueList.add(String.valueOf(67.0*i) + "€");
                currentValueList.add(String.valueOf(97.0*i) + "€");
                differenceList.add(String.valueOf(2*i) + "%");
                opStateList.add("Open");
            }
        }

        //________initialize adapters
        horizontalAdapter = new DividenAdapter(dividendDateList, dividendValueList, dividendPercentageList);
        verticalAdapter = new PurchaseAdapter(purchaseDateList,purchasePriceList,purchaseValueList,currentValueList,differenceList,opStateList);

        //________initialize layout managers
        mLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerVertical = new LinearLayoutManager(this);

        //________set layout managers
        rvHorizontal.setLayoutManager(mLayoutManagerHorizontal);
        rvVertical.setLayoutManager(mLayoutManagerVertical);

        //________set adapters
        rvHorizontal.setAdapter(horizontalAdapter);
        rvVertical.setAdapter(verticalAdapter);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StockValueActivity.this, AddTransactionFragment.class);
                startActivity(i);
            }
        });


    }

    public void renderData(ArrayList<String> dates, ArrayList<Double> allAmounts) {


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("1");
        xAxisLabel.add("7");
        xAxisLabel.add("14");
        xAxisLabel.add("21");
        xAxisLabel.add("28");
        xAxisLabel.add("30");

        XAxis xAxis = volumeReportChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.enableGridDashedLine(2f, 7f, 0f);
        xAxis.setAxisMaximum(allAmounts.size()-1);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(allAmounts.size(), true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(7f);
        xAxis.setLabelRotationAngle(315f);

        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawLimitLinesBehindData(true);





        //LimitLine ll2 = new LimitLine(35f, "");
        //ll2.setLineWidth(4f);
        //ll2.enableDashedLine(10f, 10f, 0f);
        //ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        //ll2.setTextSize(10f);
        //ll2.setLineColor(Color.parseColor("#FFFFFF"));

        xAxis.removeAllLimitLines();
        //xAxis.addLimitLine(ll2);


        YAxis leftAxis = volumeReportChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        //leftAxis.addLimitLine(ll1);
        //leftAxis.addLimitLine(ll2);

        leftAxis.setAxisMaximum((float) ((Collections.max(allAmounts).floatValue() + Collections.max(allAmounts).floatValue()*0.001)));
        leftAxis.setAxisMinimum((float) ((Collections.min(allAmounts).floatValue() - Collections.max(allAmounts).floatValue()*0.001)));
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        //XAxis xAxis = mBarChart.getXAxis();
        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());

        volumeReportChart.getDescription().setEnabled(true);
        Description description = new Description();
        // description.setText(UISetters.getFullMonthName());//commented for weekly reporting
        description.setText("DAY");
        description.setTextSize(15f);
        volumeReportChart.getDescription().setPosition(0f, 0f);
        volumeReportChart.setDescription(description);
        volumeReportChart.getAxisRight().setEnabled(false);

        //setData()-- allAmounts is data to display;
        setDataForWeeksWise(allAmounts);

    }

    private void setDataForWeeksWise(List<Double> amounts) {

        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        ArrayList<Entry> values = new ArrayList<>();
        Iterator<Double> itr = amounts.iterator();
        int i=0;
        while (itr.hasNext()){
            values.add(new Entry( i, Float.valueOf(df.format(itr.next()))));
            i++;
        }



        LineDataSet set1;
        if (volumeReportChart.getData() != null &&
                volumeReportChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) volumeReportChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            volumeReportChart.getData().notifyDataChanged();
            volumeReportChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Price");
            set1.setDrawCircles(true);
            set1.enableDashedLine(10f, 0f, 0f);
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setColor(getResources().getColor(R.color.purple_200));
            set1.setCircleColor(getResources().getColor(R.color.purple_200));
            set1.setLineWidth(2f);//line size
            set1.setCircleRadius(4f);
            set1.setDrawCircleHole(true);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(5f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);

            if (Utils.getSDKInt() >= 18) {
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.blue_bg);
//                set1.setFillDrawable(drawable);
                set1.setFillColor(Color.WHITE);

            } else {
                set1.setFillColor(Color.WHITE);
            }
            set1.setDrawValues(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);

            volumeReportChart.setData(data);
        }
    }

}