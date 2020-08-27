package com.ferit.filipcesnek.dotaapp.tournamentInfo.matches

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.MatchDetailsActivity
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoActivity
import com.ferit.filipcesnek.dotaapp.tournamentInfo.MatchFromApi
import com.ferit.filipcesnek.dotaapp.tournaments.FirebaseTournament
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.tournament_match_row.view.*
import kotlinx.android.synthetic.main.tournament_row.view.*
import java.util.concurrent.TimeUnit

class TourneyMatchesAdapter(val currentUser: FirebaseUser?): RecyclerView.Adapter<TourneyMatchesAdapter.TourneyMatchesViewHolder>() {
    inner class TourneyMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var database = FirebaseDatabase.getInstance()
        var firebaseMatchesRef = database.getReference("matches/${currentUser?.uid}")
        var dataSnapshot: Iterable<DataSnapshot> = mutableListOf()

        init {
            itemView.setOnClickListener{
                val position: Int = adapterPosition
                val match = matches[position]

                val context = itemView.context
                val intent = Intent(context, MatchDetailsActivity::class.java)

                intent.putExtra("matchId", match.matchId)
                itemView.context.startActivity(intent)
            }
        }
        fun bind(result: MatchFromApi) {
            itemView.team1NameTourney.text = result.team1
            itemView.team2NameTourney.text = result.team2
            val minutes = TimeUnit.SECONDS.toMinutes(result.duration.toLong())
            var seconds  = result.duration % 60
            val position: Int = adapterPosition
            if (seconds < 10) {
                itemView.matchDuration.text = "Duration: $minutes:0$seconds"
            } else {
                itemView.matchDuration.text = "Duration: $minutes:$seconds"
            }
            itemView.matchWinnerTournament.text = "Winner: ${result.winner}"
            if(position % 2 == 1)
            {
                itemView.setBackgroundColor(Color.parseColor("#e8f5e9"));
            }
            else
            {
                itemView.setBackgroundColor(Color.parseColor("#e0f7fa"));
            }

            itemView.add_match_btn.setOnClickListener {
                val item = FirebaseMatch(result, currentUser?.uid)
                val newRef: DatabaseReference = firebaseMatchesRef.push()
                newRef.setValue(item)
            }

        }
    }
    val matches: MutableList<MatchFromApi> = mutableListOf()
    fun refreshData(newResults: List<MatchFromApi>) {
        matches.clear()
        matches.addAll(newResults)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourneyMatchesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_match_row, parent, false)
        return TourneyMatchesViewHolder(
            view
        )
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: TourneyMatchesViewHolder, position: Int) {
        holder.bind(matches[position])

    }
}