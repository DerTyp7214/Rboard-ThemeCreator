package de.dertyp7214.rboard_themecreator.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboard_themecreator.MainActivity
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.screens.GboardTheme

class ThemeAdapter(
    private val activity: Activity,
    recyclerView: RecyclerView,
    private val list: ArrayList<GboardTheme>
) :
    RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

    init {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.theme, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.title.text = item.zipFile.name.removeSuffix(".zip")
        holder.title.setOnClickListener {
            activity.startActivity(Intent(activity, MainActivity::class.java).apply {
                putExtra("fileName", item.zipFile.absolutePath)
            })
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.textView)
    }
}