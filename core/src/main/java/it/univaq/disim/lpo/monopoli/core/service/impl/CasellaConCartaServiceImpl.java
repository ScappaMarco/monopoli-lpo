package it.univaq.disim.lpo.monopoli.core.service.impl;

import java.util.Random;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Carta;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaConCarta;
import it.univaq.disim.lpo.monopoli.core.datamodel.CategoriaCarteEnumeration;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.datamodel.TipoCartaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.CasellaNonAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;

public class CasellaConCartaServiceImpl implements CasellaNonAcquistabileService<CasellaConCarta> {

    @Override
    public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " si trova su una casella con un Imprevisto o Probabilita': "
                        + partita.getGiocatoreDiTurno().getPosizioneGiocatore().getNomeCasella())
                .reset());
        CasellaConCarta c = (CasellaConCarta) partita.getGiocatoreDiTurno().getPosizioneGiocatore();
        Carta cartaPescata = null;
        if (c.getTipoCarta() == TipoCartaEnumeration.IMPREVISTO) {
            cartaPescata = partita.getImprevisti().get(0);
            // questa operazione serve a reinserire la carta all'interno della lista in
            // ultima posizione
            partita.getImprevisti().remove(cartaPescata);
            partita.getImprevisti().add(cartaPescata);
        } else {
            cartaPescata = partita.getProbabilita().get(0);
            // questa operazione serve a reinserire la carta all'interno della lista in
            // ultima posizione
            partita.getProbabilita().remove(cartaPescata);
            partita.getProbabilita().add(cartaPescata);
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("L'effetto della Carta pescata dal Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e': " + cartaPescata.getEffetto())
                .reset());
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.GUADAGNA) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " guadagnera' una somma di "
                            + cartaPescata.getSommaDaRiscuotere())
                    .reset());
            partita.getGiocatoreDiTurno()
                    .setBilancio(partita.getGiocatoreDiTurno().getBilancio() + cartaPescata.getSommaDaRiscuotere());
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.PAGA) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " paghera' una somma di "
                            + cartaPescata.getSommaDaPagare())
                    .reset());
            partita.getGiocatoreDiTurno()
                    .setBilancio(partita.getGiocatoreDiTurno().getBilancio() - cartaPescata.getSommaDaPagare());
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.SPOSTATI) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + "si spostera' adesso alla nuovaPosizione "
                            + cartaPescata.getNuovaDestinazione().getNomeCasella())
                    .reset());
            partita.getGiocatoreDiTurno().setPosizioneGiocatore(cartaPescata.getNuovaDestinazione());
            cartaPescata.getNuovaDestinazione().aggiungiGiocatoreSullaCasella(partita.getGiocatoreDiTurno());
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.ESCIDIPRIGIONE) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " ha pescato la Carta per poter uscire subito di Prigione")
                    .reset());
            partita.getGiocatoreDiTurno().setEsciDiPrigione(true);
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.DISTRUZIONE) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("C'e' stato un Attacco Nucleare! Tutte la Case ed Alberghi del Giocatore"
                            + partita.getGiocatoreDiTurno().getNomeGiocatore() + " andranno distrutte")
                    .reset());
            partita.getGiocatoreDiTurno().getCasePossedute().clear();
            partita.getGiocatoreDiTurno().getAlberghiPosseduti().clear();

            // utilizzo degli stream per "pulire" ogni proprietà dalle costruzioni
            partita.getGiocatoreDiTurno().getProprietaPossedute().stream().forEach(prop -> {
                prop.getCaseSullaProprieta().clear();
            });
            partita.getGiocatoreDiTurno().getProprietaPossedute().stream()
                    .filter(prop -> prop.getCaseSullaProprieta() != null).forEach(prop -> {
                        prop.rimuoviAlbergoSullaProprieta(prop.getAlbergo());
                    });
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.VAIINPRIGIONE) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " si spostera' in Prigione")
                    .reset());
            partita.getGiocatoreDiTurno().setPosizioneGiocatore(cartaPescata.getNuovaDestinazione());
            cartaPescata.getNuovaDestinazione().aggiungiGiocatoreSullaCasella(partita.getGiocatoreDiTurno());
            partita.getGiocatoreDiTurno().setInPrigione(true);
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.GUADAGNADAALTRIGIOCATORI) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Ogni Giocatore dovra' regalare 15 al Giocatore "
                            + partita.getGiocatoreDiTurno().getNomeGiocatore())
                    .reset());
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                    + 15 * (partita.getGiocatoreDiTurno().getPartitaAttuale().getListaGiocatori().size() - 1));
            partita.getListaGiocatori().stream()
                    .filter(p -> p != partita.getGiocatoreDiTurno())
                    .forEach(p -> p.setBilancio(p.getBilancio() - 15));
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.PAGAPERCASEPOSSEDUTE) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " dovra' pagare 15 per ogni casa che possiede")
                    .reset());
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                    - 15 * (partita.getGiocatoreDiTurno().getCasePossedute().size()));
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.GUADAGNAINBASEALDADO) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " guadagnera' 10 per ogni punto uscito sul dado")
                    .reset());
            Random dado = new Random();
            int numeroDado = dado.nextInt(1, 7);
            System.out.println("Dal Dado e' uscito il numero " + numeroDado);
            System.out.println("Il Giocatore guadagnera' quindi " + 10 * numeroDado);
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio() + (10 * numeroDado));
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.PAGAPERPROPRIETASENZAEDIFICI) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " paghera' una somma di 20 per ogni Proprieta' senza edifici")
                    .reset());
            long proprietaVuoteLong = partita.getGiocatoreDiTurno().getProprietaPossedute().stream()
                    .filter(Proprieta::isVuota)
                    .count();
            int proprietaVuote = (int) proprietaVuoteLong;
            partita.getGiocatoreDiTurno()
                    .setBilancio(partita.getGiocatoreDiTurno().getBilancio() - (20 * proprietaVuote));
        }
        if (cartaPescata.getCategoriaCarta() == CategoriaCarteEnumeration.TUTTIIGIOCATORIGUADAGNANO) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Ogni Giocatore guadagna 30")
                    .reset());
            partita.getListaGiocatori().stream().forEach(player -> player.setBilancio(player.getBilancio() + 30));
        }
        return null;
    }
}