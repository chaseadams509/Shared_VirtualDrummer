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
        final SoundPlayer drumPlayer = new SoundPlayer(this);

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
                drumPlayer.playBass();
            }
        });
        playHiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playHiHat();
            }
        });
        playride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playRide();
            }
        });
        playsnare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playSnare();
            }
        });
        playtom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playTom1();
            }
        });
        playtom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playTom2();
            }
        });
        playtom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playTom3();
            }
        });

    }
}