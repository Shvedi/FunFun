package com.androidkurs.micke.i_had_fun;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {
    private EditText Loginedit;
    private String logintry;
    private TwitterLoginButton twitterbtn;
    private Button loginbtn;
    private View view;
    private Controller controller;

    public DialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        initiate(view);
        addListenerOnButton(view);
        return view;

    }

    public void addListenerOnButton(View view) {
        loginbtn = (Button) view.findViewById(R.id.loginbutton);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.startLogin();
            }
        });
    //    twitterbtn = (TwitterLoginButton) view.findViewById(R.id.twitterbutton);
    //    twitterbtn.setOnClickListener(choiceListener);
    }

    public void setListener(){

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    public void initiate(View view) {
        String title = "Log in";
        setCancelable(false);
        getDialog().setTitle(title);
    }


    public void setController(Controller controller){
        this.controller = controller;
    }
}


