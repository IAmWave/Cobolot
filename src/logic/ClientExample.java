/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import input.ChatListener;
import input.ChatReader;
import input.Message;

/**
 *
 * @author Václav
 */
public class ClientExample implements ChatListener {
    
    ChatReader cr;
    
    public ClientExample(ChatReader cr) {
        this.cr = cr;
        cr.addListener(this);
        cr.start();
        cr.sendMessage("priklad posilani zpravy", "esl_lol");
    }

    @Override
    public void onMessage(Message message) {
        System.out.println(message.getMsg());
    }

}
