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
import android.widget.ImageButton;
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
    private ArrayList<BluetoothDevice> pairedDevicesArray;
    private BluetoothAdapter myBluetoothAdapter;
    private BluetoothDevice dev1;
    private BluetoothDevice dev2;
    private boolean language = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flag_activity);

        ImageButton jpnBtn = (ImageButton)this.findViewById(R.id.lang_jpn);
        jpnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = false;
                setup_page();
            }
        });

        ImageButton engBtn = (ImageButton)this.findViewById(R.id.lang_eng);
        engBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = true;
                setup_page();
            }
        });
    }

    public void setup_page() {
        if (language) {
            setContentView(R.layout.activity_hw_activity);
        }else{
            setContentView(R.layout.activity_hw_activity_j);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);
            if(language) {
                //statusText.setText("Status: not supported");
                statusText.setText(R.string.status_unsupported);
            } else {
                statusText.setText(R.string.status_unsupported_j);
            }
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
                    connect_dv(v, pos);
                }
            });

            //create the arrayAdapter that contains the BTDevices
            BTArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1);
            myListView.setAdapter(BTArrayAdapter);
            if(language) {
                statusText.setText(R.string.status_enabled);
            } else {
                statusText.setText(R.string.status_enabled_j);
            }

            if(!myBluetoothAdapter.isEnabled()) {
                listBtn.setEnabled(false);
                if(language) {
                    statusText.setText(R.string.status_disabled);
                } else {
                    statusText.setText(R.string.status_disabled_j);
                }
            }

            pairedDevicesArray = new ArrayList<>();

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
        if(language) {
            statusText.setText(R.string.status_disabled);
        } else {
            statusText.setText(R.string.status_disabled_j);
        }
    }

    public void list_bt(View view) {
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        BTArrayAdapter.clear();
        for (BluetoothDevice device : pairedDevices) {
            BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            pairedDevicesArray.add(device);
        }
    }


    public void connect_dv(View v, int pos) {
        myBluetoothAdapter.cancelDiscovery();
        String mDeviceInfo = ((TextView) v).getText().toString();
        int name_end = mDeviceInfo.indexOf("\n");

        BluetoothDevice selectedDevice = pairedDevicesArray.get(pos);
        if (dev1 == null){
            dev1 = selectedDevice;
            statusText.setText(mDeviceInfo.substring(0, name_end));
            if(language) {
                statusText.append(getResources().getString(R.string.status_drum1_selected));
            } else {
                statusText.append(getResources().getString(R.string.status_drum1_selected_j));
            }
        }
        else {
            dev2 = selectedDevice;
            statusText.setText(mDeviceInfo.substring(0, name_end));
            if(language) {
                statusText.append(getResources().getString(R.string.status_drum2_selected));
            } else {
                statusText.append(getResources().getString(R.string.status_drum2_selected_j));
            }
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
                if(language) {
                    statusText.setText(R.string.status_enabled);
                } else {
                    statusText.setText(R.string.status_enabled_j);
                }
            } else {
                if(language) {
                    statusText.setText(R.string.status_disabled);
                } else {
                    statusText.setText(R.string.status_disabled_j);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if(dev1 == null) {
            if(language) {
                statusText.setText(R.string.status_none_selected);
            } else {
                statusText.setText(R.string.status_none_selected_j);
            }
            return true;
        }

        int id = item.getItemId();
        final boolean lang = language;
        final boolean drum = true;
        final boolean hand = true;


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(hw_activity.this, Settings.class);
            intent.putExtra("lang", lang);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_blue_tooth) {
            Intent intent = new Intent(hw_activity.this, Bluetooth.class);
            intent.putExtra("lang", lang);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", dev1);
            intent.putExtra("dev2", dev2);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_credits) {
            Intent intent = new Intent(hw_activity.this, Credits.class);
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
        unregisterReceiver(bReceiver);
    }

}


