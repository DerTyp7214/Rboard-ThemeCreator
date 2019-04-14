package de.dertyp7214.rboard_themecreator.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.components.Dialog
import de.dertyp7214.rboard_themecreator.components.MaskedImageView
import de.dertyp7214.rboard_themecreator.core.runAsCommand
import de.dertyp7214.rboard_themecreator.fragments.GboardTheme
import de.dertyp7214.rboard_themecreator.fragments.ThemeEditor
import de.dertyp7214.rboard_themecreator.screens.ThemeOverview

class ThemeAdapter(
    private val activity: Activity,
    recyclerView: RecyclerView,
    private val list: ArrayList<GboardTheme>,
    private val onScroll: (position: Int) -> Unit = {},
    private val scrollPosition: (position: Int) -> Unit = {}
) :
    RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

    private val filteredList: ArrayList<GboardTheme> = list.filter { true } as ArrayList<GboardTheme>

    init {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScroll(recyclerView.computeVerticalScrollOffset())
                scrollPosition((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
            }
        })
    }

    fun filter(query: String) {
        filteredList.clear()
        filteredList.addAll(list.filter { it.zipFile.name.removeSuffix(".zip").contains(query) || query.isBlank() })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.theme, parent, false))
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]

        holder.image.setImageBitmap(null)
        Thread {
            val bitmap = item.getImage(activity)
            activity.runOnUiThread {
                holder.image.setImageBitmap(bitmap)
            }
        }.start()
        holder.title.text = item.zipFile.name.removeSuffix(".zip")
        holder.card.setOnClickListener {
            if (activity is ThemeOverview) {
                activity.setFragment(ThemeEditor(item.zipFile.absolutePath), activity.themeEditor, wait = false)
            }
        }
        holder.card.setOnLongClickListener {
            Dialog(
                activity,
                activity.getString(R.string.delete),
                "",
                activity.getString(R.string.delete_long),
                "",
                true
            ) {
                "rm ${item.zipFile.absolutePath}".runAsCommand()
                "rm ${item.zipFile.absolutePath.removeSuffix(".zip")}".runAsCommand()
                if (activity is ThemeOverview) activity.setHome(false)
            }
            true
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.textView)
        val image: MaskedImageView = v.findViewById(R.id.image)
        val card: LinearLayout = v.findViewById(R.id.card)
    }
}