package com.ferit.filipcesnek.dotaapp.tournamentInfo.matches

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.MatchDetailsActivity
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.tournamentInfo.FirebaseMatch
import com.ferit.filipcesnek.dotaapp.tournamentInfo.MatchFromApi
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tournament_match_row.view.*
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
                val item = FirebaseMatch(result)
                val newRef: DatabaseReference = firebaseMatchesRef.push()
                newRef.setValue(item)
            }

            itemView.remove_match_btn.setOnClickListener {
                val refkey = this.dataSnapshot.find {
                        el ->
                    result.matchId == el.getValue(FirebaseMatch::class.java)?.match!!.matchId
                }
                firebaseMatchesRef.child(refkey?.key.toString()).removeValue()
                itemView.add_match_btn.visibility = View.VISIBLE
                itemView.remove_match_btn.visibility = View.GONE
            }
            val ref = this
            firebaseMatchesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ref.dataSnapshot = snapshot.children
                    for (postSnapshot in snapshot.children) {
                        val match = postSnapshot.getValue(FirebaseMatch::class.java)
                        if (result.matchId == match?.match!!.matchId) {
                            itemView.add_match_btn.visibility = View.GONE
                            itemView.remove_match_btn.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("error", databaseError.message)
                }
            })
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