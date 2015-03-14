/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import input.ChatListener;

/**
 *
 * @author Václav
 */
public class ClientExample implements ChatListener {

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
    
}
