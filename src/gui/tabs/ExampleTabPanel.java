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
import java.util.ArrayDeque;
import java.util.Date;

/**
 *
 * @author Václav
 */
public class ExampleTabPanel extends AbstractTabPanel {

    String target;
    int count = 0;
    long start;
    ArrayDeque<Long> kappaQ;

    public ExampleTabPanel(ChatReader cr, String target) {
        super(cr);
        this.kappaQ = new ArrayDeque<Long>();
        this.target = target;
        this.start = System.currentTimeMillis();
    }

    @Override
    public void onMessage(Message message) {
        if (message.getMsg().contains(target)) {
            this.kappaQ.add((Long) System.currentTimeMillis());
            count++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (!kappaQ.isEmpty()) {
            while (kappaQ.getFirst() < System.currentTimeMillis() - 60000) {
                kappaQ.removeFirst();
                if (kappaQ.isEmpty()) {
                    break;
                }
            }
        }
        g.setColor(Color.black);
        g.drawString("Count of " + target + ": " + count, 50, 50);
        g.drawString("Total KPM: " + (count / ((System.currentTimeMillis() - start + 1.0) / 60000)), 50, 60);
        g.drawString("KPM last minute: " + kappaQ.size(), 50, 70);
    }

    @Override
    public void onUserMessage(Message message) {
        //kdyz se zavola cr.sendMessage
    }
}
