package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private Handler mHandler;
    private int stick;

    public ConnectThread(BluetoothDevice device, Handler cHandler, int s) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mHandler = cHandler;
        stick = s;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(StaticVars.MY_UUID);
        } catch (IOException e) {
            mHandler.obtainMessage(StaticVars.FAIL_CONNECT).sendToTarget();
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                mHandler.obtainMessage(StaticVars.FAIL_CONNECT).sendToTarget();
                return;
            }
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
        } catch (IOException e) {
            mHandler.obtainMessage(StaticVars.FAIL_CONNECT).sendToTarget();
        }
    }
}