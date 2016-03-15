package s10.shared_virtualdrummer;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by cadams on 3/15/16.
 */
public class SoundPlayer {
    private MediaPlayer bass;
    private MediaPlayer hiHat;
    private MediaPlayer ride;
    private MediaPlayer snare;
    private MediaPlayer tom1;
    private MediaPlayer tom2;
    private MediaPlayer tom3;

    public SoundPlayer(Context con) {
        bass = MediaPlayer.create(con, R.raw.bass);
        hiHat = MediaPlayer.create(con, R.raw.hi_hat);
        ride = MediaPlayer.create(con, R.raw.ride);
        snare = MediaPlayer.create(con, R.raw.snare);
        tom1 = MediaPlayer.create(con, R.raw.tom1);
        tom2 = MediaPlayer.create(con, R.raw.tom2);
        tom3 = MediaPlayer.create(con, R.raw.tom3);
    }

    public void playBass(){
        if (bass.isPlaying()){
            bass.seekTo(0);
        }
        bass.start();
    }

    public void playHiHat(){
        if (hiHat.isPlaying()){
            hiHat.seekTo(0);
        }
        hiHat.start();
    }

    public void playRide(){
        if (ride.isPlaying()){
            ride.seekTo(0);
        }
        ride.start();
    }

    public void playSnare(){
        if (snare.isPlaying()){
            snare.seekTo(0);
        }
        snare.start();
    }

    public void playTom1(){
        if (tom1.isPlaying()){
            tom1.seekTo(0);
        }
        tom1.start();
    }

    public void playTom2(){
        if (tom2.isPlaying()){
            tom2.seekTo(0);
        }
        tom2.start();
    }

    public void playTom3(){
        if (tom3.isPlaying()){
            tom3.seekTo(0);
        }
        tom3.start();
    }
}
