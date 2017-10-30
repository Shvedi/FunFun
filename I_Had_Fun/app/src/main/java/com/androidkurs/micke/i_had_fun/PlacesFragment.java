package com.androidkurs.micke.i_had_fun;


import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;



public class PlacesFragment extends DialogFragment {
    private RecyclerView rView;
    private PlacesAdapter rAdapter;
    private Button postTweetBtn;
    private Controller controller;
    private DataFragment dataFrag;
    private ArrayList<mPlace> placeList;
    private Button happybtn,happierbtn,veryhappybtn,happiestbtn;
    private String fun;
    private int buttonHighlighted;
    private boolean isHighlighted;
    private int happy;



    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.Theme){
            @Override
            public void onBackPressed() {
               controller.placeFragDismissed();
            }
        };
    }
    @Override public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParam = window.getAttributes();
        windowParam.dimAmount = 0.50f;
        windowParam.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParam);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_places, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.controller =(Controller)((MainActivity) getActivity()).getController();
        this.dataFrag = controller.getDataFrag();
        getDialog().setCanceledOnTouchOutside(false);
        this.placeList = dataFrag.getPlaceList();
        isHighlighted = false;
        initializeComponents(v);
        regListeners();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //animation();

    }

    private void animation(float toX){
        float x = getView().getX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(getView(), "x", x, toX);
        animator.setDuration(4000);
        animator.start();

    }

    private void initializeComponents(View v) {
        View.OnClickListener choiceListener = new ChoiceButtonListener();
        postTweetBtn = (Button) v.findViewById(R.id.postTweetBtn);
        rView = (RecyclerView) v.findViewById(R.id.placesRecycler);
        rAdapter = new PlacesAdapter(placeList, (MainActivity) getActivity());
        LinearLayoutManager lLM = new LinearLayoutManager(getContext());
        rView.setLayoutManager(lLM);
       // rView.addItemDecoration(new ItemDecor(getActivity()));
        rView.setAdapter(rAdapter);


        happybtn = (Button) v.findViewById(R.id.happybutton);
        happierbtn = (Button) v.findViewById(R.id.happierbutton);
        veryhappybtn = (Button) v.findViewById(R.id.veryhappybutton);
        happiestbtn = (Button) v.findViewById(R.id.happiestbutton);
        happybtn.setOnClickListener(choiceListener);
        happierbtn.setOnClickListener(choiceListener);
        veryhappybtn.setOnClickListener(choiceListener);
        happiestbtn.setOnClickListener(choiceListener);


        postTweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHighlighted&&rAdapter.getPlace()!=null) {
                    controller.tweetBtnPressed(rAdapter.getPlace());
                    isHighlighted = false;
                    rAdapter.setPlace(null);
                }
                else{
                    Toast.makeText(getActivity(),"Choose happiness-level and/or a place!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void regListeners() {
    }


    private class ChoiceButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            if (v.getId() == R.id.happybutton) {
                fun = "I had fun at ";

                controller.setFunString(fun);
                highlightButton(1);

                happy = 1;
                controller.setFunString(fun);
                controller.setHappy(1);

            }
            else if(v.getId() == R.id.happierbutton) {
                fun = "I had very fun at ";
                happy = 2;
                controller.setFunString(fun);

                highlightButton(2);

                controller.setHappy(2);


            }
            else if(v.getId() == R.id.veryhappybutton) {
                fun = "I had a blast at ";
                happy = 3;
                controller.setFunString(fun);

                highlightButton(3);

                controller.setHappy(3);


            }
            else if(v.getId() == R.id.happiestbutton) {
                fun = "It was amazing at ";
                happy = 4;
                controller.setFunString(fun);

                highlightButton(4);

                controller.setHappy(4);

            }
        }
    }

    private void highlightButton(int i) {
        happierbtn.setElevation(50);
        resetHighlight();
        switch (i){
            case 1:
                if(isHighlighted && buttonHighlighted == 1){
                    resetHighlight();
                    isHighlighted = false;
                    break;
                }
                happybtn.setBackground(getResources().getDrawable(R.drawable.happy1highlight,null));
                isHighlighted = true;
                buttonHighlighted = 1;
                break;
            case 2:
                if(isHighlighted && buttonHighlighted == 2){
                    resetHighlight();
                    isHighlighted = false;
                    break;
                }
                happierbtn.setBackground(getResources().getDrawable(R.drawable.happy2highlight,null));
                isHighlighted = true;
                buttonHighlighted = 2;
                break;
            case 3:
                if(isHighlighted && buttonHighlighted == 3){
                    resetHighlight();
                    isHighlighted = false;
                    break;
                }
                veryhappybtn.setBackground(getResources().getDrawable(R.drawable.happy3highlight,null));
                isHighlighted = true;
                buttonHighlighted = 3;
                break;
            case 4:
                if(isHighlighted && buttonHighlighted == 4){
                    resetHighlight();
                    isHighlighted = false;
                    break;
                }
                happiestbtn.setBackground(getResources().getDrawable(R.drawable.happy4highlight,null));
                isHighlighted = true;
                buttonHighlighted = 4;
        }
    }

    public boolean isHighlighted(){
        return isHighlighted;
    }
    private void resetHighlight(){
        happybtn.setBackground(getResources().getDrawable(R.drawable.happy1));
        happierbtn.setBackground(getResources().getDrawable(R.drawable.happy2));
        veryhappybtn.setBackground(getResources().getDrawable(R.drawable.happy3));
        happiestbtn.setBackground(getResources().getDrawable(R.drawable.happy4));
    }



}
