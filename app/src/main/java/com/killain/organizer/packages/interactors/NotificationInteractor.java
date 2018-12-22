package com.killain.organizer.packages.interactors;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.killain.organizer.R;
import com.killain.organizer.packages.task_watcher.TaskWatcher;
import com.killain.organizer.packages.tasks.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationInteractor {

    private ArrayList<Task> arrayList;
    private Context context;
    private TaskWatcher taskWatcher;
    public Observable<Task> taskObservable;
    private int count = 0;
    private Date date;
    private DataManager dataManager;

    public NotificationInteractor(ArrayList<Task> arrayList,
                                  Context context) {

        this.arrayList = arrayList;
        this.context = context;
        taskObservable = taskObservable.fromIterable(arrayList);
        taskWatcher = getObserver();
        dataManager = new DataManager(context, null);
        subscribeCycle();
    }

//    private Observable<Task> convertArrayListToObserver(ArrayList<Task> arrayList) {
//        return taskObservable.fromIterable(arrayList);
//    }

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

    private void createNotification(String title, String message, Task task) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String raw = task.getDate();
        try {
            date = sdf.parse(raw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (task.isNotificationShowed() == false) {
            NotificationCompat.Builder b = new NotificationCompat.Builder(context);
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(date.getTime())
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setTicker("{Ticker string}")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentInfo("INFO");

            android.app.NotificationManager nm = (android.app.NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(count, b.build());
            task.setNotificationShowed(true);
            dataManager.updateTask(task);
//            taskDAO.updateTask(task);
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

//    taskObservable = convertArrayListToObserver(arrayList);
//
//        taskObservable.subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.single())
//            .delay(600,TimeUnit.MILLISECONDS)
//                .repeat()
//                .subscribe(taskWatcher);

}
