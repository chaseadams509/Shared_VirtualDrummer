package s10.shared_virtualdrummer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import java.util.Set;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);
            //volume
            /*
            AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            SeekBar volControl = (SeekBar)findViewById(R.id.volume_home);
            volControl.setMax(maxVolume);
            volControl.setProgress(curVolume);
            volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
                }
            });
            */
            //home menu
            //Button set_h = (Button) this.findViewById(R.id.settings_home);
            //Button drumop_h = (Button) this.findViewById(R.id.drum_options_home);
            //drum options
            Button back_do = (Button) this.findViewById(R.id.back_drum_options);
            Button set_do = (Button) this.findViewById(R.id.settings_drum_options);
            //settings menu
            //Button back_s = (Button) this.findViewById(R.id.back_settings);
/*
            set_h.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                viewFlipper.setDisplayedChild(2);
            }
            }
            );
            drumop_h.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                viewFlipper.setDisplayedChild(1);
            }
            }

            );

            */
            back_do.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                viewFlipper.setDisplayedChild(0);
            }
            }

            );
            set_do.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                viewFlipper.setDisplayedChild(2);
            }
            }

            );
/*
            back_s.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                viewFlipper.setDisplayedChild(0);
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hw_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //Sound add item selected for menu switching.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play) {
            Intent intent = new Intent(Settings.this, Sound.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            /*
            Intent intent = new Intent(Settings.this, Settings.class);
            startActivity(intent);
            return true;
            */
        }
        if (id == R.id.action_blue_tooth) {
            Intent intent = new Intent(Settings.this, hw_activity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}