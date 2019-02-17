package com.killain.organizer.packages.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.killain.organizer.R

class IntroFragment1 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_intro_fragment1, container, false)
    }

    companion object {
        fun newInstance(): IntroFragment1 {
            return IntroFragment1()
        }
    }
}
