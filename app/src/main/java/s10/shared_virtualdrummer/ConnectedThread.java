package s10.shared_virtualdrummer;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler cHandler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        mHandler = cHandler;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            mHandler.obtainMessage(StaticVars.FAIL_CONNECT).sendToTarget();
        }
        mmInStream = tmpIn;
    }

    public void run() {
        byte[] buffer;  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                buffer = new byte[1024];
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                mHandler.obtainMessage(StaticVars.MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                break;
            }

        }
    }

    // Call this from the main activity to shutdown the connection
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            mHandler.obtainMessage(StaticVars.FAIL_CONNECT).sendToTarget();
        }
    }
}
