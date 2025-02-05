package com.example.dorak.util

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {

    private const val PREF_NAME = "user_prefs"
    private const val KEY_MOBILE = "mobile"
    private const val KEY_EMAIL = "email"
    private const val KEY_NAME = "user_name"
    private const val KEY_PASSWORD = "password"
    private const val KEY_GENDER = "gender"

    private const val KEY_IS_SAVED_ANY_USER = "is_Added_user"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveMobileAndPassword(context: Context, mobileNumber: String , password: String , isSavedAnyUser : Boolean) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_MOBILE, mobileNumber)
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(KEY_IS_SAVED_ANY_USER, isSavedAnyUser)
        editor.apply()
    }

    fun saveUserInfo(context: Context, userName: String, email:String, gender:String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_NAME, userName)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_GENDER, gender)
        editor.apply()
    }

    fun isAddedAnyUser(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_SAVED_ANY_USER, false)
    }
    fun getUsername(context: Context): String? {
        return getPreferences(context).getString(KEY_NAME, "")
    }

    fun getMobile(context: Context): String? {
        return getPreferences(context).getString(KEY_MOBILE, "")
    }

    fun getPassword(context: Context): String? {
        return getPreferences(context).getString(KEY_PASSWORD, "")
    }

    fun getGender(context: Context): String? {
        return getPreferences(context).getString(KEY_GENDER, "")
    }

    fun getEmail(context: Context): String? {
        return getPreferences(context).getString(KEY_EMAIL, "")
    }



    fun clearUserDate(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_MOBILE)
        editor.remove(KEY_PASSWORD)
        editor.remove(KEY_IS_SAVED_ANY_USER) // Optionally clear this as well
        editor.remove(KEY_GENDER)
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_NAME)
        editor.apply()
        editor.commit()
    }

    fun setUserData(context: Context, isAddedUrl: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_SAVED_ANY_USER, isAddedUrl)
        editor.apply()
    }

}
