package com.laksana.githubuser.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.laksana.githubuser.R
import com.laksana.githubuser.activity.ReminderActivity

class PreferenceFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var reminder: String
    private lateinit var currentLanguage: String
    private lateinit var reminderPreference: SwitchPreference

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?){
        setPreferencesFromResource(R.xml.preferences, rootKey)

        init()
        setSummaries()
    }

    private fun setSummaries() {
        val sh=preferenceManager.sharedPreferences
        reminderPreference.switchTextOff = sh.getBoolean(reminder, false).toString()
    }

    private fun init() {
        reminder = this.resources.getString(R.string.key_reminder)
        currentLanguage = resources.getString(R.string.key_language)
        reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if(preference.key == context?.getString(R.string.key_language)) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            return true
        }
        return false
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if(key == reminder) {
            val on: Boolean = sharedPreferences.getBoolean(reminder, false)
            if(on) {
                Toast.makeText(activity, "Switch Enabled", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ReminderActivity::class.java)
                startActivity(intent)
            } else  {
                Toast.makeText(activity, "Switch Disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }


}