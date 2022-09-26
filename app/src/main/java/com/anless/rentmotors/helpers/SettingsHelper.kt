package com.anless.rentmotors.helpers

import com.google.gson.Gson
import android.content.Context
import androidx.preference.PreferenceManager
import com.anless.rentmotors.ui.personalInfo.PersonalInfo

class SettingsHelper(context: Context) {
    companion object {
        private const val KEY_PERSONAL_INFO = "PERSONAL_INFO"
    }
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun setPersonalInfo(personalInfo: PersonalInfo?) {
        val gson = Gson()
        val personalInfoJson = gson.toJson(personalInfo)
        preferences.edit()
            .putString(KEY_PERSONAL_INFO, personalInfoJson)
            .apply()
    }

    fun getPersonalInfo(): PersonalInfo? {
        return try {
            val personalInfoJson = preferences.getString(KEY_PERSONAL_INFO, null)
            val gson = Gson()
            gson.fromJson(personalInfoJson, PersonalInfo::class.java)
        } catch (e: Exception) {
            null
        }
    }
}