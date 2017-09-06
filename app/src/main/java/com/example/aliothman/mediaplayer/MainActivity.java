package com.example.aliothman.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.WeakHashMap;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar ;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    int seekvalue;
    ListView  listView;

    ArrayList<SongInfo> songInfos = new ArrayList<SongInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                 seekvalue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               mediaPlayer.seekTo(seekvalue);
            }
        });
//        songAdapter = new SongAdapter(getSongInfos(),MainActivity.this);
        listView = (ListView) findViewById(R.id.Listsong);
        CheckUserPermsions();
        //listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SongInfo songInfo = songInfos.get(position);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(songInfo.Path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mythread mythread = new mythread();
        mythread.start();

    }

    public  ArrayList<SongInfo> getSongInfos(){
        songInfos.clear();

        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC +" !=0";
        Cursor cursor = getContentResolver().query(allsongsuri,null,selection,null,null);

        if(cursor != null){
            if (cursor.moveToFirst()){

                do{
                    String songname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    songInfos.add(new SongInfo(songname,fullpath,album_name,artist_name));

                }while (cursor.moveToNext());
            }
            cursor.close();
        }

        return songInfos;
    }



//    public  ArrayList<SongInfo>  getSongInfos(){
//        songInfos.clear();
//        songInfos.add(new SongInfo("http://server6.mp3quran.net/thubti/001.mp3","Al-Fatihah","Mosahaf1","Agami"));
//        songInfos.add(new SongInfo("http://server6.mp3quran.net/thubti/002.mp3","Al-A'raf","Mosahaf2","shatri"));
//        songInfos.add(new SongInfo("http://server6.mp3quran.net/thubti/003.mp3","Ar-Ra'd","Mosahaf3","Mohamed Saleh"));
//        songInfos.add(new SongInfo("http://server6.mp3quran.net/thubti/004.mp3","An-Nahl","Mosahaf4","Al3arefy"));
//        songInfos.add(new SongInfo("http://server6.mp3quran.net/thubti/005.mp3","Al-Mu'minun","Mosahf5","Shankity"));
//
//       return songInfos;
//    }

    public void playmp(View view) {
        mediaPlayer.start();
    }

    public void stopmp(View view) {
        mediaPlayer.stop();
    }

    public void pausemp(View view) {
        mediaPlayer.pause();
    }



    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

      Loadsong();
    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                      Loadsong();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"you deny " , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void Loadsong(){
        songAdapter = new SongAdapter(getSongInfos(),MainActivity.this);
        listView.setAdapter(songAdapter);
    }


    private  class mythread extends  Thread{

        @Override
        public void run() {
            super.run();

            while (true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if(mediaPlayer !=null)
                       seekBar.setProgress(mediaPlayer.getCurrentPosition());

                   }
               });

            }

        }
    }

}
