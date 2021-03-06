package com.ferit.filipcesnek.dotaapp.tournamentInfo.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoViewModel
import com.ferit.filipcesnek.dotaapp.tournamentInfo.MatchFromApi
import com.ferit.filipcesnek.dotaapp.tournaments.TournamentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_tournament_list.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_tourney_matches.*

class TourneyMatchesFragment : Fragment() {
    private val viewModel by activityViewModels<TournamentInfoViewModel>()
    private var matches: List<MatchFromApi> = listOf()
    var mFirebaseAuth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = mFirebaseAuth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tourney_matches, container, false)
        observeTournamentData()
        return root
    }

    private fun observeTournamentData() {
        viewModel.mutableTournamentData.observe(viewLifecycleOwner, Observer { newValue->
            this.matches = newValue.matches
            recyclerViewTournamentMatches.adapter = TourneyMatchesAdapter(currentUser)
            recyclerViewTournamentMatches.layoutManager = LinearLayoutManager(
                activity,
                RecyclerView.VERTICAL,
                false
            )
            recyclerViewTournamentMatches.addItemDecoration(
                DividerItemDecoration(activity, RecyclerView.VERTICAL)
            )
            recyclerViewTournamentMatches.itemAnimator = DefaultItemAnimator()
            (recyclerViewTournamentMatches.adapter as TourneyMatchesAdapter).refreshData(matches)
        })
    }
}