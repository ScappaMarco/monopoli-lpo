package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Servizio;
import it.univaq.disim.lpo.monopoli.core.service.CasellaAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaDiProprietaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaGiaPossedutaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SaldoInsufficienteException;

public class ServizioServiceImpl implements CasellaAcquistabileService<Servizio> {

    @Override
    public void aggiornaTassa(Partita partita, Player player, Servizio servizio) {
        if (servizio.verificaNumeroServizi(partita) == 0) {
            servizio.setTassa(0);
        } else if (servizio.verificaNumeroServizi(partita) == 1) {
            servizio.setTassa(servizio.getTassa());
        } else if (servizio.verificaNumeroServizi(partita) == 2) {
            player.getServiziPosseduti().stream()
                    .forEach(p -> p.setTassa(p.getTassaConDueServizi()));
        }
    }

    @Override
    public void acquistaCasella(Partita partita, Servizio servizio)
            throws SaldoInsufficienteException {
        if (partita.getGiocatoreDiTurno().getBilancio() < servizio.getPrezzoAcquisto()) {
            throw new SaldoInsufficienteException(
                    "Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non ha abbastanza finanze per acquistare il Servizio "
                            + servizio.getNomeCasella());
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di comprare il Servizio "
                        + servizio.getNomeCasella() + " al prezzo di "
                        + servizio.getPrezzoAcquisto())
                .reset());

        // settaggio proprietario
        servizio.setProprietario(partita.getGiocatoreDiTurno());

        // sottrazione dei soldi
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        - servizio.getPrezzoAcquisto());

        // aggiunta alle liste
        partita.getGiocatoreDiTurno().aggiungiCasellaAcquistabile(servizio);
        partita.getGiocatoreDiTurno().aggiungiServizioPosseduto(servizio);

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a(partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' il nuovo proprietario del Servizio "
                        + servizio.getNomeCasella())
                .reset());

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il nuovo saldo del giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' di "
                        + partita.getGiocatoreDiTurno().getBilancio())
                .reset());
    }

    @Override
    public void aggiornamentoScambioGiocatoreDiTurno(Partita partita, Player player, Servizio servizioDaScambiare) {
        partita.getGiocatoreDiTurno().getServiziPosseduti().remove(servizioDaScambiare);
        player.getServiziPosseduti().add(servizioDaScambiare);
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), servizioDaScambiare);
    }

    public void aggiornamentoScambioPlayer(Partita partita, Player player, Servizio servizioDaRicevere) {
        partita.getGiocatoreDiTurno().getServiziPosseduti().add(servizioDaRicevere);
        player.getServiziPosseduti().remove(servizioDaRicevere);
        aggiornaTassa(partita, player, servizioDaRicevere);
    }

    @Override
    public void vendi(Partita partita, Servizio servizioDaVendere) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di vendere un Servizio: "
                        + servizioDaVendere.getNomeCasella())
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " guadagnera' una cifra di " + servizioDaVendere.getPrezzoVendita()
                        + " dalla vendita")
                .reset());
        partita.getGiocatoreDiTurno().getServiziPosseduti().remove(servizioDaVendere);
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        + servizioDaVendere.getPrezzoVendita());
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), servizioDaVendere);
    }

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " si trova su un servizio': "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore()
                                .getNomeCasella())
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il prezzo per l'acquisto del Servizio e' di "
                        + ((CasellaAcquistabile) partita.getGiocatoreDiTurno()
                                .getPosizioneGiocatore())
                                .getPrezzoAcquisto()
                        + ", ed il Saldo del Giocatore "
                        + partita.getGiocatoreDiTurno().getNomeGiocatore() + " e' di "
                        + partita.getGiocatoreDiTurno().getBilancio())
                .reset());
        AzioniCasellaEnumeration mode = null;
        try {
            verificaSeLibera(partita,
                    (CasellaAcquistabile) partita.getGiocatoreDiTurno().getPosizioneGiocatore());
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
    public void visualizzaInformazioni(Partita partita, Servizio servizio) {
        System.out.println("Informazioni sul Servizio " + servizio.getNomeCasella() + ": ");
        System.out.println("\t prezzo d'acquisto: " + servizio.getPrezzoAcquisto());
        System.out.println("\t tassa: " + servizio.getTassa());
    }
}