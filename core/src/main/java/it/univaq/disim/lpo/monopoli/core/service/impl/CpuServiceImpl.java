package it.univaq.disim.lpo.monopoli.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.Albergo;
import it.univaq.disim.lpo.monopoli.core.datamodel.Casa;
import it.univaq.disim.lpo.monopoli.core.datamodel.Casella;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Cpu;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.datamodel.Servizio;
import it.univaq.disim.lpo.monopoli.core.datamodel.Stazione;
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

public class CpuServiceImpl implements PlayerService<Cpu> {

    @Override
    public ModeEnumeration getMode(Partita partita) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' gestito dal Computer, di conseguenza giochera' la Partita")
                .reset());
        return ModeEnumeration.GIOCA;
    }

    @Override
    public ActionEnumeration getAzione(Partita partita) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Computer scegliera' casualmente la sua azione tra...").reset());
        System.out.println("\t1 costruisci");
        System.out.println("\t0 muovi");
        Random r = new Random();
        ActionEnumeration azioneCpu = null;

        if (r.nextBoolean())
            azioneCpu = ActionEnumeration.COSTRUISCI;
        else
            azioneCpu = null;
        return azioneCpu;
    }

    @Override
    public AzioniCasellaEnumeration getAzioneCasella() {
        System.out.println("Il Computer provera' a comprare la Casella...");
        return AzioniCasellaEnumeration.COMPRA;
    }

    @Override
    public AlbergoOCasaEnumeration getCasaOAlbergo(Partita partita) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il computer " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " decidera' se vuole costruire una Casa oppure un Albergo casualmente...")
                .reset());
        AlbergoOCasaEnumeration alc = null;
        Random r = new Random();
        if (r.nextBoolean())
            alc = AlbergoOCasaEnumeration.CASA;
        else
            alc = AlbergoOCasaEnumeration.ALBERGO;
        return alc;
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
                    "Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
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
                .a("I valori dei dadi usciti sono stati: " + dadi.get(0) + ", e " + dadi.get(1)
                        + " (spostarsi di "
                        + numeroDadi + ")")
                .reset());
        Casella nuovaPosizione = null;
        if ((chiave + numeroDadi) >= partita.getTabellone().getCaselleTabellone().size()) {
            // verifica di aver superato il via nel tabellone
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " e' passato dal via, gli verranno accreditati "
                            + partita.getVia().getPrezzoAccredito())
                    .reset());
            int chiavePosizione = (chiave + numeroDadi)
                    - partita.getTabellone().getCaselleTabellone().size();
            nuovaPosizione = partita.getTabellone().getCaselleTabellone().get(chiavePosizione);
            // accredito al giocatore del prezzo del via (100)
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio()
                    + partita.getVia().getPrezzoAccredito());
        } else {
            // non si è arrivati alla fine del tabellone
            System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                    .a("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non e' passato dal via.")
                    .reset());
            nuovaPosizione = partita.getTabellone().getCaselleTabellone().get(chiave + numeroDadi);
        }
        // set della nuova posizione al giocatore
        partita.getGiocatoreDiTurno().setPosizioneGiocatore(nuovaPosizione);
        // aggiornamento sui giocatori presenti sulla casella di nuova posizione
        nuovaPosizione.aggiungiGiocatoreSullaCasella(partita.getGiocatoreDiTurno());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La nuova posizione del BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' "
                        + nuovaPosizione.getNomeCasella())
                .reset());
    }

    @Override
    public void costruisciCasa(Partita partita, Proprieta proprieta)
            throws ProprietaConTutteLeCaseException, SerieCompletaException, SaldoInsufficienteException {
        if (proprieta.getCaseSullaProprieta().size() == 4) {
            throw new ProprietaConTutteLeCaseException(
                    "La proprieta' " + proprieta.getNomeCasella()
                            + " ha gia' tutte le case costruite");
        }
        if (!(proprieta.verificaSerieCompleta(partita, partita.getGiocatoreDiTurno()))) {
            throw new SerieCompletaException(
                    "Non puoi costuire su questa proprieta', per poterlo fare devi avere una Serie Completa");
        }
        if (partita.getGiocatoreDiTurno().getBilancio() < proprieta.getPrezzoCostruzione()) {
            throw new SaldoInsufficienteException("Il BOT "
                    + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " non possiede abbastanza denaro per costruire una casa sulla proprieta'"
                    + proprieta.getNomeCasella());
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " ha deciso di costruire una Casa sulla Proprieta' "
                        + proprieta.getNomeCasella())
                .reset());
        Casa casa = new Casa(partita.getGiocatoreDiTurno(), proprieta);
        partita.getGiocatoreDiTurno().getCasePossedute().add(casa);
        proprieta.aggiungiCasaSullaProprieta(casa);

        // pagamento prezzo per la costruzione
        System.out.println("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " ha pagato "
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
            throw new ProprietaConAlbergoException("La Proprieta' " + proprieta.getNomeCasella()
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
        Albergo albergo = new Albergo(partita.getGiocatoreDiTurno(), proprieta);
        partita.getGiocatoreDiTurno().getAlberghiPosseduti().add(albergo);
        proprieta.aggiungiAlbergoSullaProprieta(albergo);

        // pagamento prezzo per la costruzione
        System.out.println("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " ha pagato "
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
        // NON IMPLEMENTATO PER IL BOT CPU
        throw new UnsupportedOperationException("Unimplemented method 'vendiCostruzione'");
    }

    public boolean decidiEsitoScambio(Partita partita, Player giocatore, CasellaAcquistabile casellaDaScambiare,
            CasellaAcquistabile casellaDaRicevere) {
        boolean accettato = false;
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
                .a("Scelta casuale tra: ").reset());
        System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                .a("\t - (1) accetta lo scambio ").reset());
        System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                .a("\t - (2) declina lo scambio ").reset());
        Random random = new Random();
        int sceltaScambio = random.nextInt(1, 3);
        if (sceltaScambio == 1) {
            System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                    .a("Lo Scambio e' stato accettato dal BOT " + giocatore.getNomeGiocatore())
                    .reset());
            scambioAccettato(partita, giocatore, casellaDaScambiare, casellaDaRicevere);
            accettato = true;
        }
        if (sceltaScambio == 2) {
            System.out.println(Ansi.ansi().fg(giocatore.getColoreGiocatore())
                    .a("Lo scambio e' stato declinato dal BOT " + giocatore.getNomeGiocatore())
                    .reset());
            accettato = false;
        }
        return accettato;
    }

    @Override
    public void scambioAccettato(Partita partita, Player giocatore, CasellaAcquistabile casellaDaScambiare,
            CasellaAcquistabile casellaDaRicevere) {
        System.out.println(
                "Lo scambio tra il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e il BOT "
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
                + " e' in Prigione e provera' ad uscire");
        if (partita.getGiocatoreDiTurno().IsEsciSubitoDiPrigione() == true) {
            // verifica se il giocatore di turno possiede la carta per uscire subito di
            // prigione
            System.out.println("Il Giocatore possiede la carta per uscire subito di Prigione");
            esciDiPrigione(partita);
        } else if (partita.getGiocatoreDiTurno().getNumeroTurniInPrigione() == 3) {
            // verifica che il giocatore di turno stia da 3 turni in prigione
            System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " e' da 3 trurni in Prigione, di conseguenza uscira' pagando 50.");
            partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio() - 50);
            esciDiPrigione(partita);
        } else {
            // se turno in prigione != 3, fa scegliere al giocatore cosa provare
            System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                    + " non possiede la carta per uscire subito di Prigione");
            System.out.println("Selezione casuale tra: ");
            System.out.println("\t - (1) per uscire subito pagando 50");
            System.out.println("\t - (2) per tentare di fare doppio con i dadi");
            Random r = new Random();
            int sceltaGiocatoreInPrigione = r.nextInt(1, 3);
            if (sceltaGiocatoreInPrigione == 1) {
                if (partita.getGiocatoreDiTurno().getBilancio() < 50) {
                    throw new SaldoInsufficienteException(
                            "Il giocatore " + partita.getGiocatoreDiTurno()
                                    .getNomeGiocatore()
                                    + " non possiede un saldo sufficiente per uscire di prigione");
                }
                // esce subito pagando 50
                System.out.println("Il giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " uscira' subito pagando 50.");
                partita.getGiocatoreDiTurno()
                        .setBilancio(partita.getGiocatoreDiTurno().getBilancio() - 50);
                esciDiPrigione(partita);
            } else {
                // tenta la sorte con i dadi
                System.out.println("Il giocatore" + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " tentera' di uscire lanciando i dadi.");
                List<Integer> dadi = lanciaDadi(partita);
                System.out.println("I valori dei due dadi sono stati: " + dadi.get(0) + ", e "
                        + dadi.get(1));
                if (dadi.get(0) == dadi.get(1)) {
                    // verifica che i dadi abbiano valore uguale
                    System.out.println("Il giocatore "
                            + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " uscira' di Prigione dato che ha fatto doppio con i dadi.");
                    esciDiPrigione(partita);
                } else {
                    // se i dadi non hanno valore uguale aumenta il numero dei turni in prigione del
                    // giocatore di 1
                    System.out.println("Il giocatore "
                            + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " rimarra' in Prigione dato che non ha fatto doppio con i dadi.");
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
                    "Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + "non ha nessuna casella da vendere");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " non ha abbastanza soldi ed e' costretto a vendere delle caselle")
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La casella verra' scelta casualmente tra:").reset());
        partita.getGiocatoreDiTurno().getCasellePossedute().stream().map(CasellaAcquistabile::getNomeCasella)
                .forEach(nomeCasella -> System.out
                        .println(Ansi.ansi()
                                .fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                                .a("\t - " + nomeCasella).reset()));
        Random r = new Random();
        int indiceLista = r.nextInt(0, partita.getGiocatoreDiTurno().getCasellePossedute().size());
        CasellaAcquistabile casellaDaVendere = partita.getGiocatoreDiTurno().getCasellePossedute()
                .get(indiceLista);
        partita.getGiocatoreDiTurno().getCasellePossedute().remove(casellaDaVendere);
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La casella scelta (casualmente) dal BOT e' " + casellaDaVendere.getNomeCasella())
                .reset());
        return casellaDaVendere;
    }

    @Override
    public void abbandona(Partita partita) {
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " e' costretto ad Abbandonare la Partita")
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

    @Override
    public Proprieta selezionaProprieta(Partita partita) throws CategoriaCasellaVuotaException {
        if (partita.getGiocatoreDiTurno().getProprietaPossedute().isEmpty()) {
            throw new CategoriaCasellaVuotaException(
                    "Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                            + " non possiede alcuna Proprieta'");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il BOT " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + ", scegliera' casualmente una delle sue Proprieta'")
                .reset());
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La casella verra' scelta casualmente tra:").reset());
        partita.getGiocatoreDiTurno().getCasellePossedute().stream().map(CasellaAcquistabile::getNomeCasella)
                .forEach(nomeCasella -> System.out
                        .println(Ansi.ansi()
                                .fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                                .a("\t - " + nomeCasella).reset()));
        Random r = new Random();
        int indiceLista = r.nextInt(0, partita.getGiocatoreDiTurno().getProprietaPossedute().size());
        Proprieta proprieta = partita.getGiocatoreDiTurno().getProprietaPossedute()
                .get(indiceLista);
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("La casella scelta (casualmente) dal BOT e' " + proprieta.getNomeCasella())
                .reset());
        return proprieta;
    }

    @Override
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
}