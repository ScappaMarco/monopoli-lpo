package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;

/*Sacmbio: entità utile per gestire lo scambio tra due proprietà e tra due giocatori*/
public class Scambio implements Serializable{
    private boolean esitoScambio;
    private Player giocatore1;
    private Player giocatore2;

    public Scambio(Player giocatore1, Player giocatore2) {
        this.giocatore1 = giocatore1;
        this.giocatore2 = giocatore2;
    }

    /*metodi get e set per l'esito dello scambio*/
    public boolean isEsitoScambio() {
        return this.esitoScambio;
    }

    public void setEsitoScambio(boolean esitoScambio) {
        this.esitoScambio = esitoScambio;
    }
    /*metodi get e set per l'esito dello scambio*/

    /*metodi get e set per il giocatore 1(il giocatore 1 è quello che inizializza lo scambio)*/
    public Player getGiocatore1() {
        return this.giocatore1;
    }

    public void setGiocatore1(Player giocatore1) {
        this.giocatore1 = giocatore1;
    }
    /*metodi get e set per il giocatore 1(il giocatore 1 è quello che inizializza lo scambio)*/

    /*metodi get e set per il giocatore 2(il giocatore 2 è quello che riceve lo scambio)*/
    public Player getGiocatore2() {
        return this.giocatore2;
    }

    public void setGiocatore2(Player giocatore2) {
        this.giocatore2 = giocatore2;
    }
    /*metodi get e set per il giocatore 2(il giocatore 2 è quello che riceve lo scambio)*/
}