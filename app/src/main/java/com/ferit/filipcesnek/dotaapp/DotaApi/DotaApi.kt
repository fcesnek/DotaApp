package com.ferit.filipcesnek.dotaapp.DotaApi

import com.ferit.filipcesnek.dotaapp.MatchDetails
import com.ferit.filipcesnek.dotaapp.matches.Match
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TournamentData
import com.ferit.filipcesnek.dotaapp.tournaments.Tournament
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://dota-app-api.herokuapp.com"

interface DotaApi {
    @GET("/tourneyList")
    fun fetchTournaments(): Call<List<Tournament>>

    @GET("/gamesList")
    fun fetchMatches(): Call<List<Match>>

    @GET("/tourneyInfo")
    fun fetchTourneyInfo(@Query("url") url: String): Call<TournamentData>

    @GET("/matchInfo")
        fun fetchMatchInfo(@Query("matchId") matchId: String): Call<MatchDetails>
}