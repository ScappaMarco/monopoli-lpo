package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;

/*Casa: entità che gestisce le costruzioni di topo casa (massimo 4 per proprieta)*/
public class Casa implements Serializable{
    private Player proprietario;
    private Proprieta proprietaCostruzione;

    public Casa(Player proprietario, Proprieta proprietaCostruzione) {
        this.proprietario = proprietario;
        this.proprietaCostruzione = proprietaCostruzione;
    }

    /*metodi get e set per il proprietario della casa*/
    public Player getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(Player proprietario) {
        this.proprietario = proprietario;
    }
    /*metodi get e set per il proprietario della casa*/

    /*metodi get e set per la proprieta dove è costruita la casa*/
    public Proprieta getProprietaCostruzione() {
        return this.proprietaCostruzione;
    }

    public void setProprietaCostruzione(Proprieta proprietaCostruzione) {
        this.proprietaCostruzione = proprietaCostruzione;
    }
    /*metodi get e set per la proprieta dove è costruita la casa*/
}
