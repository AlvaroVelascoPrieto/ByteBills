package com.example.bytebills.ui.billgroup;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bytebills.R;

import java.util.ArrayList;
public class BillFragment extends AppCompatActivity {
    private RecyclerView rvVertical, rvHorizontal;
    private ArrayList<String> titleDataList;
    private ArrayList<String> messageDataList;
    private RecyclerView.LayoutManager mLayoutManagerHorizontal;
    private RecyclerView.LayoutManager mLayoutManagerVertical;
    private BillAdapter verticalAdapter;
    private BillAdapter horizontalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_group_activity);

        rvVertical = (RecyclerView) findViewById(R.id.rvVertical);
        rvHorizontal = (RecyclerView) findViewById(R.id.rvHorizontal);
        titleDataList = new ArrayList<>();
        messageDataList = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            titleDataList.add("Person " + i);
            messageDataList.add("Bill " + i);
        }

        //________initialize adapters
        verticalAdapter = new BillAdapter(titleDataList, false);
        horizontalAdapter = new BillAdapter(titleDataList, true);

        //________initialize layout managers
        mLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerVertical = new LinearLayoutManager(this);

        //________set layout managers
        rvHorizontal.setLayoutManager(mLayoutManagerHorizontal);
        rvVertical.setLayoutManager(mLayoutManagerVertical);

        //________set adapters
        rvHorizontal.setAdapter(horizontalAdapter);
        rvVertical.setAdapter(verticalAdapter);
    }
}