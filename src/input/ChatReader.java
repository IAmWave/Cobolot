/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Václav
 */
public class ChatReader {
    ArrayList<ChatListener> listeners;
    
    //do konstruktoru pak asi muzes dat argumenty jako channel a podobne.
    //username a heslo muzeme asi hardcodovat, to se menit nebude
    //veci ktery chat zpracovavaj bych sem nedaval
    public ChatReader(){ 
        listeners = new ArrayList<>();
    }
    
    public void start(){ //zacne cist zpravy
        //NA TENHLE PRIKLAD JSEM DAL PERIOD NA 1000 MS
        //V IMPLEMENTACI TO VRAT NA NECO ROZUMNYHO
        new Timer().scheduleAtFixedRate(new ReaderTask(), 50, 1000);
    }
    
    public void addListener(ChatListener l){
        listeners.add(l);
    }
    
    //asi bych sem dal i posilani zpravy, i kdyz je to "reader"
    //pripadne prejmenujeme jestli mas nejakej napad :D
    public void sendMessage(String message){ 
    
    }
    
    private class ReaderTask extends TimerTask{

        @Override
        public void run() {
            //tady patri to, co chces delat kazdych 50 ms - nejaky to cteni IRC.
            //nejsem si jistej jestli se to da implementovat takhle (volani metody kazdych 50 ms)
            //tak to kdyztak uprav :)
            
            //kdyz prijde zprava:
            if(true){
                for(ChatListener l : listeners){
                    l.onMessage("sem prijde skutecna zprava");
                }
            }
        }
        
    }
}
