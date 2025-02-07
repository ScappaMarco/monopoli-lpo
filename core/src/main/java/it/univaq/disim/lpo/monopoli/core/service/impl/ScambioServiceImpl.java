package it.univaq.disim.lpo.monopoli.core.service.impl;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.service.ScambioService;
import it.univaq.disim.lpo.monopoli.core.service.exception.CategoriaCasellaVuotaException;

public class ScambioServiceImpl implements ScambioService {

    @Override
    public Player scegliGiocatoreScambio(Partita partita) {
        Integer[] indiciGiocatori = new Integer[partita.getListaGiocatori().size()];
        for (int i = 0; i < partita.getListaGiocatori().size(); i++) {
            if (!(partita.getListaGiocatori().get(i).equals(partita.getGiocatoreDiTurno()))) {
                indiciGiocatori[i] = i;
            }
        }
        System.out.println(
                "Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", a chi vuoi proporre lo scambio?");
        partita.getListaGiocatori().stream()
                .filter(p -> !p.equals(partita.getGiocatoreDiTurno()))
                .forEach(p -> System.out.println("\t seleziona " + partita.getListaGiocatori().indexOf(p)
                        + " per scegliere: " + p.getNomeGiocatore()));
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", immetti il numero dell'indice (esattamente come scritto sopra) del Giocatore con cui vuoi scambiare")
                .reset());
        System.out.print(":> ");
        int giocatoreScelto = CommandLineSingleton.getInstance().readIntegerUntilPossibleValue(indiciGiocatori);

        Player player = partita.getListaGiocatori().get(giocatoreScelto);
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha proposto uno scambio al Giocatore "
                        + player.getNomeGiocatore())
                .reset());
        return player;
    }

    @Override
    public CasellaAcquistabile scegliCasellaDaScambiare(Partita partita, Player player)
            throws CategoriaCasellaVuotaException {
        if (partita.getGiocatoreDiTurno().getCasellePossedute().isEmpty()) {
            throw new CategoriaCasellaVuotaException("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " non puo' effettuare Scambi dato che al momento non possiede alcuna Casella Acquistabile");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", scegli quale delle tue Caselle vuoi Scambiare con il Giocatore "
                        + player.getNomeGiocatore())
                .reset());
        Integer[] indiciCaselle = new Integer[partita.getGiocatoreDiTurno().getCasellePossedute().size()];
        for (int i = 0; i < partita.getGiocatoreDiTurno().getCasellePossedute().size(); i++) {
            indiciCaselle[i] = i;
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Scegli tra: ")
                .reset());
        partita.getGiocatoreDiTurno().getCasellePossedute().stream()
                .forEach(p -> System.out
                        .println("\t seleziona " + partita.getGiocatoreDiTurno().getCasellePossedute().indexOf(p)
                                + " per scegliere: " + p.getNomeCasella()));

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", immetti il numero dell'indice (esattamente come scritto sopra) della Casella che vuoi Scambiare (Casella in uscita)")
                .reset());
        System.out.print(":> ");
        int sceltaCasella = CommandLineSingleton.getInstance().readIntegerUntilPossibleValue(indiciCaselle);

        CasellaAcquistabile casellaAcquistabile = partita.getGiocatoreDiTurno().getCasellePossedute()
                .get(sceltaCasella);
        return casellaAcquistabile;
    }

    @Override
    public CasellaAcquistabile scegliCasellaDaRicevere(Partita partita, Player player)
            throws CategoriaCasellaVuotaException {
        if (player.getCasellePossedute().isEmpty()) {
            throw new CategoriaCasellaVuotaException("Il Giocatore " + player.getNomeGiocatore()
                    + " non puo' effettuare Scambi dato che al momento non possiede alcuna Casella Acquistabile");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", scegli quale casella vuoi Ricevere dallo scambio con il Giocatore "
                        + player.getNomeGiocatore())
                .reset());
        Integer[] indiciCaselle = new Integer[player.getCasellePossedute().size()];
        for (int i = 0; i < player.getCasellePossedute().size(); i++) {
            indiciCaselle[i] = i;
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Scegli tra: ")
                .reset());
        player.getCasellePossedute().stream()
                .forEach(p -> System.out.println("\t seleziona " + player.getCasellePossedute().indexOf(p)
                        + " per selezionare " + p.getNomeCasella()));
                    
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", immetti il numero dell'indice (esattamente come scritto sopra) della Casella che vuoi Ricevere (Casella in entrata)")
                .reset());
        System.out.print(":> ");
        int scleta = CommandLineSingleton.getInstance().readIntegerUntilPossibleValue(indiciCaselle);
            
        CasellaAcquistabile casellaAcquistabile = player.getCasellePossedute().get(scleta);
        return casellaAcquistabile;
    }
}