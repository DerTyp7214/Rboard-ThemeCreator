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

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }
        })
    }
}