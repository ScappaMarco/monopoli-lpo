package it.univaq.disim.lpo.monopoli.core.datamodel;

import org.fusesource.jansi.Ansi.Color;

/*Cpu: entità figlia di Player che rappresenterà il giocatore gestito dal computer*/
public class Cpu extends Player {
    public Cpu(String nomeGiocaore, Partita partita, int bilancio, Color color) {
        super(nomeGiocaore, bilancio, partita, color);
    }
}