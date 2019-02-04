package com.killain.organizer.packages.interactors;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.killain.organizer.R;
import com.killain.organizer.packages.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationInteractor {

    private ArrayList<Task> arrayList;
    private Context context;
    private Observable<Task> observable;
    private static final String NOTIFICATION_CHANNEL_ID = "1";

    private int count = 0;
    private Date date;

    private DataManager dataManager;
    private Calendar calendar;
    private String dateString, timeString;
    private DateHelper dateHelper;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");


    public NotificationInteractor(Context context) {
        this.context = context;
        dataManager = new DataManager(context, null);
        arrayList = dataManager.getTasksByState(false, false);
        dateHelper = new DateHelper();
        subscription();
    }

    private void createNotification(String title, String message, Task task) {

        Log.d("Notification MESSAGE", "createNotification() is called");

        if (!task.isNotificationShowed()) {

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Channel", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1");

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(null)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setContentTitle(message)
                    .setContentInfo("this is content info");

            if (notificationManager != null) {
                notificationManager.notify((int)(System.currentTimeMillis()/1000), mBuilder.build());
            }

            task.setNotificationShowed(true);
            dataManager.updateTask(task);
            arrayList.remove(task);
            count++;
        }
    }

    private Observer<Task> getObserver() {
        return new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                getCurrentDateAndTime();
            }

            @Override
            public void onNext(Task task) {
                if (dateHelper.longToString(task.getDate()).equals(dateString) && task.getTime().equals(timeString)) {
                    Log.d("LOGGER", "Date and time are equal, condition is passed");
                    createNotification(task.getTask_string(), task.getTask_string(), task);
                }
                Log.d("LOGGER", task.getTask_string());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                subscription();
            }
        };
    }

    private Observable<Task> getObservable() {
        return Observable.fromIterable(arrayList);
    }

//    private void reloadTasks() {
//        arrayList = dataManager.getTasksByState(false, false);
//        taskObservable = Observable.fromIterable(arrayList);
//    }

    private void getCurrentDateAndTime() {
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        dateString = sdf_date.format(date);
        timeString = sdf_time.format(date);
    }

    private void subscription() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        arrayList = dataManager.getTasksByState(false, false);
        getObservable()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(getObserver());
    }
}