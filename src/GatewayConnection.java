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

    /**
     * Continuously read vital details sent from a vital monitor and displays
     * them in console along with the Vital Monitor ID
     *
     * Printing out vital details to the console is synchronized since multiple threads
     * might try to write to the stdout at the same time and it might cause data races.
     */
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
            VitalPrinter printer = new VitalPrinter();
            while(true){
                String messageFromVitalMonitor = br.readLine();
                synchronized (printer){
                    printer.printVitalDetails(messageFromVitalMonitor);
                }
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
