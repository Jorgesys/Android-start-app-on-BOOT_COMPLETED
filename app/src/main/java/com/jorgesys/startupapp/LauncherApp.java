package com.jorgesys.startupapp;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

public class LauncherApp extends Service {

    private static final String TAG = "LauncherApp";

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,  "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() , service started..." + Utils.getCurrentDate());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,  "onStartCommand()");
        new AsyncTask<Service, Void, Service>() {
            @Override
            protected Service doInBackground(Service... params) {
                Service service = params[0];
                PackageManager pm = service.getPackageManager();
                try {
                    Intent target = pm.getLaunchIntentForPackage("com.jorgesys.startupapp");
                    if (target != null) {
                        service.startActivity(target);
                        synchronized (this) {
                            wait(5000); //Wait for 5 seconds
                        }

                        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_main);
                        Log.d(TAG, String.valueOf("Current Date : " + Utils.getCurrentDate()));
                        // Update textView in main layout.
                        remoteViews.setTextViewText(R.id.textView, "Application started @ " + Utils.getCurrentDate());


                    } else {
                        throw new ActivityNotFoundException();
                    }
                } catch (ActivityNotFoundException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
                return service;
            }

            @Override
            protected void onPostExecute(Service service) {
                service.stopSelf();
            }
        }.execute(this);

        return START_STICKY;
    }


    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }



}