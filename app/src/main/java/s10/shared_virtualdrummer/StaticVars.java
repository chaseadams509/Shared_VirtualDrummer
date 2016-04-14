package s10.shared_virtualdrummer;

import java.util.UUID;

/**
 * Created by cadams on 3/13/16.
 */
public class StaticVars {
    public final static String EXTRA_MESSAGE = "s10.shared_virtualdrummer";
    public final static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public final static int REQUEST_ENABLE_BT = 1;

    public final static int FAIL_CONNECT = -1;
    public final static int SUCCESS_CONNECT_1 = 0;
    public final static int SUCCESS_CONNECT_2 = 1;
    public final static int MESSAGE_READ = 9;
}
