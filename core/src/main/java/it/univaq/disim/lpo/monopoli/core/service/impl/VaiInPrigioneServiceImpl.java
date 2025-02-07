package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.VaiInPrigione;
import it.univaq.disim.lpo.monopoli.core.service.CasellaNonAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;

public class VaiInPrigioneServiceImpl implements CasellaNonAcquistabileService<VaiInPrigione> {

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " si trova sulla casella \"vai in prigionre\": "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella())
                .reset());

        VaiInPrigione v = (VaiInPrigione) partita.getGiocatoreDiTurno().getPosizioneGiocatore();
        v.inCarcere(partita);
        partita.getGiocatoreDiTurno().setInPrigione(true);
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " andra' adesso in prigione ")
                .reset());
        return null;
    }

}
