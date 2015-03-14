/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchchatclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Lenovo
 */
public class TwitchChatClient {

    BufferedWriter writer = null;
    BufferedReader reader = null;
    
    String server = "irc.twitch.tv";
    String nick = "cobolot";
    String login = "cobolot";
    String oAuth = "oauth:19y7ivmsmck4ct9vw7sdg1j4u6232o";
    String channel = "#sodapoppin";
    public boolean goalReached = false;
    public String target = "OJWADPAWPOJAPOJWAOJWP";
    public int pastaCount = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new TwitchChatClient("#sodapoppin");
        } catch (IOException ex) {
            Logger.getLogger(TwitchChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TwitchChatClient(String channel) throws IOException {
        this.channel = channel;
        // Connect directly to the IRC server.
        Socket socket;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            socket = new Socket(server, 6667);

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.writer = writer;
            this.reader = reader;
        } catch (IOException ex) {
            Logger.getLogger(TwitchChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Log on to the server.
        writer.write("PASS " + oAuth + "\r\n");
        writer.write("NICK " + login + "\r\n");
        writer.flush();

        // Read lines from the server until it tells us we have connected.
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.indexOf("004") >= 0) {
                // We are now logged in.
                break;
            } else if (line.indexOf("433") >= 0) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }

        // Join the channel.
        writer.write("JOIN " + channel + "\r\n");
        writer.flush();
    }

    public String readAll() throws IOException {
        String out = "";
        String line = reader.readLine();
        // if (!line.contains("PART") && !line.contains("JOIN") && !line.contains("tmi.twitch.tv 353")) {
        if (line.startsWith("PING ")) {
            // We must respond to PINGs to avoid being disconnected.
            writer.write("PONG " + line.substring(5) + "\r\n");
            writer.flush();
            System.out.println(line);
        } else if (line.contains("PRIVMSG")) {
            System.out.println(line);
            String msg = line.substring(line.indexOf(this.channel) + channel.length() + 2);
            if (msg.contains(target) && !line.contains(this.nick)) {
                goalReached = true;
                this.pastaCount++;
            }
            out = msg;

        } else {
            // Print the raw line received by the bot.
            System.out.println(line);
        }
        return out;
    }

    public void sendMessage(String msg) {
        try {
            writer.write("PRIVMSG " + this.channel + " :" + msg + "\r\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(TwitchChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
