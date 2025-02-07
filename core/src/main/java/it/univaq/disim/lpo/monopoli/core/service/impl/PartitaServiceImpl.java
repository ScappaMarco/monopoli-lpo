package it.univaq.disim.lpo.monopoli.core.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import it.univaq.disim.lpo.monopoli.core.datamodel.Carta;
import it.univaq.disim.lpo.monopoli.core.datamodel.Casella;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Cpu;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.datamodel.Tabellone;
import it.univaq.disim.lpo.monopoli.core.datamodel.Umano;
import it.univaq.disim.lpo.monopoli.core.service.CasellaAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.CasellaService;
import it.univaq.disim.lpo.monopoli.core.service.PartitaService;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.ScambioService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.ActionEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AlbergoOCasaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.ModeEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.exception.CategoriaCasellaVuotaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.GiocatoreInPrigioneException;
import it.univaq.disim.lpo.monopoli.core.service.exception.InizializzaPartitaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaConAlbergoException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaConPocheCaseException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaConTutteLeCaseException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaSenzaCostruzioniException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SaldoInsufficienteException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SerieCompletaException;
import it.univaq.disim.lpo.monopoli.core.service.impl.factoryes.CasellaAcquistabileServiceFactory;
import it.univaq.disim.lpo.monopoli.core.service.impl.factoryes.CasellaServiceFactory;
import it.univaq.disim.lpo.monopoli.core.service.impl.factoryes.PlayerServiceFactory;

public class PartitaServiceImpl implements PartitaService {

    @Override
    public void gioca(Partita partita) {
        while (true) {
            // verifica se è il primo turno della partita...
            if (partita.getTurniPartita() == 0) {
                System.out.println("");
                System.out.println("----------------INIZIO PARTITA------------------");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // ...se lo è sorteggiamo il giocatore che inizierà la partita
                partita.setGiocatoreDiTurno(this.sorteggioInizioPartita(partita));
                System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                        .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                + " comincera' la Partita " + partita.getNomePartita())
                        .reset());
            } else {
                // ...se non lo è allora stampiamo il nome del giocatore di turno
                System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                        .a("E' il turno del giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()).reset());
            }
            List<Player> listGiocatoriInizioPartita = partita.getListaGiocatori();
            Player giocatoreDiTurno = partita.getGiocatoreDiTurno();
            // stampa delle informazioni della partita tramite toString ridefinito in
            // partita
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(partita);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // selezione del service corrispondente al tipo del giocatore di turno
            PlayerService playerService = PlayerServiceFactory.getPlayerService(giocatoreDiTurno.getClass());
            ModeEnumeration mode = playerService.getMode(partita);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // caso salva
            if (mode == ModeEnumeration.SALVA) {
                System.out.println("Inserisci un identificativo per salvare la Partita");
                System.out.print(":> ");
                try {
                    String fileName = CommandLineSingleton.getInstance().readString();
                    salvaPartita(partita, fileName);
                    System.out.println(
                            "Ottime notizie: la Partita e' stata salvata correttamente sotto l'identificativo di "
                                    + fileName);
                } catch (ClassNotFoundException | CloneNotSupportedException | IOException e) {
                    System.out.println("E' stato impossibile salvare la partita. Ci scusiamo per il disagio");
                }
                return;
            }
            // caso abbandona
            if (mode == ModeEnumeration.ABBANDONA) {
                // se un giocatore abbandona non viene eseguito il comando per passare il turno
                // ma viene settato il turno in una maniera diversa
                int indiceGiocatoreDiTurno = listGiocatoriInizioPartita.indexOf(giocatoreDiTurno);
                Player giocatoreUscente = giocatoreDiTurno;
                // System.out.println(indiceGiocatoreDiTurno);
                playerService.abbandona(partita);
                // se la lista è lunga 1 allora terminiamo la partita
                if (partita.getListaGiocatori().size() == 1) {
                    System.out.println(Ansi.ansi().fg(partita.getListaGiocatori().get(0).getColoreGiocatore())
                            .a("E' rimasto un solo giocatore nella Partita, quindi "
                                    + partita.getListaGiocatori().get(0).getNomeGiocatore()
                                    + " e' il vincitore della Partita")
                            .reset());
                    return;
                }
                // ...se non lo è verifichiamo che l' indice del giocatore di turno non sia
                // maggiore o uguale della lunghezza della lista...
                if (indiceGiocatoreDiTurno >= partita.getListaGiocatori().size()) {
                    // ...se la lunghezza della lista è maggiore o uguale dell inidce del giocatore
                    // di turno allora settiamo il truno al primo giocatore della lista
                    partita.setGiocatoreDiTurno(partita.getListaGiocatori().get(0));
                } else {
                    // se invecie non lo è settiamo il turno all indice del giocatore di turno
                    partita.setGiocatoreDiTurno(partita.getListaGiocatori().get(indiceGiocatoreDiTurno));
                }
                System.out.println("Dopo l'abbandono della Partita del Giocatore " + giocatoreUscente.getNomeGiocatore()
                        + ", i Giocatori della Partita sono: ");
                for (Player p : partita.getListaGiocatori()) {
                    System.out.println("\t -" + Ansi.ansi().fg(p.getColoreGiocatore()).a(p.getNomeGiocatore()).reset());
                }
            }
            // caso gioca
            if (mode == ModeEnumeration.GIOCA) {
                if (partita.getGiocatoreDiTurno().getBilancio() < 0) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " ha terminato tutti i suoi fondi, quindi sara' costretto ad abbandonare la partita")
                            .reset());
                    partita.getListaGiocatori().remove(partita.getGiocatoreDiTurno());
                }
                // visualizziamo prima di tutto le caselle possedute dal giocatore di turno
                playerService.visualizzaCasellePossedute(partita);
                // facciamo scegliere l'azione dal giocatore di turno
                ActionEnumeration action = null;
                if (giocatoreDiTurno.IsInPrigione()) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " si trova in Prigione, e dovra' provare ad uscire")
                            .reset());
                } else {
                    action = playerService.getAzione(partita);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // caso costruisci
                if (action == ActionEnumeration.COSTRUISCI) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " ha scelto di costruire")
                            .reset());
                    try { // verifichiamo se il giocatore possiede qualche proprietà
                          // facciamo selezionare al giocatore la proprietà che vuole costruire
                        Proprieta proprieta = playerService.selezionaProprieta(partita);
                        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                        + ", adesso deve scegliere se costruire una Casa oppure un Albergo")
                                .reset());
                        // facciamo scegliere al giocatore se vuole costruire una casa o un albergo
                        AlbergoOCasaEnumeration alc = playerService.getCasaOAlbergo(partita);
                        // verifica se il giocatore di turno vuole costruire una casa
                        if (alc == AlbergoOCasaEnumeration.CASA) {
                            try { // blocco try catch per le eccezioni costruisci casa
                                playerService.costruisciCasa(partita, proprieta);
                                // gestione eccezione proprieta con tutte le case
                            } catch (ProprietaConTutteLeCaseException e) {
                                System.out.println(e.getMessage());
                                //// gestione eccezione serie completa
                            } catch (SerieCompletaException s) {
                                System.out.println(s.getMessage());
                                // gestione eccezione saldo insufficiente
                            } catch (SaldoInsufficienteException y) {
                                System.out.println(y.getMessage());
                                // verifichiamo se il giocatore di turno ha la quantita necessatria di soldi per
                                // costruire
                                while (partita.getGiocatoreDiTurno().getBilancio() < proprieta.getPrezzoCostruzione()) {
                                    try {
                                        CasellaAcquistabile c = playerService.vendiCasellaAcquistabile(partita);
                                        CasellaAcquistabileService casellaAcquistabileService = CasellaAcquistabileServiceFactory
                                                .getCasellaAcquistabileService(c.getClass());
                                        casellaAcquistabileService.vendi(partita, c);
                                    } catch (CategoriaCasellaVuotaException x) {
                                        System.out.println(x.getMessage());
                                    }
                                }
                            }
                        }
                        // verifica se il giocatore di turno vuole costruire un albergo
                        if (alc == AlbergoOCasaEnumeration.ALBERGO) {
                            try { // blocco try catch per le eccezioni del metodo costruisci albergo
                                playerService.costruisciAlbergo(partita, proprieta);
                                // gestione eccezione prprietà con albergo
                            } catch (ProprietaConAlbergoException e) {
                                System.out.println(e.getMessage());
                                // gestione eccezione poche case
                            } catch (ProprietaConPocheCaseException x) {
                                System.out.println(x.getMessage());
                                // gestione eccezione serie completa
                            } catch (SerieCompletaException t) {
                                System.out.println(t.getMessage());
                                // gestione eccezione saldo insufficiente
                            } catch (SaldoInsufficienteException s) {
                                System.out.println(s.getMessage());
                            }

                        }
                        // gestione eccezione nnessuna proprietà disponibile
                    } catch (CategoriaCasellaVuotaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                // caso sambia
                if (action == ActionEnumeration.SCAMBIA) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " ha scelto di scambiare")
                            .reset());
                    // scambio service di tipo scambio service impl
                    ScambioService scambioService = new ScambioServiceImpl();
                    // scelta del giocatore con cui scambiare con player service (solo un umano puo
                    // inizializzare uno scambio)
                    Player playerScambio = scambioService.scegliGiocatoreScambio(partita);
                    // inizializzazione caselle da scambiare e da ricevere
                    CasellaAcquistabile casellaDaScambiare = null;
                    CasellaAcquistabile casellaDaRicevere = null;
                    try { // blocco try catch per scegliere la casella da scambiare
                        casellaDaScambiare = scambioService.scegliCasellaDaScambiare(partita,
                                playerScambio);
                        try { // blocco try chatch per scegliere la casella da ricevere
                            casellaDaRicevere = scambioService.scegliCasellaDaRicevere(partita, playerScambio);
                            // assegnazione dei service corrispondenti per le due caselle scelte
                            CasellaAcquistabileService cs1 = CasellaAcquistabileServiceFactory
                                    .getCasellaAcquistabileService(casellaDaScambiare.getClass());
                            CasellaAcquistabileService cs2 = CasellaAcquistabileServiceFactory
                                    .getCasellaAcquistabileService(casellaDaRicevere.getClass());
                            cs1.visualizzaInformazioni(partita, casellaDaScambiare); // visualizzazione informazioni
                                                                                     // casella da scambiare
                            // casella da scambiare
                            cs2.visualizzaInformazioni(partita, casellaDaRicevere); // visualizzazione informazioni
                                                                                    // casella da ricevere
                            // assegnazione a player service corrispondente del giocatore a cui è stato
                            // proposto lo scambio
                            PlayerService playerServiceScambio = PlayerServiceFactory
                                    .getPlayerService(playerScambio.getClass());
                            // assegnazione di una variabile booleana dell'esito dello scambio (basato sul
                            // tipo di giocatore)
                            boolean accettato = playerServiceScambio.decidiEsitoScambio(partita, playerScambio,
                                    casellaDaScambiare,
                                    casellaDaRicevere);
                            if (accettato) { // verifica se lo scambio è stato accettato
                                cs1.aggiornamentoScambioGiocatoreDiTurno(partita, playerScambio, casellaDaScambiare); // aggiornamento
                                // scambio giocatore di turno
                                cs2.aggiornamentoScambioPlayer(partita, playerScambio, casellaDaRicevere); // aggiornamento
                                                                                                           // scambio
                                                                                                           // player
                            }
                        } catch (CategoriaCasellaVuotaException c) {
                            // gestione eccezione per la casella da ricevere
                            System.out.println(c.getMessage());
                        }
                    } catch (CategoriaCasellaVuotaException e) {
                        // gestione eccezione per la casella da scambiare
                        System.out.println(e.getMessage());
                    }
                }
                // caso vendi costruzione
                if (action == ActionEnumeration.VENDICOSTRUZIONE) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " ha scelto di vendere una costruzione")
                            .reset());
                    try { // blocco try catch per la selezione della proprietà su vendere la costruzione
                        Proprieta p = playerService.selezionaProprieta(partita);
                        try { // blocco try catch per la vednita della costruzione
                            playerService.vendiCostruzione(partita, p);
                        } catch (ProprietaSenzaCostruzioniException t) {
                            // gestione eccezione proprietà denza costruzioni
                            System.out.println(t.getMessage());
                        }
                    } catch (CategoriaCasellaVuotaException e) {
                        // gestione eccezione nessuna proprietà
                        System.out.println(e.getMessage());
                    }

                }
                // caso vendi proprietà
                if (action == ActionEnumeration.VENDIPROPRIETA) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " ha scelto di vendere una proprieta'")
                            .reset());
                    try { // blocco try catch per la selezione della casella acquistabile da vendere
                        CasellaAcquistabile casellaDaVendere = playerService.vendiCasellaAcquistabile(partita);
                        CasellaAcquistabileService cas = CasellaAcquistabileServiceFactory
                                .getCasellaAcquistabileService(casellaDaVendere.getClass());
                        cas.vendi(partita, casellaDaVendere);
                    } catch (CategoriaCasellaVuotaException c) {
                        // gestione eccezione nessuna casella acquistabile
                        System.out.println(c.getMessage());
                    }
                }
                // caso muovi (azione = null)
                if (action == null) {
                    System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                            .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                    + " non eseguira' nessuna azione speciale")
                            .reset());
                }
                // movimento garantito dopo ognuna delle azioni eseguite sopra
                System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                        .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                + " eseguira' adesso la sua mossa")
                        .reset());
                try { // blocco try catch gestione movimento
                    playerService.muoviti(partita);
                    // selezione del service appropriato in base alla casella corrente del giocatore
                    Casella nuovaPosizione = partita.getGiocatoreDiTurno().getPosizioneGiocatore();
                    CasellaService casellaService = CasellaServiceFactory.getCasellaService(nuovaPosizione.getClass());
                    // il metodo get azione è dinamico in base al tipo di service richiamato
                    AzioniCasellaEnumeration azioneCasella = casellaService.getAzioneCasella(partita, playerService);

                    // dichiarazione variabile casella acquistabile service in previsione se la
                    // casella corrente è una casella acquistabile
                    CasellaAcquistabileService casellaAcquistabileService = CasellaAcquistabileServiceFactory
                            .getCasellaAcquistabileService(nuovaPosizione.getClass());
                    if (azioneCasella == null) {
                        // casella non acquistabile
                    } else {
                        // casella acquistabile
                        /*
                         * azione casella è diverso da null se e
                         * solo se viene chiamato il metodo su
                         * una casella acquistabile, altrimenti
                         * è null
                         */
                        if (azioneCasella == AzioniCasellaEnumeration.COMPRA) {
                            // blocco try catch per la gestione del metodo acquista casella
                            do {
                                try {
                                    casellaAcquistabileService.acquistaCasella(partita,
                                            (CasellaAcquistabile) nuovaPosizione);
                                    casellaAcquistabileService.aggiornaTassa(partita, giocatoreDiTurno,
                                            (CasellaAcquistabile) nuovaPosizione);
                                } catch (SaldoInsufficienteException e) {
                                    try { // blocco try catch per la gestione della casella acquistabile da vendere
                                        CasellaAcquistabile ca = playerService.vendiCasellaAcquistabile(partita);
                                        casellaAcquistabileService.vendi(partita, ca);
                                    } catch (CategoriaCasellaVuotaException x) {
                                        // gestione eccezione casella da vendere mancante
                                        System.out.println("Il Giocatore " + giocatoreDiTurno.getNomeGiocatore()
                                                + " non ha piu' alcuna Casella da vendere, verra' quindi fatto uscire dalla partita");
                                        playerService.abbandona(partita);
                                        System.out.println(x.getMessage());
                                        break;
                                    }
                                }
                            } while (giocatoreDiTurno.getBilancio() < ((CasellaAcquistabile) nuovaPosizione)
                                    .getPrezzoAcquisto());
                        }
                        if (azioneCasella == AzioniCasellaEnumeration.NONLIBERA) {
                            do {
                                // blocco try catch per la gestione del pagamento della cassa
                                try {
                                    casellaAcquistabileService.pagaTassa(partita,
                                            (CasellaAcquistabile) nuovaPosizione);
                                    // gestione eccezione saldo insufficiente
                                } catch (SaldoInsufficienteException e1) {
                                    try {
                                        CasellaAcquistabile c = playerService.vendiCasellaAcquistabile(partita);
                                        casellaAcquistabileService.vendi(partita, c);
                                    } catch (CategoriaCasellaVuotaException x) {
                                        System.out.println("Il Giocatore " + giocatoreDiTurno.getNomeGiocatore()
                                                + " non ha piu' alcuna Casella da vendere, verra' quindi fatto uscire dalla partita");
                                        playerService.abbandona(partita);
                                        System.out.println(x.getMessage());
                                        break;
                                        // ...
                                    }
                                }
                            } while (giocatoreDiTurno.getBilancio() < ((CasellaAcquistabile) nuovaPosizione)
                                    .getTassa());
                        }
                        if (azioneCasella == AzioniCasellaEnumeration.TUA) {
                            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                                    .a("La Casella " + nuovaPosizione.getNomeCasella() + " e' di tua proprieta'")
                                    .reset());
                        }
                        // giocatore non interessato all acquisto della casella
                        if (azioneCasella == AzioniCasellaEnumeration.NONCOMPRA) {
                            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                                            + " non vuole comprare la Casella" + nuovaPosizione.getNomeCasella())
                                    .reset());
                        }
                    }
                    // gestione eccezione giocatore in prigione
                } catch (GiocatoreInPrigioneException e) {
                    System.out.println(e.getMessage());
                    try {
                        playerService.tentaDiUscireDiPrigione(partita);
                    } catch (SaldoInsufficienteException e1) {
                        System.out.println(e1.getMessage());
                    }
                }
                // passaggio turno
                System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                        .a("ATTENZIONE: digitare (1) per passare il turno al prossimo giocatore").reset());
                System.out.print(":> ");
                int prossimoTurno = CommandLineSingleton.getInstance()
                        .readIntegerUntilPossibleValue(new Integer[] { 1 });
                System.out.println("----------------PASSA TURNO------------------");
                passaTurno(partita);
            }
            partita.setTurniPartita(partita.getTurniPartita() + 1);
        }

    }

    @Override
    public Player sorteggioInizioPartita(Partita partita) {
        System.out.print("Stiamo effettuando un sorteggio per decidere chi comincera' la Partita");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(".");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Random rand = new Random();
        int indexStartPlayer = rand.nextInt(0, partita.getListaGiocatori().size());
        Player startPlayer = partita.getListaGiocatori().get(indexStartPlayer);
        return startPlayer;
    }

    @Override
    public void setPosizioneInizialeGiocatori(Partita partita) {
        Casella c = partita.getTabellone().getCaselleTabellone().get(0); // via
        partita.getListaGiocatori().stream()
                .forEach(p -> p.setPosizioneGiocatore(c));
    }

    @Override
    public Partita inizializzaPartita() throws InizializzaPartitaException {
        // inizializzazione libreria stampe colorate
        AnsiConsole.systemInstall();
        int bilancioInizialeGiocatore = 500;

        // inizializzazione lista
        List<Player> listaGiocatori = new ArrayList<>();

        // inizializzazione della partita (tabellone: ancora null, verrà inizializzato
        // in seguito)
        Partita partita = new Partita("TEMP_p", null, true);

        System.out.println("Benvenuto! Giochiamo a Monopoli");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Inanzitutto, seleziona: ");
        System.out.println("\t(1) se vuoi giocare una nuova Partita");
        System.out.println("\t(2) se vuoi caricare una Partita esistente");
        System.out.print(":> ");
        Integer primaAzione = CommandLineSingleton.getInstance().readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (primaAzione == 2) {
            while (true) {
                System.out.println("Seleziona il node del file da caricare");
                System.out.print(":> ");
                String partitaFile = CommandLineSingleton.getInstance().readString();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    return caricaPartita(partitaFile);
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (CloneNotSupportedException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (primaAzione == 1) {
            System.out.println("Adesso, specifica il numero di Giocatori (da 2 a 6 Giocatori consentiti)");
            System.out.print(":> ");
            int numeroGiocatori = CommandLineSingleton.getInstance()
                    .readIntegerUntilPossibleValue(new Integer[] { 2, 3, 4, 5, 6 });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // inizializzazione della partita in base al numero di giocatori (da 2 a 6)
            if (numeroGiocatori == 2) {
                // 2 player = max 1 bot
                System.out.println("In 2 Giocatori sono consentiti al massimo 1 Giocatori gestiti dal Computer");

                // inizializzazione giocatori
                Player g1 = new Umano("TEMP1", partita, bilancioInizialeGiocatore, Color.GREEN);
                Player g2 = null;

                System.out.println("Seleziona: ");
                System.out.println("\t(1) se vuoi giocare giocare UMANO vs UMANO");
                System.out.println("\t(2) se vuoi giocare giocare UMANO vs COMPUTER");
                System.out.print(":> ");
                // (UMANO vs UMANO) oppure (UMANO vs COMPUTER)
                int modalitaDueGiocatori = CommandLineSingleton.getInstance()
                        .readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (modalitaDueGiocatori == 1) {
                    System.out.println("La Partita sara' UMANO vs UMANO");
                    g2 = new Umano("TEMP2", partita, bilancioInizialeGiocatore, Color.RED);
                }
                if (modalitaDueGiocatori == 2) {
                    System.out.println("La Partita sara' UMANO vs COMPUTER");
                    g2 = new Cpu("TEMP2", partita, bilancioInizialeGiocatore, Color.RED);
                    System.out.println("Il Giocatore gestito dal COMPUTER e' il Giocatore 2");

                }
                partita.setG1(g1);
                partita.setG2(g2);

                // set nomi giocatori
                System.out.println(Ansi.ansi().fg(partita.getG1().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 1"));
                System.out.print(":> ");
                g1.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG2().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 2"));
                System.out.print(":> ");
                g2.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // aggiunta giocatori alla lista
                listaGiocatori.add(g1);
                listaGiocatori.add(g2);
            }
            if (numeroGiocatori == 3) {
                // 3 player = max 1 bot
                System.out.println("In 3 Giocatori sono consentiti al massimo 1 Giocatori gestiti dal Computer");

                // inizializzazione giocatori
                Player g1 = new Umano("TEMP1", partita, bilancioInizialeGiocatore, Color.GREEN);
                Player g2 = new Umano("TEMP2", partita, bilancioInizialeGiocatore, Color.RED);
                Player g3 = null;

                System.out.println("Seleziona: ");
                System.out.println("\t(1) se vuoi giocare UMANO vs UMANO vs UMANO");
                System.out.println("\t(2) se vuoi giocare UMANO vs UMANO vs COMPUTER");
                System.out.print(":> ");

                // (UMANO vs UMANO vs COMPUTER) oppurer (UMANO vs UMANO vs UMANO)
                int modalitaTreGiocatori = CommandLineSingleton.getInstance()
                        .readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (modalitaTreGiocatori == 1) {
                    System.out.println("La Partita sara' UMANO vs UMANO vs UMANO");
                    g3 = new Umano("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                }
                if (modalitaTreGiocatori == 2) {
                    System.out.println("La Partita sara' UMANO vs UMANO vs COMPUTER");
                    g3 = new Cpu("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                    System.out.println("Il Giocatore gestito dal COMPUTER e' il Giocatore 3");
                }
                partita.setG1(g1);
                partita.setG2(g2);
                partita.setG3(g3);

                // set nomi giocatori
                System.out.println(Ansi.ansi().fg(partita.getG1().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 1"));
                System.out.print(":> ");
                g1.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG2().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 2"));
                System.out.print(":> ");
                g2.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG3().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 3"));
                System.out.print(":> ");
                g3.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // aggiunta giocatori alla lista
                listaGiocatori.add(g1); // fisso
                listaGiocatori.add(g2); // fisso
                listaGiocatori.add(g3); // dinamico
            }
            if (numeroGiocatori == 4) {
                // 4 player = max 2 bot
                System.out.println("In 4 Giocatori sono consentiti al massimo 2 Giocatori gestiti dal Computer");

                // inizializzazione giocatori
                Player g1 = new Umano("TEMP1", partita, bilancioInizialeGiocatore, Color.GREEN);
                Player g2 = new Umano("TEMP2", partita, bilancioInizialeGiocatore, Color.RED);
                Player g3 = null;
                Player g4 = null;

                // (UMANO vs UMANO vs UMANO vs UMANO), (UMANO vs UMANO vs UMANO vs COMPUTER)
                // oppure (UMANO vs UMANO vs COMPUTER vs COMPUTER)
                System.out.println(
                        "Inserisci il numero di Giocatori gestiti dal computer (massimo 2 in una partita da 4 Giocatori)");
                System.out.print(":> ");
                int numeroComputer = CommandLineSingleton.getInstance()
                        .readIntegerUntilPossibleValue(new Integer[] { 0, 1, 2 });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (numeroComputer == 0) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs UMANO");
                    g3 = new Umano("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                    g4 = new Umano("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                }
                if (numeroComputer == 1) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs COMPUTER");
                    g3 = new Umano("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                    g4 = new Cpu("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    System.out.println("Il Giocatore gestito dal COMPUTER e' il Giocatore 4");
                }
                if (numeroComputer == 2) {
                    System.out.println("La partita sara' UMANO vs UMANO vs COMPUTER vs COMPUTER");
                    g3 = new Cpu("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                    g4 = new Cpu("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    System.out.println("I Giocatori gestiti dal COMPUTER sono il Giocatore 3 e il Giocatore 4");
                }
                partita.setG1(g1);
                partita.setG2(g2);
                partita.setG3(g3);
                partita.setG4(g4);

                // set nomi giocatori
                System.out.println(Ansi.ansi().fg(partita.getG1().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 1"));
                System.out.print(":> ");
                g1.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG2().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 2"));
                System.out.print(":> ");
                g2.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG3().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 3"));
                System.out.print(":> ");
                g3.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG4().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 4"));
                System.out.print(":> ");
                g4.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // aggiunta giocatori alla lista
                listaGiocatori.add(g1);
                listaGiocatori.add(g2);
                listaGiocatori.add(g3);
                listaGiocatori.add(g4);
            }
            if (numeroGiocatori == 5) {
                // 5 player = max 2 bot
                System.out.println("In 5 Giocatori sono consentiti al massimo 2 Giocatori gestiti dal Computer");

                // inizializzazione giocatori
                Player g1 = new Umano("TEMP1", partita, bilancioInizialeGiocatore, Color.GREEN);
                Player g2 = new Umano("TEMP2", partita, bilancioInizialeGiocatore, Color.RED);
                Player g3 = new Umano("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                Player g4 = null;
                Player g5 = null;

                // (UMANO vs UMANO vs UMANO vs UMANO vs UMANO), (UMANO vs UMANO vs UMANO vs
                // UMANO vs COMPUTER), oppure (UMANO vs UMANO vs UMANO vs COMPUTER vs COMPUTER)
                System.out.println(
                        "Inserisci il numero di Giocatori gestiti dal Computer (massimo 2 in una partita da 5 Giocatori)");
                System.out.print(":> ");
                int numeroComputer = CommandLineSingleton.getInstance()
                        .readIntegerUntilPossibleValue(new Integer[] { 0, 1, 2 });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (numeroComputer == 0) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs UMANO vs UMANO");
                    g4 = new Umano("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Umano("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                }
                if (numeroComputer == 1) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs UMANO vs COMPUTER");
                    g4 = new Umano("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Cpu("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                    System.out.println("Il Giocatore gestito dal COMPUTER e' il Giocatore 5");
                }
                if (numeroComputer == 2) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs COMPUTER vs COMPUTER");
                    g4 = new Cpu("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Cpu("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                    System.out.println("I Giocatori gestiti dal COMPUTER sono il Giocatore 4 e il Giocatore 5");
                }
                partita.setG1(g1);
                partita.setG2(g2);
                partita.setG3(g3);
                partita.setG4(g4);
                partita.setG5(g5);

                // set nomi giocatori
                System.out.println(Ansi.ansi().fg(partita.getG1().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 1"));
                System.out.print(":> ");
                g1.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG2().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 2"));
                System.out.print(":> ");
                g2.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG3().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 3"));
                System.out.print(":> ");
                g3.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG4().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 4"));
                System.out.print(":> ");
                g4.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG5().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 5"));
                System.out.print(":> ");
                g5.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // aggiunta giocatori alla lista
                listaGiocatori.add(g1);
                listaGiocatori.add(g2);
                listaGiocatori.add(g3);
                listaGiocatori.add(g4);
                listaGiocatori.add(g5);

            }
            if (numeroGiocatori == 6) {
                // 6 player = max 3 bot
                System.out.println("In 6 Giocatori sono consentiti al massimo 3 Giocatori gestiti dal Computer");

                // inizializzazionme giocatori
                Player g1 = new Umano("TEMP1", partita, bilancioInizialeGiocatore, Color.GREEN);
                Player g2 = new Umano("TEMP2", partita, bilancioInizialeGiocatore, Color.RED);
                Player g3 = new Umano("TEMP3", partita, bilancioInizialeGiocatore, Color.MAGENTA);
                Player g4 = null;
                Player g5 = null;
                Player g6 = null;

                // (UMANO vs UMANO vs UMANO vs UMANO vs UMANO vs UMANO), (UMANO vs UMANO vs
                // UMANO vs UMANO vs UMANO vs COMPUTER), (UMANO vs UMANO vs UMANO vs UMANO vs
                // COMPUTER vs COMPUTER) oppure (UMANO vs UMANO vs UMANO vs COMPUTER vs COMPUTER
                // vs COMPUTER)
                System.out.println(
                        "Inserisci il numero di Giocatori gestiti dal Computer (massimo 3 in una partita da 6 Giocatori)");
                System.out.print(":> ");
                int numeroComputer = CommandLineSingleton.getInstance()
                        .readIntegerUntilPossibleValue(new Integer[] { 0, 1, 2, 3 });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (numeroComputer == 0) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs UMANO vs UMANO vs UMANO");
                    g4 = new Umano("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Umano("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                    g6 = new Umano("TEMP6", partita, bilancioInizialeGiocatore, Color.CYAN);
                }
                if (numeroComputer == 1) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs UMANO vs UMANO vs COMPUTER");
                    g4 = new Umano("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Umano("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                    g6 = new Cpu("TEMP6", partita, bilancioInizialeGiocatore, Color.CYAN);
                    System.out.println("Il Giocatore gestito dal COMPUTER e' il Giocatore 6");
                }
                if (numeroComputer == 2) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs UMANO vs COMPUTER vs COMPUTER");
                    g4 = new Umano("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Cpu("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                    g6 = new Cpu("TEMP6", partita, bilancioInizialeGiocatore, Color.CYAN);
                    System.out.println("I Giocatori gestiti dal COMPUTER sono il Giocatore 5 e il Giocatore 6");
                }
                if (numeroComputer == 3) {
                    System.out.println("La partita sara' UMANO vs UMANO vs UMANO vs COMPUTER vs COMPUTER vs COMPUTER");
                    g4 = new Cpu("TEMP4", partita, bilancioInizialeGiocatore, Color.BLUE);
                    g5 = new Cpu("TEMP5", partita, bilancioInizialeGiocatore, Color.YELLOW);
                    g6 = new Cpu("TEMP6", partita, bilancioInizialeGiocatore, Color.CYAN);
                    System.out.println(
                            "I Giocatori gestiti dal COMPUTER sono il Giocatore 4, il Giocatore 5 e il Giocatore 6");
                }
                partita.setG1(g1);
                partita.setG2(g2);
                partita.setG3(g3);
                partita.setG4(g4);
                partita.setG5(g5);
                partita.setG6(g6);

                // set nomi giocatori
                System.out.println(Ansi.ansi().fg(partita.getG1().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 1"));
                System.out.print(":> ");
                g1.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG2().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 2"));
                System.out.print(":> ");
                g2.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG3().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 3"));
                System.out.print(":> ");
                g3.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG4().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 4"));
                System.out.print(":> ");
                g4.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG5().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 5"));
                System.out.print(":> ");
                g5.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Ansi.ansi().fg(partita.getG6().getColoreGiocatore())
                        .a("Immetti il nome per il Giocatore 6"));
                System.out.print(":> ");
                g6.setNomeGiocatore(CommandLineSingleton.getInstance().readString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // aggiunta giocatori alla lista
                listaGiocatori.add(g1);
                listaGiocatori.add(g2);
                listaGiocatori.add(g3);
                listaGiocatori.add(g4);
                listaGiocatori.add(g5);
                listaGiocatori.add(g6);
            }
            // set nome della partita
            System.out.println(Ansi.ansi().fg(Color.DEFAULT)
                    .a("Immetti il nome per la partita attuale"));
            System.out.print(":> ");
            partita.setNomePartita(CommandLineSingleton.getInstance().readString());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // set lista giocatori
            partita.setListaGiocatori(listaGiocatori);

            // generazione (e set incluso) del tabellone dinamico
            generaTabellone(numeroGiocatori, partita);

            System.out.println("Nome Partita: " + partita.getNomePartita() + ". Numero di Giocatori: " + numeroGiocatori
                    + " . Numero Caselle Tabellone: " + partita.getTabellone().getCaselleTabellone().size());

            // creazione (e set incluso) delle liste imprevisti e probabilità
            generaMazziImprevistiEProbabilita(partita);

            // setta la posizione iniziale di tutti i giocatori al Via (casella 0)
            setPosizioneInizialeGiocatori(partita);
            partita.setTurniPartita(0);

            System.out.println("La Partita comincera' tra");
            System.out.println("3");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return partita;
        } else
            throw new InizializzaPartitaException();
    }

    @Override
    public void salvaPartita(Partita p, String filename)
            throws CloneNotSupportedException, IOException, ClassNotFoundException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(p);
        oos.close();
    }

    @Override
    public Partita caricaPartita(String filename)
            throws CloneNotSupportedException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Partita partita = (Partita) ois.readObject();
        ois.close();
        return partita;
    }

    @Override
    public void generaTabellone(int numeroGiocatori, Partita partita) {
        Tabellone tabellone = partita.creaTabellone(numeroGiocatori);
        partita.setTabellone(tabellone);
    }

    public void generaMazziImprevistiEProbabilita(Partita partita) {
        // imprevisti
        List<Carta> imprevisti = partita.creaListaImprevisti();
        partita.setImprevisti(imprevisti);
        // probabilità
        List<Carta> probabilita = partita.creaListaProbabilita();
        partita.setProbabilita(probabilita);
    }

    @Override
    public void passaTurno(Partita partita) {
        List<Player> listaGiocatori = partita.getListaGiocatori();
        Player giocatoreDiTurno = partita.getGiocatoreDiTurno();

        // Trova l'indice del giocatore di turno attuale
        int indiceAttuale = listaGiocatori.indexOf(giocatoreDiTurno);

        // Calcola il prossimo indice del giocatore di turno
        int prossimoIndice = (indiceAttuale + 1) % listaGiocatori.size();

        // Imposta il nuovo giocatore di turno
        partita.setGiocatoreDiTurno(listaGiocatori.get(prossimoIndice));
    }
}