package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;

/**
 * Created by cadams on 3/11/16.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private Handler mHandler;
    private int stick;
    //private final BluetoothDevice mmDevice;

    public ConnectThread(BluetoothDevice device, Handler cHandler, int s) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mHandler = cHandler;
        stick = s;
        //mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(StaticVars.MY_UUID);
            /*
            if(s == 1) {
                tmp = device.createRfcommSocketToServiceRecord(StaticVars.uuid_stick1);
            } else {
                tmp = device.createRfcommSocketToServiceRecord(StaticVars.uuid_stick2);
            }
            */
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        // myBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }

            mHandler.obtainMessage(StaticVars.FAIL_CONNECT).sendToTarget();
            return;
        }

        // Do work to manage the connection (in a separate thread)
        if(stick == 1) {
            mHandler.obtainMessage(StaticVars.SUCCESS_CONNECT_1, mmSocket).sendToTarget();
        } else {
            mHandler.obtainMessage(StaticVars.SUCCESS_CONNECT_2, mmSocket).sendToTarget();
        }
    }

    // Will cancel an in-progress connection, and close the socket
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}