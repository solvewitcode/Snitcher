package com.example.snitcher;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class myviewholder extends RecyclerView.ViewHolder {
    StyledPlayerView styledPlayerView;
    ExoPlayer player;
    TextView vtitleview;
    TextView watchinvr;
    PlayerView playerView;
    private Activity activity;

    private ImageView fullScreenButton;
    boolean isFullScreen = false;

    public myviewholder(@NonNull View itemView , Activity activity) {
        super(itemView);
        this.activity = activity;
        styledPlayerView=itemView.findViewById(R.id.exoplayerview);
        vtitleview=itemView.findViewById(R.id.vtitle);
        watchinvr=itemView.findViewById(R.id.watchinvr);
        fullScreenButton=itemView.findViewById(R.id.fullScreenButton);

        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFullScreen();
            }
        });
    }
    private void toggleFullScreen() {
            if (isFullScreen) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                // Adjust layout params as needed
                fullScreenButton.setImageResource(R.drawable.ic_fullscreen);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                fullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit);
            }
            isFullScreen = !isFullScreen;
        }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    void prepareexoplayer(Application application, String videotitle, String videourl)
    {
        player= new ExoPlayer.Builder(application.getApplicationContext()).build();

        try{
            vtitleview.setText(videotitle);
            styledPlayerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(videourl);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(false);


        }catch (Exception e)
        {
            Log.d("Exoplayer Crashed",e.getMessage().toString());
        }

    }

}
