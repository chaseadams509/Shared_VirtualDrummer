package s10.shared_virtualdrummer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Set;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);
        if (language) {
            setContentView(R.layout.settings);
        }else{
            setContentView(R.layout.settings_j);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton lang_switch = (ImageButton) this.findViewById(R.id.lang_set);
        lang_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Settings.class);
                intent.putExtra("lang", !language);
                startActivity(intent);
                return;
            }
        });
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
        Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play) {
            Intent intent = new Intent(Settings.this, Sound.class);
            intent.putExtra("lang", language);
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
            intent.putExtra("lang", language);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}