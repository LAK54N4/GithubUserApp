package com.laksana.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.laksana.githubuser.R
import com.laksana.githubuser.fragment.TimePickerFragment
import com.laksana.githubuser.listener.DialogTimeListener
import com.laksana.githubuser.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.set_alarm.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderActivity: AppCompatActivity(), View.OnClickListener, DialogTimeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_alarm)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Setting Reminder"
        actionBar.setDisplayHomeAsUpEnabled(true)

        alarmReceiver = AlarmReceiver()

        btn_repeating_time.setOnClickListener(this)
        btn_set_repeating_alarm.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_repeating_time -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_set_repeating_alarm -> {
                val repeatTime = tv_repeating_time.text.toString()
                val repeatMessage = edt_repeating_message.text.toString()
                alarmReceiver.setRepeatingAlarm(this,
                        AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage)
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        tv_repeating_time.text = dateFormat.format(calendar.time)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@ReminderActivity, FavoriteActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        finish()
    }
}