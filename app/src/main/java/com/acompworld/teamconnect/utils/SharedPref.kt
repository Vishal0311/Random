package com.acompworld.teamconnect.utils

import android.content.Context
import android.content.SharedPreferences
import com.acompworld.teamconnect.api.model.responses.LoginResponse
import com.acompworld.teamconnect.api.model.responses.LoginResponseData
import com.acompworld.teamconnect.ui.login.LoginActivity

object SharedPref {
    private val SharedPrefName: String = "SharedPrefTeamConnect"

    private const val USERID_KEY:String = "userid"
    private const val EMPNO_KEY :String = "empno"
    private const val EMPNAME_KEY = "empname"
    private const val PROJECTCODE_KEY = "projectcode"
    private const val PROJECTNAME_KEY = "projectname"
    private const val DEPARTMENT_KEY = "department"
    private const val DESIGNATION_KEY = "designation"

    fun getSharedPreferences(context:Context) :SharedPreferences {
        return context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE)
    }

    fun saveUserData(context: Context, user:LoginResponse) {
        val pref = getSharedPreferences(context)
        val editor = pref.edit()
        editor.putInt(USERID_KEY,user.data.userid)
        editor.putString(EMPNO_KEY, user.data.empno)
        editor.putString(EMPNAME_KEY, user.data.empname)
        editor.putString(PROJECTCODE_KEY, user.data.projectCode)
        editor.putString(PROJECTNAME_KEY, user.data.projectname)
        editor.putString(DEPARTMENT_KEY, user.data.department)
        editor.putString(DESIGNATION_KEY, user.data.designation)
        editor.commit()
    }

    fun getSavedUserData(context: Context):LoginResponseData? {
        if(context!=null) {
            val pref = getSharedPreferences(context)
            val user = LoginResponseData(
                pref.getInt(USERID_KEY, 0),
                pref.getString(EMPNO_KEY,"")!!,
                pref.getString(EMPNAME_KEY,"")!!,
                pref.getString(PROJECTCODE_KEY,"")!!,
                pref.getString(PROJECTNAME_KEY,"")!!,
                pref.getString(DEPARTMENT_KEY,"")!!,
                pref.getString(DESIGNATION_KEY,"")!!

            )
            return user
            //return pref.getString(key, "")
        }
        return null
    }

    fun logoutSession(context: Context) {
        val pref = getSharedPreferences(context).edit()
        pref.clear().apply()
        Utils.moveToAndClearHistory(context, LoginActivity::class.java)
    }
}