package uk.ac.warwick.dcs.cs261.team14.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Ming on 2/12/2017.
 */
public class LiveStreamTask implements Runnable {
    @Override
    public void run() {
        try {
            Socket socket = new Socket("cs261.dcs.warwick.ac.uk", 80);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
               System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
