package com.ferit.filipcesnek.dotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.ferit.filipcesnek.dotaapp.DotaApi.Networking
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TournamentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_details.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.tournament_team_row.view.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MatchDetailsActivity : AppCompatActivity(), Callback<MatchDetails> {
    lateinit var matchId: String
    var mFirebaseAuth = FirebaseAuth.getInstance()
    var currentUser = mFirebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_details)
        this.matchId = intent.getStringExtra("matchId")
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        updateUI(this.currentUser)
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
            updateUI(currentUser = this.currentUser)
        }
        findMatchInfo()
    }
    private fun findMatchInfo() {
        try {
            Networking.dotaApiSearchService.fetchMatchInfo(matchId = this.matchId).enqueue(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            toolbar_register.visibility = View.GONE
            toolbar_login.visibility = View.GONE
            toolbar_logout.visibility = View.VISIBLE
        } else {
            toolbar_register.visibility = View.VISIBLE
            toolbar_login.visibility = View.VISIBLE
            toolbar_logout.visibility = View.GONE
        }
    }

    private fun setUpUi(response: MatchDetails) {
        val team1Logo: ImageView = findViewById(R.id.team1Logo)
        val team2Logo: ImageView = findViewById(R.id.team2Logo)

        toolbar_title.text = "Match Details - ${response.matchId}"
        val team1 = response.radiantTeam
        val team2 = response.direTeam
        Picasso.get()
            .load(team1.logo_url)
            .placeholder(R.mipmap.ic_launcher)
            .error(android.R.drawable.stat_notify_error)
            .into(team1Logo)

        Picasso.get()
            .load(team2.logo_url)
            .placeholder(R.mipmap.ic_launcher)
            .error(android.R.drawable.stat_notify_error)
            .into(team2Logo)

        val team1Name: TextView = findViewById(R.id.team1Name)
        val team2Name: TextView = findViewById(R.id.team2Name)

        team1Name.text = team1.name
        team2Name.text = team2.name

        val team1score: TextView = findViewById(R.id.team1Score)
        team1score.text = team1.score.toString()
        val team2score: TextView = findViewById(R.id.team2Score)
        team2score.text = team2.score.toString()

        val team1Label: TextView = findViewById(R.id.matchDetailsTeam1Label)
        val team2Label: TextView = findViewById(R.id.matchDetailsTeam2Label)

        team1Label.text = team1.name
        team2Label.text = team2.name

        val winnerTextView: TextView = findViewById(R.id.matchWinner)
        var winner: String = ""

        if(team1.team_id == response.winner) {
            winner = team1.name
        } else if (team2.team_id == response.winner) {
            winner = team2.name
        }
        winnerTextView.text = "Winner: " + winner

        val team1Players = response.radiantPlayers
        val team2Players = response.direPlayers

        val player1Team1Name: TextView = findViewById(R.id.player1team1Name)
        val player2Team1Name: TextView = findViewById(R.id.player2team1Name)
        val player3Team1Name: TextView = findViewById(R.id.player3team1Name)
        val player4Team1Name: TextView = findViewById(R.id.player4team1Name)
        val player5Team1Name: TextView = findViewById(R.id.player5team1Name)

        player1Team1Name.text = team1Players[0].name
        player2Team1Name.text = team1Players[1].name
        player3Team1Name.text = team1Players[2].name
        player4Team1Name.text = team1Players[3].name
        player5Team1Name.text = team1Players[4].name

        val player1Team1Hero: TextView = findViewById(R.id.player1team1Hero)
        val player2Team1Hero: TextView = findViewById(R.id.player2team1Hero)
        val player3Team1Hero: TextView = findViewById(R.id.player3team1Hero)
        val player4Team1Hero: TextView = findViewById(R.id.player4team1Hero)
        val player5Team1Hero: TextView = findViewById(R.id.player5team1Hero)

        player1Team1Hero.text = team1Players[0].hero
        player2Team1Hero.text = team1Players[1].hero
        player3Team1Hero.text = team1Players[2].hero
        player4Team1Hero.text = team1Players[3].hero
        player5Team1Hero.text = team1Players[4].hero

        val player1Team1Kills: TextView = findViewById(R.id.player1team1Kills)
        val player2Team1Kills: TextView = findViewById(R.id.player2team1Kills)
        val player3Team1Kills: TextView = findViewById(R.id.player3team1Kills)
        val player4Team1Kills: TextView = findViewById(R.id.player4team1Kills)
        val player5Team1Kills: TextView = findViewById(R.id.player5team1Kills)

        player1Team1Kills.text = team1Players[0].kills.toString()
        player2Team1Kills.text = team1Players[1].kills.toString()
        player3Team1Kills.text = team1Players[2].kills.toString()
        player4Team1Kills.text = team1Players[3].kills.toString()
        player5Team1Kills.text = team1Players[4].kills.toString()

        val player1Team1Deaths: TextView = findViewById(R.id.player1team1Deaths)
        val player2Team1Deaths: TextView = findViewById(R.id.player2team1Deaths)
        val player3Team1Deaths: TextView = findViewById(R.id.player3team1Deaths)
        val player4Team1Deaths: TextView = findViewById(R.id.player4team1Deaths)
        val player5Team1Deaths: TextView = findViewById(R.id.player5team1Deaths)

        player1Team1Deaths.text = team1Players[0].deaths.toString()
        player2Team1Deaths.text = team1Players[1].deaths.toString()
        player3Team1Deaths.text = team1Players[2].deaths.toString()
        player4Team1Deaths.text = team1Players[3].deaths.toString()
        player5Team1Deaths.text = team1Players[4].deaths.toString()

        val player1Team1Assists: TextView = findViewById(R.id.player1team1Assists)
        val player2Team1Assists: TextView = findViewById(R.id.player2team1Assists)
        val player3Team1Assists: TextView = findViewById(R.id.player3team1Assists)
        val player4Team1Assists: TextView = findViewById(R.id.player4team1Assists)
        val player5Team1Assists: TextView = findViewById(R.id.player5team1Assists)

        player1Team1Assists.text = team1Players[0].assists.toString()
        player2Team1Assists.text = team1Players[1].assists.toString()
        player3Team1Assists.text = team1Players[2].assists.toString()
        player4Team1Assists.text = team1Players[3].assists.toString()
        player5Team1Assists.text = team1Players[4].assists.toString()

        val player1Team2Name: TextView = findViewById(R.id.player1team2Name)
        val player2Team2Name: TextView = findViewById(R.id.player2team2Name)
        val player3Team2Name: TextView = findViewById(R.id.player3team2Name)
        val player4Team2Name: TextView = findViewById(R.id.player4team2Name)
        val player5Team2Name: TextView = findViewById(R.id.player5team2Name)

        player1Team2Name.text = team2Players[0].name
        player2Team2Name.text = team2Players[1].name
        player3Team2Name.text = team2Players[2].name
        player4Team2Name.text = team2Players[3].name
        player5Team2Name.text = team2Players[4].name

        val player1Team2Hero: TextView = findViewById(R.id.player1team2Hero)
        val player2Team2Hero: TextView = findViewById(R.id.player2team2Hero)
        val player3Team2Hero: TextView = findViewById(R.id.player3team2Hero)
        val player4Team2Hero: TextView = findViewById(R.id.player4team2Hero)
        val player5Team2Hero: TextView = findViewById(R.id.player5team2Hero)

        player1Team2Hero.text = team2Players[0].hero
        player2Team2Hero.text = team2Players[1].hero
        player3Team2Hero.text = team2Players[2].hero
        player4Team2Hero.text = team2Players[3].hero
        player5Team2Hero.text = team2Players[4].hero

        val player1Team2Kills: TextView = findViewById(R.id.player1team2Kills)
        val player2Team2Kills: TextView = findViewById(R.id.player2team2Kills)
        val player3Team2Kills: TextView = findViewById(R.id.player3team2Kills)
        val player4Team2Kills: TextView = findViewById(R.id.player4team2Kills)
        val player5Team2Kills: TextView = findViewById(R.id.player5team2Kills)

        player1Team2Kills.text = team2Players[0].kills.toString()
        player2Team2Kills.text = team2Players[1].kills.toString()
        player3Team2Kills.text = team2Players[2].kills.toString()
        player4Team2Kills.text = team2Players[3].kills.toString()
        player5Team2Kills.text = team2Players[4].kills.toString()

        val player1Team2Deaths: TextView = findViewById(R.id.player1team2Deaths)
        val player2Team2Deaths: TextView = findViewById(R.id.player2team2Deaths)
        val player3Team2Deaths: TextView = findViewById(R.id.player3team2Deaths)
        val player4Team2Deaths: TextView = findViewById(R.id.player4team2Deaths)
        val player5Team2Deaths: TextView = findViewById(R.id.player5team2Deaths)

        player1Team2Deaths.text = team2Players[0].deaths.toString()
        player2Team2Deaths.text = team2Players[1].deaths.toString()
        player3Team2Deaths.text = team2Players[2].deaths.toString()
        player4Team2Deaths.text = team2Players[3].deaths.toString()
        player5Team2Deaths.text = team2Players[4].deaths.toString()

        val player1Team2Assists: TextView = findViewById(R.id.player1team2Assists)
        val player2Team2Assists: TextView = findViewById(R.id.player2team2Assists)
        val player3Team2Assists: TextView = findViewById(R.id.player3team2Assists)
        val player4Team2Assists: TextView = findViewById(R.id.player4team2Assists)
        val player5Team2Assists: TextView = findViewById(R.id.player5team2Assists)

        player1Team2Assists.text = team2Players[0].assists.toString()
        player2Team2Assists.text = team2Players[1].assists.toString()
        player3Team2Assists.text = team2Players[2].assists.toString()
        player4Team2Assists.text = team2Players[3].assists.toString()
        player5Team2Assists.text = team2Players[4].assists.toString()
    }

    override fun onResponse(call: Call<MatchDetails>, response: Response<MatchDetails>) {
        response.body()?.let { setUpUi(it) }
    }

    override fun onFailure(call: Call<MatchDetails>, t: Throwable) {
        Toast.makeText(this, R.string.networkError, Toast.LENGTH_SHORT).show()
    }
}