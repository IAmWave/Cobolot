/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import input.ChatReader;

/**
 *
 * @author Václav
 */
public class Launcher {
    public static void main(String[] args){
        //misto ClientExample pak dame nejaky to klikatko
        ChatReader cr = new ChatReader();
        ClientExample client = new ClientExample(cr);
    }
}
