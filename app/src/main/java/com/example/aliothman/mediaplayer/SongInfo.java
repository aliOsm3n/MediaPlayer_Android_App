package com.example.aliothman.mediaplayer;

/**
 * Created by AliOthman on 8/31/2017.
 */

public class SongInfo {

    public  String Path;
    public  String Song_name;
    public  String Album_name;
    public  String artist_name;

    public SongInfo(String path, String song_name, String album_name, String artist_name) {
        Path = path;
        Song_name = song_name;
        Album_name = album_name;
        this.artist_name = artist_name;
    }
}
