package s10.shared_virtualdrummer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Sound extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sound);

    final MediaPlayer bass = MediaPlayer.create(this, R.raw.bass);
    final MediaPlayer hiHat = MediaPlayer.create(this, R.raw.hi_hat);
    final MediaPlayer ride = MediaPlayer.create(this, R.raw.ride);
    final MediaPlayer snare = MediaPlayer.create(this, R.raw.snare);
    final MediaPlayer tom1 = MediaPlayer.create(this, R.raw.tom1);
    final MediaPlayer tom2 = MediaPlayer.create(this, R.raw.tom2);
    final MediaPlayer tom3 = MediaPlayer.create(this, R.raw.tom3);

        Button playbass = (Button) this.findViewById(R.id.play_bass);
        Button playHiHat = (Button) this.findViewById(R.id.play_hi_hat);
        Button playride = (Button)  this.findViewById(R.id.play_ride);
        Button playsnare = (Button) this.findViewById(R.id.play_snare);
        Button playtom1 = (Button) this.findViewById(R.id.play_tom1);
        Button playtom2 = (Button) this.findViewById(R.id.play_tom2);
        Button playtom3 = (Button) this.findViewById(R.id.play_tom3);

        playbass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bass.isPlaying()){
                    bass.seekTo(0);
                }
                bass.start();
            }
        });
        playHiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiHat.isPlaying()){
                    hiHat.seekTo(0);
                }
                else {
                    hiHat.start();
                }
            }
        });
        playride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ride.isPlaying()){
                    ride.seekTo(0);
                }
                ride.start();
            }
        });
        playsnare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snare.isPlaying()){
                    snare.seekTo(0);
                }
                snare.start();
            }
        });
        playtom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tom1.isPlaying()){
                    tom1.seekTo(0);
                }
                tom1.start();
            }
        });
        playtom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tom2.isPlaying()){
                    tom2.seekTo(0);
                }
                tom2.start();
            }
        });
        playtom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tom3.isPlaying()){
                tom3.seekTo(0);
            }
                tom3.start();
            }
        });

    }
}