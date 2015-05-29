/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author colander
 */
public class SessionManager {

    public static void main(String[] args) {
        new SessionManager();
    }

    ArrayList<ChatWriter> sessions;
    final int MAX_CHANNEL_COUNT = 15;
    final String LOG_DIRECTORY = "logs";
    TwitchApiReader api;

    public SessionManager() {
        this.sessions = new ArrayList<>();
        //this.sessions.add(new ChatWriter("sodapoppin", "sodapoppin.txt"));
        this.api = new TwitchApiReader();
        new Timer().schedule(new ResetTask(), 0, 900000);
    }

    public void newSession(String channel) {
        this.sessions.add(new ChatWriter(channel, LOG_DIRECTORY + "/" + channel + "/" + channel + "-" + System.currentTimeMillis() + ".txt"));
        System.out.println(timestamp() + " NEW SESSION: " + channel);
    }

    private class ResetTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("RESET");
            for (int i = 0; i < sessions.size(); i++) {
                sessions.get(i).writeAll();
                sessions.get(i).cr.stop();
            }
            sessions = new ArrayList<>();
            String[] str = api.getTopChannels(MAX_CHANNEL_COUNT);
            for (int i = 0; i < str.length; i++) {
                newSession(str[i]);
            }
        }
    }

    public String timestamp() {
        Date d = new Date();
        return new String(d.getHours() + ":"+ d.getMinutes() + ":"+d.getSeconds());
    }
}
