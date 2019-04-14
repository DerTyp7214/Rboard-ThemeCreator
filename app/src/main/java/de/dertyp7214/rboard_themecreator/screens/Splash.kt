package de.dertyp7214.rboard_themecreator.screens

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.core.getIntent
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ObjectAnimator.ofFloat(icLauncher, "scaleY", 1F).apply {
            interpolator = OvershootInterpolator()
            duration = 850
            start()
        }
        ObjectAnimator.ofFloat(icLauncher, "scaleX", 1F).apply {
            interpolator = OvershootInterpolator()
            duration = 850
            start()
        }
        ObjectAnimator.ofFloat(icLauncher, "alpha", 1F).apply {
            duration = 750
            start()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    ActivityCompat.requestPermissions(
                        this@Splash,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        42
                    )
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            42 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(ThemeOverview::class.java.getIntent(this))
                    finish()
                }
            }
        }
    }
}
