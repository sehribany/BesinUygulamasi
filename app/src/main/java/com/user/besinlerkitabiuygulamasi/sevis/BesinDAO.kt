package com.user.besinlerkitabiuygulamasi.sevis

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.user.besinlerkitabiuygulamasi.model.Besin

@Dao
interface BesinDAO {
    //Data Access Object
    //insert-> Room , insert into
    //suspend -> coroutine scope
    //vararg -> birden fazla ve istediğimiz sayıda besin
    // List<Long> -> long , id'ler

    @Insert
    suspend fun insertAll(vararg besin: Besin):List<Long>

    @Query("SELECT * FROM besin")
    suspend fun getAllBesin():List<Besin>

    @Query("SELECT * FROM besin WHERE uuid=:besinId ")
    suspend fun getBesin(besinId:Int):Besin

    @Query("DELETE FROM besin")
    suspend fun deleteAllBesin()

}