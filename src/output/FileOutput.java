package output;

import datamining.TwitchApiReader;
import input.Message;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author Colander
 */
public class FileOutput {

    public static void write(String s[], String address) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(address, true))) {
            //System.out.println("Writing to " + address);
            for (int i = 0; i < s.length; i++) {
                bw.write(s[i]);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            System.err.println("Write failed.");
        }
    }

    public static void writeMessages(ArrayList<Message> msgs, String address) {
        final String SEPARATOR = "|";
        System.out.println(System.currentTimeMillis() + " WRITING TO: " + address);
        new File(address).getParentFile().mkdirs();
        boolean first = !new File(address).exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(address, true))) {
            if (first) {
                TwitchApiReader api = new TwitchApiReader();
                bw.write(api.getViewerCount(msgs.get(0).getChannel()) + SEPARATOR + api.getGame(msgs.get(0).getChannel()));
                bw.newLine();
            };
            for (int i = 0; i < msgs.size(); i++) {
                Message msg = msgs.get(i);
                bw.write(msg.getTime() + SEPARATOR + msg.getChannel() + SEPARATOR + msg.getUser() + SEPARATOR + msg.getMsg());
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Write failed.");
        }
    }
}
