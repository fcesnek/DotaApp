package com.ferit.filipcesnek.dotaapp.matches

data class Match (
    val team1score: String,
    val team2score: String,
    val status: String,
    val team1: MatchTeamInfo,
    val team2: MatchTeamInfo,
    val startTime: String,
    val tournament: MatchTournamentInfo
)