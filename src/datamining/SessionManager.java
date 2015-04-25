/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author colander
 */
public class SessionManager {

    public static void main(String[] args) {
        new SessionManager();
    }

    ArrayList<ChatWriter> sessions;
    final int MAX_CHANNEL_COUNT = 10;
    final String LOG_DIRECTORY = "logs";
    TwitchApiReader api;

    public SessionManager() {
        this.sessions = new ArrayList<>();
        this.sessions.add(new ChatWriter("sodapoppin", "sodapoppin.txt"));
        this.api = new TwitchApiReader();
        String[] str = api.getTopChannels(this.MAX_CHANNEL_COUNT);
        for (int i = 0; i < str.length; i++) {
            newSession(str[i]);
        }
    }

    public void newSession(String channel) {
        this.sessions.add(new ChatWriter(channel, LOG_DIRECTORY + "/" + channel + "/" + channel + "_" + System.currentTimeMillis()  + ".txt"));
        System.out.println(System.currentTimeMillis() + " NEW SESSION: " + channel);
    }
}
