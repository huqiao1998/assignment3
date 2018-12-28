package com.example.a93584.exercise;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class LocalVideoActivity extends AppCompatActivity {
    private VideoView vv_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);

        vv_video = (VideoView) findViewById(R.id.videoView);

        //给VideoView添加视频来源
        vv_video.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        //实例化媒体控制器
        MediaController  mediaController=new MediaController(this);
        mediaController.setMediaPlayer(vv_video);
        vv_video.setMediaController(mediaController);

    }


}