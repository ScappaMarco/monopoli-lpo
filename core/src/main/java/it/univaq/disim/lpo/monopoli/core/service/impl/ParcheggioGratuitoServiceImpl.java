package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.ParcheggioGratuito;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.service.CasellaNonAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;

public class ParcheggioGratuitoServiceImpl implements CasellaNonAcquistabileService<ParcheggioGratuito>{

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
         System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " si trova sul parcheggio gratuito: "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella())
                .reset());
                return null;

    }
    
}
