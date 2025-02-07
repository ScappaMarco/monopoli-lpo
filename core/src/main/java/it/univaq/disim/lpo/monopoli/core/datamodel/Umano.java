package it.univaq.disim.lpo.monopoli.core.datamodel;

import org.fusesource.jansi.Ansi.Color;

/*Umano: entitò figlia di Player che rappresenterà il giocatore reale*/
public class Umano extends Player {

    public Umano(String nomeGiocaore, Partita partita, int bilancio, Color color) {
        super(nomeGiocaore, bilancio, partita, color);
    }
}