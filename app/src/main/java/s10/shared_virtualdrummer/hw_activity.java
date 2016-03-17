package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;


public class hw_activity extends AppCompatActivity {
    private Button onBtn;
    private Button offBtn;
    private Button listBtn;
    private TextView statusText;
    private TextView dataText1;
    private TextView dataText2;

    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> pairedDevicesArray;

    private BluetoothAdapter myBluetoothAdapter;
    private ConnectThread stick1_connect;
    private ConnectedThread stick1_maintain;
    private ConnectThread stick2_connect;
    private ConnectedThread stick2_maintain;

    private SoundPlayer drumPlayer;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            check_msg_connection(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_activity);
        drumPlayer = new SoundPlayer(this);
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

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);
            statusText.setText("Status: not supported");
        } else {
            statusText = (TextView)findViewById(R.id.status_text);
            dataText1 = (TextView)findViewById(R.id.raw_text);
            dataText2 = (TextView)findViewById(R.id.raw_text2);

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

        }
        if(!myBluetoothAdapter.isEnabled()) {
            listBtn.setEnabled(false);
        }

        pairedDevicesArray = new ArrayList<BluetoothDevice>();
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
        String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
        int name_end = mDeviceInfo.indexOf("\n");
        statusText.setText("Status: connecting to " + mDeviceInfo.substring(0, name_end));

        BluetoothDevice selectedDevice = pairedDevicesArray.get(pos);
        if(stick1_connect == null) {
            statusText.setText("Status: connecting to (1)" + mDeviceInfo.substring(0, name_end));
            stick1_connect = new ConnectThread(selectedDevice, mHandler, 1);
            stick1_connect.start();
        } else {
            statusText.setText("Status: connecting to (2)" + mDeviceInfo.substring(0, name_end));
            stick2_connect = new ConnectThread(selectedDevice, mHandler, 2);
            stick2_connect.start();
        }
    }

    public void check_msg_connection(Message msg) {
        switch(msg.what) {
            case StaticVars.SUCCESS_CONNECT_1:
                stick1_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler, 1);
                stick1_maintain.start();
                statusText.append("-> SUCCESS(1)!");
                break;
            case StaticVars.SUCCESS_CONNECT_2:
                stick2_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler, 2);
                stick2_maintain.start();
                statusText.append("-> SUCCESS(2)!");
                break;
            case StaticVars.FAIL_CONNECT:
                statusText.append("-> FAILED.");
                break;
            case StaticVars.MESSAGE_READ_1:
                byte[] readBuf = (byte[])msg.obj;
                String s = new String(readBuf);
                //dataText1.setText("Status: Data1 is " + s);
                process_drum_data(s);
                break;
            case StaticVars.MESSAGE_READ_2:
                byte[] readBuf2 = (byte[])msg.obj;
                String s2 = new String(readBuf2);
                //dataText2.setText("Status: Data2 is " + s2);
                process_drum_data(s2);
                break;
        }
    }

    public void process_drum_data(String data) {
        //Yaw Pitch Ax Ay Az\n
        //sscanf(data, "%f %f %f %f %f\n", Yaw, Pitch, Ax, Ay, Az);
        Scanner parser = new Scanner(data);
        float Yaw = parser.nextFloat();
        float Pitch = parser.nextFloat();
        float Ax = parser.nextFloat();
        float Ay = parser.nextFloat();
        float Az = parser.nextFloat();
        dataText1.setText("Y:" + Yaw + "\nP:" + Pitch +
                "\nAx:" + Ax +"\nAy:" + Ay + "\nAz:" + Az);
        /*
        if(Pitch < -1) {
            if(Yaw < -35 && Yaw >= -70) {
                drumPlayer.playSnare();
            } else if(Yaw < 0 && Yaw >= -35) {
                drumPlayer.playTom1();
            } else if(Yaw < 35 && Yaw >= 0) {
                drumPlayer.playTom2();
            } else if(Yaw < 70 && Yaw >= 35) {
                drumPlayer.playTom3();
            }
        }
        */

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
        if (id == R.id.action_settings) {
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


