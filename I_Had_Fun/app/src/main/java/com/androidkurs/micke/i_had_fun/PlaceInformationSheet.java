package com.androidkurs.micke.i_had_fun;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Gustaf Bohlin on 29/09/2017.
 */

public class PlaceInformationSheet extends BottomSheetDialogFragment {

    private View rootView;
    private TextView placeInfoTitle, placeInfo;
    private ImageView placeInfoImage;
    private LinearLayout placeInfoLayout;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_place_information, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0);
        return rootView;
    }

    private void initializeComponents() {
        placeInfoLayout = (LinearLayout) rootView.findViewById(R.id.placeInfoLayout);
        placeInfoTitle = (TextView) rootView.findViewById(R.id.placeInfo_title);
        placeInfo = (TextView) rootView.findViewById(R.id.placeInfo);
        placeInfoImage = (ImageView) rootView.findViewById(R.id.placePhoto);

    }

    @Override
    public void onResume() {
        super.onResume();
        initializeComponents();
        ((MainActivity)getActivity()).getController().updateSheetContent(this);

    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.v("USERINFORMATIONFRAGMENT", "dismissed");
        super.onDismiss(dialog);
    }

    public void setContent(mPlace placeToDisplay) {
       Bitmap bitmap = placeToDisplay.getBitmap();
        if(bitmap!=null){
            placeInfoImage.setImageBitmap(placeToDisplay.getBitmap());
        }
        placeInfoTitle.setText(placeToDisplay.getName());

        placeInfo.setText(placeToDisplay.getMsg());

    }
}
