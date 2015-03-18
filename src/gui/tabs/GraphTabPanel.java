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

/**
 *
 * @author Václav
 */
public class GraphTabPanel extends AbstractTabPanel {

    final int PADDING = 30;
    
    long startTime;
    int lastMinute = 0;
    ArrayDeque<Integer> totalCount;
    int dataPoints = 10;
    int secondsPerPoint;

    public GraphTabPanel(ChatReader cr, int seconds) {
        super(cr);
        startTime = System.currentTimeMillis();
        totalCount = new ArrayDeque<>();
        for (int i = 0; i < dataPoints; i++) {
            totalCount.addFirst(0);
        }
        secondsPerPoint = seconds;
    }

    private int getMinute() {
        long delta = System.currentTimeMillis() - startTime;
        return (int) (delta / 1000 / secondsPerPoint);
    }

    @Override
    public void onMessage(Message message) {
        if (getMinute() > lastMinute) {
            totalCount.addLast(1);
            if (totalCount.size() > dataPoints) {
                totalCount.removeFirst();
            }
            lastMinute = getMinute();
        } else {
            totalCount.addLast(totalCount.pollLast() + 1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        Integer[] arr = totalCount.toArray(new Integer[0]);
        int max = 0;
        for (Integer n : arr) {
            max = Math.max(max, n);
        }
        int width = getWidth() / arr.length;
        for (int i = 0; i < arr.length; i++) {
            int h = (int) (getHeight() * (((double) arr[i]) / max));
            g.setColor(Color.black);
            g.fillRect(i * width, getHeight() - h, width, h);
            g.setColor(Color.white);
            g.drawString(arr[i] + "", i * width + 5, getHeight() - h + 15);
        }
        g.setColor(Color.red);
    }

    @Override
    public void onUserMessage(Message message) {
        //kdyz se zavola cr.sendMessage
    }
}
