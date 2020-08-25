package com.ferit.filipcesnek.dotaapp

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ferit.filipcesnek.dotaapp.DotaApi.Networking
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TournamentData
import kotlinx.android.synthetic.main.activity_tournament_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TournamentInfoViewModel(application: Application): AndroidViewModel(application) {
    val someChangingVar = MutableLiveData<TournamentData>()
    fun setSomeChangingVar(newData: TournamentData) {
        someChangingVar.value = newData
    }
}

class TournamentInfoActivity : AppCompatActivity(), Callback<TournamentData> {
    private val viewModel by viewModels<TournamentInfoViewModel>()
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_info)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        this.url = intent.getStringExtra("url")
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tourneyInfo, R.id.navigation_tourneyStandings, R.id.navigation_matches, R.id.navigation_teams
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        findTourneyInfo()
    }

    private fun findTourneyInfo() {
        try {
            Networking.dotaApiSearchService.fetchTourneyInfo(url = this.url).enqueue(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailure(call: Call<TournamentData>, t: Throwable) {
        Toast.makeText(this, R.string.networkError, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<TournamentData>, response: Response<TournamentData>) {
        val results = response.body()
        results?.let { viewModel.setSomeChangingVar(it) }
        Log.d("Api_Results", results.toString())
    }
}