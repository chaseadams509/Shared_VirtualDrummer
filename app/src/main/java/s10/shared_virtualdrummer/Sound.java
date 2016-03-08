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
        Button playsnare = (Button) this.findViewById(R.id.play_snare);
        Button playtom1 = (Button) this.findViewById(R.id.play_tom1);
        Button playtom2 = (Button) this.findViewById(R.id.play_tom2);
        Button playtom3 = (Button) this.findViewById(R.id.play_tom3);

        playbass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bass.start();
            }
        });
        playHiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiHat.start();
            }
        });
        playsnare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snare.start();
            }
        });
        playtom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tom1.start();
            }
        });
        playtom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tom2.start();
            }
        });
        playtom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tom3.start();
            }
        });

    }
}