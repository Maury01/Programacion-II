package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class Reproductor extends AppCompatActivity {
    VideoView Video;
    String URLTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        URLTrailer = "/storage/emulated/0/xd/c mamo.mp4";
        Video  = (VideoView) findViewById(R.id.vdoVideo);
        ObtenerURLVideo();
    }

    public void ObtenerURLVideo(){
        Video.setVideoURI(Uri.parse(URLTrailer));
        MediaController mediaController = new MediaController(this);
        Video.setMediaController(mediaController);
        mediaController.setAnchorView(Video);
    }
}