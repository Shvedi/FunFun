package com.androidkurs.micke.i_had_fun;


import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;



public class PlacesFragment extends DialogFragment {
    private RecyclerView rView;
    private PlacesAdapter rAdapter;
    private Button postTweetBtn;
    private Controller controller;
    private DataFragment dataFrag;
    private ArrayList<mPlace> placeList;


    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_places, container, false);
        this.controller =(Controller)((MainActivity) getActivity()).getController();
        this.dataFrag = controller.getDataFrag();
        this.placeList = dataFrag.getPlaceList();
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

        postTweetBtn = (Button) v.findViewById(R.id.postTweetBtn);
        rView = (RecyclerView) v.findViewById(R.id.placesRecycler);
        rAdapter = new PlacesAdapter(placeList, (MainActivity) getActivity());
        LinearLayoutManager lLM = new LinearLayoutManager(getContext());
        rView.setLayoutManager(lLM);
        rView.addItemDecoration(new ItemDecor(getActivity()));
        rView.setAdapter(rAdapter);

        postTweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.tweetBtnPressed(rAdapter.getPlace());
                rAdapter.setPlace(null);
            }
        });

    }

    private void regListeners() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        controller.placeFragDismissed();
    }
}
