package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;

/*Casa: entità che gestisce le costruzioni di topo albergo (massimo 1 per proprieta)*/
public class Albergo implements Serializable{
    private Player proprietario;
    private Proprieta proprietaCostruzione;

    public Albergo(Player proprietario, Proprieta proprietaCostruzione) {
        this.proprietario = proprietario;
        this.proprietaCostruzione = proprietaCostruzione;
    }

    /*metodi get e set per la proprietà dell'albergo*/
    public Proprieta getProprietaCostruzione() {
        return this.proprietaCostruzione;
    }

    public void setProprietaCostruzione(Proprieta proprietaCostruzione) {
        this.proprietaCostruzione = proprietaCostruzione;
    }
    /*metodi get e set per la proprietà dell'albergo*/

    /*metodi get e set per il proprietario dell'albergo*/
    public Player getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(Player proprietario) {
        this.proprietario = proprietario;
    }
    /*metodi get e set per il proprietario dell'albergo*/
}