package pjatk.s10552.multimediawidget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundService {

    MediaPlayer mediaPlayer = null;
    Context context;
    private int songId = 0;

    private final int[] resID = { R.raw.aem__pharaoh_ramses_ii, R.raw.aem__nenchefkas_orchestra };

    public SoundService(Context context) {
        this.context = context;
        mediaPlayer = MediaPlayer.create(context , resID[0]);
        mediaPlayer.setVolume(100,100);
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void playSong(int songIndex) {
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(context , resID[songIndex]);
        mediaPlayer.start();
    }

    public void playSounds(){
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d("yesla", "playSounds: lalalallalalalalalallalala~");
        }
        else {
            mediaPlayer.pause();
            Log.d("nola", "playSounds: __________________________");
        }  //+change text label to @string/widget_sounds_stop
    }
    public void nextTrack(){
        songId ++;
        if(songId == resID.length) songId = 0;
        playSong(songId);
    }

    public void prevTrack(){
        songId --;
        if(songId == -1) songId = resID.length-1;
        playSong(songId);
    }

}