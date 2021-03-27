package com.example.weatherapp.presentation

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:isVisible")
fun isVisible(view: View,text:String?){
    view.visibility = if(text?.isNotEmpty() == true) View.VISIBLE else View.GONE
}