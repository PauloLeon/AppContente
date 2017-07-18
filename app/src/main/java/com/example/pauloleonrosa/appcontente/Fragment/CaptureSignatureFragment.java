package com.example.pauloleonrosa.appcontente.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pauloleonrosa.appcontente.R;

import capture.CaptureSignature;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pauloleonrosa on 21/03/17.
 */

public class CaptureSignatureFragment extends Fragment {

    public static final int SIGNATURE_ACTIVITY = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_assign, container, false);
        Button getSignature = (Button) rootView.findViewById(R.id.signature);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaptureSignature.class);
                startActivityForResult(intent,SIGNATURE_ACTIVITY);
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status  = bundle.getString("status");
                    if(status.equalsIgnoreCase("done")){
                        Toast toast = Toast.makeText(getActivity(), "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                }
                break;
        }

    }
}
