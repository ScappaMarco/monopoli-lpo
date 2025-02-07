package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.PrigioneETransito;
import it.univaq.disim.lpo.monopoli.core.service.CasellaNonAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;

public class PrigioneETransitoServiceImpl implements CasellaNonAcquistabileService<PrigioneETransito> {

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " si trova sulla casella prigione e transito: "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella())
                .reset());

        if (partita.getGiocatoreDiTurno().IsInPrigione()) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " si trova in Prigione.")
                    .reset());
        } else {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " si trova nella casella di Transito")
                .reset());
        }
        return null;

    }

}
