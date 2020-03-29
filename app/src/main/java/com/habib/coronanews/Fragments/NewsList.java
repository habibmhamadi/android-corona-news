package com.habib.coronanews.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.habib.coronanews.Adapters.NewsRecyclerAdapter;
import com.habib.coronanews.Constant;
import com.habib.coronanews.HomeActivity;
import com.habib.coronanews.Models.News;
import com.habib.coronanews.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsList extends Fragment {
    private View view;
    private Toolbar toolbar;
    private ArrayList<News> list;
    private RecyclerView recyclerView;
    private NewsRecyclerAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    public NewsList(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_news,container,false);
        init();
        return view;
    }

    private void init() {
        toolbar = view.findViewById(R.id.toolbarNews);
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recycler_news);
        refreshLayout = view.findViewById(R.id.refresh_news);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        StringRequest request = new StringRequest(Request.Method.GET, Constant.NEWS_URL, r->{
            try {
                JSONObject object = new JSONObject(r);
                if (object.getString("status").equals("ok")){
                    JSONArray array = object.getJSONArray("articles");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject article = array.getJSONObject(i);
                        News news = new News();
                        news.setTitle(article.getString("title"));
                        news.setDesc(article.getString("description"));
                        news.setDate(article.getString("publishedAt").substring(0,10));
                        news.setImage(article.getString("urlToImage"));
                        list.add(news);
                    }
                    adapter = new NewsRecyclerAdapter(getContext(),list);
                    recyclerView.setAdapter(adapter);
                }
                refreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(true);
            }
            refreshLayout.setRefreshing(false);
        },e->{
            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(true);
            refreshLayout.setRefreshing(false);
        }){

        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
