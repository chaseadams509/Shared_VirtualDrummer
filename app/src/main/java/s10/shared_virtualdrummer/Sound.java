package s10.shared_virtualdrummer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Sound extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    Intent get_intent = getIntent();
    final boolean language = get_intent.getBooleanExtra("lang", true);
    if (language) {
        setContentView(R.layout.sound);
    }else{
        setContentView(R.layout.sound_j);
    }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SoundPlayer drumPlayer = new SoundPlayer(this);

        Button playbass = (Button) this.findViewById(R.id.play_bass);
        Button playHiHat = (Button) this.findViewById(R.id.play_hi_hat);
        Button playride = (Button)  this.findViewById(R.id.play_ride);
        Button playsnare = (Button) this.findViewById(R.id.play_snare);
        Button playtom1 = (Button) this.findViewById(R.id.play_tom1);
        Button playtom2 = (Button) this.findViewById(R.id.play_tom2);
        Button playtom3 = (Button) this.findViewById(R.id.play_tom3);
        Button playrim = (Button) this.findViewById(R.id.play_rim);
        Button playcenter = (Button) this.findViewById(R.id.play_center);

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
        playrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playRim();
            }
        });
        playcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumPlayer.playCenter();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);
        if (language) {
            getMenuInflater().inflate(R.menu.menu_hw_activity, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_hw_activity_j, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //Sound add item selected for menu switching.
        int id = item.getItemId();
        Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);
        final boolean drum = get_intent.getBooleanExtra("drum", true);
        final boolean hand = get_intent.getBooleanExtra("hand", true);
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play) {
            Intent intent = new Intent(Sound.this, Sound.class);
            startActivity(intent);
            return true;
        }
        */
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Sound.this, Settings.class);
            intent.putExtra("lang", language);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            finish();
            startActivity(intent);

        }
        if (id == R.id.action_blue_tooth) {

            Intent intent = new Intent(Sound.this, hw_activity.class);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}