package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Tassa;
import it.univaq.disim.lpo.monopoli.core.service.CasellaNonAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;

public class TassaServiceImpl implements CasellaNonAcquistabileService<Tassa> {

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " e' finito su una tassa: "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella())
                .reset());
                //cast concordato
        Tassa tassa = (Tassa) partita.getGiocatoreDiTurno().getPosizioneGiocatore();
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " deve adesso pagare una somma pari a " + tassa.getPrezzoTassa())
                .reset());
        partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio() - tassa.getPrezzoTassa());
        return null;
    }
}
