package de.dertyp7214.rboard_themecreator.screens

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.transition.TransitionManager
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.transitionseverywhere.ChangeText
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.core.*
import de.dertyp7214.rboard_themecreator.fragments.About
import de.dertyp7214.rboard_themecreator.fragments.BaseFragment
import de.dertyp7214.rboard_themecreator.fragments.ThemeEditor
import de.dertyp7214.rboard_themecreator.fragments.ThemeList
import de.dertyp7214.rboard_themecreator.helpers.ColorHelper
import de.dertyp7214.rboard_themecreator.themes.CssDef
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ThemeOverview : AppCompatActivity() {

    private var currentFragment: BaseFragment? = null
    private var currentFragmentId: Int = 0
    private lateinit var drawerArrow: DrawerArrowDrawable
    lateinit var drawer: Drawer

    val themeList = 1
    val themeEditor = 2
    val about = 3

    var themeEditorInstance: ThemeEditor? = null

    var themeListScrollState = 0

    val tmpThemes = HashMap<String, Pair<Long, ArrayList<CssDef>>>()

    private val themeManager = "com.rkbdi.board.manager"
    private val currentItems = ArrayList<Long>()
    internal lateinit var toolbar: Toolbar
    private lateinit var defaultTitle: String

    fun updateDrawer() {
        drawer.removeItem(1337)
        currentItems.forEach {
            drawer.removeItem(it)
        }

        currentItems.clear()
        drawer.addItem(DividerDrawerItem().withIdentifier(1337))
        tmpThemes.entries.forEach {
            currentItems.add(it.value.first)
            drawer.addItem(PrimaryDrawerItem()
                .withIdentifier(it.value.first)
                .withSelectable(false)
                .withName(it.key)
                .withOnDrawerItemClickListener { _, _, _ ->
                    createTheme(it.key, true)
                    true
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_overview)
        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        defaultTitle = title.toString()

        toolbar.setToolbarColor(ColorHelper.getAttrColor(this, android.R.attr.windowBackground))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) window.navigationBarDividerColor =
            resources.getColor(R.color.graySemi, null)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

        drawer = DrawerBuilder()
            .withActivity(this)
            .withTranslucentStatusBar(false)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(themeList.toLong())
                    .withSelectable(true)
                    .withSetSelected(true)
                    .withName(R.string.main)
                    .withIcon(R.drawable.ic_home),
                PrimaryDrawerItem()
                    .withIdentifier(about.toLong())
                    .withSelectable(true)
                    .withSetSelected(false)
                    .withName(R.string.about)
                    .withIcon(R.drawable.ic_about),
                DividerDrawerItem()
            )
            .withOnDrawerItemClickListener { _, _, drawerItem ->
                when (drawerItem.identifier) {
                    themeList.toLong() -> {
                        setHome()
                    }
                    about.toLong() -> {
                        setFragment(About(), about)
                    }
                }
                drawer.closeDrawer()
                true
            }
            .build()

        if (themeManager.appInstalled(packageManager)) {
            drawer.addItem(
                PrimaryDrawerItem()
                    .withSelectable(false)
                    .withSetSelected(false)
                    .withName(R.string.theme_manager)
                    .withIcon(R.drawable.ic_open)
                    .withIconColor(ColorHelper.getAttrColor(this, android.R.attr.textColor))
                    .withOnDrawerItemClickListener { _, _, _ ->
                        themeManager.open(this).invokeDelay(250)
                        true
                    }
            )
        }

        drawerArrow = DrawerArrowDrawable(this).apply {
            color = Color.BLACK
            toolbar.navigationIcon = this
            TransitionManager.beginDelayedTransition(
                toolbar.getTextViewTitle()?.parent as ViewGroup,
                ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
            )
        }

        setHome(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                if (currentFragmentId != themeList) onBackPressed()
                else drawer.openDrawer()
                true
            }
            R.id.menu_create -> {
                createTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setHome(wait: Boolean = true) {
        ObjectAnimator.ofFloat(toolbar, "elevation", if (themeListScrollState > 0) 12.dp(this).toFloat() else 0F)
            .start()
        setFragment(ThemeList(themeListScrollState), themeList, wait = wait, back = false, forceReplace = true) {
            drawer.setSelection(themeList.toLong(), false)
            toolbar.inflateMenu(R.menu.search)
            title = defaultTitle
            val item = toolbar.menu.findItem(R.id.menu_search)
            val search = item.actionView as SearchView
            search.apply {
                setOnSearchClickListener {
                    TransitionManager.beginDelayedTransition(toolbar)
                    toolbar.menu.findItem(R.id.menu_search).expandActionView()
                }
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (it is ThemeList) it.filter(query ?: "")
                        val view = this@ThemeOverview.currentFocus
                        if (view != null) {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (it is ThemeList) it.filter(newText ?: "")
                        return true
                    }
                })
            }
            toolbar.setToolbarColor(ColorHelper.getAttrColor(this, android.R.attr.windowBackground))
        }
    }

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() == true) {
            when (currentFragmentId) {
                themeEditor -> {
                    setHome(false)
                }
                about -> {
                    setHome(false)
                }
                themeList -> {

                }
            }
        }
    }

    @SuppressLint("SdCardPath")
    private fun createTheme(saved: String = "", wait: Boolean = false) {
        val file = assets.open("thememe.zip").saveAs("/sdcard/Documents/thememe.zip")
        setFragment(ThemeEditor(file.absolutePath, saved), themeEditor, wait = wait, forceReplace = true)
    }

    fun setFragment(
        fragment: BaseFragment,
        id: Int,
        animated: Boolean = true,
        wait: Boolean = true,
        back: Boolean = true,
        forceReplace: Boolean = false,
        switched: (instance: BaseFragment) -> Unit = {}
    ) {
        if (currentFragmentId != id || forceReplace)
            Handler().postDelayed({
                currentFragmentId = id
                currentFragment = fragment
                if (id != themeList) ObjectAnimator.ofFloat(toolbar, "elevation", 0F)
                    .start()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameLayout, fragment)
                if (animated) fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
                ObjectAnimator.ofFloat(drawerArrow, "progress", if (back) 1F else 0F).start()
                toolbar.menu.clear()
                switched(fragment)
            }, if (wait) 250 else 0L)
    }

    @SuppressLint("SdCardPath")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val f = File(data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH))
            CropImage.activity(Uri.fromFile(f))
                .start(this)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            val uri = result.uri
            val file = File(Objects.requireNonNull(uri.path))
            val bitmap = file.bitmap
            themeEditorInstance?.bottomSheetImageView?.setImageBitmap(bitmap)
            bitmap?.compress(
                Bitmap.CompressFormat.PNG,
                100,
                File("/sdcard/Documents/gboardTheme/", "background").outputStream()
            )
        }
    }
}