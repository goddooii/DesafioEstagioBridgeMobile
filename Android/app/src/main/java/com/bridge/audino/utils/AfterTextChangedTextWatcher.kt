package com.bridge.audino.utils

import android.text.Editable
import android.text.TextWatcher

interface AfterTextChangedTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //do nothing
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged(s.toString())
    }

    fun afterTextChanged(text: String)
}