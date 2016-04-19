package s10.shared_virtualdrummer;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    private MediaPlayer bass;
    private MediaPlayer hiHat;
    private MediaPlayer ride;
    private MediaPlayer snare;
    private MediaPlayer tom1;
    private MediaPlayer tom2;
    private MediaPlayer tom3;
    private MediaPlayer rim;
    private MediaPlayer center;

    public SoundPlayer(Context con) {
        bass = MediaPlayer.create(con, R.raw.bass);
        hiHat = MediaPlayer.create(con, R.raw.hi_hat);
        ride = MediaPlayer.create(con, R.raw.ride);
        snare = MediaPlayer.create(con, R.raw.snare);
        tom1 = MediaPlayer.create(con, R.raw.tom1);
        tom2 = MediaPlayer.create(con, R.raw.tom2);
        tom3 = MediaPlayer.create(con, R.raw.tom3);
        rim  = MediaPlayer.create(con, R.raw.rim);
        center = MediaPlayer.create(con, R.raw.center);
    }

    public void destroy() {
        bass.stop();
        hiHat.stop();
        ride.stop();
        snare.stop();
        tom1.stop();
        tom2.stop();
        tom3.stop();
        rim.stop();
        center.stop();

        bass.release();
        hiHat.release();
        ride.release();
        snare.release();
        tom1.release();
        tom2.release();
        tom3.release();
        rim.release();
        center.release();
    }

   /*
   public void playBass(){
        if (bass.isPlaying()){
            bass.seekTo(0);
        }
        bass.start();
    }
    */

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
    public void playRim(){
        if (rim.isPlaying()){
            rim.seekTo(0);
        }
        rim.start();
    }
    public void playCenter(){
        if (center.isPlaying()){
            center.seekTo(0);
        }
        center.start();
    }
}
