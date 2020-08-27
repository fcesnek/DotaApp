package com.ferit.filipcesnek.dotaapp.tournaments

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tournament_row.view.*


class TournamentsAdapter(val currentUser: FirebaseUser?): RecyclerView.Adapter<TournamentsAdapter.TournamentsViewHolder>() {
    inner class TournamentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var database = FirebaseDatabase.getInstance()
        var firebaseTournamentsRef = database.getReference("tournaments/${currentUser?.uid}")
        var dataSnapshot: Iterable<DataSnapshot> = mutableListOf()
        init {
            itemView.setOnClickListener{
                var position: Int = adapterPosition
                val context = itemView.context
                val tournament = tournaments[position]
                val intent = Intent(context, TournamentInfoActivity::class.java)

                intent.putExtra("url", tournament.link)
                itemView.context.startActivity(intent)
            }
        }
        fun bind(result: Tournament, currentUser: FirebaseUser?) {
            itemView.tourneyName.text = result.name
            itemView.tourneyStatus.text = result.status
            itemView.add_tournament_btn.setOnClickListener {
                val item = FirebaseTournament(result, currentUser?.uid)
                val newRef: DatabaseReference = firebaseTournamentsRef.push()
                newRef.setValue(item)
            }

            itemView.remove_tournament_btn.setOnClickListener {
                val refkey = this.dataSnapshot.find {
                    el ->
                    result.name == el.getValue(FirebaseTournament::class.java)?.tournament!!.name
                }
                firebaseTournamentsRef.child(refkey?.key.toString()).removeValue()
                itemView.add_tournament_btn.visibility = View.VISIBLE
                itemView.remove_tournament_btn.visibility = View.GONE
            }
            val ref = this
            firebaseTournamentsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ref.dataSnapshot = snapshot.children
                    for (postSnapshot in snapshot.children) {
                        val tournament = postSnapshot.getValue(FirebaseTournament::class.java)
                        if (result.name == tournament?.tournament!!.name) {
                            itemView.add_tournament_btn.visibility = View.GONE
                            itemView.remove_tournament_btn.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("error", databaseError.message)
                }
            })


            when (result.status) {
                "Ongoing" -> {
                    itemView.tourneyStatus.setBackgroundColor(Color.parseColor("#80c8e6c9"))
                }
                "Upcoming" -> {
                    itemView.tourneyStatus.setBackgroundColor(Color.parseColor("#80bbdefb"))
                }
                "Completed" -> {
                    itemView.tourneyStatus.setBackgroundColor(Color.parseColor("#80fff9c4"))
                }
            }
            if(currentUser != null) {
                itemView.add_tournament_btn.visibility = View.VISIBLE
            } else {
                itemView.add_tournament_btn.visibility = View.GONE
            }
        }
    }
    val tournaments: MutableList<Tournament> = mutableListOf()
    fun refreshData(newResults: List<Tournament>) {
        tournaments.clear()
        tournaments.addAll(newResults)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.tournament_row,
            parent,
            false
        )
        return TournamentsViewHolder(
            view
        )
    }

    override fun getItemCount() = tournaments.size

    override fun onBindViewHolder(holder: TournamentsViewHolder, position: Int) {
        holder.bind(tournaments[position], currentUser)
    }
}