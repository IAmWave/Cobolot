/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

/**
 *
 * @author Lenovo
 */
public class Message {

    private String channel;
    private String user;
    private String msg;
    private long time;
    public Message(String channel, String user, String msg) {
        this.channel = channel;
        this.user = user;
        this.msg = msg;
        time = System.currentTimeMillis();
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    public long getTime() {
        return time;
    }
}
