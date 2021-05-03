package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620
public class Reproductor extends AppCompatActivity {
    VideoView Video;
    String URLTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        Video  = (VideoView) findViewById(R.id.vdoVideo);
        MediaController mediaController = new MediaController(this);
        Video.setMediaController(mediaController);
        Video.setVideoPath(URLTrailer);
        Video.start();


        mediaController.setAnchorView(Video);
    }
}