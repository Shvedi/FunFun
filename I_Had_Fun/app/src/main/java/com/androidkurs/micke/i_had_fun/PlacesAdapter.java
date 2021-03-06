package com.androidkurs.micke.i_had_fun;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hello on 2017-10-19.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    private MainActivity activity;
    private ArrayList<mPlace> dataList;
    private mPlace currentPlace;

    public PlacesAdapter(ArrayList<mPlace> placeList, MainActivity mainActivity) {
        this.activity = mainActivity;
        this.dataList = placeList;
    }

    public void swap(ArrayList<mPlace> placeList) {
        this.dataList = placeList;
        this.notifyDataSetChanged();
    }

    public void setPlace(mPlace place) {
        this.currentPlace = place;
    }
    public mPlace getPlace(){
        return this.currentPlace;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
     //   private ImageView imView;
        private FrameLayout cardView;
        private FrameLayout cardFrame;




        public MyViewHolder(View v) {
            super(v);
          //  imView = (ImageView)v.findViewById(R.id.imView);
            name = (TextView)v.findViewById(R.id.placeName);
            cardView = (FrameLayout)v.findViewById(R.id.placeCard);


        }


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_cardview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.transparent,null));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<dataList.size(); i++){
                    if(i==position){
                        mPlace place = dataList.get(i);
                        place.setSelected(!place.isSelected());
                        if(place.isSelected()){

                        }else{

                        }


                    }else{
                        dataList.get(i).setSelected(false);
                    }
                }
                swap(dataList);



            }
        });
        if(dataList.get(position).isSelected()){
            holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary,null));
           // holder.imView.setImageDrawable(activity.getDrawable(R.drawable.check));
            setPlace(dataList.get(position));
        }else{
            holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.transparent,null));
            //holder.imView.setImageDrawable(null);
        }
        String id = dataList.get(position).getId();
        /*if(activity.containsId(dataList.get(position).getId())){
            holder.imView.setImageBitmap(activity.getBitmap(id));
        }*/
        holder.name.setText(this.dataList.get(position).getName());



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
