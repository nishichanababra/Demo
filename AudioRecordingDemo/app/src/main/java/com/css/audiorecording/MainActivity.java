package com.css.audiorecording;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


/************************************************************************************
 *       Audio recording demo created by Purva on 29 Feb
 *      Functionality : Records audio on button click
 /************************************************************************************/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStartrecording,btnStoprecording,btnPlayrecording,btnSendrecording;
    MediaRecorder mediaRecorder;
    String outputFile = null;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjects();
        initUicomponents();
        registerForlistner();
    }

    /**
     *
     * @param filename
     * @param extention
     * @return
     */
    private String makeFile(String filename, String extention) {
        file = new File(Environment.getExternalStorageDirectory() + "/"+filename+"/");
        if(!file.exists())
            file.mkdirs();
        else
            Log.d("error", "dir. already exists");
        outputFile = file+"/"+extention;
        Log.e("Audio demo ", "outputFile :: " + outputFile);

        return outputFile;
    }

    private void initObjects() {
        mediaRecorder=new MediaRecorder();
    }

    private void initUicomponents() {
        btnStartrecording=(Button)findViewById(R.id.btnStartrecording);
        btnStoprecording=(Button)findViewById(R.id.btnStoprecording);
        btnPlayrecording=(Button)findViewById(R.id.btnPlayrecording);
        btnSendrecording=(Button)findViewById(R.id.btnSendrecording);
    }
    private void registerForlistner() {
        btnStartrecording.setOnClickListener(this);
        btnStoprecording.setOnClickListener(this);
        btnPlayrecording.setOnClickListener(this);
        btnSendrecording.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartrecording:

               startRecording();
               Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnStoprecording:
                stopRecording();


                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
                break;

            case R.id.btnPlayrecording:
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                break;
        }


    }

    private void stopRecording() {
        if (null != mediaRecorder) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void startRecording() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(makeFile("Purva_new", "Purva.mp3"));
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
/********************************************************************************
 *  Class used ::   MediaRecorder records audio and video
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 <uses-permission android:name="android.permission.RECORD_AUDIO" />

 *********************************************************************************/