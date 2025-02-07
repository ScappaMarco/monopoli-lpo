package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*Casella: entità che gestirà tutto ciò che riguarda le caselle, ha due figli; CasellaAcquistabile e CasellaNonAcquistabile*/
public abstract class Casella implements Serializable {
    private String nomeCasella;
    private List<Player> giocatoriSullaCasella;
    private Tabellone tabellone;

    public Casella(String nomeCasella) {
        this.nomeCasella = nomeCasella;
        this.giocatoriSullaCasella = new ArrayList<>();
    }

    /* metodi get e set per il nome della casella */
    public String getNomeCasella() {
        return this.nomeCasella;
    }

    public void setNomeCasella(String nomeCasella) {
        this.nomeCasella = nomeCasella;
    }
    /* metodi get e set per il nome della casella */

    /* metodi get e set per la lista dei giocatori sulla casella */
    public List<Player> getGiocatoriSullaCasella() {
        return this.giocatoriSullaCasella;
    }

    public void setGiocatoriSullaCasella(List<Player> giocatoriSullaCasella) {
        this.giocatoriSullaCasella = giocatoriSullaCasella;
    }
    /* metodi get e set per la lista dei giocatori sulla casella */

    /* metodi per aggiungere e rimuovere un giocatore specifico dalla casella */
    public void aggiungiGiocatoreSullaCasella(Player p) {
        this.giocatoriSullaCasella.add(p);
        p.setPosizioneGiocatore(this);
    }

    public void rimuovigiocatoreSullaCasella(Player p) {
        this.giocatoriSullaCasella.remove(p);
    }
    /* metodi per aggiungere e rimuovere un giocatore specifico dalla casella */

    /* metodi get e set per il tabellone delle casella */
    public Tabellone getTabellone() {
        return this.tabellone;
    }

    public void setTabellone(Tabellone tabellone) {
        this.tabellone = tabellone;
    }
}
