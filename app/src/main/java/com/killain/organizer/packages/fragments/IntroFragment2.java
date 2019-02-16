package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.killain.organizer.R;

public class IntroFragment2 extends Fragment {

    private FloatingActionButton fab;

    public IntroFragment2() {
    }

    public static IntroFragment2 newInstance() {
        return new IntroFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro_fragment2, container, false);

        fab = view.findViewById(R.id.intro_frg2_fab);

        fab.setOnClickListener(v ->
                Toast.makeText(getContext(), "You are amazing! :)", Toast.LENGTH_SHORT).show());

        return view;
    }
}
