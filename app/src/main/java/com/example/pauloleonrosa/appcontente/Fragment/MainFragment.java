package com.example.pauloleonrosa.appcontente.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pauloleonrosa.appcontente.R;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       /* Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=av.duque de caxias 1735+belem"));
        startActivity(intent); */
        return inflater.inflate(R.layout.fragment_main,container,false);
    }
}
