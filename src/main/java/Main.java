import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    String word = in.readLine().toLowerCase();
                    List<PageEntry> pageEntryList = engine.search(word);
                    out.println(convertJson(pageEntryList));
                }
            }
        }
    }

    public static String convertJson(List<PageEntry> pageList) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .create();
        return gson.toJson(pageList);
    }
}