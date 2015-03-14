/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

/**
 *
 * @author Václav
 */
public interface ChatListener { //Implementuji tridy, ktere chteji dostavat zpravy z chatu

    public void onMessage(String message);
}
