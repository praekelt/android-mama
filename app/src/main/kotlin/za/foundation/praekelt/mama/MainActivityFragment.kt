package za.foundation.praekelt.mama

import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.fragment_main.rl_frag_main_root
import org.jetbrains.anko.*
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import za.foundation.praekelt.mama.rest.adapter.RepoAdapter
import za.foundation.praekelt.mama.rest.model.Repo
import za.foundation.praekelt.mama.rest.service.UCDService
import za.foundation.praekelt.mama.util.Constants
import java.util.concurrent.TimeUnit

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment : Fragment(), AnkoLogger {
    var isConnected:Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)
        if(savedInstanceState == null && !checkIfLocalRepoExists()) {
            cloneRepo()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super<Fragment>.onResume()
        toast("connected = $isConnected")
        if(!isConnected)
            Snackbar.make(rl_frag_main_root, "No internet connection available", Snackbar.LENGTH_LONG).show()


    }

    fun checkIfLocalRepoExists():Boolean{
        var exists:Boolean = false;
        var existsFuture = async{
            info("async start")
            if(act.getFilesDir().listFiles(filter={file -> file.getName() == "repo"})?.size() == 1)
                exists = true
            info("async end = $exists")
        }
        existsFuture.get(500, TimeUnit.MILLISECONDS)
        return exists;
    }

    fun cloneRepo(): Boolean{
        info("cloning repo")
        var success = false;

        val networkInfo: NetworkInfo? = act.connectivityManager.getActiveNetworkInfo()
        if(networkInfo?.isConnected()?:false == false) {
            isConnected = false
            return isConnected
        }

        val gson:Gson = GsonBuilder().registerTypeAdapter(javaClass<Repo>(), RepoAdapter()).create()

        val restAdapter:RestAdapter = RestAdapter.Builder()
                            .setEndpoint(Constants.BASE_URL)
                            .setConverter(GsonConverter(gson))
                            .build()

        info("creating service")
        val ucdService:UCDService = restAdapter.create(javaClass<UCDService>())

        info("getting repo status")
        ucdService.getRepoStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({println(it)}, {it.printStackTrace()})

        info("clone repo success = $success")
        return success
    }
}
