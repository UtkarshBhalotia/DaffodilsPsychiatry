package com.daffodils.psychiatry.activity;

import android.content.Intent;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class ExoPlayerActivity extends AppCompatActivity {

    PlayerView exoPlayerView;
    ExoPlayer exoPlayer;
    ProgressBar progressBar;
    String videoURL = "";
    private TextView playbackSpeed;
    float playBackSpeed = 1f;
    PlaybackParams param = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lyt_exoplayer);
        exoPlayerView = findViewById(R.id.idExoPlayerVIew);
        progressBar = new ProgressBar(this);
        playbackSpeed = findViewById(R.id.playbackSpeed);

        playbackSpeed.setText("Change Speed (1x)");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        videoURL = i.getStringExtra("VideoURL");

        if (GlobalConst.OFFLINE_MODE.equals("True")) {
            initializePlayer();
        } else {
               try {

            playbackSpeed.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Please wait while the video is Buffering.", Toast.LENGTH_LONG).show();
            exoPlayer = new ExoPlayer.Builder(this).build();
            Uri videouri = Uri.parse(videoURL);
            MediaItem mediaItem = MediaItem.fromUri(videouri);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.prepare();
            exoPlayerView.setPlayer(exoPlayer);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                param = new PlaybackParams();
            }
            //exoPlayer.setPlayWhenReady(true);

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e("TAG", "Error : " + e.toString());
        }

        playbackSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerSpeedDialog();
            }
        });


        exoPlayer.addListener(new Player.Listener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                progressBar.setVisibility(View.VISIBLE);
                exoPlayer.stop();
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
        }

    }

    private void initializePlayer() {
        playbackSpeed.setVisibility(View.GONE);
        String filePath = videoURL;
        Uri uri = Uri.parse(filePath);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "MyExoPlayer");
        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        exoPlayerView.setPlayer(exoPlayer);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            param = new PlaybackParams();
        }
        exoPlayer.setPlayWhenReady(true);
    }

    private void showPlayerSpeedDialog() {
        String[] playerSpeedArrayLabels = {"0.8x", "1.0x", "1.2x", "1.5x", "1.8x", "2.0x"};

        PopupMenu popupMenu = new PopupMenu(ExoPlayerActivity.this, playbackSpeed);
        for (int i = 0; i < playerSpeedArrayLabels.length; i++) {
            popupMenu.getMenu().add(i, i, i, playerSpeedArrayLabels[i]);
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            CharSequence itemTitle = item.getTitle();
            playBackSpeed = Float.parseFloat(itemTitle.subSequence(0, 3).toString() +"f");

            playbackSpeed.setText("Change Speed (" + itemTitle + ")" );

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                param.setSpeed(playBackSpeed);// 1f is 1x, 2f is 2x
//            }
            exoPlayer.setPlaybackSpeed(playBackSpeed);

            return false;
        });
        popupMenu.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.stop();
    }
}
