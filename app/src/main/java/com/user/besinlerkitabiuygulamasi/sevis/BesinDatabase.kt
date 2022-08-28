package com.user.besinlerkitabiuygulamasi.sevis

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.user.besinlerkitabiuygulamasi.model.Besin

@Database(entities = arrayOf(Besin::class), version = 1)
abstract class BesinDatabase:RoomDatabase() {

    abstract fun besinDao():BesinDAO

    //Singleton ->Tek bir obje (farklı theradlardan aynı anda tek bir objeye ulaşılsın )
    companion object{

        @Volatile private var instance:BesinDatabase?=null
        private val lock=Any()
        operator  fun invoke(context:Context)= instance ?: synchronized(lock){
            instance ?: databaseOlustur(context).also {
                instance=it
            }
        }


        private fun databaseOlustur(context:Context)=Room.databaseBuilder(
            context.applicationContext,
            BesinDatabase::class.java,
            "besindatabase").build()
    }
    }

