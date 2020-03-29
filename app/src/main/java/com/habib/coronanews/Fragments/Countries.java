package com.habib.coronanews.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.habib.coronanews.Constant;
import com.habib.coronanews.Adapters.CountriesRecyclerAdapter;
import com.habib.coronanews.HomeActivity;
import com.habib.coronanews.Models.Country;
import com.habib.coronanews.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Countries extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private CountriesRecyclerAdapter adapter;
    private ArrayList<Country> list ;

    public  Countries (){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_countries,container,false);
        init();
        return view;
    }

    public void init(){
        toolbar = view.findViewById(R.id.toolbarCountries);
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recycler_countries);
        refreshLayout = view.findViewById(R.id.refresh_countries);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Constant c = new Constant();
        getCountries();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCountries();
            }
        });

    }

    private void getCountries(){
        refreshLayout.setRefreshing(true);
        list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.URL,r->{
            try {
                JSONObject object = new JSONObject(r);
                if (object.getInt("results")==206 || object.getInt("results")==200){
                    JSONArray array = object.getJSONArray("response");
                    for (int i=0;i<array.length();i++){
                        JSONObject country = array.getJSONObject(i);
                        if (!country.getString("country").equals("All")){
                            JSONObject cases = country.getJSONObject("cases");
                            JSONObject deaths = country.getJSONObject("deaths");
                            String newCases = "0",newDeaths="";
                            if (!cases.isNull("new")){
                                newCases = cases.getString("new");
                            }
                            if (!deaths.isNull("new")){
                                newDeaths = deaths.getString("new");
                            }
                            String name = country.getString("country");

                            String code = Constant.map.get(name.replaceAll("-"," "));
                            String flagUrl = "https://www.countryflags.io/"+code+"/shiny/64.png";

                            Country c = new Country();
                            c.setName(name);
                            c.setNewCases(newCases);
                            c.setActive(cases.getInt("active"));
                            c.setCritical(cases.getInt("critical"));
                            c.setRecovered(cases.getInt("recovered"));
                            c.setNewDeaths(newDeaths);
                            c.setTotalDeaths(deaths.getInt("total"));
                            c.setDay(country.getString("day"));
                            c.setFlag(flagUrl);
                            c.setTotalCases(cases.getInt("total"));
                            list.add(c);
                        }
                    }
                    adapter = new CountriesRecyclerAdapter(getContext(),list);
                    recyclerView.setAdapter(adapter);
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


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}
