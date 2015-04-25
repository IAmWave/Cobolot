/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import input.ChatListener;
import input.ChatReader;
import input.Message;
import java.util.ArrayList;
import output.FileOutput;

/**
 *
 * @author colander
 */
public class ChatWriter implements ChatListener {
    
    String sessionAddress;
    ArrayList<Message> unsavedMessages;
    final int MAX_UNSAVED_MESSAGES = 100;
    ChatReader cr;

    public ChatWriter(String channel, String sessionAddress) {
        this.unsavedMessages = new ArrayList<>();
        this.sessionAddress = sessionAddress;
        
        this.cr = new ChatReader(channel, true);
        cr.addListener(this);
        cr.start();
    }

    @Override
    public void onMessage(Message message) {
        this.unsavedMessages.add(message);
        if(this.unsavedMessages.size()> this.MAX_UNSAVED_MESSAGES){
            writeAll();
        }
    }

    @Override
    public void onUserMessage(Message message) {
        this.unsavedMessages.add(message);
        if(this.unsavedMessages.size()> this.MAX_UNSAVED_MESSAGES){
            writeAll();
        }
    }

    public void writeAll() {
        FileOutput.writeMessages(this.unsavedMessages, this.sessionAddress);
        this.unsavedMessages = new ArrayList<>();
    }

}
