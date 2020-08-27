package com.ferit.filipcesnek.dotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.DotaApi.Networking
import com.ferit.filipcesnek.dotaapp.tournaments.Tournament
import com.ferit.filipcesnek.dotaapp.tournaments.TournamentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_tournament_list.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TournamentListActivity : AppCompatActivity(), Callback<List<Tournament>> {
    var mFirebaseAuth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = mFirebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_list)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        toolbar_title.text = "Tournament List"
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
    }

    private fun setUpUi(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            toolbar_register.visibility = View.GONE
            toolbar_login.visibility = View.GONE
            toolbar_logout.visibility = View.VISIBLE
        } else {
            toolbar_register.visibility = View.VISIBLE
            toolbar_login.visibility = View.VISIBLE
            toolbar_logout.visibility = View.GONE
        }

        recyclerViewTournaments.adapter = TournamentsAdapter(currentUser)
        recyclerViewTournaments.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        recyclerViewTournaments.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
        recyclerViewTournaments.itemAnimator = DefaultItemAnimator()
        findTournaments()
    }

    private fun findTournaments() {
        Networking.dotaApiSearchService.fetchTournaments().enqueue(this)
    }

    override fun onFailure(call: Call<List<Tournament>>, t: Throwable) {
        Toast.makeText(this, R.string.networkError, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<List<Tournament>>, response: Response<List<Tournament>>) {
        val results = response.body() ?: listOf()
        Log.d("Results", results.toString())
        (recyclerViewTournaments.adapter as TournamentsAdapter).refreshData(results)
    }
}