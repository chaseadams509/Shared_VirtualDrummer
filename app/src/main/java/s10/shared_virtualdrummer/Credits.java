package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Credits  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent get_intent = getIntent();
        TextView creditsText;
        boolean language = get_intent.getBooleanExtra("lang", true);
        setContentView(R.layout.credits);

        creditsText = (TextView)findViewById(R.id.credits);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(language) {
            creditsText.setText(R.string.credits_text);
        } else {
            creditsText.setText(R.string.credits_text_j);
        }
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
        int id = item.getItemId();
        Intent get_intent = getIntent();
        final boolean lang = get_intent.getBooleanExtra("lang", true);
        final boolean drum = get_intent.getBooleanExtra("drum", true);
        final boolean hand = get_intent.getBooleanExtra("hand", true);
        final BluetoothDevice dev1 = get_intent.getParcelableExtra("dev1");
        final BluetoothDevice dev2 = get_intent.getParcelableExtra("dev2");


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Credits.this, Settings.class);
            intent.putExtra("lang", lang);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_blue_tooth) {
            Intent intent = new Intent(Credits.this, Bluetooth.class);
            intent.putExtra("lang", lang);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
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