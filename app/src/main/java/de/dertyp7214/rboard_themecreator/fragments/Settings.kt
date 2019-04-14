package de.dertyp7214.rboard_themecreator.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.invokeDelay
import de.dertyp7214.rboard_themecreator.helpers.ColorHelper
import de.dertyp7214.rboard_themecreator.screens.ThemeOverview

class Settings(private val scrollState: Int = 0) : BaseFragment() {

    private var elevate = false
    private lateinit var activity: AppCompatActivity
    private val preferences = ArrayList<Preference>()

    private val DARK_MODE = "dark_mode"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        activity = getActivity()!! as AppCompatActivity

        val prefs = getSharedPreferences()

        preferences.add(SwitchPreference(DARK_MODE, prefs.getBoolean(DARK_MODE, false), getString(R.string.dark_mode)))

        activity.title = getString(R.string.settings)

        val recyclerView: RecyclerView = v.findViewById(R.id.rv)

        Adapter(activity, recyclerView, preferences) { key, value ->
            getSharedPreferences().edit {
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                }
            }
            when (key) {
                DARK_MODE -> {
                    val ac = activity
                    if (ac is ThemeOverview) {
                        {
                            ac.startActivity(Intent(ac, ThemeOverview::class.java).apply {
                                putExtra("fragment", ac.settings)
                            })
                            ac.finish()
                        }.invokeDelay(150)
                    }
                }
            }
        }

        return v
    }

    private fun getSharedPreferences(): SharedPreferences {
        return activity.getSharedPreferences("settings", MODE_PRIVATE)
    }
}

private class Adapter(
    val activity: AppCompatActivity,
    recyclerView: RecyclerView,
    val list: ArrayList<Preference>,
    val onChange: (key: String, value: Any) -> Unit
) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    init {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LinearLayout(activity).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        })
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val pref = list[position]) {
            is SwitchPreference -> {
                holder.root.addView(pref.getView(activity, onChange))
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val root = v as ViewGroup
    }
}

private open class Preference(val key: String, var value: Any, val title: String = "") {
    open fun getView(context: Context): View {
        return View(context)
    }
}

private open class PreferenceString(key: String, value: String, title: String = "") : Preference(key, value, title)
private open class PreferenceInt(key: String, value: Int, title: String = "") : Preference(key, value, title)
private open class PreferenceBoolean(key: String, value: Boolean, title: String = "") : Preference(key, value, title)

private class SwitchPreference(key: String, checked: Boolean, title: String = "") :
    PreferenceBoolean(key, checked, title) {
    override fun getView(context: Context): View {
        return this.getView(context) { _, _ -> }
    }

    fun getView(context: Context, onChecked: (key: String, isChecked: Boolean) -> Unit): View {
        return LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            setPadding(20.dp(context), 15.dp(context), 20.dp(context), 15.dp(context))
            val switch = Switch(context).apply {
                isChecked = value as Boolean
                text = title
                setTextSize(COMPLEX_UNIT_SP, 16F)
                setTextColor(ColorHelper.getAttrColor(context, android.R.attr.textColor))
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                setOnCheckedChangeListener { _, isChecked -> onChecked(key, isChecked) }
            }
            setOnClickListener { switch.isChecked = !switch.isChecked }
            addView(switch)
        }
    }
}