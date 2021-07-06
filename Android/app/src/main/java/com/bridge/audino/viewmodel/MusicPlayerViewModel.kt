package com.bridge.audino.viewmodel

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridge.audino.model.Track
import com.bridge.audino.repository.TrackRepository
import com.bridge.audino.utils.listener.MusicPlayerListener

class MusicPlayerViewModel : ViewModel(), MusicPlayerListener, SeekBar.OnSeekBarChangeListener {
    companion object {
        const val TRACK_ID = "com.bridge.audino.viewmodel.MusicPlayerViewModel.track"
        const val REPEAT_OFF = 0
        const val REPEAT_ON = 1
        const val REPEAT_ONE = 2

        const val ALBUM_ID = "com.bridge.audino.viewmodel.MusicPlayerViewModel.albumId"
        const val ARTIST_ID = "com.bridge.audino.viewmodel.MusicPlayerViewModel.artistId"
        const val QUERY_TEXT = "com.bridge.audino.viewmodel.MusicPlayerViewModel.query"
        const val FROM_ID = "com.bridge.audino.viewmodel.MusicPlayerViewModel.fromId"

        const val FROM_HOME = 0
        const val FROM_FAVORITES = 1
        const val FROM_SEARCH = 2
        const val FROM_ARTIST = 3
        const val FROM_ALBUM = 4
    }

    private val mCurrentTrack: MutableLiveData<Track> by lazy { MutableLiveData<Track>() }
    private var mTrackList: List<Track> = listOf()

    private val mMediaPlayer: MediaPlayer = MediaPlayer().apply { configMediaPlayer(this) }
    private val mIsLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    private val mIsMusicPlaying: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    private val mRepeatStatus: MutableLiveData<Int> by lazy { MutableLiveData<Int>().apply { value = REPEAT_OFF } }
    private var mPlayListFinished: Boolean = false
    private val mTrackDuration: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mPlayerCurrentPosition: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mIsFavorite: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    private var isMediaPlayerReleased: Boolean = false

    private lateinit var mRunnable: Runnable
    private var mHandler: Handler = Handler()

    fun isLoading(): LiveData<Boolean> {
        return mIsLoading
    }

    fun getCurrentTrack(): LiveData<Track> {
        return mCurrentTrack
    }

    fun isMusicPlaying(): LiveData<Boolean> {
        return mIsMusicPlaying
    }

    fun getRepeatStatus(): LiveData<Int> {
        return mRepeatStatus
    }

    fun getTrackDuration(): LiveData<Int> {
        return mTrackDuration
    }

    fun getPlayerCurrentPosition(): LiveData<Int> {
        return mPlayerCurrentPosition
    }

    fun isFavorite(): LiveData<Boolean> {
        return mIsFavorite
    }

    override fun onShuffleClicked() {

    }

    override fun onSkipPreviousClicked() {
        playPreviousTrack()
    }

    override fun onPlayOrPauseClicked() {
        if (mPlayListFinished) {
            restartPlayList()
        } else {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
                mHandler.removeCallbacks(mRunnable)
            } else {
                mMediaPlayer.start()
                mHandler.postDelayed(mRunnable, 1000)
            }
            mIsMusicPlaying.postValue(mMediaPlayer.isPlaying)
        }
    }

    override fun onSkipNextClicked() {
        playNextTrack()
    }

    override fun onRepeatClicked() {

    }

    fun onFavoriteClicked() {
        mCurrentTrack.value?.let {
            persistFavorite(it.id)
        }
    }

    private fun persistFavorite(id: String) {
        val trackClicked = mTrackList.find { it.id == id } ?: return
        if (trackClicked.favorite) {
            TrackRepository.removeFavorite(id, {
                setFavoriteFromLists(id, false)
            }) {
                Log.d(
                    "TrackListViewModel",
                    "A API falhou em remover a musica de id $id da lista de musicas favoritas"
                )
            }
        } else {
            TrackRepository.addFavorite(id, {
                setFavoriteFromLists(id, true)
            }) {
                Log.d(
                    "TrackListViewModel",
                    "A API falhou em adicionar a musica de id $id à lista de musicas favoritas"
                )
            }
        }
    }

    private fun setFavoriteFromLists(id: String, favorite: Boolean) {
        mTrackList.forEach {
            if (it.id == id) {
                it.favorite = favorite
                if (mCurrentTrack.value?.id == id) {
                    mIsFavorite.postValue(favorite)
                }
                return
            }
        }
    }

    fun init(trackId: String?, from: Int, textQuery: String?, artistId: String?, albumId: String?) {
        when (from) {
            FROM_HOME -> {
                if (trackId == null) throw Exception("Nao foi passado o id da musica")
                TrackRepository.getPopular({ trackList ->
                    updateFavoritesConfigureAndPlay(trackId, trackList)
                }, {
                    Log.d("TrackListViewModel", "A API falhou em resgatar a lista de top musicas")
                })
            }
            FROM_FAVORITES -> {
                if (trackId == null) throw Exception("Nao foi passado o id da musica")
                TrackRepository.getFavorites({ trackList ->
                    trackList.forEach { it.favorite = true }
                    configureListAndPlay(trackId, trackList)
                }, {
                    Log.d("TrackListViewModel", "A API falhou em resgatar a lista de musicas favoritas")
                })
            }
            FROM_SEARCH -> {
                if (trackId == null) throw Exception("Nao foi passado o id da musica")
                if (textQuery == null) throw Exception("Nao foi passada a query")
                TrackRepository.queryList(textQuery, { trackList ->
                    updateFavoritesConfigureAndPlay(trackId, trackList)
                }) {
                    Log.d("TrackListViewModel", "A API falhou em resgatar uma lista com a query $textQuery")
                }
            }
            FROM_ARTIST -> {
                if (artistId == null) throw Exception("Não foi passado o ID do artista")
                TrackRepository.getArtist(artistId, { fullArtist ->
                    val trackList = fullArtist.topTracks
                    updateFavoritesConfigureAndPlay(trackId, trackList)
                }) {
                    Log.d("TrackListViewModel", "A API falhou em resgatar o artista de id $artistId")
                }
            }
            FROM_ALBUM -> {
                if (albumId == null) throw Exception("Não foi passado o ID do álbum")
                TrackRepository.getAlbum(albumId, { fullAlbum ->
                    val trackList = fullAlbum.tracks
                    updateFavoritesConfigureAndPlay(trackId, trackList)
                }) {
                    Log.d("TrackListViewModel", "A API falhou em resgatar o álbum de id $albumId")
                }
            }
        }
    }

    private fun updateFavoritesConfigureAndPlay(trackId: String?, list: List<Track>) {
        TrackRepository.getFavorites({ favList ->
            list.forEach { it.favorite = favList.contains(it) }
            configureListAndPlay(trackId, list)
        }, {
            Log.d("TrackListViewModel", "A API falhou em resgatar a lista de musicas favoritas")
        })
    }

    private fun clearTrackList(list: List<Track>): List<Track> {
        return list.filter { !it.previewUrl.isNullOrEmpty() }
    }

    private fun configureListAndPlay(trackId: String?, list: List<Track>) {
        val cleanList = clearTrackList(list)
        mTrackList = cleanList
        val track = cleanList.find { it.id == trackId } ?: cleanList.first()
        mIsLoading.postValue(false)
        playTrack(track)
    }

    private fun restartPlayList() {
        val trackToPlay = mTrackList.first()
        mCurrentTrack.postValue(trackToPlay)
        mPlayListFinished = false
        playTrack(trackToPlay)
    }

    private fun onMusicComplete() {
        if (!mMediaPlayer.isLooping) {
            mCurrentTrack.value?.let {
                val isLast = it == mTrackList.last()
                if (isLast && mRepeatStatus.value == REPEAT_OFF) {
                    mPlayListFinished = true
                    mIsMusicPlaying.postValue(false)
                } else {
                    playNextTrack()
                }
            }
        }
    }

    private fun playPreviousTrack() {
        mCurrentTrack.value?.let {
            val isFirst = it == mTrackList.first()
            val previousTrack = if (isFirst) {
                if (mRepeatStatus.value == REPEAT_ON) {
                    mTrackList.last()
                } else {
                    it
                }
            } else {
                val previousIndex = mTrackList.indexOf(it) - 1
                mTrackList[previousIndex]
            }
            playTrack(previousTrack)
        }
    }

    private fun playNextTrack() {
        mCurrentTrack.value?.let {
            val isLast = it == mTrackList.last()
            val nextTrack = if (isLast) {
                if (mRepeatStatus.value == REPEAT_ON) {
                    mTrackList.first()
                } else {
                    return
                }
            } else {
                val nextIndex = mTrackList.indexOf(it) + 1
                mTrackList[nextIndex]
            }
            playTrack(nextTrack)
        }
    }

    private fun playTrack(track: Track?) {
        if (!isMediaPlayerReleased) {
            val url = track?.previewUrl ?: throw Exception("Música não especificada!")
            mMediaPlayer.apply {
                reset()
                setDataSource(url)
                prepare()
                start()
            }
            mMediaPlayer.isLooping = mRepeatStatus.value == REPEAT_ONE
            mCurrentTrack.postValue(track)
            mIsFavorite.postValue(track.favorite)
            mTrackDuration.postValue(mMediaPlayer.duration)
            initializeProgress()
            mIsMusicPlaying.postValue(mMediaPlayer.isPlaying)
        }
    }

    private fun initializeProgress() {
        mRunnable = Runnable {
            mPlayerCurrentPosition.postValue(mMediaPlayer.currentPosition)
            mHandler.postDelayed(mRunnable, 1000)
        }
        mHandler.postDelayed(mRunnable, 1000)
    }

    private fun configMediaPlayer(mediaPlayer: MediaPlayer) {
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            setOnCompletionListener {
                onMusicComplete()
            }
        }
    }

    fun releaseMusicPlayer() {
        mHandler.removeCallbacksAndMessages(null)
        mMediaPlayer.release()
        isMediaPlayerReleased = true
    }

    override fun onProgressChanged(seekBar: SeekBar?, position: Int, p2: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBar != null) {
            mMediaPlayer.seekTo(seekBar.progress)
        }
        mHandler.postDelayed(mRunnable, 1000)
    }

    fun onDestroy() {
        releaseMusicPlayer()
    }
}