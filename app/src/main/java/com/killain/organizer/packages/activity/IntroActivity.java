package com.killain.organizer.packages.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.IntroFragment1;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_intro);

        int drawableResourceId = this.getResources().getIdentifier("ic_custom_calendar", "drawable", this.getPackageName());

        addSlide(IntroFragment1.newInstance());

        addSlide(AppIntroFragment.newInstance("Title", "Description", drawableResourceId,
                Color.parseColor("#000000")));
        showDoneButton(true);
    }

    @Override
    public void onDonePressed() {
        finish();
    }
}
