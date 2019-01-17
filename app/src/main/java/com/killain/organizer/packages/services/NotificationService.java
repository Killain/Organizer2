package com.killain.organizer.packages.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interactors.NotificationInteractor;
import com.killain.organizer.packages.tasks.Task;

import java.util.ArrayList;

public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startObserving();
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
        DataManager dataManager = new DataManager(getApplicationContext(), null);
        ArrayList<Task> arrayList = dataManager.getTasksByState(false, false);
        NotificationInteractor interactor = new NotificationInteractor(arrayList, getApplicationContext());
    }
}
