package se.af8143mah.assignment3;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogueFragment extends DialogFragment {
    private EditText Loginedit;
    private Button loginbtn;
    private String logintry;


    public DialogueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialogue, container, false);
        initiate(view);
        addListenerOnButton(view);
        return view;

    }

    public void addListenerOnButton(View view) {
        View.OnClickListener choiceListener = new ChoiceButtonListener();
        loginbtn = (Button) view.findViewById(R.id.loginbutton);
        loginbtn.setOnClickListener(choiceListener);
    }

    public void initiate(View view) {
        Loginedit = (EditText) view.findViewById(R.id.editname);
        Loginedit.setText("hej");
        String title = "Log in";
        setCancelable(false);
        getDialog().setTitle(title);

    }

    private class ChoiceButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v.getId() == R.id.loginbutton) {
                logintry = Loginedit.getText().toString();
                if(logintry.equals("hej")){
                    //setCancelable(true);
                    //onStop();
                    onDestroyView();
                }
            }
        }
    }


}


