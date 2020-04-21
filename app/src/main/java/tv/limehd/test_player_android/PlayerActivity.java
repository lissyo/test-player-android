package tv.limehd.test_player_android;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistParserFactory;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TrackNameProvider;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import static tv.limehd.test_player_android.MainActivity.PLAYER_URL;

public class PlayerActivity extends Activity {

    private String playerUrl;

    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private DefaultDataSourceFactory mediaDataSourceFactory;
    private DefaultBandwidthMeter bandwidthMeter;
    private TrackGroupArray lastSeenTrackGroupArray;
    private TrackNameProvider trackNameProvider;
    private PlayerView mPlayerView;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerUrl = Objects.requireNonNull(getIntent().getExtras()).getString(PLAYER_URL);
        mPlayerView = findViewById(R.id.player_view);
        setUpVideo();
    }

    private void setUpVideo() {
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getApplication().getPackageName()), bandwidthMeter);
    }

    private void initializePlayer(){
        lastSeenTrackGroupArray = null;
        mPlayerView.requestFocus();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        trackSelector.setParameters(trackSelectorParameters);
        LoadControl loadControl = new DefaultLoadControl();
        DefaultRenderersFactory renderersFactory =
                new DefaultRenderersFactory(this);
        player = ExoPlayerFactory.newSimpleInstance(this, renderersFactory, trackSelector, loadControl);
        player.setPlayWhenReady(true);
        player.addAnalyticsListener(new EventLogger(trackSelector));
        //TODO nullpointer
        MediaSource videoSource = new HlsMediaSource.Factory(mediaDataSourceFactory)
                .setPlaylistParserFactory(
                        new DefaultHlsPlaylistParserFactory())
                .createMediaSource(Uri.parse(playerUrl));
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                if (trackGroups != lastSeenTrackGroupArray) {
                        /*MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                        if (mappedTrackInfo != null) {
                            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                                //Log.e("logs", "error_unsupported_video");
                            }
                            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                                //Log.e("logs", "error_unsupported_audio");
                            }
                        }*/
                    lastSeenTrackGroupArray = trackGroups;
                }
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    releasePlayer();
                    initializePlayer();
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                initializePlayer();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        player.prepare(videoSource);
        mPlayerView.setUseController(true);
        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
