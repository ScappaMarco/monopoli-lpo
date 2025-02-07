package it.univaq.disim.lpo.monopoli.core.datamodel;

/*Stazione: entità figlia di CasellaAcquistabile che gestisce le (al massimo) quattro stazioni*/
public class Stazione extends CasellaAcquistabile {
    private int tassaConDueStazioni;
    private int tassaConTreStazioni;
    private int tassaConQuattroStazioni;

    //int tassaConDueStazioni, int tassaConTreStazioni, int tassaConQuattroStazioni, 
    //int prezzoVendita
    public Stazione(String nomeCasella, int prezzoAcquisto, int tassa, int prezzoVendita, int tassaConDueStazioni, int tassaConTreStazioni, int tassaConQuattroStazioni) {
        super(prezzoAcquisto, tassa, prezzoVendita, nomeCasella);
        this.tassaConDueStazioni = tassaConDueStazioni;
        this.tassaConTreStazioni = tassaConTreStazioni;
        this.tassaConQuattroStazioni = tassaConQuattroStazioni;
    }

    /*metodi get e set per la tassa con due stazioni*/
    public int getTassaConDueStazioni() {
        return this.tassaConDueStazioni;
    }

    public void setTassaConDueStazioni(int tassaConDueStazioni) {
        this.tassaConDueStazioni = tassaConDueStazioni;
    }
    /*metodi get e set per la tassa con due stazioni*/

    /*metodi get e set per la tassa con tre stazioni*/
    public int getTassaConTreStazioni() {
        return this.tassaConTreStazioni;
    }

    public void setTassaConTreStazioni(int tassaConTreStazioni) {
        this.tassaConTreStazioni = tassaConTreStazioni;
    }
    /*metodi get e set per la tassa con tre stazioni*/

    /*metodi get e set per la tassa con quattro stazioni*/
    public int getTassaConQuattroStazioni() {
        return this.tassaConQuattroStazioni;
    }

    public void setTassaConQuattroStazioni(int tassaConQuattroStazioni) {
        this.tassaConQuattroStazioni = tassaConQuattroStazioni;
    }
    /*metodi get e set per la tassa con quattro stazioni*/

    public int verificaNumeroStazioni(Partita partita) {
        int stazioniPossedute = 0;
        if(partita.getGiocatoreDiTurno().getStazioniPossedute().size() == 0) {
            stazioniPossedute = 0;
        } else if(partita.getGiocatoreDiTurno().getStazioniPossedute().size() == 1) {
            stazioniPossedute = 1;
        } else if(partita.getGiocatoreDiTurno().getStazioniPossedute().size() == 2) {
            stazioniPossedute = 2;
        } else if(partita.getGiocatoreDiTurno().getStazioniPossedute().size() == 3) {
            stazioniPossedute = 3;
        } else if(partita.getGiocatoreDiTurno().getStazioniPossedute().size() == 4) {
            stazioniPossedute = 4;
        }
        return stazioniPossedute;
    }
}