package com.example.sergio.videoplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;


public class MainActivity extends ActionBarActivity {
    private VideoView vv;
    private int stopPosition;
    private ProgressBar pb;
    private boolean play = false;
    private boolean pausado = false;
    private ImageView pausaImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaController mc =new MediaController(this);
        mc.setAnchorView(vv);
        //pausaImg = (ImageView)findViewById(R.id.imageViewPausa);
        //pausaImg.setVisibility(View.GONE);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setProgress(0);
        pb.setMax(100);
        vv = (VideoView) findViewById(R.id.videoView);
        vv.setMediaController(mc);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
            mediaChooser.setType("video/*");
            startActivityForResult(mediaChooser, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            stopPosition = 0;
            Uri selectedVideoLocation = data.getData();

            vv.setVideoURI(data.getData());
            pausado = false;
            play = true;
            HiloVideo hv = new HiloVideo();
            hv.execute();
        }
    }
    @Override
    public void onPause() {
        Log.d("A", "onPause called");
        super.onPause();
        stopPosition = vv.getCurrentPosition(); //stopPosition
        vv.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("B", "onResume called");
        vv.seekTo(stopPosition);
        vv.start(); //O usar resume()
    }
    private class HiloVideo extends AsyncTask<Void,Intent,Void>{
        int duration = 0;
        int current = 0;
        @Override
        protected Void doInBackground(Void... params) {
            play = true;
            vv.start();

            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    duration = vv.getDuration();
                }
            });
            do {
                current = vv.getCurrentPosition();

                try {
                   pb.setProgress((int) (current * 100 / duration));
                    if(pb.getProgress() >= 100){
                        play = false;
                        break;
                    }
                } catch (Exception e) {

                }
            } while (pb.getProgress() <= 100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Intent... values) {
            super.onProgressUpdate(values);
            super.onProgressUpdate(values);
            System.out.println(values[0]);
            pb.setProgress(Integer.valueOf(values[0].toString()));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* vv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(play){
                    if(!pausado){
                        vv.pause();
                        pausado = true;
                        pausaImg.setVisibility(View.VISIBLE);
                    }else if(pausado){
                        vv.start();
                        pausado = false;
                        pausaImg.setVisibility(View.GONE);
                    }
                }
            return true;
            }*
        });*/
    }
}
