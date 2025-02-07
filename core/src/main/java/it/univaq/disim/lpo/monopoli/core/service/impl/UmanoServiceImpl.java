package it.univaq.disim.lpo.monopoli.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Albergo;
import it.univaq.disim.lpo.monopoli.core.datamodel.Casa;
import it.univaq.disim.lpo.monopoli.core.datamodel.Casella;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.datamodel.Servizio;
import it.univaq.disim.lpo.monopoli.core.datamodel.Stazione;
import it.univaq.disim.lpo.monopoli.core.datamodel.Umano;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.ActionEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AlbergoOCasaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.ModeEnumeration;
import it.univaq.disim.lpo.monopoli.core.service.exception.CategoriaCasellaVuotaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.GiocatoreInPrigioneException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaConAlbergoException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaConPocheCaseException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaConTutteLeCaseException;
import it.univaq.disim.lpo.monopoli.core.service.exception.ProprietaSenzaCostruzioniException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SaldoInsufficienteException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SerieCompletaException;

public class UmanoServiceImpl implements PlayerService<Umano> {

    @Override
    public ModeEnumeration getMode(Partita partita) {
        System.out.println("Selezionare una delle seguenti azioni:");
        System.out.println("\t1 gioca");
        System.out.println("\t2 salva");
        System.out.println("\t3 abbandona");
        System.out.print(":> ");
        ModeEnumeration mode = null;
        Integer scelta = CommandLineSingleton.getInstance()
                .readIntegerUntilPossibleValue(new Integer[] { 1, 2, 3 });
        if (scelta == 1)
            mode = ModeEnumeration.GIOCA;
        if (scelta == 2)
            mode = ModeEnumeration.SALVA;
        if (scelta == 3)
            mode = ModeEnumeration.ABBANDONA;
        return mode;
    }

    @Override
    public ActionEnumeration getAzione(Partita partita) {
        System.out.println("Selezionare una delle seguanti azioni: ");
        System.out.println("\t1 scambia");
        System.out.println("\t2 costruisci");
        System.out.println("\t3 vendi costruzione");
        System.out.println("\t4 vendi proprieta");
        System.out.println("\t0 se vuoi solo fare una mossa");
        System.out.print(":> ");
        ActionEnumeration azione = null;
        Integer scelta = CommandLineSingleton.getInstance()
                .readIntegerUntilPossibleValue(new Integer[] { 0, 1, 2, 3, 4 });
        if (scelta == 1)
            azione = ActionEnumeration.SCAMBIA;
        if (scelta == 2)
            azione = ActionEnumeration.COSTRUISCI;
        if (scelta == 3)
            azione = ActionEnumeration.VENDICOSTRUZIONE;
        if (scelta == 4)
            azione = ActionEnumeration.VENDIPROPRIETA;
        if (scelta == 0)
            azione = null;
        return azione;
    }

    @Override
    public AzioniCasellaEnumeration getAzioneCasella() {
        System.out.println("Vuoi comprare la Casella?");
        System.out.println("\t1 si");
        System.out.println("\t2 no");
        System.out.print(":> ");
        AzioniCasellaEnumeration azione = null;
        Integer scelta = CommandLineSingleton.getInstance()
                .readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
        if (scelta == 1)
            azione = AzioniCasellaEnumeration.COMPRA;
        if (scelta == 2)
            azione = AzioniCasellaEnumeration.NONCOMPRA;
        return azione;
    }

    public AlbergoOCasaEnumeration getCasaOAlbergo(Partita partita) {
        System.out.println("Selezionare cosa si vuole costruire: ");
        System.out.println("\t1 casa");
        System.out.println("\t2 albergo");
        System.out.print(":> ");
        AlbergoOCasaEnumeration alc = null;
        Integer scelta = CommandLineSingleton.getInstance()
                .readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
        if (scelta == 1)
            alc = AlbergoOCasaEnumeration.CASA;
        if (scelta == 2)
            alc = AlbergoOCasaEnumeration.ALBERGO;
        return alc;
    }

    public void visualizzaCasellePossedute(Partita partita) {
        System.out.println(
                "Ecco le Caselle possedute del Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + ": ");
        if (!(partita.getGiocatoreDiTurno().getProprietaPossedute().isEmpty())) {
            System.out.println("\t Proprieta': ");
            partita.getGiocatoreDiTurno().getProprietaPossedute()
                    .stream()
                    .forEach(p -> System.out.println("\t " + p.getNomeCasella() + " (" + p.getColore() + ")"));
        } else {
            System.out.println("Il Giocatore non possiede alcuna Proprieta'.");
        }

        if (!(partita.getGiocatoreDiTurno().getServiziPosseduti().isEmpty())) {
            System.out.println("\t Servizi: ");
            partita.getGiocatoreDiTurno().getServiziPosseduti()
                    .stream()
                    .map(Servizio::getNomeCasella)
                    .forEach(nomeCasella -> System.out.println("\t " + nomeCasella));
        } else {
            System.out.println("Il Giocatore non possiede alcun Servizio.");
        }

        if (!(partita.getGiocatoreDiTurno().getStazioniPossedute().isEmpty())) {
            System.out.println("\t Stazioni: ");
            partita.getGiocatoreDiTurno().getStazioniPossedute()
                    .stream()
                    .map(Stazione::getNomeCasella)
                    .forEach(nomeCasella -> System.out.println("\t " + nomeCasella));
        } else {
            System.out.println("Il Giocatore non possiede alcuna Stazione.");
        }
    }

    @Override
    public List<Integer> lanciaDadi(Partita partita) {
        List<Integer> dadi = new ArrayList<>(2);

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " sta lanciando i dadi..."));

        // creazione oggetto random
        Random dado = new Random();

        // generazione due numeri random (a simboleggiare i due dadi)
        int primoDado = dado.nextInt(1, 7);
        dadi.add(primoDado);

        int secondoDado = dado.nextInt(1, 7);
        dadi.add(secondoDado);

        return dadi;
    }

    @Override
    public void muoviti(Partita partita) throws GiocatoreInPrigioneException {
        // player sarà sempre il giocatore di turno
        if (partita.getGiocatoreDiTurno().IsInPrigione()) {
            throw new GiocatoreInPrigioneException(
                    "Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non puo' moversi: si trova in prigione");
        }
        // get della vecchia posizione
        Casella vecchiaPosizione = partita.getGiocatoreDiTurno().getPosizioneGiocatore();
        int chiave = 0;
        // ciclo per cercare la chiave della posizione del giocatore
        for (int i = 0; i < partita.getTabellone().getCaselleTabellone().size(); i++) {
            if (partita.getTabellone().getCaselleTabellone().get(i).equals(vecchiaPosizione)) {
                chiave = i;
            }
        }
        // lista dei dadi
        List<Integer> dadi = lanciaDadi(partita);
        int numeroDadi = dadi.get(0) + dadi.get(1);
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("I valori dei dadi sono stati: " + dadi.get(0) + ", e " + dadi.get(1)
                        + " (spostarsi di "
                        + numeroDadi + ")")
                .reset());
        Casella nuovaPosizione = null;
        if ((chiave + numeroDadi) >= partita.getTabellone().getCaselleTabellone().size()) {
            // verifica di aver superato il via nel tabellone
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " e' passato dal via, gli verranno accreditati "
                            + partita.getVia().getPrezzoAccredito())
                    .reset());
            int chiavePosizione = (chiave + numeroDadi)
                    - partita.getTabellone().getCaselleTabellone().size();
            nuovaPosizione = partita.getTabellone().getCaselleTabellone().get(chiavePosizione);
            // accredito al giocatore del prezzo del via (100)
            partita.getGiocatoreDiTurno()
                    .setBilancio(partita.getGiocatoreDiTurno().getBilancio() + partita.getVia().getPrezzoAccredito());
        } else {
            // non si è arrivati alla fine del tabellone
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " non e' passato dal via.")
                    .reset());
            nuovaPosizione = partita.getTabellone().getCaselleTabellone().get(chiave + numeroDadi);
        }
        // set della nuova posizione al giocatore
        partita.getGiocatoreDiTurno().setPosizioneGiocatore(nuovaPosizione);
        // aggiornamento sui giocatori presenti sulla casella di nuova posizione
        nuovaPosizione.aggiungiGiocatoreSullaCasella(partita.getGiocatoreDiTurno());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La nuova posizione del giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " e' "
                        + nuovaPosizione.getNomeCasella())
                .reset());
    }

    @Override
    public Proprieta selezionaProprieta(Partita partita) throws CategoriaCasellaVuotaException {
        if (partita.getGiocatoreDiTurno().getProprietaPossedute().isEmpty()) {
            throw new CategoriaCasellaVuotaException(
                    "Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non possiede alcuna Proprieta'");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", scegli una delle tue Proprieta'")
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Scegli tra: ")
                .reset());
        for (Proprieta p : partita.getGiocatoreDiTurno().getProprietaPossedute()) {
            System.out.println(
                    "\t seleziona " + partita.getGiocatoreDiTurno().getProprietaPossedute().indexOf(p) + " per: "
                            + p.getNomeCasella() + " (" + p.getColore() + ")");
        }
        Integer[] indici = new Integer[partita.getGiocatoreDiTurno().getProprietaPossedute().size()];
        for (int i = 0; i < partita.getGiocatoreDiTurno().getProprietaPossedute().size(); i++) {
            indici[i] = i;
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", immetti il numero dell'indice (esattamente come scritto sopra) della Proprieta' che hai scelto")
                .reset());
        System.out.print(":> ");
        int sceltaProprieta = CommandLineSingleton.getInstance()
                .readIntegerUntilPossibleValue(indici);
        Proprieta proprieta = partita.getGiocatoreDiTurno().getProprietaPossedute().get(sceltaProprieta);
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha scelto la Proprieta': "
                        + partita.getGiocatoreDiTurno().getProprietaPossedute().get(sceltaProprieta).getNomeCasella())
                .reset());
        return proprieta;
    }

    @Override
    public void costruisciCasa(Partita partita, Proprieta proprieta)
            throws ProprietaConTutteLeCaseException, SerieCompletaException, SaldoInsufficienteException {
        if (proprieta.getCaseSullaProprieta().size() == 4) {
            throw new ProprietaConTutteLeCaseException(
                    "La Proprieta' " + proprieta.getNomeCasella()
                            + " ha gia' 4 Case al suo interno");
        }
        if (!(proprieta.verificaSerieCompleta(partita, partita.getGiocatoreDiTurno()))) {
            throw new SerieCompletaException(
                    "Non puoi costuire su questa proprieta', per poterlo fare devi avere una Serie Completa");
        }
        if (partita.getGiocatoreDiTurno().getBilancio() < proprieta.getPrezzoCostruzione()) {
            throw new SaldoInsufficienteException("Il Giocatore "
                    + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " non possiede abbastanza denaro per costruire una casa sulla proprieta'"
                    + proprieta.getNomeCasella());
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di costruire una Casa sulla Proprieta' "
                        + proprieta.getNomeCasella())
                .reset());
        Casa casa = new Casa(partita.getGiocatoreDiTurno(), proprieta);
        partita.getGiocatoreDiTurno().getCasePossedute().add(casa);
        proprieta.aggiungiCasaSullaProprieta(casa);

        // pagamento prezzo per la costruzione
        System.out.println("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " ha pagato "
                + proprieta.getPrezzoCostruzione() + " per costruire una casa sulla Proprieta' "
                + proprieta.getNomeCasella());
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        - proprieta.getPrezzoCostruzione());

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La proprieta' " + proprieta.getNomeCasella() + " ha adesso "
                        + proprieta.getCaseSullaProprieta().size() + " Case su di essa")
                .reset());
    }

    @Override
    public void costruisciAlbergo(Partita partita, Proprieta proprieta)
            throws ProprietaConAlbergoException, ProprietaConPocheCaseException, SerieCompletaException,
            SaldoInsufficienteException {
        if (proprieta.getAlbergo() != null) {
            throw new ProprietaConAlbergoException(
                    "La Proprieta' " + proprieta.getNomeCasella()
                            + " ha gia' un Albergo al suo interno");
        }
        if (proprieta.getCaseSullaProprieta().size() != 4) {
            throw new ProprietaConPocheCaseException(
                    "Non e' possibile costruire un Albergo su questa Proprieta': essa dispone di sole "
                            + proprieta.getCaseSullaProprieta().size() + " Case");
        }
        if (!(proprieta.verificaSerieCompleta(partita, partita.getGiocatoreDiTurno()))) {
            throw new SerieCompletaException(
                    "Non puoi costuire su questa proprieta', per poterlo fare devi avere una Serie Completa");
        }
        if (partita.getGiocatoreDiTurno().getBilancio() < proprieta.getPrezzoCostruzione()) {
            throw new SaldoInsufficienteException("Il BOT "
                    + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " non possiede abbastanza denaro per costruire un albergo sulla proprieta'"
                    + proprieta.getNomeCasella());
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di costruire un Albergo sulla Proprieta' "
                        + proprieta.getNomeCasella())
                .reset());
        Albergo albergo = new Albergo(partita.getGiocatoreDiTurno(), proprieta);
        partita.getGiocatoreDiTurno().getAlberghiPosseduti().add(albergo);
        proprieta.aggiungiAlbergoSullaProprieta(albergo);

        // pagamento prezzo per la costruzione
        System.out.println("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " ha pagato "
                + proprieta.getPrezzoCostruzione() + " per costruire un Albergo sulla Proprieta' "
                + proprieta.getNomeCasella());
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        - proprieta.getPrezzoCostruzione());

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La proprieta' " + proprieta.getNomeCasella() + " ha adesso un Albergo su di essa")
                .reset());
    }

    @Override
    public void vendiCostruzione(Partita partita, Proprieta proprieta) throws ProprietaSenzaCostruzioniException {
        if ((proprieta.isVuota())) {
            throw new ProprietaSenzaCostruzioniException(
                    "La proprieta' non contiene alcune costruzione da vendere");
        }
        if (proprieta.costruzioniSullaProprieta() > 4) {
            System.out.println("La Proprieta' " + proprieta.getNomeCasella()
                    + " ha un albergo su di essa, quindi quest'ultimo verra' venduto");
            Albergo albergoDaVendere = proprieta.getAlbergo();
            proprieta.rimuoviAlbergoSullaProprieta(albergoDaVendere);
        } else {
            System.out.println("La Proprieta' " + proprieta.getNomeCasella()
                    + " non ha un albergo su di essa, quindi verra' venduta una Casa");
            Casa casaDaVendere = proprieta.getCaseSullaProprieta().get(0);
            proprieta.rimuoviCasaSullaProprieta(casaDaVendere);
        }

        // accredito prezzo vendita al giocatore
        partita.getGiocatoreDiTurno()
                .setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                        + (proprieta.getPrezzoCostruzione() / 2));

        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il nuovo bilancio del Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' di "
                        + partita.getGiocatoreDiTurno().getBilancio())
                .reset());
    }

    public boolean decidiEsitoScambio(Partita partita, Player giocatore, CasellaAcquistabile casellaDaScambiare,
            CasellaAcquistabile casellaDaRicevere) {
        boolean accettao = false;
        System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                .a(giocatore.getNomeGiocatore() + ", il Giocatore "
                        + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ti ha proposto uno scambio: sei disposto a scabiare la tua Casella: "
                        + casellaDaRicevere.getNomeCasella() + " per la Casella del giocatore "
                        + partita.getGiocatoreDiTurno().getNomeGiocatore() + ": "
                        + casellaDaScambiare.getNomeCasella()
                        + "?")
                .reset());
        System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                .a("Selezionare: ").reset());
        System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                .a("\t - (1) se vuoi accettare lo Scambio ").reset());
        System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                .a("\t - (2) se vuoi declinare lo Scambio ").reset());
        int esitoScambio = CommandLineSingleton.getInstance()
                .readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
        if (esitoScambio == 1) {
            System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                    .a("Lo Scambio e' stato accettato dal Giocatore "
                            + giocatore.getNomeGiocatore())
                    .reset());
            scambioAccettato(partita, giocatore, casellaDaScambiare, casellaDaRicevere);
            accettao = true;
        }
        if (esitoScambio == 2) {
            System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                    .a("Lo Scambio e' stato declinato dal Giocatore "
                            + giocatore.getNomeGiocatore())
                    .reset());
            accettao = false;
        }
        return accettao;
    }

    public void scambioAccettato(Partita partita, Player giocatore, CasellaAcquistabile casellaDaScambiare,
            CasellaAcquistabile casellaDaRicevere) {
        System.out.println(
                "Lo Scambio tra il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e il Giocatore "
                        + giocatore.getNomeGiocatore() + " e' stato accettato da "
                        + giocatore.getNomeGiocatore());
        // cambiamo i proprietari delle caselle
        casellaDaRicevere.setProprietario(partita.getGiocatoreDiTurno());
        partita.getGiocatoreDiTurno().getCasellePossedute().add(casellaDaRicevere);
        partita.getGiocatoreDiTurno().getCasellePossedute().remove(casellaDaScambiare);

        casellaDaScambiare.setProprietario(giocatore);
        giocatore.getCasellePossedute().add(casellaDaScambiare);
        giocatore.getCasellePossedute().remove(casellaDaRicevere);
    }

    @Override
    public void tentaDiUscireDiPrigione(Partita partita) throws SaldoInsufficienteException {
        System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                + " e' in prigionee vuole provare a uscire!");
        if (partita.getGiocatoreDiTurno().IsEsciSubitoDiPrigione() == true) {
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " possiede la carta per poter uscire subito di Prigione")
                    .reset());
            // verifica se il giocatore di turno possiede la carta per uscire subito di
            // prigione
            esciDiPrigione(partita);
        } else if (partita.getGiocatoreDiTurno().getNumeroTurniInPrigione() >= 3) {
            if (partita.getGiocatoreDiTurno().getBilancio() < 50) {
                throw new SaldoInsufficienteException("Il Giocatore "
                        + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " non ha abbastanza soldi per poter uscire di prigione");
            }
            // verifica che il giocatore di turno stia da 3 turni in prigione
            System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " e' da 3 turni in prigione, di conseguenza uscira' pagando 50.");
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio() - 50);
            esciDiPrigione(partita);
        } else {
            // se turno in prigione != 3, fa scegliere al giocatore cosa provare
            System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " non possiede la carta per uscire subito di prigione");
            System.out.println(
                    "Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + ", scegli una delle seguenti azioni: (1) per uscire subito pagando 50, (2) per tentare di fare doppio con i dadi");
            int sceltaGiocatoreInPrigione = CommandLineSingleton.getInstance()
                    .readIntegerUntilPossibleValue(new Integer[] { 1, 2 });
            if (sceltaGiocatoreInPrigione == 1) {
                if (partita.getGiocatoreDiTurno().getBilancio() < 50) {
                    throw new SaldoInsufficienteException(
                            "Il Giocatore " + partita.getGiocatoreDiTurno()
                                    .getNomeGiocatore()
                                    + " non ha abbastanza soldi per poter uscire di prigione");
                }
                // esce subito pagando
                System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di uscire subito pagando 50.");
                partita.getGiocatoreDiTurno()
                        .setBilancio(partita.getGiocatoreDiTurno().getBilancio() - 50);
                esciDiPrigione(partita);
            } else {
                // tenta i dadi
                System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di tentare di uscire lanciando i dadi.");
                List<Integer> dadi = lanciaDadi(partita);
                System.out.println(
                        "I valori dei dadi sono stati: " + dadi.get(0) + ", e " + dadi.get(1));
                if (dadi.get(0) == dadi.get(1)) {
                    // verifica che i dadi abbiano valore uguale
                    System.out.println("Il giocatore "
                            + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " esce di prigione dato che ha fatto doppio con i dadi.");
                    esciDiPrigione(partita);
                } else {
                    // se i dadi non hanno valore uguale aumenta il numero dei turni in prigione del
                    // giocatore di 1
                    System.out.println("Il giocatore "
                            + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " rimane in prigione dato che non ha fatto doppio con i dadi.");
                    partita.getGiocatoreDiTurno()
                            .setNumeroTurniInPrigione(partita.getGiocatoreDiTurno()
                                    .getNumeroTurniInPrigione() + 1);
                }
            }
        }
    }

    @Override
    public void esciDiPrigione(Partita partita) {
        partita.getGiocatoreDiTurno().setInPrigione(false);
        System.out.println("Il giocatore "
                + partita.getGiocatoreDiTurno().getNomeGiocatore()
                + " uscira' adesso di prigione");
        // settaggio turni in prigione a 0
        partita.getGiocatoreDiTurno().setNumeroTurniInPrigione(0);
        // fa muovere il giocatore uscito
        try {
            muoviti(partita);
        } catch (GiocatoreInPrigioneException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public CasellaAcquistabile vendiCasellaAcquistabile(Partita partita) throws CategoriaCasellaVuotaException {
        if (partita.getGiocatoreDiTurno().getCasellePossedute().isEmpty()) {
            throw new CategoriaCasellaVuotaException(
                    "Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non possiede alcuna Casella da vendere");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", scegli quale delle tue Caselle vuoi vendere")
                .reset());
        Integer[] indici = new Integer[partita.getGiocatoreDiTurno().getCasellePossedute().size()];
        for (int i = 0; i < partita.getGiocatoreDiTurno().getCasellePossedute().size(); i++) {
            indici[i] = i;
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Scegli tra: ")
                .reset());
        partita.getGiocatoreDiTurno().getCasellePossedute().stream().forEach(
                p -> System.out.println("Seleziona " + partita.getGiocatoreDiTurno().getCasellePossedute().indexOf(p)
                        + " per selezionare " + p.getNomeCasella()));
        /*
        for (Casella p : partita.getGiocatoreDiTurno().getCasellePossedute()) {
            System.out.println(
                    "\t seleziona " + partita.getGiocatoreDiTurno().getProprietaPossedute().indexOf(p) + " per: "
                            + p.getNomeCasella());
        }
                            */
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", immetti il numero dell'indice (esattamente come scritto sopra) della Casella che vuoi vendere")
                .reset());
        System.out.print(":> ");
        int scleta = CommandLineSingleton.getInstance().readIntegerUntilPossibleValue(indici);
        CasellaAcquistabile casellaAcquistabileDaVendere = partita.getGiocatoreDiTurno().getCasellePossedute()
                .get(scleta);
        partita.getGiocatoreDiTurno().getCasellePossedute().remove(casellaAcquistabileDaVendere);
        casellaAcquistabileDaVendere.setProprietario(null);

        return casellaAcquistabileDaVendere;
    }

    @Override
    public void abbandona(Partita partita) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di Abbandonare la Partita")
                .reset());

        partita.getListaGiocatori().remove(partita.getGiocatoreDiTurno());

        partita.getGiocatoreDiTurno().getCasellePossedute()
                .stream()
                .forEach(c -> c.setProprietario(null));

        partita.getGiocatoreDiTurno().getProprietaPossedute()
                .stream()
                .forEach(p -> {
                    p.getCaseSullaProprieta().clear();
                });
        partita.getGiocatoreDiTurno().getProprietaPossedute()
                .stream()
                .filter(p -> p.getAlbergo() != null)
                .forEach(p -> p.rimuoviAlbergoSullaProprieta(p.getAlbergo()));
        System.out.println("Adesso la Partita sara' giocata da " + partita.getListaGiocatori().size()
                + " Giocatori");
    }
}