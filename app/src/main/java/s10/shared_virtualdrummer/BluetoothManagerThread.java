package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by cadams on 3/11/16.
 */
public class BluetoothManagerThread extends Thread {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            manage_message(msg);
        }
    };

    private ConnectThread stick1_connect;
    private ConnectedThread stick1_maintain;
    private ConnectThread stick2_connect;
    private ConnectedThread stick2_maintain;

    private SoundPlayer drumPlayer;
    private boolean hasPlayed = false;
    private Context cc;

    public BluetoothManagerThread(Context con, BluetoothDevice device1, BluetoothDevice device2) {
        cc = con;
        Toast.makeText(cc, "Entered BT Man", Toast.LENGTH_SHORT).show();

        drumPlayer = new SoundPlayer(cc);

        stick1_connect = new ConnectThread(device1, mHandler, 1);
        stick1_connect.start();
        Toast.makeText(cc, "Starting Connecting", Toast.LENGTH_SHORT).show();
        //stick2_connect = new ConnectThread(device2, mHandler, 2);
        //stick2_connect.start();
    }

    public void run() {
        while(true); //I WILL NEVER DIE! MWAHAHA
    }

    // Will cancel an in-progress connection, and close the socket
    public void cancel() {
        //BAIL! BAIL!
        destroy_connections();
    }

    private void manage_message(Message msg) {
        switch(msg.what) {
            case StaticVars.SUCCESS_CONNECT_1:
                stick1_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler, 1);
                stick1_maintain.start();
                Toast.makeText(cc, "Connected to Drumstick", Toast.LENGTH_LONG).show();

                break;
            case StaticVars.SUCCESS_CONNECT_2:
                stick2_maintain = new ConnectedThread((BluetoothSocket) msg.obj, mHandler, 2);
                stick2_maintain.start();
                break;
            case StaticVars.FAIL_CONNECT:
                destroy_connections();
                break;
            case StaticVars.MESSAGE_READ_1:
                byte[] readBuf = (byte[])msg.obj;
                String s = new String(readBuf);

                String r = parse_data(s);
                if(!r.isEmpty()) {
                    process_drum_data(r);
                }

                break;
            case StaticVars.MESSAGE_READ_2:
                byte[] readBuf2 = (byte[])msg.obj;
                String s2 = new String(readBuf2);
                /*
                String r = parse_data(s);
                if(!r.isEmpty()) {
                    process_drum_data(r);
                }
                */
                break;
        }
    }

    private String leftovers = "";
    private String parse_data(String data) {
        String total_data = leftovers + data;
        String[] nSplit = total_data.split("\\n");
        int last = nSplit.length - 1;
        int curr = nSplit.length - 2;
        if(last > 0) {
            //Found a newLine
            leftovers = nSplit[last];
            String ret = nSplit[curr];
            if(ret.contains("@")) {
                //Valid starting
                Scanner fChecker = new Scanner(ret);
                int count = 0;
                fChecker.next();
                while(fChecker.hasNextFloat()) {
                    fChecker.nextFloat();
                    count++;
                }
                if(count == 2) {
                    //perfect number of variables
                    return ret;
                }
            }
            //Invalid, throw away
            return "";

        } else {
            //Didn't find a newLine yet
            leftovers = nSplit[last];
            return "";
        }
    }

    private void process_drum_data(String data) {
        float Yaw = Float.NaN;
        float Az = Float.NaN;

        Scanner parser = new Scanner(data);
        parser.next(); //remove @ sign
        if(parser.hasNextFloat())
            Yaw = parser.nextFloat();

        if(parser.hasNextFloat())
            Az = parser.nextFloat();
        Toast.makeText(cc, "Y:" + Yaw + "\tAz:" + Az, Toast.LENGTH_SHORT).show();


        if(!hasPlayed && Az < StaticVars.AZ_THRES) {
            if(Yaw < -35 && Yaw >= -70) {
                drumPlayer.playSnare();
            } else if(Yaw < 0 && Yaw >= -35) {
                drumPlayer.playTom1();
            } else if(Yaw < 35 && Yaw >= 0) {
                drumPlayer.playTom2();
            } else if(Yaw < 70 && Yaw >= 35) {
                drumPlayer.playTom3();
            }
            hasPlayed = true;
        }
        if(Az > StaticVars.AZ_THRES) {
            hasPlayed = false;
        }
    }

    private void destroy_connections() {
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
}