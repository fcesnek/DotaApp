package com.ferit.filipcesnek.dotaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.DotaApi.Networking
import com.ferit.filipcesnek.dotaapp.matches.Match
import com.ferit.filipcesnek.dotaapp.matches.MatchesAdapter
import com.ferit.filipcesnek.dotaapp.tournaments.Tournament
import com.ferit.filipcesnek.dotaapp.tournaments.TournamentsAdapter
import kotlinx.android.synthetic.main.activity_matches_list.*
import kotlinx.android.synthetic.main.activity_tournament_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchesListActivity : AppCompatActivity(), Callback<List<Match>> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches_list)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        setUpUi()
    }

    private fun setUpUi() {
        recyclerViewMatches.adapter = MatchesAdapter()
        recyclerViewMatches.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        recyclerViewMatches.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
        recyclerViewMatches.itemAnimator = DefaultItemAnimator()
        findMatches()
    }

    private fun findMatches() {
        Networking.dotaApiSearchService.fetchMatches().enqueue(this)
    }

    override fun onFailure(call: Call<List<Match>>, t: Throwable) {
        Toast.makeText(this, R.string.networkError, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
        val results = response.body() ?: listOf()
        Log.d("Results", results.toString())
        (recyclerViewMatches.adapter as MatchesAdapter).refreshData(results)
    }
}