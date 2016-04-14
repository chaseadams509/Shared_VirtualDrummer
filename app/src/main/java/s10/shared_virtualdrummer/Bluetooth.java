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
    private boolean language = true;
    private int numConnected = 0;

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
        language = get_intent.getBooleanExtra("lang", true);
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

        if(language) {
            statusText.setText("Starting...");
        } else {
            statusText.setText("少々お待ちください");
        }

        if(dev1 != null) {
            stick1_connect = new ConnectThread(dev1, mHandler, 1);
            stick1_connect.start();
        }
        if(dev2 != null) {
            stick2_connect = new ConnectThread(dev2, mHandler, 2);
            stick2_connect.start();
        }

        if(language) {
            statusText.setText("Connecting to drumsticks");
        } else {
            statusText.setText("撥らを繋ぎ中です");
        }
    }

    public void destroy_connections() {
        numConnected = 0;
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
                if(language) {
                    statusText.setText("Connected to first drumstick");
                } else {
                    statusText.setText("一本目の撥を繋いだところ");
                }
                numConnected++;
                break;
            case StaticVars.SUCCESS_CONNECT_2:
                stick2_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler);
                stick2_maintain.start();
                if(language) {
                    statusText.setText("Connected to second drumstick");
                } else {
                    statusText.setText("二本目の撥を繋いだところ");
                }
                numConnected++;
                break;
            case StaticVars.FAIL_CONNECT:
                destroy_connections();
                if(language) {
                    statusText.setText("Failed to connect to drumsticks");
                } else {
                    statusText.setText("撥らを繋ぐ失敗");
                }
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

        if(numConnected == 2 || (numConnected == 1 && dev2 == null)) {
            if(language) {
                statusText.setText("Ready to Play!\n");
                if(kitType) {
                    if(rightHand) {
                        statusText.append("Currently Playing right-handed Rock Kit");
                    } else {
                        statusText.append("Currently Playing left-handed Rock Kit");
                    }
                } else {
                    statusText.append("Currently Playing Japanese Taiko Drum");
                }
            } else {
                statusText.setText("プレイ始めましょう!\n");
                if(kitType) {
                    if(rightHand) {
                        statusText.append("今、右利きでロックキットはプレイ中です。");
                    } else {
                        statusText.append("今、左利きでロックキットはプレイ中です。");
                    }
                } else {
                    statusText.append("今、和太鼓はプレイ中です。");
                }
            }
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

            if (Yaw < -40 && Yaw >= -80) {
                drumPlayer.playSnare();
            } else if (Yaw < 0 && Yaw >= -40) {
                drumPlayer.playTom1();
            } else if (Yaw < 40 && Yaw >= 0) {
                drumPlayer.playTom2();
            } else if (Yaw < 80 && Yaw >= 40) {
                drumPlayer.playTom3();
            } else if (Yaw < -80){
                drumPlayer.playHiHat();
            } else { // Yaw > 80
                drumPlayer.playRide();
            }
        } else {
            //Taiko Drum
            if (Yaw < 40 && Yaw > -40) {
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
        int id = item.getItemId();
        Intent get_intent = getIntent();
        final boolean lang = get_intent.getBooleanExtra("lang", true);
        final boolean drum = get_intent.getBooleanExtra("drum", true);
        final boolean hand = get_intent.getBooleanExtra("hand", true);
        final BluetoothDevice d1 = get_intent.getParcelableExtra("dev1");
        final BluetoothDevice d2 = get_intent.getParcelableExtra("dev2");


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Bluetooth.this, Settings.class);
            intent.putExtra("lang", lang);
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
