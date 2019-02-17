package com.killain.organizer.packages.activity

import android.os.Bundle

import com.github.paolorotolo.appintro.AppIntro

import com.killain.organizer.packages.fragments.IntroFragment1
import com.killain.organizer.packages.fragments.IntroFragment2
import com.killain.organizer.packages.fragments.IntroFragment3

class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_intro);

        addSlide(IntroFragment1.newInstance())
        addSlide(IntroFragment2.newInstance())
        addSlide(IntroFragment3.newInstance())
        showDoneButton(true)
    }

    override fun onDonePressed() {
        finish()
    }
}
