package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Stazione;
import it.univaq.disim.lpo.monopoli.core.service.CasellaAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaDiProprietaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaGiaPossedutaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SaldoInsufficienteException;

public class StazioneServiceImpl implements CasellaAcquistabileService<Stazione> {

    @Override
    public void aggiornaTassa(Partita partita, Player player, Stazione stazione) {
        if (stazione.verificaNumeroStazioni(partita) == 0) {
            stazione.setTassa(0);
        } else if (stazione.verificaNumeroStazioni(partita) == 1) {
            stazione.setTassa(stazione.getTassa());
        } else if ((stazione.verificaNumeroStazioni(partita)) == 2) {
            player.getStazioniPossedute().stream().forEach(s -> s.setTassa(s.getTassaConDueStazioni()));
        } else if (stazione.verificaNumeroStazioni(partita) == 3) {
            player.getStazioniPossedute().stream().forEach(s -> s.setTassa(s.getTassaConTreStazioni()));
        } else if (stazione.verificaNumeroStazioni(partita) == 4) {
            player.getStazioniPossedute().stream().forEach(s -> s.setTassa(s.getTassaConQuattroStazioni()));
        }
    }

    @Override
    public void acquistaCasella(Partita partita, Stazione stazione)
            throws SaldoInsufficienteException {
        if (partita.getGiocatoreDiTurno().getBilancio() < stazione.getPrezzoAcquisto()) {
            throw new SaldoInsufficienteException(
                    "Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non ha abbastanza finanze per acquistare la Stazione "
                            + stazione.getNomeCasella());
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di comprare la Stazione "
                        + stazione.getNomeCasella() + " al prezzo di "
                        + stazione.getPrezzoAcquisto())
                .reset());

        // settaggio proprietario
        stazione.setProprietario(partita.getGiocatoreDiTurno());

        // sottrazione dei soldi
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        - stazione.getPrezzoAcquisto());

        // aggiunta alle liste
        partita.getGiocatoreDiTurno().aggiungiCasellaAcquistabile(stazione);
        partita.getGiocatoreDiTurno().aggiungiStazionePosseduta(stazione);

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a(partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' il nuovo proprietario della Stazione "
                        + stazione.getNomeCasella())
                .reset());

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il nuovo saldo del Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' di "
                        + partita.getGiocatoreDiTurno().getBilancio())
                .reset());
    }

    @Override
    public void aggiornamentoScambioGiocatoreDiTurno(Partita partita, Player player, Stazione stazioneDaScambiare) {
        partita.getGiocatoreDiTurno().getStazioniPossedute().remove(stazioneDaScambiare);
        player.getStazioniPossedute().add(stazioneDaScambiare);
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), stazioneDaScambiare);
    }

    public void aggiornamentoScambioPlayer(Partita partita, Player player, Stazione stazioneDaRicevere) {
        partita.getGiocatoreDiTurno().getStazioniPossedute().add(stazioneDaRicevere);
        player.getStazioniPossedute().remove(stazioneDaRicevere);
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), stazioneDaRicevere);
    }

    @Override
    public void vendi(Partita partita, Stazione stazioneDaVendere) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di vendere una Stazione: "
                        + stazioneDaVendere.getNomeCasella())
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " guadagnera' una cifra di " + stazioneDaVendere.getPrezzoVendita()
                        + " dalla vendita")
                .reset());
        partita.getGiocatoreDiTurno().getStazioniPossedute().remove(stazioneDaVendere);
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        + stazioneDaVendere.getPrezzoVendita());
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), stazioneDaVendere);
    }

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " si trova su una stazione: "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella())
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il prezzo per l'acquisto della Stazione e' di "
                        + ((CasellaAcquistabile) partita.getGiocatoreDiTurno().getPosizioneGiocatore())
                                .getPrezzoAcquisto()
                        + ", ed il Saldo del Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " e' di "
                        + partita.getGiocatoreDiTurno().getBilancio())
                .reset());
        AzioniCasellaEnumeration mode = null;
        try {
            verificaSeLibera(partita, (CasellaAcquistabile) partita.getGiocatoreDiTurno().getPosizioneGiocatore());
            mode = playerService.getAzioneCasella();
        } catch (CasellaGiaPossedutaException e) {
            System.out.println(e.getMessage());
            mode = AzioniCasellaEnumeration.NONLIBERA;
        } catch (CasellaDiProprietaException c) {
            mode = AzioniCasellaEnumeration.TUA;
        }
        return mode;
    }

    @Override
    public void visualizzaInformazioni(Partita partita, Stazione stazione) {
        System.out.println("Informazioni sulla Stazione " + stazione.getNomeCasella() + ": ");
        System.out.println("\t prezzo d'acquisto: " + stazione.getPrezzoAcquisto());
        System.out.println("\t tassa: " + stazione.getTassa());
    }
}