package com.killain.organizer.packages.interactors;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.killain.organizer.R;
import com.killain.organizer.packages.task_watcher.TaskWatcher;
import com.killain.organizer.packages.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationInteractor {

    private ArrayList<Task> arrayList;
    private Context context;
    private TaskWatcher taskWatcher;
    private Observable<Task> taskObservable;
    private int count = 0;
    private Date date;
    private DataManager dataManager;
    private SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
    private String dateString, timeString;

    public NotificationInteractor(ArrayList<Task> arrayList,
                                  Context context) {

        this.arrayList = arrayList;
        this.context = context;
        taskObservable = Observable.fromIterable(arrayList);
        taskWatcher = getObserver();
        dataManager = new DataManager(context, null);
        subscribeCycle();
    }

    private TaskWatcher getObserver() {
        return new TaskWatcher(){
            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
            }

            @Override
            public void onNext(Task task) {
                Calendar calendar = Calendar.getInstance();
                date = calendar.getTime();

                timeString = sdf_time.format(date);
                dateString = sdf_date.format(date);

                if (task.getDate().equals(dateString) && task.getTime().equals(timeString)) {
                    createNotification(task.getTitle(), task.getTask_string(), task);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onComplete() {
                subscribeCycle();
                super.onComplete();
            }
        };
    }

    private void createNotification(String title, String message, Task task) {

        if (!task.isNotificationShowed()) {
            NotificationCompat.Builder b = new NotificationCompat.Builder(context);
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(date.getTime())
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setTicker(task.getTask_string())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentInfo("");

            android.app.NotificationManager nm = (android.app.NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.notify(count, b.build());
            }
            task.setNotificationShowed(true);
            dataManager.updateTask(task);
            count++;
        }
    }

    private void subscribeCycle() {
        taskObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .delay(600, TimeUnit.MILLISECONDS)
                .repeat()
                .subscribe(taskWatcher);
    }
}
