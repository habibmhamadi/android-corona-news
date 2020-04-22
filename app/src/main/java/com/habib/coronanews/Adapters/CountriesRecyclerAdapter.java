package com.habib.coronanews.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.habib.coronanews.CountryDetailActivity;
import com.habib.coronanews.HomeActivity;
import com.habib.coronanews.Models.Country;
import com.habib.coronanews.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

public class CountriesRecyclerAdapter extends RecyclerView.Adapter<CountriesRecyclerAdapter.Holder> {

    private Context context;
    private ArrayList<Country> list;
    private ArrayList<Country> listAll;
    private NumberFormat numberFormat = NumberFormat.getInstance();

    public CountriesRecyclerAdapter(Context context, ArrayList<Country> list){
        this.context=context;
        this.list =list;
        numberFormat.setGroupingUsed(true);
        this.listAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_country,parent,false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Country country = list.get(position);
        holder.txtNo.setText((position+1)+"");
        holder.txtCountryName.setText(country.getName());
        holder.txtTotalCases.setText(numberFormat.format(country.getTotalCases())+"");
        Picasso.get().load(country.getFlag()).into(holder.imgCountryFlag);
        if (country.getNewCases().equals("0")){
            holder.txtNewCases.setVisibility(View.GONE);
        }
        else {
            holder.txtNewCases.setVisibility(View.VISIBLE);
            holder.txtNewCases.setText(country.getNewCases()+" new");
        }
        holder.cardView.setOnClickListener(v->{
            Intent i = new Intent(((HomeActivity)context), CountryDetailActivity.class);
            i.putExtra("countryName",country.getName());
            i.putExtra("countryImg",country.getFlag());
            i.putExtra("date",country.getDay());
            i.putExtra("cases",country.getTotalCases());
            i.putExtra("recovered",country.getRecovered());
            i.putExtra("deaths",country.getTotalDeaths());
            i.putExtra("active",country.getActive());
            ((HomeActivity)context).startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

         private TextView txtNo,txtCountryName,txtNewCases,txtTotalCases;
         private ImageView imgCountryFlag;
         private CardView cardView;

         public Holder(@NonNull View itemView) {
             super(itemView);
             txtNewCases = itemView.findViewById(R.id.txtNewCases);
             txtTotalCases = itemView.findViewById(R.id.txtTotalCases);
             txtCountryName = itemView.findViewById(R.id.txtCountryName);
             txtNo = itemView.findViewById(R.id.txtNo);
             imgCountryFlag = itemView.findViewById(R.id.imgCountryFlag);
             cardView = itemView.findViewById(R.id.card_country);
         }
     }


    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Country> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredList.addAll(listAll);
            }
            else{
                for(Country post: listAll){
                    if(post.getName().toLowerCase().contains(constraint.toString().toLowerCase())
                            ){
                        filteredList.add(post);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Country>) results.values);
            notifyDataSetChanged();
        }
    };


}
