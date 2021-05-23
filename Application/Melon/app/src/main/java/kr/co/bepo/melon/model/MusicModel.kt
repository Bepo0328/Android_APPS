package kr.co.bepo.melon.model

data class MusicModel(
    val idx: Long,
    val track: String,
    val streamUrl: String,
    val artist: String,
    val coverUrl: String,
    val isPlaying: Boolean = false
)