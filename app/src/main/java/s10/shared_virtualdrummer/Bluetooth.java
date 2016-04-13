package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.regex.Pattern;

public class Bluetooth extends AppCompatActivity {
    private TextView statusText;
    private BluetoothDevice dev1;
    private BluetoothDevice dev2;
    private ConnectThread stick1_connect;
    private ConnectedThread stick1_maintain;
    private ConnectThread stick2_connect;
    private ConnectedThread stick2_maintain;
    private SoundPlayer drumPlayer;
    private boolean kitType = true;
    private boolean rightHand = true;

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
        Intent get_intent = getIntent();
        final boolean language = get_intent.getBooleanExtra("lang", true);
        kitType = get_intent.getBooleanExtra("drum", true);
        rightHand = get_intent.getBooleanExtra("hand", true);
        if (language) {
            setContentView(R.layout.bluetooth);
        }else{
            setContentView(R.layout.bluetooth);
        }
        dev1 = get_intent.getExtras().getParcelable("dev1");
        dev2 = get_intent.getExtras().getParcelable("dev2");
        drumPlayer = new SoundPlayer(this);

        statusText = (TextView)findViewById(R.id.status_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusText.setText("Started Launching BT Acitivity");

        if(dev1 != null) {
            stick1_connect = new ConnectThread(dev1, mHandler, 1);
            stick1_connect.start();
        }
        if(dev2 != null) {
            stick2_connect = new ConnectThread(dev2, mHandler, 2);
            stick2_connect.start();
        }
        statusText.setText("Started Connecting to BT devices");
    }

    public void destroy_connections() {
        if(stick1_connect != null) {
            stick1_connect.cancel();
            stick1_connect = null;
        }
        if(stick1_maintain != null) {
            stick1_maintain.cancel();
            stick1_maintain = null;
        }
        if(stick2_connect != null) {
            stick2_connect.cancel();
            stick2_connect = null;
        }
        if(stick2_maintain != null) {
            stick2_maintain.cancel();
            stick2_maintain = null;
        }
    }


    public void check_msg_connection(Message msg) {
        switch(msg.what) {
            case StaticVars.SUCCESS_CONNECT_1:
                stick1_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler);
                stick1_maintain.start();
                statusText.setText("Successfully connected to Dev1");
                break;
            case StaticVars.SUCCESS_CONNECT_2:
                stick2_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler);
                stick2_maintain.start();
                statusText.setText("Successfully connected to Dev2");
                break;
            case StaticVars.FAIL_CONNECT:
                destroy_connections();
                statusText.setText("Failed to connect to BT devices");
                break;
            case StaticVars.MESSAGE_READ:
                byte[] readBuf = (byte[])msg.obj;
                String s = new String(readBuf);
                int r = parse_data(s);
                if(r != Integer.MIN_VALUE) {
                    process_drum_data(r);
                }
                break;
        }
    }

    public int parse_data(String data) {
        String[] nSplit = data.split("\\n");
        int last = nSplit.length - 1;
        int curr = nSplit.length - 2;
        if(last > 0) {
            //Found a newLine
            String ret = nSplit[curr];

            String[] nums = ret.split(" ");
            if(nums[0].length() == 4)
                ret = nums[0];
            else if (nums.length > 1 && nums[1].length() == 4)
                ret = nums[1];
            else if (nums.length > 2 && nums[2].length() == 4)
                ret = nums[2];
            else
                return Integer.MIN_VALUE;

            Pattern intPattern = Pattern.compile("[+-]\\d{3}");
            if(intPattern.matcher(ret).matches() ) {
                int ret_int =  Integer.parseInt(ret);
                return ret_int;
            }
            return Integer.MIN_VALUE;
        } else {
            //Didn't find a newLine
            return Integer.MIN_VALUE;
        }
    }

    public void process_drum_data(int data) {
        int Yaw = data;

        if(kitType) {
            //Normal Drum Kit
            if(!rightHand) {
                //Left Handed, invert everything
                Yaw = -1 * Yaw;
            }

            if (Yaw < -45 && Yaw >= -90) {
                drumPlayer.playSnare();
            } else if (Yaw < 0 && Yaw >= -45) {
                drumPlayer.playTom1();
            } else if (Yaw < 45 && Yaw >= 0) {
                drumPlayer.playTom2();
            } else if (Yaw < 90 && Yaw >= 45) {
                drumPlayer.playTom3();
            } else {
                drumPlayer.playRide();
            }
        } else {
            //Taiko Drum
            if (Yaw < 35 && Yaw > -35) {
                drumPlayer.playCenter();
            } else {
                drumPlayer.playRim();
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
        final BluetoothDevice d1 = get_intent.getParcelableExtra("dev1");
        final BluetoothDevice d2 = get_intent.getParcelableExtra("dev2");


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play) {
            Intent intent = new Intent(Bluetooth.this, Sound.class);
            //intent.putExtra("blue", pairedDevicesArray);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", d1);
            intent.putExtra("dev2", d2);
            destroy_connections();
            drumPlayer.destroy();
            finish();
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Bluetooth.this, Settings.class);
            intent.putExtra("lang", language);
            intent.putExtra("drum", drum);
            intent.putExtra("hand", hand);
            intent.putExtra("dev1", d1);
            intent.putExtra("dev2", d2);
            destroy_connections();
            drumPlayer.destroy();
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        destroy_connections();
        super.onDestroy();
    }
}
