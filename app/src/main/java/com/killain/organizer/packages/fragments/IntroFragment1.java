package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.killain.organizer.R;

public class IntroFragment1 extends Fragment {

    public IntroFragment1() {
    }

    public static IntroFragment1 newInstance() {
        return new IntroFragment1();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_fragment1, container, false);

    }

}
