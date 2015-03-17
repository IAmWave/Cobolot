/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tabs;

import input.ChatListener;
import input.ChatReader;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Václav
 */

//třídu budou extendovat jednotlivé taby
public abstract class AbstractTabPanel extends JPanel implements ChatListener {
     ChatReader cr;
    public AbstractTabPanel(ChatReader cr){
        cr.addListener(this);
        this.cr = cr;
    }
}
