package de.dertyp7214.rboard_themecreator

import android.app.Activity
import android.app.Application
import android.os.Bundle

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {}
            override fun onActivityDestroyed(activity: Activity?) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
            override fun onActivityStopped(activity: Activity?) {}
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                applyTheme(activity)
            }

            override fun onActivityStarted(activity: Activity?) {
                applyTheme(activity)
            }

            override fun onActivityResumed(activity: Activity?) {
                applyTheme(activity)
            }
        })
    }

    fun applyTheme(activity: Activity?) {
        if (activity != null) {
            if (getSharedPreferences("settings", MODE_PRIVATE).getBoolean("dark_mode", false))
                activity.setTheme(R.style.AppTheme_Dark)
            else
                activity.setTheme(R.style.AppTheme)
        }
    }
}