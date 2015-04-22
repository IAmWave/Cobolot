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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Václav
 */
public class WordsPanel extends AbstractTabPanel {

    final static String[] IGNORED = {"the", "a", "an",
        "i", "me", "you", "u", "he", "that", "them", "this", "it",
        "your", "his",
        "is", "are",
        "of", "and", "to"};
    final static String SKIPPED = "[']";
    final static int COMPRESSED_WIDTH = 130;
    final static int PADDING = 30;
    final static boolean FILTERING = true;

    HashMap<String, Integer> map;
    int dataPoints = 15;
    int seconds;
    ArrayDeque<Message> messages;

    public WordsPanel(ChatReader cr, int seconds) {
        super(cr);
        this.seconds = seconds;
        messages = new ArrayDeque<>();
        map = new HashMap<>();
    }

    @Override
    public void onMessage(Message message) {
        handleMessage(message, true);
        messages.addFirst(message);
        while (!messages.isEmpty()
                && messages.peekLast().getTime() + seconds * 1000 < System.currentTimeMillis()) {
            //odstranovat stare
            handleMessage(messages.pollLast(), false);
        }
    }

    private void handleMessage(Message message, boolean adding) {
        String[] words = message.getMsg().replaceAll(SKIPPED, "").split("\\P{Alnum}+");
        Set<String> wordSet = new HashSet<>();
        for (int i = 0; i < words.length; i++) {
            wordSet.add(words[i]);
        }
        Iterator<String> it = wordSet.iterator();
        while (it.hasNext()) {
            String cur = it.next().toLowerCase();
            if (cur.equals("")) continue;
            if (FILTERING) {
                boolean ok = true;
                for (int j = 0; j < IGNORED.length; j++) {
                    if (cur.equals(IGNORED[j])) ok = false;
                }
                if (!ok) continue;
            }
            int occurences = (map.containsKey(cur)) ? map.get(cur) : 0;
            if (occurences > 1 || adding) {
                map.put(cur, occurences + (adding ? 1 : (-1)));
            } else {
                map.remove(cur);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        HashMap.Entry[] entries = map.entrySet().toArray(new HashMap.Entry[0]);
        Arrays.sort(entries, new WordsComparator());
        int max = 0;
        int total = 0;
        int cumulative = 0;
        for (int i = 0; i < entries.length; i++) {
            max = Math.max(max, (Integer) entries[i].getValue());
            total += (int) entries[i].getValue();
        }

        //g.drawString("Messages in last "+seconds + " s: "+messages.size(), 20, 20);
        //g.drawString("Unique words: "+entries.length, 20, 35);
        int height = getHeight() / dataPoints;
        for (int i = 0; i < dataPoints; i++) {
            if (i == entries.length) break;
            cumulative += (int) entries[i].getValue();
            int w = (int) (getWidth() * (((double) (Integer) entries[i].getValue()) / max));
            g.setColor(Color.black);
            g.fillRect(0, i * height, w, height);
            int percentage = (int) (100 * ((double) cumulative / total));
            if (w > COMPRESSED_WIDTH) {
                g.setColor(Color.white);
                g.drawString(entries[i].getKey() + "", 5, i * height + 13);
                g.drawString(entries[i].getValue() + " (" + percentage + "%)",
                        w - 50, i * height + 13);
            } else {
                g.drawString(entries[i].getKey() + "   " + entries[i].getValue() + " (" + percentage + "%)",
                        w + 5, i * height + 13);
            }

        }

        g.setColor(Color.red);
        int percentage = (int) (100 * ((double) cumulative / total));
        g.drawString("showing " + cumulative + " of " + total + " ("
                + percentage + "%)", getWidth() - 150, getHeight() - 25);
        g.drawString("messages in last " + seconds + " s: " + messages.size(), getWidth() - 150, getHeight() - 5);
    }

    @Override
    public void onUserMessage(Message message) {
        //kdyz se zavola cr.sendMessage
    }

    private static class WordsComparator implements Comparator<HashMap.Entry> {

        @Override
        public int compare(HashMap.Entry l, HashMap.Entry r) {
            return -((Integer) l.getValue()).compareTo((Integer) r.getValue());
        }
    }
}
