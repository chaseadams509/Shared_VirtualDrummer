package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Set;


public class hw_activity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "s10.shared_virtualdrummer";

    private final static int REQUEST_ENABLE_BT = 1;
    private Button onBtn;
    private Button offBtn;
    private Button listBtn;
    //private Button findBtn;
    //private Button cancelBtn;
    private TextView statusText;
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button soundMenu = (Button) findViewById(R.id.SoundMenu);
        soundMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(hw_activity.this, Sound.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);
            //findBtn.setEnabled(false);
            //cancelBtn.setEnabled(false);
            statusText.setText("Status: not supported");
        } else {
            statusText = (TextView)findViewById(R.id.status_text);

            onBtn = (Button)findViewById(R.id.turnOn);
            onBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turn_bt_on(v);
                }
            });

            offBtn = (Button)findViewById(R.id.turnOff);
            offBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turn_bt_off(v);
                }
            });

            listBtn = (Button)findViewById(R.id.paired);
            listBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_bt(v);
                }
            });
            /*
            findBtn = (Button)findViewById(R.id.search);
            findBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    find_bt(v);
                }
            });
            cancelBtn = (Button)findViewById(R.id.cancel_search);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel_finding_bt(v);
                }
            });
            */
            myListView = (ListView)findViewById(R.id.listView1);
            //create the arrayAdapter that contains the BTDevices
            BTArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1);
            myListView.setAdapter(BTArrayAdapter);

        }
        if(!myBluetoothAdapter.isEnabled()) {
            listBtn.setEnabled(false);
            //findBtn.setEnabled(false);
            //cancelBtn.setEnabled(false);
        }
        /*
        EditText editText = (EditText)findViewById(R.id.edit_message);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Hardware Bluetooth available check
        if(mBluetoothAdapter == null) {
            editText.setText("No Bluetooth Available!", TextView.BufferType.EDITABLE);
            return;
        }
        editText.setText("Bluetooth Available!", TextView.BufferType.EDITABLE);

        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        */
    }

    public void turn_bt_on(View view){
        if(!myBluetoothAdapter.isEnabled()){
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        }
        listBtn.setEnabled(true);
        //findBtn.setEnabled(true);
        //cancelBtn.setEnabled(true);
    }

    public void turn_bt_off(View view) {
        myBluetoothAdapter.disable();
        listBtn.setEnabled(false);
        //findBtn.setEnabled(false);
        //cancelBtn.setEnabled(false);
        statusText.setText("Status: Disconnected");
    }

    public void list_bt(View view) {
        pairedDevices = myBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices)
            BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
    }

    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discvoery finds a device
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };
/*
    public void find_bt(View view) {
        if(!myBluetoothAdapter.isDiscovering()) {
            BTArrayAdapter.clear();
            myBluetoothAdapter.startDiscovery();
            statusText.setText("Status: Discovering");
            registerReceiver(bReceiver,
                    new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    public void cancel_finding_bt(View view) {
        if(myBluetoothAdapter.isDiscovering()) {
            myBluetoothAdapter.cancelDiscovery();
        }
        statusText.setText("Status: Enabled");
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Check which request we're responding to
        if(requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            //if(resultCode == RESULT_OK){
            if(myBluetoothAdapter.isEnabled()) {
                statusText.setText("Status: Enabled");
            } else {
                statusText.setText("Status: Disabled");
            }
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        //Do something in response to button
        Intent intent = new Intent(this, hw_Message.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }

}
