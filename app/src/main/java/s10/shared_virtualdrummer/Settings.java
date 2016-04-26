package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
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
        final Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);
        final boolean drumset = get_intent.getBooleanExtra("drum", true);
        final boolean handed = get_intent.getBooleanExtra("hand", true);
        final boolean debug = get_intent.getBooleanExtra("debug", false);
        final BluetoothDevice d1 = get_intent.getParcelableExtra("dev1");
        final BluetoothDevice d2 = get_intent.getParcelableExtra("dev2");

        if (language) {
            setContentView(R.layout.settings);
        }else{
            setContentView(R.layout.settings_j);
        }
        Button taiko_set = (Button) this.findViewById(R.id.Taiko);
        Button rock_set = (Button) this.findViewById(R.id.Rock_Kit);
        if (!drumset){
            taiko_set.setBackgroundColor(Color.parseColor("#0619bf"));
            taiko_set.setTextColor(Color.WHITE);
            rock_set.setBackgroundColor(Color.LTGRAY);
            rock_set.setTextColor(Color.BLACK);
        }
        Button left_set = (Button) this.findViewById(R.id.left_handed);
        Button right_set = (Button) this.findViewById(R.id.right_handed);
        if (!handed){
            left_set.setBackgroundColor(Color.parseColor("#0619bf"));
            left_set.setTextColor(Color.WHITE);
            right_set.setBackgroundColor(Color.LTGRAY);
            right_set.setTextColor(Color.BLACK);
        }
        Button debug_set = (Button) this.findViewById(R.id.set_debug);
        Button release_set = (Button) this.findViewById(R.id.set_release);
        if (debug){
            debug_set.setBackgroundColor(Color.parseColor("#0619bf"));
            debug_set.setTextColor(Color.WHITE);
            release_set.setBackgroundColor(Color.LTGRAY);
            release_set.setTextColor(Color.BLACK);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton lang_switch = (ImageButton) this.findViewById(R.id.lang_set);
        lang_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Settings.class);
                intent.putExtra("lang", !language);
                intent.putExtra("drum", drumset);
                intent.putExtra("hand", handed);
                intent.putExtra("debug", debug);
                intent.putExtra("dev1", d1);
                intent.putExtra("dev2", d2);
                finish();
                startActivity(intent);
            }
        });
        taiko_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drumset) {
                    Intent intent = new Intent(Settings.this, Settings.class);
                    intent.putExtra("drum", false);
                    intent.putExtra("lang", language);
                    intent.putExtra("hand", handed);
                    intent.putExtra("debug", debug);
                    intent.putExtra("dev1", d1);
                    intent.putExtra("dev2", d2);
                    finish();
                    startActivity(intent);
                }
            }
        });
        rock_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drumset) {
                    Intent intent = new Intent(Settings.this, Settings.class);
                    intent.putExtra("drum", true);
                    intent.putExtra("lang", language);
                    intent.putExtra("hand", handed);
                    intent.putExtra("debug", debug);
                    intent.putExtra("dev1", d1);
                    intent.putExtra("dev2", d2);
                    finish();
                    startActivity(intent);
                }
            }
        });
        left_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handed) {
                    Intent intent = new Intent(Settings.this, Settings.class);
                    intent.putExtra("hand", false);
                    intent.putExtra("lang", language);
                    intent.putExtra("drum", drumset);
                    intent.putExtra("debug", debug);
                    intent.putExtra("dev1", d1);
                    intent.putExtra("dev2", d2);
                    finish();
                    startActivity(intent);
                }
            }
        });
        right_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!handed) {
                    Intent intent = new Intent(Settings.this, Settings.class);
                    intent.putExtra("hand", true);
                    intent.putExtra("lang", language);
                    intent.putExtra("drum", drumset);
                    intent.putExtra("debug", debug);
                    intent.putExtra("dev1", d1);
                    intent.putExtra("dev2", d2);
                    finish();
                    startActivity(intent);
                }
            }
        });
        debug_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!debug) {
                    Intent intent = new Intent(Settings.this, Settings.class);
                    intent.putExtra("hand", handed);
                    intent.putExtra("lang", language);
                    intent.putExtra("drum", drumset);
                    intent.putExtra("debug", true);
                    intent.putExtra("dev1", d1);
                    intent.putExtra("dev2", d2);
                    finish();
                    startActivity(intent);
                }
            }
        });
        release_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug) {
                    Intent intent = new Intent(Settings.this, Settings.class);
                    intent.putExtra("hand", handed);
                    intent.putExtra("lang", language);
                    intent.putExtra("drum", drumset);
                    intent.putExtra("debug", false);
                    intent.putExtra("dev1", d1);
                    intent.putExtra("dev2", d2);
                    finish();
                    startActivity(intent);
                }
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
        final boolean debug = get_intent.getBooleanExtra("debug", false);
        final BluetoothDevice dev1 = get_intent.getExtras().getParcelable("dev1");
        final BluetoothDevice dev2 = get_intent.getExtras().getParcelable("dev2");

        if (id == R.id.action_blue_tooth) {
            Intent intent = new Intent(Settings.this, Bluetooth.class);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("debug", debug);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
            finish();
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_credits) {
            Intent intent = new Intent(Settings.this, Credits.class);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("debug", debug);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}