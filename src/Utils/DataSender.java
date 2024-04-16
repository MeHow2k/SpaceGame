package Utils;

import org.json.JSONObject;

import java.io.*;
import java.net.*;

public class DataSender {
    private static final String SERVER_HOST = "172.160.225.248"; // Adres serwera azure do zbierania danych
    //private static final String SERVER_HOST = "192.168.1.40"; // Adres serwera do testów (lokalnie)
    private static final int SERVER_PORT = 43211; // Port serwera
    public void sendData(JSONObject jsonObject) {
        try {
            // Utworzenie socketu i połączenie z serwerem
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            // Utworzenie strumienia do wysyłania danych do serwera
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            // Wysłanie danych do serwera
            writer.println(jsonObject.toString());
            // Zamknięcie połączenia
            writer.close();
            outputStream.close();
            socket.close();
            System.out.println("Data successfully sent to the server.");
        } catch (IOException e) {
            System.err.println("Error while sending data to the server.");
        }
    }
}
