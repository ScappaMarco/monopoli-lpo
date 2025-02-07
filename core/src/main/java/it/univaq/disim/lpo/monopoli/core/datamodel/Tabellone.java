package it.univaq.disim.lpo.monopoli.core.datamodel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*Tabellone: entità che gestirà la posizione delle caselle*/
public class Tabellone implements Serializable{
    private Partita partita;
    private Map<Integer, Casella> caselleTabellone; //Casella + posizione

    public Tabellone() {
        this.caselleTabellone = new HashMap<>();
    }

    /*metodi get e set per la partita del tabellone*/
    public Partita getPartita() {
        return this.partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }
    /*metodi get e set per la partita del tabellone*/
    
    /*metodi get e set per la mappa delle caselle del tabellone dinamico*/
    public Map<Integer, Casella> getCaselleTabellone() {
        return this.caselleTabellone;
    }

    public void setCaselleTabellone(Map<Integer, Casella> mappaDaDueGiocatori) {
        this.caselleTabellone = mappaDaDueGiocatori;
    }
    /*metodi get e set per la mappa delle caselle del tabellone dinamico*/
}