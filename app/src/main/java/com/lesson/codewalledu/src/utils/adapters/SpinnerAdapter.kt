package com.lesson.codewalledu.src.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.lesson.codewalledu.R

class SpinnerAdapter(context: Context, private val languages: List<String>, private val flagImageIds: List<Int>)
    : ArrayAdapter<String>(context, 0, languages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_with_flag, parent, false)

        val imageViewFlag = view.findViewById<ImageView>(R.id.imageViewFlag)
        val textViewName = view.findViewById<TextView>(R.id.textViewName)



        imageViewFlag.setImageResource(flagImageIds[position])
        textViewName.text = languages[position]

        val sharedPreferences = parent.context.getSharedPreferences("language", Context.MODE_PRIVATE)
        sharedPreferences.edit().also {
            it.putString("language",languages[position])
            it.putInt("language_image",flagImageIds[position])
            it.apply()
        }


        return view
    }
}