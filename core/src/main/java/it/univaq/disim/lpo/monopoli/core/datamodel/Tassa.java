package it.univaq.disim.lpo.monopoli.core.datamodel;

/*Tassa: entità figlia di CasellaNonAcquistabile che gestisce le (al massimo) due tasse*/
public class Tassa extends CasellaNonAcquistabile {
    private int prezzoTassa;

    public Tassa(String nomeCasella, int prezzoTassa) {
        super(nomeCasella);
        this.prezzoTassa = prezzoTassa;
    }

    /*metodi get e set per il prezzo della tassa*/
    public int getPrezzoTassa() {
        return this.prezzoTassa;
    }

    public void setPrezzoTassa(int prezzoTassa) {
        this.prezzoTassa = prezzoTassa;
    }
    /*metodi get e set per il prezzo della tassa*/
}