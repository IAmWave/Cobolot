/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tabs;

import input.ChatReader;
import input.Message;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;

/**
 *
 * @author Václav
 */
public class ExampleTabPanel extends AbstractTabPanel {

    String target;
    int count = 0;
    long start;

    public ExampleTabPanel(ChatReader cr, String target) {
        super(cr);
        this.target = target;
        this.start = System.currentTimeMillis();
    }

    @Override
    public void onMessage(Message message) {
        if (message.getMsg().contains(target)) {
            count++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.drawString("Count of " + target + ": " + count, 50, 50);
        g.drawString("KPM: " + (count / ((System.currentTimeMillis() - start +1.0) / 60000)), 50, 60);
    }
}
