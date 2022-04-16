/*
E/17/207 - Pasindu Marasinghe
* */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class GatewayConnection extends Thread{
    private final Monitor vitalMonitor;

    public GatewayConnection(Monitor vitalMonitor){
        this.vitalMonitor = vitalMonitor;
    }

    private void handleMonitorConnection(){
        try {
            String monitorID = this.vitalMonitor.getMonitorID();
            int monitorPort = this.vitalMonitor.getPort();
            InetAddress monitorIP = this.vitalMonitor.getIp();

            Socket gatewaySocket = new Socket(monitorIP, monitorPort);
            System.out.println("Vital monitor "+monitorID+" connected.");

            /* Read Vitals from a vital monitor and display them on console*/
            InputStreamReader in = new InputStreamReader(gatewaySocket.getInputStream());
            BufferedReader br = new BufferedReader(in);
            while(true){
                String messageFromVitalMonitor = br.readLine();
                System.out.println(messageFromVitalMonitor);
            }
        }
        catch(IOException e){}
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        this.handleMonitorConnection();
    }
}
