/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import input.ChatListener;
import input.ChatReader;

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
        cr.sendMessage("priklad posilani zpravy");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

}
