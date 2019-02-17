package com.killain.organizer.packages.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.github.clans.fab.FloatingActionButton
import com.killain.organizer.R
import com.killain.organizer.packages.models.Task
import com.killain.organizer.packages.recyclerview_adapters.IntroRVAdapter
import java.util.ArrayList

class IntroFragment3 : Fragment() {

    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: IntroRVAdapter? = null
//    private val recyclerViewInteractor: RecyclerViewInteractor? = null
    private val arrayList = ArrayList<Task?>()
    private var isFabClicked = false

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_intro_fragment3, container, false)

        fab = view.findViewById(R.id.intro_frg3_fab)
        recyclerView = view.findViewById(R.id.intro_frg3_recycler)

        val task = Task()
        task.time = "13:44"
        task.task_string = "Call Charlie and remind her about the project"
        arrayList.add(task)
        adapter = IntroRVAdapter(context, this)
        adapter?.arrayList = arrayList
        adapter?.notifyDataSetChanged()
        recyclerView?.adapter = adapter

        fab?.setOnClickListener {
            if (!isFabClicked) {
                val task1 = Task()
                task1.time = "18:00"
                task1.task_string = "Rate this app in Google Play"
                arrayList.add(task1)
                adapter?.arrayList = arrayList
                adapter?.notifyDataSetChanged()
                isFabClicked = true
            } else {
                Toast.makeText(context, "See? It's easy!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    fun refreshAdapterOnDelete() {
        adapter?.notifyItemRemoved(0)
    }

    companion object {
        fun newInstance(): IntroFragment3 {
            return IntroFragment3()
        }
    }
}
