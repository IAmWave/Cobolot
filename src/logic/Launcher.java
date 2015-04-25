/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import gui.GUIFrame;
import input.ChatReader;

/**
 *
 * @author Václav
 */
public class Launcher {

    public static void main(String[] args) {
        ChatReader cr = new ChatReader("sodapoppin", false);
        new GUIFrame(cr).setVisible(true);
    }
}
