package com.user.besinlerkitabiuygulamasi.sevis

import com.user.besinlerkitabiuygulamasi.model.Besin
import io.reactivex.Single
import retrofit2.http.GET

interface BesinAPI {
    //GET(veri çekeceksek),POST(veri yollayacaksak)
    //https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json
    //BASE_URL -> https://raw.githubusercontent.com/
    //atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")

    fun getBesin(): Single<List<Besin>>





}