package com.habib.coronanews.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.habib.coronanews.Constant;
import com.habib.coronanews.R;

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

public class Overall extends Fragment {
    private View view;
    private PieChartView pieChartView;
    private SwipeRefreshLayout refreshLayout;
    private TextView txtDeaths,txtRecovered,txtCases,txtDate;
    private ImageButton btnShowInfo;
    int mCases = 0,mDeaths=0,mRecovered=0;
    String date = "";
    private List<SliceValue> list;
    public  Overall (){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_overall,container,false);
        init();
        return view;
    }

    private void init(){
        btnShowInfo = view.findViewById(R.id.btnShowInfo);
        pieChartView = view.findViewById(R.id.pieChartWorld);
        refreshLayout = view.findViewById(R.id.refresh_world);
        txtCases = view.findViewById(R.id.txtCasesWorld);
        txtDeaths = view.findViewById(R.id.txtDeathsWorld);
        txtRecovered = view.findViewById(R.id.txtRecoverdWorld);
        txtDate = view.findViewById(R.id.txtDateWorld);
        getData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        btnShowInfo.setOnClickListener(v->{
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Info");
            builder.setMessage("\nCorona News v1.0\n\nby Habib Mhamadi");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Overall", "onClick: ");
                }
            }).setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Overall", "onClick: ");
                }
            });
            builder.show();
        });
    }

    private void getData(){
        refreshLayout.setRefreshing(true);
        list = new ArrayList<>();
        mCases=0;mDeaths=0;mRecovered=0;
        StringRequest request = new StringRequest(Request.Method.GET, Constant.URL, r->{
            try {
                JSONObject object = new JSONObject(r);

                if (object.getInt("results")==206 || object.getInt("results")==200){
                    JSONArray array = object.getJSONArray("response");
                    for (int i=0;i<array.length();i++){
                        JSONObject country = array.getJSONObject(i);
                        if (country.getString("country").equals("All")){
                            JSONObject cases = country.getJSONObject("cases");
                            JSONObject deaths = country.getJSONObject("deaths");
                            date = country.getString("day");

                            mCases+=cases.getInt("total");
                            mDeaths+=deaths.getInt("total");
                            mRecovered+=cases.getInt("recovered");
                        }

                    }


                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setGroupingUsed(true);

                    txtRecovered.setText("Recovered: "+numberFormat.format(mRecovered));
                    txtDeaths.setText("Deaths: "+numberFormat.format(mDeaths));
                    txtCases.setText("Cases: "+numberFormat.format(mCases));
                    txtDate.setText(date);
                    float total = mCases+mDeaths+mRecovered;
                    float x = (mCases*100)/total;
                    float y = (mRecovered*100)/total;
                    float z = (mDeaths*100)/total;

                    DecimalFormat decimalFormat = new DecimalFormat("##.00");

                    list.add(new SliceValue(total*mCases,getContext().getResources().getColor(R.color.colorPrimary)).setLabel(decimalFormat.format(x)));
                    list.add(new SliceValue(total*mRecovered,getContext().getResources().getColor(R.color.colorGreen)).setLabel(decimalFormat.format(y)));
                    list.add(new SliceValue(total*mDeaths,getContext().getResources().getColor(R.color.colorRed)).setLabel(decimalFormat.format(z)));
                    PieChartData data = new PieChartData(list);
                    data.setHasLabels(true).setCenterText1FontSize(14);
                    data.setHasCenterCircle(true).setCenterText1("COVID 19").setCenterText1Color(getContext().getResources().getColor(R.color.colorGrey1)).setCenterText1FontSize(15);
                    pieChartView.setPieChartData(data);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
            refreshLayout.setRefreshing(false);
        },e->{
            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("x-rapidapi-host",Constant.HOST);
                map.put("x-rapidapi-key",Constant.KEY);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}
