package com.example.bytebills.ui.stockvalue;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.example.bytebills.Preferences.DEFAULT_LANGUAGE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.StockInfoWorker;
import com.example.bytebills.controller.StocksUserTransactionsWorker;
import com.example.bytebills.controller.StocksUserWorker;
import com.example.bytebills.model.BillGroup;
import com.example.bytebills.ui.addTransaction.AddTransactionFragment;
import com.example.bytebills.ui.home.StockAdapter;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
    private Float currentPrice = 0.0f;
    private Float totalCurrentPrice = 0f;
    private Float totalPurchasePrice = 0f;
    private Float totalDifference = 0f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_stockvalue);

        FloatingActionButton add = findViewById(R.id.fab);
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Double> allAmounts = new ArrayList<>();

        stock_id = getIntent().getStringExtra("stockSymbol");
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
                            String[] valueList = dataValues.replace("{", "").replace("}", "").split(",");
                            for (String value : valueList) {
                                dates.add(value.split("\":\"")[0].replace("\"", ""));
                                allAmounts.add(Double.valueOf(value.split("\":\"")[1].replace("\"", "")));
                            }
                            System.out.println(allAmounts);
                            renderData(dates, allAmounts);

                        } catch (NullPointerException | IndexOutOfBoundsException e){
                            System.out.println("NO DATA");
                        }

                    }
                });

        WorkManager.getInstance().enqueue(stockInfoWork);

        String username = MainActivity.username;

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

        Data data2 = new Data.Builder()
                .putString("username", username)
                .putString("symbol", stock_id)
                .build();

        OneTimeWorkRequest stockTransacitonsUserWork =
                new OneTimeWorkRequest.Builder(StocksUserTransactionsWorker.class)
                        .setInputData(data2)
                        .build();


        WorkManager.getInstance().getWorkInfoByIdLiveData(stockTransacitonsUserWork.getId())
                .observe(StockValueActivity.this, status -> {
                    if (status != null && status.getState().isFinished()) {
                        String [] transacciones= String.valueOf(status).split("transactions :")[1].replace(", }","!").split("!")[0].split("],");
                        for (String transaccion : transacciones){
                            List<String> elementos = Arrays.asList(transaccion.replace("[", "").replace("]","").split(","));
                            if (elementos.size()==4){
                                System.out.println("Dividendo");
                                dividendDateList.add(elementos.get(0));
                                dividendValueList.add(elementos.get(3).replaceAll("\"","")+"€");
                                totalCurrentPrice += Float.valueOf(elementos.get(3).replaceAll("\"",""));
                            } else if (elementos.size()==8) {
                                System.out.println("Compra");
                                purchaseDateList.add(elementos.get(4));
                                purchasePriceList.add(elementos.get(2).replaceAll("\"",""));
                                purchaseValueList.add(String.format("%.2f",Float.valueOf(elementos.get(3).replaceAll("\"",""))*Float.valueOf(elementos.get(2).replaceAll("\"",""))));
                                currentValueList.add(String.format("%.2f",Float.valueOf(elementos.get(3).replaceAll("\"",""))*currentPrice));
                                differenceList.add(String.format("%.2f",100.0f*(((Float.valueOf(elementos.get(3).replaceAll("\"",""))*currentPrice)/(Float.valueOf(elementos.get(3).replaceAll("\"",""))*Float.valueOf(elementos.get(2).replaceAll("\"",""))))-1.0f))+"%");
                                totalPurchasePrice += Float.valueOf(elementos.get(3).replaceAll("\"",""))*Float.valueOf(elementos.get(2).replaceAll("\"",""));
                                totalCurrentPrice += Float.valueOf(elementos.get(3).replaceAll("\"",""))*currentPrice;
                                opStateList.add("Open");
                            }else if (elementos.size()==1){
                                currentPrice = Float.valueOf(elementos.get(0).replaceAll("\"",""));
                            }
                        }
                        totalDifference = 100f*(totalCurrentPrice/totalPurchasePrice-1f);


                        TextView differenceValueTotalView = findViewById(R.id.textViewPercentDiffValue);
                        differenceValueTotalView.setText(String.format("%.2f", totalDifference) + "%");

                        TextView totalValueView = findViewById(R.id.textViewTotalValue);
                        totalValueView.setText(String.valueOf(totalCurrentPrice));

                        TextView valueDiffValueView = findViewById(R.id.textViewValueDiffValue);
                        valueDiffValueView.setText(String.valueOf(totalCurrentPrice - totalPurchasePrice));

                        horizontalAdapter = new DividenAdapter(dividendDateList, dividendValueList, dividendPercentageList);
                        verticalAdapter = new PurchaseAdapter(purchaseDateList,purchasePriceList,purchaseValueList,currentValueList,differenceList,opStateList);

                        mLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                        mLayoutManagerVertical = new LinearLayoutManager(this);

                        rvHorizontal.setLayoutManager(mLayoutManagerHorizontal);
                        rvVertical.setLayoutManager(mLayoutManagerVertical);

                        rvHorizontal.setAdapter(horizontalAdapter);
                        rvVertical.setAdapter(verticalAdapter);
                    }
                });

        WorkManager.getInstance().enqueue(stockTransacitonsUserWork);



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