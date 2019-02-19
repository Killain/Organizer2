package com.killain.organizer.packages.interactors

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.util.Log


import com.killain.organizer.R
import com.killain.organizer.packages.activity.TasksActivity
import com.killain.organizer.packages.models.Task

import org.threeten.bp.LocalDate

import java.text.SimpleDateFormat

import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotificationInteractor(private val context: Context?) {

    private var arrayList: ArrayList<Task>? = null
//    private var observable: Observable<Task>? = null
    private val NOTIFICATION_CHANNEL_ID = "1"
    private var count = 0
    private var date: Date? = null
    private val dataManager: DataManager?
    private lateinit var calendar: Calendar
//    private val dateString: String? = null
    private var timeString: String? = null
    var dateHelper: DateHelper
    private lateinit var localDate: LocalDate
    private var comparison: Long = 0

    private val sdf_time = SimpleDateFormat("HH:mm")

    init {
        dataManager = context?.let { DataManager(it) }
        arrayList = dataManager?.getTasksByState(false, false)
        dateHelper = DateHelper()
        subscription()
    }

    private val observer: Observer<Task?>
        get() = object : Observer<Task?> {
            override fun onSubscribe(d: Disposable) {
                getCurrentDateAndTime()
            }

            override fun onNext(task: Task) {
                if (comparison == task.date && task.time == timeString) {
                    Log.d("LOGGER", "Date and time are equal, condition is passed")
                    if (!task.isNotificationShowed) {
                        createNotification(task.task_string, task.task_string, task)
                    }
                }
                Log.d("LOGGER", task.task_string)
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {
                subscription()
            }
        }

    private fun createNotification(title: String, message: String, task: Task?) {

        Log.d("Notification MESSAGE", "createNotification() is called")

        val notificationManager = context
                ?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Channel", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        //            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1");
        //            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        //                    .setDefaults(Notification.DEFAULT_ALL)
        //                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
        //                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
        //                    .setSound(null)
        //                    .setPriority(NotificationCompat.PRIORITY_HIGH)
        //                    .setAutoCancel(true)
        //                    .setContentText("Organizer app")
        //                    .setContentTitle(message)
        //                    .setContentInfo("");
        //
        //            if (notificationManager != null) {
        //                notificationManager.notify((int)(System.currentTimeMillis()/1000), mBuilder.build());
        //            }

        //        Intent notificationIntent = new Intent(this, TasksActivity.class);
        //        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        val notification = context?.let {
            NotificationCompat.Builder(it, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Reminder")
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                //.setLargeIcon(BitmapFactory.decodeResource(R.drawable.xyz))
                .setColor(context.resources.getColor(R.color.colorAccent))
                .setVibrate(longArrayOf(0, 300, 300, 300))
                //                .setSound()
                .setLights(Color.WHITE, 1000, 5000)
                //.setWhen(System.currentTimeMillis())
                //                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(title))
        }

        notificationManager?.notify(1, notification?.build())

        task?.isNotificationShowed = true
        dataManager?.updateTask(task)
        arrayList?.remove(task)
        count++
    }

    private fun getObservable(): Observable<Task> {
        return Observable.fromIterable(arrayList!!)
    }

    private fun getCurrentDateAndTime() {
        calendar = Calendar.getInstance()
        localDate = LocalDate.now()
        comparison = localDate.toEpochDay()
        date = calendar.time
        timeString = sdf_time.format(date)
    }

    private fun subscription() {
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        arrayList = dataManager?.getTasksByState(false, false)
        getObservable()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(observer)
    }
}