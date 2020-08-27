package com.ferit.filipcesnek.dotaapp

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_tournament_info.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TournamentInfoViewModel(application: Application): AndroidViewModel(application) {
    val mutableTournamentData = MutableLiveData<TournamentData>()
    fun setChangingTourneyData(newData: TournamentData) {
        mutableTournamentData.value = newData
    }
}

class TournamentInfoActivity : AppCompatActivity(), Callback<TournamentData> {
    var mFirebaseAuth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = mFirebaseAuth.currentUser
    private val viewModel by viewModels<TournamentInfoViewModel>()
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_info)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        this.url = intent.getStringExtra("url")!!
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tourneyInfo, R.id.navigation_tourneyStandings, R.id.navigation_matches, R.id.navigation_teams
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        toolbar_title.text = "Tournament Information"
        toolbar_login.setOnClickListener { v ->
            val intent = Intent(v?.context, LoginActivity::class.java)
            startActivity(intent)
        }

        toolbar_register.setOnClickListener { v ->
            val intent = Intent(v?.context, SignUpActivity::class.java)
            startActivity(intent)
        }

        toolbar_logout.setOnClickListener { v ->
            mFirebaseAuth.signOut()
            this.currentUser = mFirebaseAuth.currentUser
            setUpUi(this.currentUser)
        }
        setUpUi(this.currentUser)

        findTourneyInfo()
    }

    private fun setUpUi(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            toolbar_register.visibility = View.GONE
            toolbar_login.visibility = View.GONE
            toolbar_logout.visibility = View.VISIBLE
        } else {
            toolbar_register.visibility = View.VISIBLE
            toolbar_login.visibility = View.VISIBLE
            toolbar_logout.visibility = View.GONE
        }
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
        results?.let { viewModel.setChangingTourneyData(it) }
        Log.d("Api_Results", results.toString())
    }
}