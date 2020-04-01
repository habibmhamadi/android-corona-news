package com.habib.coronanews.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Overall extends Fragment {
    private View view;
    private PieChartView pieChartView;
    private SwipeRefreshLayout refreshLayout;
    private TextView txtDeaths,txtRecovered,txtCases,txtDate,txtLastUpdate;
    private ImageButton btnShowInfo;
    int mCases = 0,mDeaths=0,mRecovered=0;
    String date = "";
    private List<SliceValue> list;
    private  NumberFormat numberFormat = NumberFormat.getInstance();
    private Calendar calendar;

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
        txtLastUpdate = view.findViewById(R.id.txtLastUpdateOverall);
        numberFormat.setGroupingUsed(true);
        calendar = Calendar.getInstance();
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
                JSONArray errors = object.getJSONArray("errors");
                if (errors.length()==0){
                    JSONArray array = object.getJSONArray("response");
                    for (int i=0;i<array.length();i++){
                        JSONObject country = array.getJSONObject(i);
                        if (country.getString("country").equals("All")){
                            JSONObject cases = country.getJSONObject("cases");
                            JSONObject deaths = country.getJSONObject("deaths");
                            String time = country.getString("time").substring(10,16);
                            time = time.replaceAll("[A-Z]+","");
                            date = country.getString("day");
                            String DATE_FORMAT = "yyyy-MM-dd hh:mm";
                            String dateInString = date+" "+time+"";
                            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                            Date d = formatter.parse(dateInString);
                            calendar.setTime(d);
                            calendar.add(Calendar.MINUTE,270);
                            mCases+=cases.getInt("total");
                            mDeaths+=deaths.getInt("total");
                            mRecovered+=cases.getInt("recovered");
                            String h = calendar.get(Calendar.HOUR)+"";
                            String m = calendar.get(Calendar.MINUTE)+"";
                            if (Integer.parseInt(h)<10) h = "0"+h;
                            if (Integer.parseInt(m)<10) m = "0"+m;
                            txtLastUpdate.setText("Last update: "+h+":"+m);

                        }

                    }


                    txtRecovered.setText("Recovered: "+numberFormat.format(mRecovered));
                    txtDeaths.setText("Deaths: "+numberFormat.format(mDeaths));
                    txtCases.setText("Cases: "+numberFormat.format(mCases));

                    txtDate.setText("Update every 15 minutes");
                    float total = mCases+mDeaths+mRecovered;
                    float x = (mCases*100)/total;
                    float y = (mRecovered*100)/total;
                    float z = (mDeaths*100)/total;

                    DecimalFormat decimalFormat = new DecimalFormat("##.00");

                    list.add(new SliceValue(total*mCases,getContext().getResources().getColor(R.color.colorPrimary)).setLabel(decimalFormat.format(x)+"% active"));
                    list.add(new SliceValue(total*mRecovered,getContext().getResources().getColor(R.color.colorGreen)).setLabel(decimalFormat.format(y)+"% recovered"));
                    list.add(new SliceValue(total*mDeaths,getContext().getResources().getColor(R.color.colorRed)).setLabel(decimalFormat.format(z)+"% deaths"));
                    PieChartData data = new PieChartData(list);
                    data.setHasLabels(true).setCenterText1FontSize(14);
                    pieChartView.setPieChartData(data);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
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
