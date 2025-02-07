package it.univaq.disim.lpo.monopoli.core.datamodel;

/*CasellaNonAcquistabile: entità figlia di casella che gestisce le caselle che non possno essere comprate, ha sei figli: PrigioneETransito, Tassa, Via, CasellaConCarta, ParcheggioGratuito e VaiInPrigione*/
public abstract class CasellaNonAcquistabile extends Casella{
    public CasellaNonAcquistabile(String nomeCasella) {
        super(nomeCasella);
    }
}
