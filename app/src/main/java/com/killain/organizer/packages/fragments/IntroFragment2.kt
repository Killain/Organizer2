package com.killain.organizer.packages.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.github.clans.fab.FloatingActionButton
import com.killain.organizer.R

class IntroFragment2 : Fragment() {

    private lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_intro_fragment2, container, false)

        fab = view.findViewById(R.id.intro_frg2_fab)

        fab.setOnClickListener { Toast.makeText(context, "You are amazing! :)", Toast.LENGTH_SHORT).show() }

        return view
    }

    companion object {
        fun newInstance(): IntroFragment2 {
            return IntroFragment2()
        }
    }
}
