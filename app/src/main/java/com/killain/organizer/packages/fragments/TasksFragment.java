package com.killain.organizer.packages.fragments;

import android.app.NotificationManager;
import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.card.CardAdapter;
import com.killain.organizer.R;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.task_watcher.TaskWatcher;
import com.killain.organizer.packages.tasks.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TasksFragment extends Fragment implements OnStartDragListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int oldScrollYPostion = 0;

    private String mParam1;
    private String mParam2;

    private int count = 0;
    private TextView noTaskTxt;
    private FloatingActionMenu fam;
    private Observable<Task> taskObservable;
    private TaskWatcher taskWatcher;
    public RelativeLayout relative_layout;
    private FloatingActionButton fab_simple_task, fab_big_task;
    private ScrollView scrollView;
    private ItemTouchHelper mItemTouchHelper;
    public ArrayList<Task> arrayList;
    public RecyclerView recyclerView;
    public CardAdapter cardAdapter;
    private AppDatabase db;
    private TasksFragment fragment;
    private TaskDAO taskDAO;
    private int cycle = 1;
    private Date date;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        TasksFragment tasksFragment = new TasksFragment();
        return tasksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = AppDatabase.getAppDatabase(getContext());
        taskDAO = db.getTaskDAO();
        taskWatcher = getObserver();
        fragment = TasksFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        noTaskTxt = RootView.findViewById(R.id.no_task_txt);

        getTasksFromDb();

        taskObservable = convertArrayListToObserver(arrayList);

        taskObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .delay(600, TimeUnit.MILLISECONDS)
                .repeat()
                .subscribe(taskWatcher);

        FloatingActionMenu fam = RootView.findViewById(R.id.fam_tasks);

        fab_simple_task = RootView.findViewById(R.id.fab_simple_task);
        fab_big_task = RootView.findViewById(R.id.fab_big_task);

        scrollView = RootView.findViewById(R.id.tasks_frg_scroll_view);
        relative_layout = RootView.findViewById(R.id.parent_layout_tasks_fragment);
        recyclerView = RootView.findViewById(R.id.recycler_fragment_tasks);
        cardAdapter = new CardAdapter(getContext(), arrayList, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(cardAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(cardAdapter);

        fab_simple_task.setOnClickListener(v -> {
            AddTaskDialogFragment dialog = new AddTaskDialogFragment();
            dialog.task_type = "simple";
            dialog.setListener(TasksFragment.this);
            dialog.setTargetFragment(TasksFragment.this, 1);
            dialog.show(getFragmentManager(), "AddTaskDialogFragment");
        });

        fab_big_task.setOnClickListener(v -> {
            Fragment newFragment = new AddBigTaskFragment();
            ((AddBigTaskFragment) newFragment).setListener(this);
            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.fragment1, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (scrollView.getScrollY() > oldScrollYPostion) {
                fam.hideMenu(true);
//                    fam.showMenu(true);
            } else if (scrollView.getScrollY() < oldScrollYPostion || scrollView.getScrollY() <= 0) {
                fam.showMenu(true);
//                    fam.hideMenu(true);
            }
            oldScrollYPostion = scrollView.getScrollY();
        });

        return RootView;
    }

    public static String getCurrentDateWithoutTime() {

        Date date;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String result = sdf.format(date);

        return result;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public ArrayList<Task> getTasksFromDb() {
        arrayList = (ArrayList<Task>) taskDAO.getAllTasksByState(false);
        if (arrayList.size() == 0 ) {
            noTaskTxt.setVisibility(View.VISIBLE);
        }
        return arrayList;
    }

    public void createNotification(String title, String message, Task task) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String raw = task.getDate();
        try {
            date = sdf.parse(raw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (task.isNotificationShowed() == false) {
            NotificationCompat.Builder b = new NotificationCompat.Builder(this.getContext());
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(date.getTime())
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setTicker("{Ticker string}")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentInfo("INFO");

            NotificationManager nm = (NotificationManager) this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(count, b.build());
            task.setNotificationShowed(true);
            taskDAO.updateTask(task);
            count++;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        db.destroyInstance();
    }

    private Observable<Task> convertArrayListToObserver(ArrayList<Task> arrayList) {
       return taskObservable.fromIterable(arrayList);
    }

    private TaskWatcher getObserver() {
        return new TaskWatcher(){
            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
            }

            @Override
            public void onNext(Task task) {
                createNotification(task.getTitle(), task.getTask_string(), task);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        };

    }

    public void refreshFragment() {
        getActivity().runOnUiThread(() -> cardAdapter.refreshItems(getTasksFromDb()));
    }

    public void updateArrayList(ArrayList<Task> tasks) {
        this.arrayList = tasks;
        if (arrayList.size() == 0) {
            noTaskTxt.setVisibility(View.VISIBLE);
        } else {
            noTaskTxt.setVisibility(View.GONE);
        }
    }
}