package com.killain.organizer.packages.fragments

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView

import com.github.clans.fab.FloatingActionButton
import com.killain.organizer.packages.enums.AdapterRefreshType
import com.killain.organizer.packages.enums.DialogType
import com.killain.organizer.packages.interactors.RecyclerViewInteractor
import com.killain.organizer.packages.interactors.UIInteractor
import com.killain.organizer.packages.models.Task
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter
import com.killain.organizer.R
import com.killain.organizer.packages.interfaces.FragmentUIHandler
import org.threeten.bp.LocalDate

class TasksFragment : Fragment(), FragmentUIHandler, View.OnClickListener {

    private var oldScrollYPosition = 0
    private lateinit var noTaskTxt: TextView
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var tasksFrameLayout: FrameLayout
    private lateinit var dialogFrameLayout: FrameLayout
    private lateinit var fabSimpleTask: FloatingActionButton
    private var bottomNavigationView: BottomNavigationView? = null
    private lateinit var scrollView: ScrollView
    private lateinit var recyclerView: RecyclerView
    var adapter: RVCardAdapter? = null
    private lateinit var tasksFragmentInstance: TasksFragment
    private var uiInteractor: UIInteractor? = null
    private var localDate: LocalDate? = null

    override val background: View?
        get() = relativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksFragmentInstance = this
        localDate = LocalDate.now()
    }

    override fun onStart() {
        if (adapter != null) {
            refreshAdapterOnAdd(0, AdapterRefreshType.RELOAD_FROM_DB)
        }
        super.onStart()
    }

    override fun onResume() {
        if (adapter != null) {
            refreshAdapterOnAdd(0, AdapterRefreshType.RELOAD_FROM_DB)
        }
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_tasks, container, false)

        noTaskTxt = rootView.findViewById(R.id.no_task_txt)
        fabSimpleTask = activity!!.findViewById(R.id.fab_simple_task)
        bottomNavigationView = activity!!.findViewById(R.id.navigation)
        bottomNavigationView!!.menu.getItem(1).isChecked = true
        tasksFrameLayout = activity!!.findViewById(R.id.content_frame_layout)
        dialogFrameLayout = activity!!.findViewById(R.id.dialog_frame_layout)
        scrollView = rootView.findViewById(R.id.tasks_frg_scroll_view)
        relativeLayout = rootView.findViewById(R.id.parent_layout_tasks_fragment)
        recyclerView = rootView.findViewById(R.id.recycler_fragment_tasks)

        uiInteractor = UIInteractor(relativeLayout, fabSimpleTask, bottomNavigationView)
        recyclerView.isNestedScrollingEnabled = false
        val rvInteractor = RecyclerViewInteractor(recyclerView)
        adapter = RVCardAdapter(context!!, rvInteractor.listener, this)
        rvInteractor.adapter = adapter
        adapter?.loadItemsByState()
        adapter?.fragment = tasksFragmentInstance
        rvInteractor.bind()

        fabSimpleTask.setOnClickListener(this)

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (scrollView.scrollY > oldScrollYPosition) {
                fabSimpleTask.hide(true)
            } else if (scrollView.scrollY < oldScrollYPosition || scrollView.scrollY <= 0) {
                fabSimpleTask.show(true)
            }
            oldScrollYPosition = scrollView.scrollY
        }

        return rootView
    }

    override fun refreshAdapterOnAdd(position: Int, refreshType: AdapterRefreshType) {
        if (adapter != null) {
            if (refreshType === AdapterRefreshType.RELOAD_FROM_DB) {
                adapter?.loadItemsByState()
                adapter?.notifyDataSetChanged()
            } else {
                adapter?.notifyItemInserted(position)
            }
        } else {
            return
        }
    }

    override fun refreshAdapterOnDelete(position: Int) {
        if (adapter != null) {
            adapter?.notifyItemRemoved(position)
        }
    }

    fun callDialogFragment(dialogType: DialogType,
                           fragmentUIHandler: FragmentUIHandler?,
                           date: Any?,
                           task: Task?) {

        val dialog = AddTaskDialogFragment()
        dialog.setDialogType(dialogType)
        dialog.setParams(task, date)
        dialog.setListener(fragmentUIHandler)
        if (fragmentManager != null) {
            dialog.show(fragmentManager, "dialog")
        }
        tasksFragmentInstance.onPause()
    }

    override fun onClick(v: View) {
        //        AddTaskDialogFragment dialog = new AddTaskDialogFragment();
        //        dialog.setDialogType(DialogType.ADD_NEW_TASK);
        //        dialog.setListener(this);
        //        dialog.setDate(localDate);
        //        if (getFragmentManager() != null) {
        //            dialog.show(getFragmentManager(), "dialog");
        //        }
        //        tasksFragmentInstance.onPause();
        callDialogFragment(DialogType.ADD_NEW_TASK, this, localDate, null)
    }

    override fun onDestroy() {
        adapter!!.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        adapter!!.onDestroy()
        super.onPause()
    }

    companion object {
        fun newInstance(): TasksFragment {
            return TasksFragment()
        }
    }
}