/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import java.util.ArrayList;

/**
 *
 * @author colander
 */
public class SessionManager {
    
    public static void main(String[] args) {
        new SessionManager();
    }
    
    ArrayList<ChatWriter> sessions;
    
    public SessionManager() {
        this.sessions = new ArrayList<>();
        this.sessions.add(new ChatWriter("sodapoppin", "sodapoppin.txt"));
    }
}
