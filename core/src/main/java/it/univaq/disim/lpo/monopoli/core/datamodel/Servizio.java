package it.univaq.disim.lpo.monopoli.core.datamodel;

/*Servizio: entità figlia di CasellaAcquistabile che gestisce i (al massimo) due servizi*/
public class Servizio extends CasellaAcquistabile {
    private int tassaConDueServizi;

    //int tassaConDueServizi, int prezzoAcquisto, int tassa, int prezzoVendita, String nomeCasella
    public Servizio(String nomeCasella, int prezzoAcquisto, int tassa, int tassaConDueServizi, int prezzoVendita) {
        super(prezzoAcquisto, tassa, prezzoVendita, nomeCasella);
        this.tassaConDueServizi = tassaConDueServizi;
    }

    /*metdi get e set per la tassa con due servizi*/
    public int getTassaConDueServizi() {
        return this.tassaConDueServizi;
    }

    public void setTassaConDueServizi(int tassaConDueServizi) {
        this.tassaConDueServizi = tassaConDueServizi;
    }
    /*metdi get e set per la tassa con due servizi*/

    public int verificaNumeroServizi(Partita partita) {
        int serviziPosseduti = 0;
        if(partita.getGiocatoreDiTurno().getServiziPosseduti().size() == 0) {
            serviziPosseduti = 0;
        } else if(partita.getGiocatoreDiTurno().getServiziPosseduti().size() == 1) {
            serviziPosseduti = 1;
        } else if(partita.getGiocatoreDiTurno().getServiziPosseduti().size() == 2) {
            serviziPosseduti = 2;
        }
        return serviziPosseduti;
    }
}