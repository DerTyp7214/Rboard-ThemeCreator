package de.dertyp7214.rboard_themecreator.components

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import de.dertyp7214.rboard_themecreator.R

class ImageBottomSheet(private val activity: Activity) : BottomSheetDialogFragment() {

    lateinit var imageView: ImageView
    lateinit var pickImage: MaterialButton

    private var bitmap: Bitmap? = null
    private var listener: (v: View) -> Unit = {}

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(activity, theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.image_bottom_sheet, container, false)

        imageView = v.findViewById(R.id.imageView)
        pickImage = v.findViewById(R.id.pickImage)

        imageView.setImageBitmap(bitmap)
        pickImage.setOnClickListener { listener(it) }
        return v
    }

    fun setImage(bitmap: Bitmap?, clickListener: (v: View) -> Unit = {}) {
        this.bitmap = bitmap
        this.listener = clickListener
    }
}