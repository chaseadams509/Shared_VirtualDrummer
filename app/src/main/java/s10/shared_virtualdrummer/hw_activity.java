package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Set;


public class hw_activity extends AppCompatActivity {
    private Button onBtn;
    private Button offBtn;
    private Button listBtn;
    private TextView statusText;
    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> pairedDevicesArray;
    private BluetoothAdapter myBluetoothAdapter;
    private BluetoothDevice dev1;
    private BluetoothDevice dev2;
    private boolean kitType = true;
    private boolean rightHand = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);
        if (language) {
            setContentView(R.layout.activity_hw_activity);
        }else{
            setContentView(R.layout.activity_hw_activity_j);
        }
        kitType = get_intent.getBooleanExtra("drum", true);
        rightHand = get_intent.getBooleanExtra("hand", true);
        //drumPlayer = new SoundPlayer(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);
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
            myListView = (ListView)findViewById(R.id.listView1);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> par, View v, int pos,
                                        long id) {
                    connect_dv(par, v, pos, id);
                }
            });

            //create the arrayAdapter that contains the BTDevices
            BTArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1);
            myListView.setAdapter(BTArrayAdapter);
            statusText.setText("Status: Enabled D:" + kitType + " H:" + rightHand);

            if(!myBluetoothAdapter.isEnabled()) {
                listBtn.setEnabled(false);
                statusText.setText("Status: Disconnected");
            }

            pairedDevicesArray = new ArrayList<BluetoothDevice>();
        }
    }

    public void turn_bt_on(View view){
        if(!myBluetoothAdapter.isEnabled()){
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, StaticVars.REQUEST_ENABLE_BT);
        }
        listBtn.setEnabled(true);
    }

    public void turn_bt_off(View view) {
        myBluetoothAdapter.disable();
        listBtn.setEnabled(false);
        BTArrayAdapter.clear();
       // destroy_connections();
        statusText.setText("Status: Disconnected");
    }

    public void list_bt(View view) {
        pairedDevices = myBluetoothAdapter.getBondedDevices();
        BTArrayAdapter.clear();
        for (BluetoothDevice device : pairedDevices) {
            BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            pairedDevicesArray.add(device);
        }
    }


    public void connect_dv(AdapterView<?> par, View v, int pos, long id) {
        myBluetoothAdapter.cancelDiscovery();
        String mDeviceInfo = ((TextView) v).getText().toString();
        //String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
        int name_end = mDeviceInfo.indexOf("\n");
        statusText.setText("Status: connect to " + mDeviceInfo.substring(0, name_end));

        BluetoothDevice selectedDevice = pairedDevicesArray.get(pos);
        if (dev1 == null){
            dev1 = selectedDevice;
            statusText.setText("set dev1 as " + mDeviceInfo.substring(0, name_end));
        }
        else {
            dev2 = selectedDevice;
            statusText.setText("set dev2 as " + mDeviceInfo.substring(0, name_end));
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Check which request we're responding to
        if(requestCode == StaticVars.REQUEST_ENABLE_BT) {
            // Make sure the request was successful
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


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(hw_activity.this, Settings.class);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_blue_tooth) {
            Intent intent = new Intent(hw_activity.this, Bluetooth.class);
            intent.putExtra("lang", language);
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
        unregisterReceiver(bReceiver);
    }

}


