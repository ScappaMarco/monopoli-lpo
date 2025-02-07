package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Albergo;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.service.CasellaAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaDiProprietaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaGiaPossedutaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SaldoInsufficienteException;

public class ProprietaServiceImpl implements CasellaAcquistabileService<Proprieta> {

    @Override
    public void aggiornaTassa(Partita partita, Player player, Proprieta proprieta) {
        if (proprieta.verificaSerieCompleta(partita, player)) {
            player.getProprietaPossedute().stream()
                    .filter(p -> p.getColore() == proprieta.getColore())
                    .forEach(p -> p.setTassa(p.getTassaPerSerieCompleta()));
        }
        // verifichiamo che sia presente un albergo...
        if (proprieta.getAlbergo() != null) {
            // se lo è aggiungiamo 25...
            proprieta.setTassa(proprieta.getTassa() + (15 * proprieta.getCaseSullaProprieta().size()) + 25);
        } else {
            // se non lo è non aggiungiamo niente
            proprieta.setTassa(proprieta.getTassa() + (15 * proprieta.getCaseSullaProprieta().size()) + 0);
        }
    }

    @Override
    public void acquistaCasella(Partita partita, Proprieta proprieta)
            throws SaldoInsufficienteException {
        if (partita.getGiocatoreDiTurno().getBilancio() < proprieta.getPrezzoAcquisto()) {
            throw new SaldoInsufficienteException(
                    "Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non ha abbastanza finanze per acquistare la casella "
                            + proprieta.getNomeCasella());
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di comprare la Casella "
                        + proprieta.getNomeCasella() + " al prezzo di "
                        + proprieta.getPrezzoAcquisto())
                .reset());

        // settaggio proprietario
        proprieta.setProprietario(partita.getGiocatoreDiTurno());

        // sottrazione dei soldi
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        - proprieta.getPrezzoAcquisto());

        // aggiunta alle liste
        partita.getGiocatoreDiTurno().aggiungiCasellaAcquistabile(proprieta);
        partita.getGiocatoreDiTurno().aggiungiProprietaPosseduta(proprieta);

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a(partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' il nuovo Proprietario della Casella "
                        + proprieta.getNomeCasella())
                .reset());

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il nuovo Saldo del Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' di "
                        + partita.getGiocatoreDiTurno().getBilancio())
                .reset());
    }

    @Override
    public void aggiornamentoScambioGiocatoreDiTurno(Partita partita, Player player, Proprieta proprietaDaScambiare) {
        System.out.println(
                "Attenzione: e' stata scambiata un proprieta', di conseguenza tutte le (eventuali) costruzioni su di esse verranno distrutte");
        // verifica se la proprità che è stata scambiata faceva parte di una serie
        // completa
        if (proprietaDaScambiare.verificaSerieCompleta(partita, partita.getGiocatoreDiTurno())) {
            partita.getGiocatoreDiTurno().getProprietaPossedute().stream()
                    .filter(p -> p.getColore() == proprietaDaScambiare.getColore())
                    .forEach(p -> p.getCaseSullaProprieta().clear());
            partita.getGiocatoreDiTurno().getProprietaPossedute().stream()
                    .filter(p -> p.getColore() == proprietaDaScambiare.getColore())
                    .filter(p -> p.getAlbergo() != null)
                    .forEach(p -> p.rimuoviAlbergoSullaProprieta(p.getAlbergo()));
        }
        partita.getGiocatoreDiTurno().getProprietaPossedute().remove(proprietaDaScambiare);
        player.getProprietaPossedute().add(proprietaDaScambiare);

        // aggiornamento tassa in caso la nuova proprietà abbia formato una serie
        // completa
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), proprietaDaScambiare);
    }

    public void aggiornamentoScambioPlayer(Partita partita, Player player, Proprieta proprietaDaRicevere) {

        // verifica se la proprità che è stata scambiata faceva parte di una serie
        // completa
        if (proprietaDaRicevere.verificaSerieCompleta(partita, player)) {
            // rimuviamo le eventuali case sulla proprietà...
            player.getProprietaPossedute().stream()
                    .filter(p -> p.getColore() == proprietaDaRicevere.getColore())
                    .forEach(p -> p.getCaseSullaProprieta().clear());
            // ...e l'eventuale albergo
            player.getProprietaPossedute().stream()
                    .filter(p -> p.getColore() == proprietaDaRicevere.getColore())
                    .filter(p -> p.getAlbergo() != null)
                    .forEach(p -> p.rimuoviAlbergoSullaProprieta(p.getAlbergo()));
            aggiornaTassa(partita, player, proprietaDaRicevere);
        }
        partita.getGiocatoreDiTurno().getProprietaPossedute().add(proprietaDaRicevere);
        player.getProprietaPossedute().remove(proprietaDaRicevere);
    }

    @Override
    public void vendi(Partita partita, Proprieta proprietaDaVendere) {
        if (!(proprietaDaVendere.isVuota())) {
            if (proprietaDaVendere.getAlbergo() == null) {
                System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                        .a(
                                "La Proprieta' che hai decispo di vendere ha delle costruzioni sopra di essa: verranno rimosse prima della vendita, e ti verra' restituito "
                                        + proprietaDaVendere.getPrezzoCostruzione() / 2
                                        + " per ogni costruzione su di essa (la Proprieta' "
                                        + proprietaDaVendere.getNomeCasella() + " ha: "
                                        + proprietaDaVendere.getCaseSullaProprieta().size() + " Case e nessun Albergo")
                        .reset());
            } else {
                System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                        .a(
                                "La Proprieta' che hai deciso di vendere ha delle ostruzioni sopra di essa: verranno rimosse prima della vendita, e ti verra' restituito "
                                        + proprietaDaVendere.getPrezzoCostruzione() / 2
                                        + " per ogni costruzione su di essa (la Proprieta' "
                                        + proprietaDaVendere.getNomeCasella() + " ha: "
                                        + proprietaDaVendere.getCaseSullaProprieta().size() + " Case e un Albergo")
                        .reset());
            }
            // questa parte serve per calcolare i soldi da restituire al giocatore, per ogni
            // casa costruzione presente sulla proprietà
            int valoreCostruzioni = 0;
            if (proprietaDaVendere.getAlbergo() != null) {
                valoreCostruzioni = (proprietaDaVendere.getPrezzoCostruzione() / 2) * 5;
                Albergo albergo = proprietaDaVendere.getAlbergo();
                partita.getGiocatoreDiTurno().getAlberghiPosseduti().remove(albergo);
                proprietaDaVendere.rimuoviAlbergoSullaProprieta(proprietaDaVendere.getAlbergo());
            } else {
                valoreCostruzioni = (proprietaDaVendere.getPrezzoCostruzione() / 2)
                        * proprietaDaVendere.getCaseSullaProprieta().size();
            }
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio() + valoreCostruzioni);
            // utilizzo degli stream per rimuovere le case dalla lista del giocatore
            proprietaDaVendere.getCaseSullaProprieta().stream()
                    .forEach(casa -> partita.getGiocatoreDiTurno().getCasePossedute().remove(casa));
            proprietaDaVendere.getCaseSullaProprieta().clear();
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di vendere una Proprieta': " + proprietaDaVendere.getNomeCasella())
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " guadagnera' una cifra di " + proprietaDaVendere.getPrezzoVendita()
                        + " dalla vendita della sola Proprieta', escludendo le costruzioni su di essa")
                .reset());
        partita.getGiocatoreDiTurno().getProprietaPossedute().remove(proprietaDaVendere);
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio() + proprietaDaVendere.getPrezzoVendita());
        aggiornaTassa(partita, partita.getGiocatoreDiTurno(), proprietaDaVendere);
    }


    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " si trova su una proprieta': "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella() + " di Colore "
                        + ((Proprieta) partita.getGiocatoreDiTurno().getPosizioneGiocatore()).getColore())
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il prezzo per l'acquisto della Proprieta' e' di "
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
    public void visualizzaInformazioni(Partita partita, Proprieta proprieta) {
        System.out.println("Informazioni sulla Proprieta' " + proprieta.getNomeCasella() + ": ");
        System.out.println("\t prezzo d'acquisto: " + proprieta.getPrezzoAcquisto());
        System.out.println("\t colore proprieta': " + proprieta.getColore());
        System.out.println("\t tassa: " + proprieta.getTassa());
        System.out.println("\t tassa per serie completa: " + proprieta.getTassaPerSerieCompleta());
        System.out.println("\t case presenti sulla proprieta': " + proprieta.getCaseSullaProprieta().size());
        if (proprieta.getAlbergo() != null) {
            System.out.println("\t albergo sulla proprieta': Si");
        } else {
            System.out.println("\t albergo sulla proprieta': No");
        }
    }
}