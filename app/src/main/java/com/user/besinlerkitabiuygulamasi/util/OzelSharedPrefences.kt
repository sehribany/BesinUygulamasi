package com.user.besinlerkitabiuygulamasi.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class OzelSharedPrefences {
    companion object{
        private val ZAMAN="zaman"
        private var sharedPreferences:SharedPreferences?=null

        @Volatile private var instance:OzelSharedPrefences?=null

        private var lock=Any()

        operator fun invoke(context: Context):OzelSharedPrefences= instance ?: synchronized(lock){
            instance?: ozelSharedPrefencesYap(context).also {
                instance=it
            }

        }

        private fun ozelSharedPrefencesYap(context:Context):OzelSharedPrefences{
            sharedPreferences=androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return OzelSharedPrefences()

        }
    }
    fun zamaniKaydet(zaman:Long){
        sharedPreferences?.edit(commit=true){
            putLong(ZAMAN,zaman)
        }
    }

    fun zamaniAl()= sharedPreferences?.getLong(ZAMAN,0)

}