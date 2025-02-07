package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.util.ArrayList;
import java.util.List;

/*Proprieta: entità figlia di CasellaAcquistabile che gestisce le proprietà*/
public class Proprieta extends CasellaAcquistabile {
    private int tassaPerSerieCompleta;
    private ColoreEnumeration colore;
    private int prezzoCostruzione;
    private Albergo albergo = null;
    private List<Casa> caseSullaProprietà;

    // int prezzoCostruzione,
    // int prezzoVendita
    public Proprieta(String nomeCasella, int prezzoAcquisto, int tassa,
            ColoreEnumeration colore, int tassaPerSerieCompleta, int prezzoCostruzione, int prezzoVendita) {
        super(prezzoAcquisto, tassa, prezzoVendita, nomeCasella);
        this.tassaPerSerieCompleta = tassaPerSerieCompleta;
        this.colore = colore;
        this.prezzoCostruzione = prezzoCostruzione;
        this.caseSullaProprietà = new ArrayList<>(4);
    }

    /* metodi get e set per la tassa per serie completa */
    public int getTassaPerSerieCompleta() {
        return this.tassaPerSerieCompleta;
    }

    public void setTassaPerSerieCompleta(int tassaPerSerieCompleta) {
        this.tassaPerSerieCompleta = tassaPerSerieCompleta;
    }
    /* metodi get e set per la tassa per serie completa */

    /* metodi get e set per il colore della proprietà */
    public ColoreEnumeration getColore() {
        return this.colore;
    }

    public void setColore(ColoreEnumeration colore) {
        this.colore = colore;
    }
    /* metodi get e set per il colore della proprietà */

    /*
     * metodi get e set per il prezzo di costruzione di case e alberghi sulla
     * casella
     */
    public int getPrezzoCostruzione() {
        return this.prezzoCostruzione;
    }

    public void setPrezzoCostruzione(int prezzoCostruzione) {
        this.prezzoCostruzione = prezzoCostruzione;
    }
    /*
     * metodi get e set per il prezzo di costruzione di case e alberghi sulla
     * casella
     */

    /* metodi get e set per l'albergo */
    public Albergo getAlbergo() {
        return this.albergo;
    }

    public void setAlbergo(Albergo a) {
        this.albergo = a;
    }
    /* metodi get e set per l'albergo */

    /* metodi get e set per le case sulla proprietà */
    public List<Casa> getCaseSullaProprieta() {
        return this.caseSullaProprietà;
    }

    public void setCaseSullaProprieta(List<Casa> caseSullaProprietà) {
        this.caseSullaProprietà = caseSullaProprietà;
    }
    /* metodi get e set per le case sulla proprietà */

    /* metodi per aggiungere e rimuovere una specifica casa dalla lista */
    public void aggiungiCasaSullaProprieta(Casa c) {
        this.caseSullaProprietà.add(c);
        c.setProprietaCostruzione(this);
    }

    public void rimuoviCasaSullaProprieta(Casa c) {
        this.caseSullaProprietà.remove(c);
        c.setProprietaCostruzione(null);
    }
    /* metodi per aggiungere e rimuovere una specifica casa dalla lista */

    /* metodi per aggiungere e rimuovere l'Albergo */
    public void aggiungiAlbergoSullaProprieta(Albergo a) {
        this.setAlbergo(a);
        a.setProprietaCostruzione(this);
    }

    public void rimuoviAlbergoSullaProprieta(Albergo a) {
        this.setAlbergo(null);
        a.setProprietaCostruzione(null);
    }
    /* metodi per aggiungere e rimuovere l'Albergo */

    /* metodo per verificare che la proprietà sia vuota */
    public boolean isVuota() {
        if (this.getCaseSullaProprieta().size() == 0 && this.getAlbergo() == null) {
            return true;
        } else {
            return false;
        }
    }
    /* metodo per verificare che la proprietà sia vuota */

    /* metodo per verificare la serie completa */
    public boolean verificaSerieCompleta(Partita partita, Player player) {
        ColoreEnumeration coloreProprieta = this.getColore();
        // Conta le proprietà del colore specificato
        long conteggio = player.getProprietaPossedute().stream()
                .filter(p -> p.getColore() == coloreProprieta)
                .count();
        int numeroRichiestoPerSerieCompleta = partita.serieCompleta(partita).getOrDefault(coloreProprieta, 0);
        boolean haSerieCompleta = false;
        if (conteggio >= numeroRichiestoPerSerieCompleta) {
            haSerieCompleta = true;
        }
        return haSerieCompleta;
    }
    /* metodo per verificare la serie completa */

    /* metodo per verificare il numero di costruzioni sulla proprietà */
    public int costruzioniSullaProprieta() {
        int costruzioniSullaProprieta = this.getCaseSullaProprieta().size();
        if (this.getAlbergo() != null) {
            costruzioniSullaProprieta += 1;
        }
        return costruzioniSullaProprieta;
    }
    /* metodo per verificare il numero di costruzioni sulla proprietà */
}