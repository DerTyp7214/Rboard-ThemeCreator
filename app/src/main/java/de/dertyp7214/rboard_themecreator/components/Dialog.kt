package de.dertyp7214.rboard_themecreator.components

import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setMargins
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.getDimensions

class Dialog(
    activity: Activity,
    title: String = "",
    hint: String = "",
    value: String = "",
    filter: String = "",
    submit: (value: String) -> Unit = {}
) {

    init {
        val builder = AlertDialog.Builder(activity)
        val dialogView = activity.layoutInflater.inflate(R.layout.popup, null)
        builder.setView(dialogView)
        val editText: TextInputEditText = dialogView.findViewById(R.id.textInput)
        val textInputLayout: TextInputLayout = dialogView.findViewById(R.id.textInputLayout)
        val btnOk: MaterialButton = dialogView.findViewById(R.id.ok)
        val btnCancel: MaterialButton = dialogView.findViewById(R.id.cancel)
        val titleView: TextView = dialogView.findViewById(R.id.title)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dimensions = activity.getDimensions()
        dialogView.layoutParams =
            FrameLayout.LayoutParams((dimensions.first * .8F).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
                setMargins(50.dp(activity))
            }

        if (!filter.isBlank()) {
            editText.filters = arrayOf(InputFilter { source, start, end, _, _, _ ->
                for (i in start until end)
                    if (!filter.contains(source[i]))
                        return@InputFilter ""
                null
            })
        }

        titleView.text = title
        textInputLayout.hint = hint
        editText.setText(value)
        btnOk.isEnabled = false

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnOk.isEnabled = s.toString() != value
            }
        })

        btnOk.setOnClickListener {
            submit(editText.text.toString())
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
    }
}