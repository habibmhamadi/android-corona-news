package com.habib.coronanews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class CountryDetailActivity extends AppCompatActivity {
    private PieChartView pieChartView;
    private TextView txtDeaths,txtRecovered,txtCases,txtDate,txtCountry;
    private ImageView imgCountry;
    int mCases = 0,mDeaths=0,mRecovered=0,mActive=0;
    private Toolbar toolbar;
    String date = "";
    private List<SliceValue> list;
    private String countryName,countryImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);
        countryName = getIntent().getStringExtra("countryName");
        countryImg = getIntent().getStringExtra("countryImg");
        date = getIntent().getStringExtra("date");
        mCases = getIntent().getIntExtra("cases",0);
        mDeaths = getIntent().getIntExtra("deaths",0);
        mRecovered = getIntent().getIntExtra("recovered",0);
        mActive = getIntent().getIntExtra("active",0);
        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbarCountryDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pieChartView = findViewById(R.id.pieChartCountry);
        txtCases = findViewById(R.id.txtCasesCountry);
        txtDeaths = findViewById(R.id.txtDeathsCountry);
        txtRecovered = findViewById(R.id.txtRecoverdCountry);
        imgCountry = findViewById(R.id.imgCountry);
        txtCountry = findViewById(R.id.txtCountry);
        txtDate = findViewById(R.id.txtDateCountry);
        getData();

    }

    private void getData() {
        list = new ArrayList<>();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        txtRecovered.setText("Recovered: "+numberFormat.format(mRecovered));
        txtDeaths.setText("Deaths: "+numberFormat.format(mDeaths));
        txtCases.setText("Cases: "+numberFormat.format(mCases));
        txtCountry.setText(countryName);
        txtDate.setText(date);
        Picasso.get().load(countryImg).into(imgCountry);
        float total = mCases;
        float x = (mActive*100)/total;
        float y = (mRecovered*100)/total;
        float z = (mDeaths*100)/total;

        DecimalFormat decimalFormat = new DecimalFormat("##.00");

        list.add(new SliceValue(total*mActive,getResources().getColor(R.color.colorPrimary)).setLabel(decimalFormat.format(x)+"% active"));
        list.add(new SliceValue(total*mRecovered,getResources().getColor(R.color.colorGreen)).setLabel(decimalFormat.format(y)+"% recovered"));
        list.add(new SliceValue(total*mDeaths,getResources().getColor(R.color.colorRed)).setLabel(decimalFormat.format(z)+"% deaths"));
        PieChartData data = new PieChartData(list);
        data.setHasLabels(true).setCenterText1FontSize(14);
        pieChartView.setPieChartData(data);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
