package com.example.android.politicalpreparedness.election.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter

@BindingAdapter("goneUnless")
fun goneUnless(view: View, item: String?) {
    item?.let {
        view.visibility = if (item.isNotEmpty()) View.VISIBLE else View.GONE
    }
}