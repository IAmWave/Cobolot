/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author colander
 */
public class TwitchApiReader {

    final String API_LIST_ADDRESS = "https://api.twitch.tv/kraken/streams";
    final String NAME_START = "\"name\":\"";
    final String NAME_END = "\"";

    public TwitchApiReader() {

    }

    public String getViewerCount(String channel) {
        String page = getPage("https://api.twitch.tv/kraken/streams?channel=" + channel);
        int index = page.indexOf("viewers");
        return page.substring(index + 9, page.indexOf("created") - 2);
    }

    public String getGame(String channel) {
        String page = getPage("https://api.twitch.tv/kraken/streams?channel=" + channel);
        int index = page.indexOf("game");
        return page.substring(index + 7, page.indexOf("viewers") - 3);
    }

    public String[] getTopChannels(int count) {
        String[] out = new String[count];
        String input = "";
        try {
            URL obj = new URL(this.API_LIST_ADDRESS);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            input = in.readLine();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < count; i++) {
            input = input.substring(input.indexOf(NAME_START) + NAME_START.length());
            out[i] = input.substring(0, input.indexOf(NAME_END));
        }
        return out;
    }

    public String getPage(String url) {
        String input = "";
        //long dat = System.currentTimeMillis();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            input = in.readLine();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(System.currentTimeMillis() - dat + " " + url);
        return input;
    }
}
