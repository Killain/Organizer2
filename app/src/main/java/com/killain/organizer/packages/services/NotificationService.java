package com.killain.organizer.packages.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.killain.organizer.packages.interactors.NotificationInteractor;

public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new BackgroundTask().execute();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startObserving() {
        new NotificationInteractor(getApplicationContext());
    }

    class BackgroundTask extends AsyncTask <NotificationInteractor, Integer, Integer> {

        @Override
        protected Integer doInBackground(NotificationInteractor... notificationInteractors) {
            new NotificationInteractor(getApplicationContext());
            return null;
        }


    }
}
