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
import com.ferit.filipcesnek.dotaapp.tournamentInfo.FirebaseMatch
import com.ferit.filipcesnek.dotaapp.tournamentInfo.MatchFromApi
import com.ferit.filipcesnek.dotaapp.tournamentInfo.matches.TourneyMatchesAdapter
import com.ferit.filipcesnek.dotaapp.tournaments.FirebaseTournament
import com.ferit.filipcesnek.dotaapp.tournaments.Tournament
import com.ferit.filipcesnek.dotaapp.tournaments.TournamentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_tournament_list.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_tourney_matches.*
import kotlinx.android.synthetic.main.tournament_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedMatchesActivity : AppCompatActivity() {
    var mFirebaseAuth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = mFirebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tourney_matches)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        toolbar_title.text = "Matches List"
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
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
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

        recyclerViewTournamentMatches.adapter = TourneyMatchesAdapter(currentUser)
        recyclerViewTournamentMatches.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        recyclerViewTournamentMatches.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
        recyclerViewTournamentMatches.itemAnimator = DefaultItemAnimator()
        findMatches()
    }

    private fun findMatches() {
        var database = FirebaseDatabase.getInstance()
        var firebaseTournamentsRef = database.getReference("matches/${currentUser?.uid}")

        firebaseTournamentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var results: MutableList<MatchFromApi> = mutableListOf()
                for (postSnapshot in snapshot.children) {
                    val match = postSnapshot.getValue(FirebaseMatch::class.java)
                    results.add(match!!.match)
                }
                (recyclerViewTournamentMatches.adapter as TourneyMatchesAdapter).refreshData(results)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("error", databaseError.message)
            }
        })
    }
}