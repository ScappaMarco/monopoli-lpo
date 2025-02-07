package it.univaq.disim.lpo.monopoli.core.datamodel;

/*VaiInPrigione: entità figlia di CasellaNonAcquistabile che gestisce la casella che manda in prigione*/
public class VaiInPrigione extends CasellaNonAcquistabile {
    public VaiInPrigione(String nomeCasella) {
        super(nomeCasella);
    }

    public void inCarcere(Partita partita) {
        partita.getGiocatoreDiTurno().setPosizioneGiocatore(partita.getTabellone().getCaselleTabellone()
                .get(partita.getPosizionePrigioneETransito(partita)));
    }

}
