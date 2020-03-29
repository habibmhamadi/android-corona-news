package com.habib.coronanews.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.habib.coronanews.Models.News;
import com.habib.coronanews.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.Holder> {

    private Context context;
    private ArrayList<News> list;

    public NewsRecyclerAdapter(Context context , ArrayList<News> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        News news = list.get(position);
        holder.txtTitle.setText(news.getTitle());
        holder.txtDesc.setText(news.getDesc());
        holder.txtDate.setText(news.getDate());
        Picasso.get().load(news.getImage()).into(holder.imgNews);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView txtTitle,txtDesc,txtDate;
        private ImageView imgNews;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_news);
            txtTitle = itemView.findViewById(R.id.txtTitleNews);
            txtDesc = itemView.findViewById(R.id.txtDescNewss);
            txtDate = itemView.findViewById(R.id.txtDateNews);
            imgNews = itemView.findViewById(R.id.imgNews);
        }
    }
}
