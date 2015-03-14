
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
import java.net.Socket;
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
    final String SERVER = "irc.twitch.tv";
    final String LOGIN = "cobolot";
    final String OAUTH = "oauth:19y7ivmsmck4ct9vw7sdg1j4u6232o";
    public ArrayList<String> currentChannels = new ArrayList<String>();

    //do konstruktoru pak asi muzes dat argumenty jako channel a podobne.
    //username a heslo muzeme asi hardcodovat, to se menit nebude
    //veci ktery chat zpracovavaj bych sem nedaval
    public ChatReader() {

        listeners = new ArrayList<>();

        // Connect directly to the IRC server.
        Socket socket;
        try {
            socket = new Socket(SERVER, 6667);
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
    }

    public void start() { //zacne cist zpravy
        //NA TENHLE PRIKLAD JSEM DAL PERIOD NA 1000 MS
        //V IMPLEMENTACI TO VRAT NA NECO ROZUMNYHO
        new Timer().scheduleAtFixedRate(new ReaderTask(), 1000, 50);
    }

    public void addListener(ChatListener l) {
        listeners.add(l);
    }

    public void joinChannel(String channel) {
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

    //asi bych sem dal i posilani zpravy, i kdyz je to "reader"
    //pripadne prejmenujeme jestli mas nejakej napad :D
    public void sendMessage(String msg, String channel) {
        try {
            writer.write("PRIVMSG #" + channel + " :" + msg + "\r\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ChatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ReaderTask extends TimerTask {

        @Override
        public void run() {
            //tady patri to, co chces delat kazdych 50 ms - nejaky to cteni IRC.
            //nejsem si jistej jestli se to da implementovat takhle (volani metody kazdych 50 ms)
            //tak to kdyztak uprav :)

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
