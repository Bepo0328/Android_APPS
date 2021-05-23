package kr.co.bepo.melon.model

import kr.co.bepo.melon.dto.MusicDTO
import kr.co.bepo.melon.entity.MusicEntity

fun MusicEntity.mapper(): MusicModel =
    MusicModel(
        idx = idx,
        track = track,
        streamUrl = streamUrl,
        artist = artist,
        coverUrl = coverUrl
    )

fun MusicDTO.mapper(): PlayerModel =
    PlayerModel(
        playMusicList = musics.map { musicEntity ->
            musicEntity.mapper()
        }
    )