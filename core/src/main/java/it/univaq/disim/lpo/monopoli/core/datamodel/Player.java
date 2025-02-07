package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.fusesource.jansi.Ansi.Color;

/*Player: entità che giocherà la partita, ha due figli: Umano e Cpu*/
public class Player implements Serializable {
    private String nomeGiocatore;
    private int bilancio;
    private int numeroTurniInPrigione = 0;
    private List<CasellaAcquistabile> casellePossedute;
    private List<Proprieta> proprietaPossedute;
    private List<Servizio> serviziPosseduti;
    private List<Stazione> stazioniPossedute;
    private Casella posizioneGiocatore;
    private List<Casa> casePossedute;
    private List<Albergo> alberghiPosseduti;
    private Partita partitaAttuale;
    private boolean esciSubitoDiPrigione = false;
    private boolean inPrigione = false;
    private Color color;

    public Player(String nomeGiocatore, int bilancio, Partita partita, Color color) {
        this.nomeGiocatore = nomeGiocatore;
        this.bilancio = bilancio;
        this.partitaAttuale = partita;
        this.color = color;
        this.casellePossedute = new ArrayList<>();
        this.casePossedute = new ArrayList<>();
        this.alberghiPosseduti = new ArrayList<>();
        this.proprietaPossedute = new ArrayList<>();
        this.serviziPosseduti = new ArrayList<>();
        this.stazioniPossedute = new ArrayList<>();
    }

    /* metodi get e set per il nome del giocatore */
    public String getNomeGiocatore() {
        return this.nomeGiocatore;
    }

    public void setNomeGiocatore(String nomeGiocatore) {
        this.nomeGiocatore = nomeGiocatore;
    }
    /* metodi get e set per il nome del giocatore */

    /* metodi get e set per il bilancio */
    public int getBilancio() {
        return this.bilancio;
    }

    public void setBilancio(int bilancio) {
        this.bilancio = bilancio;
    }
    /* metodi get e set per il bilancio */

    /* metodi get e set per la carta per uscire di prigione */
    public boolean IsEsciSubitoDiPrigione() {
        return this.esciSubitoDiPrigione;
    }

    public void setEsciDiPrigione(boolean esciDiPrigione) {
        this.esciSubitoDiPrigione = esciDiPrigione;
    }
    /* metodi get e set per la carta per uscire di prigione */

    /* metodi get e set per i turni in prigione */
    public void setNumeroTurniInPrigione(int numeroTurniInPrigione) {
        this.numeroTurniInPrigione = numeroTurniInPrigione;
    }

    public int getNumeroTurniInPrigione() {
        return this.numeroTurniInPrigione;
    }
    /* metodi get e set per i turni in prigione */

    /* metodi get e set per la lista delle proprieta possedute */
    public List<Proprieta> getProprietaPossedute() {
        return this.proprietaPossedute;
    }

    public void setProprietaPossedute(List<Proprieta> proprietaPossedute) {
        this.proprietaPossedute = proprietaPossedute;
    }
    /* metodi get e set per la lista delle proprieta possedute */

    /* metodi per aggiungere e rimuovere una proprieta posseduta */
    public void aggiungiProprietaPosseduta(Proprieta p) {
        this.proprietaPossedute.add(p);
        p.setProprietario(this);
    }

    public void rimuoviProprietaPosseduta(Proprieta p) {
        this.proprietaPossedute.remove(p);
        p.setProprietario(null);
    }
    /* metodi per aggiungere e rimuovere una proprieta posseduta */

    /* metodi get e set per la lista dei servizi */
    public List<Servizio> getServiziPosseduti() {
        return this.serviziPosseduti;
    }

    public void setServiziPosseduti(List<Servizio> listaServizi) {
        this.serviziPosseduti = listaServizi;
    }
    /* metodi get e set per la lista dei servizi */

    /* metodi per aggiungere e rimuovere un servizio dalla lista */
    public void aggiungiServizioPosseduto(Servizio s) {
        this.serviziPosseduti.add(s);
        s.setProprietario(this);
    }

    public void rimuoviServizioPosseduto(Servizio s) {
        this.serviziPosseduti.remove(s);
        s.setProprietario(null);
    }
    /* metodi per aggiungere e rimuovere un servizio dalla lista */

    /* metodi get e set per la lista delle stazioni */
    public List<Stazione> getStazioniPossedute() {
        return this.stazioniPossedute;
    }

    public void setStazioniPossedute(List<Stazione> listaStazioni) {
        this.stazioniPossedute = listaStazioni;
    }
    /* metodi get e set per la lista delle stazioni */

    /* metodi per aggiungere e rimuovere una stazione dalla lista */
    public void aggiungiStazionePosseduta(Stazione s) {
        this.stazioniPossedute.add(s);
        s.setProprietario(this);
    }

    public void rimuoviStazionePosseduta(Stazione s) {
        this.stazioniPossedute.remove(s);
        s.setProprietario(null);
    }
    /* metodi per aggiungere e rimuovere una stazione dalla lista */

    /* metodi get e set per le proprieta possedute */
    public List<CasellaAcquistabile> getCasellePossedute() {
        return this.casellePossedute;
    }

    public void setCasellePossedute(List<CasellaAcquistabile> casellePossedute) {
        this.casellePossedute = casellePossedute;
    }
    /* metodi get e set per le proprieta possedute */

    /* metodi per aggiungere e rimuovere dalla lista uno specifico oggetto */
    public <T extends CasellaAcquistabile> void aggiungiCasellaAcquistabile(T t) {
        this.casellePossedute.add(t);
        t.setProprietario(this);
    }

    public void rimuoviCasellaAcquistabile(CasellaAcquistabile casellaAcquistabile) {
        this.casellePossedute.remove(casellaAcquistabile);
        casellaAcquistabile.setProprietario(null);
    }
    /* metodi per aggiungere e rimuovere dalla lista uno specifico oggetto */

    /* metodi get e set per la posizione del giocatore */
    public Casella getPosizioneGiocatore() {
        return this.posizioneGiocatore;
    }

    public void setPosizioneGiocatore(Casella c) {
        this.posizioneGiocatore = c;
    }
    /* metodi get e set per la posizione del giocatore */

    /* metodi get e set per la lista della case possedute */
    public List<Casa> getCasePossedute() {
        return this.casePossedute;
    }

    public void setCasePossedute(List<Casa> casepossedute) {
        this.casePossedute = casepossedute;
    }
    /* metodi get e set per la lista della case possedute */

    /* metodi per aggiungere e rimuovere una casa */
    public void aggiungiCasaPosseduta(Casa c) {
        this.casePossedute.add(c);
        c.setProprietario(this);
    }

    public void rimuoviCasaPosseduta(Casa c) {
        this.casePossedute.remove(c);
        c.setProprietario(null);
    }
    /* metodi per aggiungere e rimuovere una casa */

    /* metodi get e set per la lista degli alberghi posseduti */
    public List<Albergo> getAlberghiPosseduti() {
        return this.alberghiPosseduti;
    }

    public void setAlberghiPosseduti(List<Albergo> alberghiPosseduti) {
        this.alberghiPosseduti = alberghiPosseduti;
    }
    /* metodi get e set per la lista degli alberghi posseduti */

    /* metodi per aggiungere e rimuovere un albergo posseduto */
    public void aggiungiAlbergoPosseduto(Albergo a) {
        this.alberghiPosseduti.add(a);
        a.setProprietario(this);
    }

    public void rimuoviAlbergoPosseduto(Albergo a) {
        this.alberghiPosseduti.remove(a);
        a.setProprietario(null);
    }
    /* metodi per aggiungere e rimuovere un albergo posseduto */

    /* metodi get e set per la partita attuale del giocatore */
    public Partita getPartitaAttuale() {
        return this.partitaAttuale;
    }

    public void setPartitaAttuale(Partita p) {
        this.partitaAttuale = p;
    }
    /* metodi get e set per la partita attuale del giocatore */

    /* metodi get e set per la verifica che il giocatore sia in prigione */
    public boolean IsInPrigione() {
        return this.inPrigione;
    }

    public void setInPrigione(boolean b) {
        this.inPrigione = b;
    }
    /* metodi get e set per la verifica che il giocatore sia in prigione */

    /* metodi per il get e set del colore del giocatore */
    public Color getColoreGiocatore() {
        return this.color;
    }

    public void setColoreGiocatore(Color color) {
        this.color = color;
    }
    /* metodi per il get e set del colore del giocatore */
}