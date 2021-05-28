package com.laksana.githubuser.listener

interface DialogTimeListener {
    fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int)
}