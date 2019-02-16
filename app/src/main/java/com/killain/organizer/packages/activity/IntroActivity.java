package com.killain.organizer.packages.activity;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import com.killain.organizer.packages.fragments.IntroFragment1;
import com.killain.organizer.packages.fragments.IntroFragment2;
import com.killain.organizer.packages.fragments.IntroFragment3;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_intro);

        addSlide(IntroFragment1.newInstance());
        addSlide(IntroFragment2.newInstance());
        addSlide(IntroFragment3.newInstance());
        showDoneButton(true);
    }

    @Override
    public void onDonePressed() {
        finish();
    }
}
