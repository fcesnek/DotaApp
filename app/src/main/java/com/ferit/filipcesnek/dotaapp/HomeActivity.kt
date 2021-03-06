package com.ferit.filipcesnek.dotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class HomeActivity : AppCompatActivity() {
    var mFirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        var currentUser = mFirebaseAuth.currentUser

        toolbar_title.text = "Home"
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
            currentUser = mFirebaseAuth.currentUser
            updateUI(currentUser)
        }

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

        savedTourneysButton.setOnClickListener { v ->
            val intent = Intent(v?.context, SavedTournamentsActivity::class.java)
            startActivity(intent)
        }

        savedMatchesButton.setOnClickListener { v ->
            val intent = Intent(v?.context, SavedMatchesActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mFirebaseAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            toolbar_register.visibility = View.GONE
            toolbar_login.visibility = View.GONE
            toolbar_logout.visibility = View.VISIBLE
            savedTourneysButton.visibility = View.VISIBLE
            savedMatchesButton.visibility = View.VISIBLE
        } else {
            toolbar_register.visibility = View.VISIBLE
            toolbar_login.visibility = View.VISIBLE
            toolbar_logout.visibility = View.GONE
            savedTourneysButton.visibility = View.GONE
            savedMatchesButton.visibility = View.GONE
        }
    }
}