package com.ferit.filipcesnek.dotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val listTournamentsButton: Button = findViewById(R.id.tournamentListButton)
        val listMatchesButton: Button = findViewById(R.id.matchesListButton)
        listTournamentsButton.setOnClickListener { v ->
            val intent = Intent(v?.context, TournamentListActivity::class.java)
            startActivity(intent)
        }

        listMatchesButton.setOnClickListener { v ->
            val intent = Intent(v?.context, MatchesListActivity::class.java)
            startActivity(intent)
        }
    }
}