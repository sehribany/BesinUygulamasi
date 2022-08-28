package com.user.besinlerkitabiuygulamasi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.user.besinlerkitabiuygulamasi.model.Besin
import com.user.besinlerkitabiuygulamasi.sevis.BesinAPIServis
import com.user.besinlerkitabiuygulamasi.sevis.BesinDatabase
import com.user.besinlerkitabiuygulamasi.util.OzelSharedPrefences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application): BaseViewModel(application) {
    val besinler=MutableLiveData<List<Besin>>()
    val besinHataMesaji=MutableLiveData<Boolean>()
    val besinYukleniyor=MutableLiveData<Boolean>()
    private var guncellemeZamani=5*60*1000*1000*1000L

    private val besinApiServis=BesinAPIServis()
    private val disposable=CompositeDisposable()
    private val ozelSharedPrefences=OzelSharedPrefences(getApplication())


    fun refreshData(){
        val kaydedilmeZamani=ozelSharedPrefences.zamaniAl()
        if(kaydedilmeZamani!=null && kaydedilmeZamani!=0L && System.nanoTime()-kaydedilmeZamani<guncellemeZamani){
            //Sqlite'dan çek
            verileriSQLitetanAl()
        }else{
            verileriInternettenAl()
        }

    }
    fun refreshFromInternet(){
        verileriInternettenAl()
    }
    private fun verileriSQLitetanAl(){
        besinYukleniyor.value=true
        launch {
            val besinListesi=BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"Besinleri Rooamdan Aldık",Toast.LENGTH_LONG).show()
        }
    }

    private fun verileriInternettenAl(){

        besinYukleniyor.value=true
        disposable.add(
            besinApiServis.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Besin>>(){
                    override fun onSuccess(t: List<Besin>) {
                        //Başarılı olursak
                        sqliteSakla(t)
                        Toast.makeText(getApplication(),"Besinleri İnternetten Aldık",Toast.LENGTH_LONG).show()


                    }

                    override fun onError(e: Throwable) {
                        //Hata Alırsak
                        besinHataMesaji.value=true
                        besinYukleniyor.value=false
                        e.printStackTrace()
                    }

                })
        )

    }

    private fun besinleriGoster(besinlerListesi:List<Besin>){
        besinler.value=besinlerListesi
        besinHataMesaji.value=false
        besinYukleniyor.value=false
    }

    private fun sqliteSakla(besinListesi:List<Besin>){

        launch{
            val dao=BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi= dao.insertAll(*besinListesi.toTypedArray())
            var i=0
            while(i<besinListesi.size){
                besinListesi[i].uuid=uuidListesi[i].toInt()
                i=i+1
            }
            besinleriGoster(besinListesi)
        }
        ozelSharedPrefences.zamaniKaydet(System.nanoTime())
    }
}