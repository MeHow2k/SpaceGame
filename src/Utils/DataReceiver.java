package Utils;
import java.io.*;
import java.net.*;
import org.json.*;
import java.nio.file.*;

//JEST TO APLIKACJA SERWERA ZBIERANIA DANYCH
public class DataReceiver {
    private static final int PORT = 43211; // Port serwera
    private static final String FILE_PREFIX = "data_"; // Prefiks nazwy pliku
    private static final String FILE_EXTENSION = ".json"; // Rozszerzenie pliku
    private static final String DATA_FOLDER = "Data"; // Nazwa folderu

    public static void main(String[] args) {
        try {
            // Utworzenie serwera nasłuchującego na określonym porcie
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);
            // Sprawdzenie istnienia folderu, jeśli nie istnieje, utwórz go
            File dataFolder = new File(DATA_FOLDER);
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            while (true) {
                // Akceptowanie połączenia od klienta
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                // Odczytanie danych wysłanych przez klienta
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                StringBuilder jsonData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }
                reader.close();

                // Konwersja otrzymanego JSON-a
                JSONObject jsonObject = new JSONObject(jsonData.toString());

                // Sprawdzenie, czy wartość klucza "Game_version" zawiera ciąg znaków "dev"
                if (!jsonObject.optString("Game_version", "").contains("dev")) {
                    // Ustalenie nazwy pliku na podstawie ostatniego dostępnego numeru
                    int nextFileNumber = getNextFileNumber();
                    String fileName = DATA_FOLDER + "/" + FILE_PREFIX + nextFileNumber + FILE_EXTENSION;

                    // Dodanie klucza "Client_socket" do obiektu JSON
                    jsonObject.put("Client_socket", clientSocket);

                    // Zapisanie danych do pliku
                    try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
                        fileWriter.write(jsonObject.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error writing to file: " + e.getMessage());
                    }
                } else {
                    System.out.println("JSON contains 'Game_version' with 'dev', skipping saving to file.");
                }

                // Zamknięcie połączenia
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Metoda do ustalenia numeru następnego pliku
    private static int getNextFileNumber() {
        int nextFileNumber = 1;
        try {
            // Uzyskanie listy plików w folderze
            Path dir = Paths.get(DATA_FOLDER);
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir, FILE_PREFIX + "*" + FILE_EXTENSION);

            // Znalezienie najwyższego numeru w istniejących plikach
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();
                int fileNumber = Integer.parseInt(fileName.substring(FILE_PREFIX.length(), fileName.length() - FILE_EXTENSION.length()));
                if (fileNumber >= nextFileNumber) {
                    nextFileNumber = fileNumber + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while getting next file number: " + e.getMessage());
        }
        return nextFileNumber;
    }
}

