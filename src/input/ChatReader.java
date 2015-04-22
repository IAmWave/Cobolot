
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Václav
 */
public class ChatReader {

    ArrayList<ChatListener> listeners;

    BufferedWriter writer = null;
    BufferedReader reader = null;
    final String SERVER = "irc.twitch.tv:6667";
    public final String LOGIN = "cobolot";
    final String OAUTH = "oauth:cwa4qb444vsnk8qzhvhlua36ebck22";
    public ArrayList<String> currentChannels = new ArrayList<String>();
    //Twitch API parsing
    private static final String API_URL_PREFIX = "http://api.twitch.tv/api/channels/";
    private static final String API_URL_SUFFIX = "/chat_properties";
    private static final String SERVERS_START = "\"chat_servers\":[\"";
    private static final String SERVERS_END = "\"";

    public ChatReader(String channel) {
        listeners = new ArrayList<>();

        String server;
        if (channel != null) server = getServer(channel);
        else server = SERVER;
        // Connect directly to the IRC server.
        Socket socket;
        try {
            socket = new Socket(server.split(":")[0], Integer.parseInt(server.split(":")[1]));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.flush();
            // Log on to the server.
            writer.write("PASS " + OAUTH + "\r\n");
            writer.write("NICK " + LOGIN + "\r\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        joinChannel(channel);
    }

    public void start() { //zacne cist zpravy
        new ReaderTask().start();
        //new Timer().scheduleAtFixedRate(new ReaderTask(), 1000, 20);
    }

    public void addListener(ChatListener l) {
        listeners.add(l);
    }

    private void joinChannel(String channel) {
        try {
            writer.write("JOIN #" + channel + "\r\n");
            writer.flush();
            this.currentChannels.add(channel);
        } catch (IOException ex) {
            Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leaveChannel(String channel) {
        for (int i = 0; i < this.currentChannels.size(); i++) {
            if (currentChannels.get(i).equals(channel)) {
                try {
                    writer.write("PART #" + channel + "\r\n");
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    public void sendMessage(String msg, String channel) {
        try {
            writer.write("PRIVMSG #" + channel + " :" + msg + "\r\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (ChatListener l : listeners) {
            l.onUserMessage(new Message(channel, this.LOGIN, msg));
        }
    }

    private String getServer(String channel) {
        try {
            URL obj = new URL(API_URL_PREFIX + channel + API_URL_SUFFIX);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String input = in.readLine();
            input = input.substring(input.indexOf(SERVERS_START) + SERVERS_START.length());
            input = input.substring(0, input.indexOf(SERVERS_END));
            in.close();
            return input;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("error in getting servers");
        return "error in getting servers";
    }

    private class ReaderTask extends Thread {

        @Override
        public void run() {
            while (true) {
                String line = "";
                try {
                    line = reader.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (line.startsWith("PING ")) {
                    try {
                        // We must respond to PINGs to avoid being disconnected.
                        writer.write("PONG " + line.substring(5) + "\r\n");
                        writer.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println(line);
                } else if (line.contains("PRIVMSG")) {
                    String msgChannel = "";
                    for (int i = 0; i < currentChannels.size(); i++) {
                        if (line.contains("PRIVMSG #" + currentChannels.get(i))) {
                            for (ChatListener l : listeners) {
                                l.onMessage(new Message(currentChannels.get(i), line.split("!")[0].substring(1), line.substring(line.indexOf(currentChannels.get(i)) + currentChannels.get(i).length() + 2)));
                            }
                        }
                    }
                } else {
                    // Vypise zpravu, pokud nezna jeji smysl
                    System.out.println(line);
                }
            }
        }
    }
}
