package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;

/*Carta: entità che serve per gestire le carte imprevisti e probabilità*/
public class Carta implements Serializable{

    private TipoCartaEnumeration tipoCarta; //imprevisto o probabilità
    private String effetto;
    private CategoriaCarteEnumeration categoriaCarta;
    private Partita partita;
    private int sommaDaPagare;
    private int sommaDaRiscuotere;
    private Casella nuovaDestinazione;

    public Carta(TipoCartaEnumeration tipoCarta, String effetto, CategoriaCarteEnumeration categoriaCarta, Partita partita, int sommaDaPagare, int sommaDaRiscuotere, Casella nuovaDestinazione) {
        this.tipoCarta = tipoCarta;
        this.effetto = effetto;
        this.categoriaCarta = categoriaCarta;
        this.partita = partita;
        this.sommaDaPagare = sommaDaPagare;
        this.sommaDaRiscuotere = sommaDaRiscuotere;
        this.nuovaDestinazione = nuovaDestinazione;
    }

    /*metodi get e set per il tipo della carta(imprevisto o probabiloità)*/
    public TipoCartaEnumeration getTipoCarta() {
        return this.tipoCarta;
    }

    public void setTipoCarta(TipoCartaEnumeration tipoCarta) {
        this.tipoCarta = tipoCarta;
    }
    /*metodi get e set per il tipo della carta(imprevisto o probabiloità)*/

    /*metodi get e set per l'effetto della carta*/
    public String getEffetto() {
        return this.effetto;
    }

    public void setEffetto(String effetto) {
        this.effetto = effetto;
    }
    /*metodi get e set per l'effetto della carta*/

    /*metodi get e set per la partita*/
    public Partita getPartita() {
        return this.partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }
    /*metodi get e set per la partita*/

    /*metodi get e set per la categoria della carta*/
    public CategoriaCarteEnumeration getCategoriaCarta() {
        return this.categoriaCarta;
    }

    public void setCategoriaCarta(CategoriaCarteEnumeration categoriaCarta) {
        this.categoriaCarta = categoriaCarta;
    }
    /*metodi get e set per la categoria della carta*/

    public int getSommaDaPagare() {
        return this.sommaDaPagare;
    }

    public void setSommaDaPagare(int sommaDaPagare) {
        this.sommaDaPagare = sommaDaPagare;
    }

    public int getSommaDaRiscuotere() {
        return this.sommaDaRiscuotere;
    }

    public void setSommaDaRiscuotere(int sommaDaRiscuotere) {
        this.sommaDaRiscuotere = sommaDaRiscuotere;
    }

    public Casella getNuovaDestinazione() {
        return this.nuovaDestinazione;
    }

    public void setNuovaDestinazione(Casella nuovaDestinazione) {
        this.nuovaDestinazione = nuovaDestinazione;
    }
}