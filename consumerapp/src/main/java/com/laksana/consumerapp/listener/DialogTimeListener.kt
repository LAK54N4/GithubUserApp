package com.laksana.consumerapp.listener

interface DialogTimeListener {
    fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int)
}