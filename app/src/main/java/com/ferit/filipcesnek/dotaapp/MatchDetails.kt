package com.ferit.filipcesnek.dotaapp

data class MatchDetails (
    val matchId: String,
    val radiantTeam: MatchDetailsTeam,
    val direTeam: MatchDetailsTeam,
    val winner: String,
    val direPlayers: List<MatchDetailsPlayer>,
    val radiantPlayers: List<MatchDetailsPlayer>
)

data class MatchDetailsTeam (
    val team_id: String,
    val name: String,
    val tag: String,
    val logo_url: String,
    val score: Int
)

data class MatchDetailsPlayer (
    val name: String,
    val accountId: String,
    val heroId: String,
    val hero: String,
    val assists: Int,
    val deaths: Int,
    val kills: Int
)