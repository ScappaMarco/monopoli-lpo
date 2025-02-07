package it.univaq.disim.lpo.monopoli.core.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/*Partita: entità che gestirà l'avanzamento della partita*/
public class Partita implements Serializable {
    private String nomePartita;
    private int turniPartita = 0; // parte da 0, incrementa ogni turno
    private Tabellone tabellone = null; // inizialmente null
    private List<Carta> imprevisti, probabilita;
    private Player g1, g2, g3, g4, g5, g6;
    private Player giocatoreDiTurno;
    private List<Player> listaGiocatori;

    public Partita(String nomePartita, Tabellone tabellone, boolean inizializza) {
        this.listaGiocatori = new ArrayList<>();
        this.imprevisti = new ArrayList<>();
        this.probabilita = new ArrayList<>();
        this.nomePartita = nomePartita;
        this.tabellone = tabellone;
    }

    public Tabellone creaTabellone(int numeroGiocatori) {
        this.tabellone = new Tabellone();
        if (numeroGiocatori == 2) {
            Map<Integer, Casella> mappaDaDueGiocatori = creaTabelloneDaDueGiocatori();
            tabellone.setCaselleTabellone(mappaDaDueGiocatori);
        }
        if (numeroGiocatori == 3) {
            Map<Integer, Casella> mappaDaTreGiocatori = creaTabelloneDaTreGiocatori();
            tabellone.setCaselleTabellone(mappaDaTreGiocatori);
        }
        if (numeroGiocatori == 4) {
            Map<Integer, Casella> mappaDaQuattroGiocatori = creaTabelloneDaQuattroGiocatori();
            tabellone.setCaselleTabellone(mappaDaQuattroGiocatori);
        }
        if (numeroGiocatori == 5) {
            Map<Integer, Casella> mappaDaCinqueGiocatori = creaTabelloneDaCinqueGiocatori();
            tabellone.setCaselleTabellone(mappaDaCinqueGiocatori);
        }
        if (numeroGiocatori == 6) {
            Map<Integer, Casella> mappaDaSeiGiocatori = creaTabelloneDaSeiGiocatori();
            tabellone.setCaselleTabellone(mappaDaSeiGiocatori);
        }
        return tabellone;
    }

    public Map<Integer, Casella> creaTabelloneDaDueGiocatori() {
        // 5 caselle per lato

        // angolo via
        tabellone.getCaselleTabellone().put(0, new Via("Via", 100));
        // angolo via

        // lato 1
        tabellone.getCaselleTabellone().put(1,
                new Proprieta("Via Verdi",
                        40, 20, ColoreEnumeration.ROSSO, 60, 20, 20));
        tabellone.getCaselleTabellone().put(2,
                new Proprieta("Via Rossi",
                        40, 12, ColoreEnumeration.ROSSO, 30, 40, 20));
        tabellone.getCaselleTabellone().put(3,
                new Stazione("Stazione Est", 32, 16, 16, 40, 0,
                        0));
        tabellone.getCaselleTabellone().put(4,
                new Proprieta("Via Bianchi",
                        80, 16, ColoreEnumeration.ROSSO, 48, 65, 40));
        tabellone.getCaselleTabellone().put(5,
                new CasellaConCarta("Imprevisto",
                        TipoCartaEnumeration.IMPREVISTO));
        // lato 1

        // angolo prigione e transito
        tabellone.getCaselleTabellone().put(6,
                new PrigioneETransito("Prigione e Transito"));
        // angolo prigione e transito

        // lato 2
        tabellone.getCaselleTabellone().put(7,
                new Proprieta("Via Roma", 72,
                        32, ColoreEnumeration.VERDE, 80, 140, 36));
        tabellone.getCaselleTabellone().put(8,
                new Servizio("Societa' Acqua Potabile", 40,
                        20, 0, 20));
        tabellone.getCaselleTabellone().put(9,
                new Proprieta("Via Milano",
                        80, 48, ColoreEnumeration.VERDE, 80, 160, 40));
        tabellone.getCaselleTabellone().put(10,
                new Proprieta("Via Firenze",
                        72, 32, ColoreEnumeration.VERDE, 80, 120, 36));
        tabellone.getCaselleTabellone().put(11,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));

        // lato 2

        // angolo parcheggio gratuito
        tabellone.getCaselleTabellone().put(12,
                new ParcheggioGratuito("Parcheggio Gratuito"));
        // angolo parcheggio gratuito

        // lato 3
        tabellone.getCaselleTabellone().put(13,
                new Proprieta("Via Giuseppe Garibaldi", 56, 24, ColoreEnumeration.BLU, 72, 100, 28));
        tabellone.getCaselleTabellone().put(14,
                new Proprieta("Largo Vittorio Emanuele II", 64, 32, ColoreEnumeration.BLU, 72, 112, 32));
        tabellone.getCaselleTabellone().put(15,
                new Stazione("Stazione Ovest", 32, 16, 16, 40, 0, 0));
        tabellone.getCaselleTabellone().put(16,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(17,
                new Proprieta("Via Guglielmo Marconi", 56, 24, ColoreEnumeration.BLU, 72, 100, 28));

        // lato 3

        // angolo vai in prigione
        tabellone.getCaselleTabellone().put(18,
                new VaiInPrigione("Vai in Prigione"));
        // angolo vai in prigione

        // lato 4
        tabellone.getCaselleTabellone().put(19,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(20,
                new Proprieta("Via Santa Maria", 40, 16, ColoreEnumeration.GIALLO, 48, 80, 20));
        tabellone.getCaselleTabellone().put(21,
                new Proprieta("Via Santa Lucia", 32, 12, ColoreEnumeration.GIALLO, 48, 64, 16));
        tabellone.getCaselleTabellone().put(22,
                new Tassa("Tassa Universitaria", 100));
        tabellone.getCaselleTabellone().put(23,
                new Proprieta("Piazza Regina Margherita", 40, 16, ColoreEnumeration.GIALLO, 48, 80, 20));

        // lato 4

        return tabellone.getCaselleTabellone();
    }

    public Map<Integer, Casella> creaTabelloneDaTreGiocatori() {
        // 6 caselle per lato

        // angolo via
        tabellone.getCaselleTabellone().put(0, new Via("Via", 100));
        // angolo via

        // lato 1
        tabellone.getCaselleTabellone().put(1,
                new Proprieta("Vicolo Corto", 16, 8, ColoreEnumeration.MARRONE, 32, 24, 8));
        tabellone.getCaselleTabellone().put(2,
                new Tassa("Tassa sulla Spazzatura", 68));
        tabellone.getCaselleTabellone().put(3,
                new Stazione("Stazione Est", 40, 24, 20, 40, 60, 0));
        tabellone.getCaselleTabellone().put(4,
                new Proprieta("Via Roma", 80, 48, ColoreEnumeration.VERDE, 88, 60, 40));
        tabellone.getCaselleTabellone().put(5,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(6,
                new Proprieta("Via Milano", 72, 32, ColoreEnumeration.VERDE, 88, 48, 36));

        // angolo prigione e transito
        tabellone.getCaselleTabellone().put(7,
                new PrigioneETransito("Prigione e Transito"));
        // angolo prigione e transito

        // lato 2
        tabellone.getCaselleTabellone().put(8,
                new Proprieta("Via Bologna", 64, 32, ColoreEnumeration.VERDE, 88, 48, 32));
        tabellone.getCaselleTabellone().put(9,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(10,
                new Proprieta("Via Giulio Cesare", 48, 20, ColoreEnumeration.ROSSO, 72, 40, 24));
        tabellone.getCaselleTabellone().put(11,
                new Stazione("Stazione Nord", 40, 24, 20, 40, 60, 0));
        tabellone.getCaselleTabellone().put(12,
                new Proprieta("Via Servio Tullio", 56, 24, ColoreEnumeration.ROSSO, 72, 48, 28));
        tabellone.getCaselleTabellone().put(13,
                new Proprieta("Via Marzio", 40, 16, ColoreEnumeration.ROSSO, 72, 40, 20));

        // lato 2

        // angolo parcheggio gratuito
        tabellone.getCaselleTabellone().put(14,
                new ParcheggioGratuito("Parcheggio Gratuito"));
        // angolo parcheggio gratuito

        // lato 3
        tabellone.getCaselleTabellone().put(15,
                new Proprieta("Via Lazio", 56, 24, ColoreEnumeration.BLU, 72, 64, 28));
        tabellone.getCaselleTabellone().put(16,
                new Servizio("Societa' petrolifera", 60, 32, 0, 28));
        tabellone.getCaselleTabellone().put(17,
                new Proprieta("Via Emilia", 40, 16, ColoreEnumeration.BLU, 72, 48, 20));
        tabellone.getCaselleTabellone().put(18,
                new Stazione("Stazione Ovet", 40, 24, 20, 40, 60, 0));
        tabellone.getCaselleTabellone().put(19,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(20,
                new Proprieta("Via Lombardia", 56, 24, ColoreEnumeration.BLU, 72, 64, 28));

        // lato 3

        // angolo vai in prigione
        tabellone.getCaselleTabellone().put(21,
                new VaiInPrigione("Vai in Prigione"));
        // angolo vai in prigione

        // lato 4
        tabellone.getCaselleTabellone().put(22,
                new Proprieta("Via XX Settembre", 48, 20, ColoreEnumeration.VIOLA, 64, 48, 24));
        tabellone.getCaselleTabellone().put(23,
                new Proprieta("Via Croce Rossa", 32, 16, ColoreEnumeration.VIOLA, 64, 40, 16));
        tabellone.getCaselleTabellone().put(24,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(25,
                new Proprieta("Via Vetoio", 40, 20, ColoreEnumeration.VIOLA, 64, 40, 20));
        tabellone.getCaselleTabellone().put(26,
                new Tassa("Tassa Universitaria", 80));
        tabellone.getCaselleTabellone().put(27,
                new Proprieta("Vicolo Stretto", 24, 12, ColoreEnumeration.MARRONE, 32, 24, 12));

        // lato 4

        return tabellone.getCaselleTabellone();
    }

    public Map<Integer, Casella> creaTabelloneDaQuattroGiocatori() {
        // 7 caselle per lato

        // angolo via
        tabellone.getCaselleTabellone().put(0, new Via("Via", 100));
        // angolo via

        // lato 1
        tabellone.getCaselleTabellone().put(1,
                new Proprieta("Viale Garibaldi", 64, 24, ColoreEnumeration.ROSSO, 88, 60, 32));
        tabellone.getCaselleTabellone().put(2,
                new Tassa("Tassa sulla Spazzatura", 80));
        tabellone.getCaselleTabellone().put(3,
                new Proprieta("Via Carlo Magno", 64, 24, ColoreEnumeration.ROSSO, 88, 60, 32));
        tabellone.getCaselleTabellone().put(4,
                new Stazione("Stazione Est", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(5,
                new Proprieta("Largo Napoleone", 80, 32, ColoreEnumeration.ROSSO, 88, 72, 40));
        tabellone.getCaselleTabellone().put(6,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(7,
                new Proprieta("Via Cintia", 56, 24, ColoreEnumeration.VERDE, 72, 60, 28));

        // lato 1

        // angolo prigione e transito
        tabellone.getCaselleTabellone().put(8,
                new PrigioneETransito("Prigione e Transito"));
        // angolo prigione e transito

        // lato 2
        tabellone.getCaselleTabellone().put(9,
                new Proprieta("Via Maestri del lavoro", 64, 32, ColoreEnumeration.VERDE, 72, 64, 32));
        tabellone.getCaselleTabellone().put(10,
                new Servizio("Societa' Elettrica", 64, 32, 0, 32));
        tabellone.getCaselleTabellone().put(11,
                new Proprieta("Viale Maraini", 56, 24, ColoreEnumeration.VERDE, 72, 60, 28));
        tabellone.getCaselleTabellone().put(12,
                new Stazione("Stazione Nord", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(13,
                new Proprieta("Via XX Settembre", 80, 40, ColoreEnumeration.ARANCIONE, 100, 80, 40));
        tabellone.getCaselleTabellone().put(14,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(15,
                new Proprieta("Via Croce Rossa", 64, 32, ColoreEnumeration.ARANCIONE, 100, 72, 32));

        // lato 2

        // angolo parcheggio gratuito
        tabellone.getCaselleTabellone().put(16,
                new ParcheggioGratuito("Parcheggio Gratuito"));
        // angolo parcheggio gratuito

        // lato 3
        tabellone.getCaselleTabellone().put(17,
                new Proprieta("Via Vetoio", 72, 40, ColoreEnumeration.ARANCIONE, 100, 80, 36));
        tabellone.getCaselleTabellone().put(18,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(19,
                new Proprieta("Via Lazio", 56, 24, ColoreEnumeration.GIALLO, 80, 72, 28));
        tabellone.getCaselleTabellone().put(20,
                new Stazione("Stazione Ovest", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(21,
                new Proprieta("Via Emilia", 64, 36, ColoreEnumeration.GIALLO, 80, 72, 36));
        tabellone.getCaselleTabellone().put(22,
                new Proprieta("Via Abruzzo", 56, 24, ColoreEnumeration.GIALLO, 80, 72, 28));
        tabellone.getCaselleTabellone().put(23,
                new Proprieta("Via Enrico Fermi", 48, 24, ColoreEnumeration.GRIGIO, 72, 60, 24));

        // lato 3

        // angolo vai in prigione
        tabellone.getCaselleTabellone().put(24,
                new VaiInPrigione("Vai in Prigione"));
        // angolo vai in prigione

        // lato 4
        tabellone.getCaselleTabellone().put(25,
                new Proprieta("Via Nikola Tesla", 56, 32, ColoreEnumeration.GRIGIO, 72, 64, 28));
        tabellone.getCaselleTabellone().put(26,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(27,
                new Proprieta("Via Galileo Galilei", 60, 36, ColoreEnumeration.GRIGIO, 72, 64, 28));
        tabellone.getCaselleTabellone().put(28,
                new Stazione("Stazione Sud", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(29,
                new Proprieta("Piazza Navona", 88, 48, ColoreEnumeration.BLU, 140, 80, 44));
        tabellone.getCaselleTabellone().put(30,
                new Tassa("Tassa Universitaria", 120));
        tabellone.getCaselleTabellone().put(31,
                new Proprieta("Piazza San Marco", 100, 56, ColoreEnumeration.BLU, 140, 88, 52));

        // lato 4

        return tabellone.getCaselleTabellone();
    }

    public Map<Integer, Casella> creaTabelloneDaCinqueGiocatori() {
        // 8 caselle per lato

        // angolo via
        tabellone.getCaselleTabellone().put(0, new Via("Via", 100));
        // angolo via

        // lato 1
        tabellone.getCaselleTabellone().put(1,
                new Proprieta("Viale Galileo Galilei", 48, 28, ColoreEnumeration.BLU, 80, 72, 24));
        tabellone.getCaselleTabellone().put(2,
                new Tassa("Tassa T.A.R.I.", 100));
        tabellone.getCaselleTabellone().put(3,
                new Proprieta("corso Leonardo Da Vinci", 56, 32, ColoreEnumeration.BLU, 80, 80, 28));
        tabellone.getCaselleTabellone().put(4,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(5,
                new Stazione("Stazione Est", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(6,
                new Proprieta("Via Duca Alberto", 64, 36, ColoreEnumeration.ARANCIONE, 104, 84, 32));
        tabellone.getCaselleTabellone().put(7,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(8,
                new Proprieta("Viale degli Abruzzi", 64, 36, ColoreEnumeration.ARANCIONE, 104, 84, 32));

        // lato 1

        // angolo prigione e transito
        tabellone.getCaselleTabellone().put(9,
                new PrigioneETransito("Prigione e Transito"));
        // angolo prigione e transito

        // lato 2
        tabellone.getCaselleTabellone().put(10,
                new Proprieta("Via Monte Amiata", 56, 32, ColoreEnumeration.ARANCIONE, 104, 76, 28));
        tabellone.getCaselleTabellone().put(11,
                new Servizio("Societa' Elettrica", 64, 32, 0, 32));
        tabellone.getCaselleTabellone().put(12,
                new Proprieta("Viale Maraini", 72, 40, ColoreEnumeration.ROSSO, 124, 96, 36));
        tabellone.getCaselleTabellone().put(13,
                new Proprieta("Viale Matteucci", 80, 48, ColoreEnumeration.ROSSO, 124, 100, 40));
        tabellone.getCaselleTabellone().put(14,
                new Stazione("Stazione Nord", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(15,
                new Proprieta("Viale Garibaldi", 72, 40, ColoreEnumeration.ROSSO, 124, 88, 36));
        tabellone.getCaselleTabellone().put(16,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(17,
                new Proprieta("Via Lazio", 40, 24, ColoreEnumeration.GIALLO, 72, 56, 20));

        // lato 2

        // angolo parcheggio gratuito
        tabellone.getCaselleTabellone().put(18,
                new ParcheggioGratuito("Parcheggio Gratuito"));
        // angolo parcheggio gratuito

        // lato 3
        tabellone.getCaselleTabellone().put(19,
                new Proprieta("Via Lombardia", 56, 32, ColoreEnumeration.GIALLO, 72, 72, 28));
        tabellone.getCaselleTabellone().put(20,
                new Proprieta("Via Basilicata", 48, 28, ColoreEnumeration.GIALLO, 72, 60, 24));
        tabellone.getCaselleTabellone().put(21,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(22,
                new Proprieta("Via XX Settembre", 64, 32, ColoreEnumeration.VERDE, 84, 76, 32));
        tabellone.getCaselleTabellone().put(23,
                new Stazione("Stazione Ovest", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(24,
                new Proprieta("Via Croce Rossa", 64, 32, ColoreEnumeration.VERDE, 84, 76, 32));
        tabellone.getCaselleTabellone().put(25,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(26,
                new Proprieta("Via Vetoio", 56, 24, ColoreEnumeration.VERDE, 84, 60, 28));

        // lato 3

        // angolo vai in prigione
        tabellone.getCaselleTabellone().put(27,
                new VaiInPrigione("Vai in Prigione"));
        // angolo vai in prigione

        // lato 4
        tabellone.getCaselleTabellone().put(28,
                new Proprieta("Piazza Duomo", 88, 56, ColoreEnumeration.GRIGIO, 140, 80, 44));
        tabellone.getCaselleTabellone().put(29,
                new Proprieta("Piazza Enrico Berlinguer", 88, 48, ColoreEnumeration.GRIGIO, 140, 80, 44));
        tabellone.getCaselleTabellone().put(30,
                new Proprieta("Largo San Fedele", 72, 40, ColoreEnumeration.GRIGIO, 140, 72, 36));
        tabellone.getCaselleTabellone().put(31,
                new Stazione("Stazione Sud", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(32,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(33,
                new Proprieta("Corso Re di Savoia", 72, 40, ColoreEnumeration.VIOLA, 112, 72, 36));
        tabellone.getCaselleTabellone().put(34,
                new Tassa("Tassa Universitaria", 120));
        tabellone.getCaselleTabellone().put(35,
                new Proprieta("Corso Alessandro Magno", 80, 48, ColoreEnumeration.VIOLA, 112, 80, 40));

        // lato 4

        return tabellone.getCaselleTabellone();
    }

    public Map<Integer, Casella> creaTabelloneDaSeiGiocatori() {
        // 9 caselle per lato

        // angolo via
        tabellone.getCaselleTabellone().put(0, new Via("Via", 100));
        // angolo via

        // lato 1
        tabellone.getCaselleTabellone().put(1,
                new Proprieta("Via Dante Alighieri", 64, 36, ColoreEnumeration.MARRONE, 88, 72, 32));
        tabellone.getCaselleTabellone().put(2,
                new Proprieta("Via Francesco Petrarca", 56, 32, ColoreEnumeration.MARRONE, 88, 64, 28));
        tabellone.getCaselleTabellone().put(3,
                new Tassa("Tassa sulla seconda casa", 112));
        tabellone.getCaselleTabellone().put(4,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(5,
                new Stazione("Stazione Est", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(6,
                new Proprieta("Via Edwin Hubble", 64, 36, ColoreEnumeration.BLU, 88, 72, 32));
        tabellone.getCaselleTabellone().put(7,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(8,
                new Proprieta("Via Werner Heisenberg", 72, 44, ColoreEnumeration.BLU, 88, 80, 36));
        tabellone.getCaselleTabellone().put(9,
                new Proprieta("Via Stephen Hawking", 64, 36, ColoreEnumeration.BLU, 88, 60, 32));

        // lato 1

        // angolo prigione e transito
        tabellone.getCaselleTabellone().put(10,
                new PrigioneETransito("Prigione e Transito"));
        // angolo prigione e transito

        // lato 2
        tabellone.getCaselleTabellone().put(11,
                new Proprieta("Via Lazio", 48, 28, ColoreEnumeration.ROSSO, 72, 48, 24));
        tabellone.getCaselleTabellone().put(12,
                new Servizio("Societa' Elettrica", 64, 32, 72, 32));
        tabellone.getCaselleTabellone().put(13,
                new Proprieta("Via Emilia", 56, 32, ColoreEnumeration.ROSSO, 72, 56, 28));
        tabellone.getCaselleTabellone().put(14,
                new Proprieta("Via Campania", 56, 32, ColoreEnumeration.ROSSO, 72, 56, 28));
        tabellone.getCaselleTabellone().put(15,
                new Stazione("Stazione Nord", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(16,
                new Proprieta("Piazza Vittorio Emanuele II", 104, 52, ColoreEnumeration.GIALLO, 128, 100, 52));
        tabellone.getCaselleTabellone().put(17,
                new Proprieta("Largo Giuseppe Garibaldi", 112, 60, ColoreEnumeration.GIALLO, 128, 104, 56));
        tabellone.getCaselleTabellone().put(18,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(19,
                new Proprieta("Corso Napoleone Bonaparte", 112, 60, ColoreEnumeration.GIALLO, 128, 104, 56));

        // lato 2

        // angolo parcheggio gratuito
        tabellone.getCaselleTabellone().put(20,
                new ParcheggioGratuito("Parcheggio Gratuito"));
        // angolo parcheggio gratuito

        // lato 3
        tabellone.getCaselleTabellone().put(21,
                new Proprieta("Via XX Settembre", 72, 40, ColoreEnumeration.VERDE, 104, 80, 36));
        tabellone.getCaselleTabellone().put(22,
                new Proprieta("Via Croce Rossa", 64, 36, ColoreEnumeration.VERDE, 104, 72, 32));
        tabellone.getCaselleTabellone().put(23,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(24,
                new Proprieta("Via Comunita' Europea", 64, 36, ColoreEnumeration.VERDE, 104, 72, 32));
        tabellone.getCaselleTabellone().put(25,
                new Stazione("Stazione Ovest", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(26,
                new Proprieta("Viale Maraini", 56, 32, ColoreEnumeration.GRIGIO, 92, 60, 28));
        tabellone.getCaselleTabellone().put(27,
                new CasellaConCarta("Imprevisto", TipoCartaEnumeration.IMPREVISTO));
        tabellone.getCaselleTabellone().put(28,
                new Servizio("Societa' Acqua Potabile", 64, 32, 72, 32));
        tabellone.getCaselleTabellone().put(29,
                new Proprieta("Viale Matteucci", 64, 32, ColoreEnumeration.GRIGIO, 92, 64, 32));

        // lato 3

        // angolo vai in prigione
        tabellone.getCaselleTabellone().put(30,
                new VaiInPrigione("Vai in Prigione"));
        // angolo vai in prigione

        // lato 4
        tabellone.getCaselleTabellone().put(31,
                new Proprieta("Viale Canali", 64, 32, ColoreEnumeration.GRIGIO, 92, 64, 32));
        tabellone.getCaselleTabellone().put(32,
                new CasellaConCarta("Probabilita'", TipoCartaEnumeration.PROBABILITA));
        tabellone.getCaselleTabellone().put(33,
                new Proprieta("Via del Corso", 88, 48, ColoreEnumeration.ARANCIONE, 112, 80, 44));
        tabellone.getCaselleTabellone().put(34,
                new Proprieta("Via Condotti", 96, 60, ColoreEnumeration.ARANCIONE, 112, 88, 48));
        tabellone.getCaselleTabellone().put(35,
                new Stazione("Stazione Sud", 48, 32, 24, 48, 72, 100));
        tabellone.getCaselleTabellone().put(36,
                new Proprieta("Via Vittorio Veneto", 88, 48, ColoreEnumeration.ARANCIONE, 112, 80, 44));
        tabellone.getCaselleTabellone().put(37,
                new Proprieta("Via Fontana", 80, 48, ColoreEnumeration.VIOLA, 100, 72, 40));
        tabellone.getCaselleTabellone().put(38,
                new Tassa("Tassa Universitaria", 128));
        tabellone.getCaselleTabellone().put(39,
                new Proprieta("Piazza della Signoria", 88, 48, ColoreEnumeration.VIOLA, 112, 80, 44));

        // lato 4

        return tabellone.getCaselleTabellone();
    }

    public Map<ColoreEnumeration, Integer> serieCompleta(Partita partita) {
        Map<ColoreEnumeration, Integer> numeroRichiestoPerSerieCompleta = new EnumMap<>(
                ColoreEnumeration.class);
        if (partita.getListaGiocatori().size() == 2) {
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ROSSO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VERDE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.BLU, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GIALLO, 3);
        } else if (partita.getListaGiocatori().size() == 3) {
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ROSSO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VERDE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VIOLA, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.BLU, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.MARRONE, 2);
        } else if (partita.getListaGiocatori().size() == 4) {
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ROSSO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VERDE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ARANCIONE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GIALLO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GRIGIO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.BLU, 2);
        } else if (partita.getListaGiocatori().size() == 5) {
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ROSSO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VERDE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ARANCIONE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GIALLO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GRIGIO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.BLU, 2);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VIOLA, 2);
        } else {
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.MARRONE, 2);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.BLU, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ROSSO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GIALLO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VERDE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.GRIGIO, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.ARANCIONE, 3);
            numeroRichiestoPerSerieCompleta.put(ColoreEnumeration.VIOLA, 2);
        }
        return numeroRichiestoPerSerieCompleta;
    }

    /* metodo che serve ad accedera al via */
    public Via getVia() {
        Via via = (Via) this.getTabellone().getCaselleTabellone().get(0);
        return via;
    }
    /* metodo che serve ad accedera al via, tramite cast */

    /* metodi get e set per il nome della partita */
    public String getNomePartita() {
        return this.nomePartita;
    }

    public void setNomePartita(String nomePartita) {
        this.nomePartita = nomePartita;
    }
    /* metodi get e set per il nome della partita */

    /* metodi get e set per il tabellone della partita */
    public Tabellone getTabellone() {
        return this.tabellone;
    }

    public void setTabellone(Tabellone tabellone) {
        this.tabellone = tabellone;
    }
    /* metodi get e set per il tabellone della partita */

    /* metodi get e set per la lista di imprevisti */
    public List<Carta> getImprevisti() {
        return this.imprevisti;
    }

    public void setImprevisti(List<Carta> imprevisti) {
        this.imprevisti = imprevisti;
    }
    /* metodi get e set per la lista di imprevisti */

    /* metodi per aggiungere o rimuovere una specifica carta */
    public void aggiungiImprevisto(Carta c) {
        this.imprevisti.add(c);
        c.setPartita(this);
    }

    public void rimuoviImprevisto(Carta c) {
        this.imprevisti.remove(c);
        c.setPartita(null);
    }
    /* metodi per aggiungere o rimuovere una specifica carta */

    /* metodi get e set per la lista di probabilità */
    public List<Carta> getProbabilita() {
        return this.probabilita;
    }

    public void setProbabilita(List<Carta> probabilita) {
        this.probabilita = probabilita;
    }
    /* metodi get e set per la lista di probabilità */

    /* metodi per aggiungere o rimuovere una specifica carta */
    public void aggiungiProbabilita(Carta c) {
        this.probabilita.add(c);
        c.setPartita(this);
    }

    public void rimuoviProbabilita(Carta c) {
        this.probabilita.remove(c);
        c.setPartita(null);
    }
    /* metodi per aggiungere o rimuovere una specifica carta */

    /* metodi get e set per i turni della partita */
    public int getTurniPartita() {
        return this.turniPartita;
    }

    public void setTurniPartita(int turniPartita) {
        this.turniPartita = turniPartita;
    }
    /* metodi get e set per i turni della partita */

    /* metodi get e set per il giocatore 1 */
    public Player getG1() {
        return this.g1;
    }

    public void setG1(Player g1) {
        this.g1 = g1;
    }
    /* metodi get e set per il giocatore 1 */

    /* metodi get e set per il giocatore 2 */
    public Player getG2() {
        return this.g2;
    }

    public void setG2(Player g2) {
        this.g2 = g2;
    }
    /* metodi get e set per il giocatore 2 */

    /* metodi get e set per il giocatore 3 */
    public Player getG3() {
        return this.g3;
    }

    public void setG3(Player g3) {
        this.g3 = g3;
    }
    /* metodi get e set per il giocatore 3 */

    /* metodi get e set per il giocatore 4 */
    public Player getG4() {
        return this.g4;
    }

    public void setG4(Player g4) {
        this.g4 = g4;
    }
    /* metodi get e set per il giocatore 4 */

    /* metodi get e set per il giocatore 5 */
    public Player getG5() {
        return this.g5;
    }
    /* metodi get e set per il giocatore 5 */

    public void setG5(Player g5) {
        this.g5 = g5;
    }
    /* metodi get e set per il giocatore 5 */

    /* metodi get e set per il giocatore 6 */
    public Player getG6() {
        return this.g6;
    }

    public void setG6(Player g6) {
        this.g6 = g6;
    }
    /* metodi get e set per il giocatore 6 */

    /* metodi get e set per la lista dei giocatori */
    public List<Player> getListaGiocatori() {
        return this.listaGiocatori;
    }

    public void setListaGiocatori(List<Player> listaGiocatori) {
        this.listaGiocatori = listaGiocatori;
    }
    /* metodi get e set per la lista dei giocatori */

    /* metodi get e set per il giocatore di turno */
    public Player getGiocatoreDiTurno() {
        return this.giocatoreDiTurno;
    }

    public void setGiocatoreDiTurno(Player giocatoreDiTurno) {
        this.giocatoreDiTurno = giocatoreDiTurno;
    }
    /* metodi get e set per il giocatore di turno */

    /* metodo per scoprire la posizione del parcheggio gratuito */
    public int getPosizioneParcheggioGratuito(Partita partita) {
        int sizeTabellone = partita.getTabellone().getCaselleTabellone().size();
        int posizioneParcheggioGratuito = sizeTabellone / 2;
        return posizioneParcheggioGratuito;
    }
    /* metodo per scoprire la posizione del parcheggio gratuito */

    /* metodo per scoprire la posizione della prigione */
    public int getPosizionePrigioneETransito(Partita partita) {
        int sizeTabellone = partita.getTabellone().getCaselleTabellone().size();
        int posizionePrigioneETransito = sizeTabellone / 4;
        return posizionePrigioneETransito;
    }
    /* metodo per scoprire la posizione della prigione */

    /* metodo per la creazione delle lista di imprevisti */
    public List<Carta> creaListaImprevisti() {
        List<Carta> imprevisti = new ArrayList<>(15);

        /* 1 */imprevisti.add(
                new Carta(TipoCartaEnumeration.IMPREVISTO, "Pagate 10 per ogni casa che possiedete",
                        CategoriaCarteEnumeration.PAGAPERCASEPOSSEDUTE, this, 0, 0, null));
        /* 2 */imprevisti.add(
                new Carta(TipoCartaEnumeration.IMPREVISTO, "E' arrivata la bolletta del gas, pagate 20",
                        CategoriaCarteEnumeration.PAGA, this, 20, 0, null));
        /* 3 */imprevisti
                .add(new Carta(TipoCartaEnumeration.IMPREVISTO,
                        "E' il vostro compleanno! Ogni giocatore vi regala 15",
                        CategoriaCarteEnumeration.GUADAGNADAALTRIGIOCATORI, this, 0, 0, null));
        /* 4 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO, "La banca vi versa un dividendo di 50",
                CategoriaCarteEnumeration.GUADAGNA, this, 0, 50, null));
        /* 5 */imprevisti.add(
                new Carta(TipoCartaEnumeration.IMPREVISTO, "Multa per eccesso di velocita'! Pagate 40",
                        CategoriaCarteEnumeration.PAGA, this, 40, 0, null));
        /* 6 */imprevisti.add(
                new Carta(TipoCartaEnumeration.IMPREVISTO, "Andate in prigione senza passare dal via",
                        CategoriaCarteEnumeration.VAIINPRIGIONE, this, 0, 0,
                        this.getTabellone().getCaselleTabellone()
                                .get(this.getPosizionePrigioneETransito(this))));
        /* 7 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO, "Potrete uscire gratis di prigione",
                CategoriaCarteEnumeration.ESCIDIPRIGIONE, this, 0, 0, null));
        /* + */// imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO, "Fai 3 passi
               // indietro", CategoriaCarteEnumeration.SPOSTATI, this, 0, 0,
               // this.getGiocatoreDiTurno().getPosizioneGiocatore()));
        /* 8 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO, "Andate fino al Parcheggio gratuito",
                CategoriaCarteEnumeration.SPOSTATI, this, 0, 0,
                this.getTabellone().getCaselleTabellone()
                        .get(this.getPosizioneParcheggioGratuito(this))));
        /* 9 */imprevisti.add(
                new Carta(TipoCartaEnumeration.IMPREVISTO,
                        "Attenzione! Attacco nucleare: perdete tutte le costruzioni",
                        CategoriaCarteEnumeration.DISTRUZIONE, this, 0, 0, null));
        /* 10 */imprevisti.add(
                new Carta(TipoCartaEnumeration.IMPREVISTO, "Avete vinto alla lotteria! Gaudagnate 35",
                        CategoriaCarteEnumeration.GUADAGNA, this, 0, 35, null));
        /* 11 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO,
                "Dovete pagare la tassa scolastica, pagate 25", CategoriaCarteEnumeration.PAGA, this,
                25, 0, null));
        /* 12 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO,
                "Complimenti! Sieti riusciti a dimostrare che P = NP!!! Guadagnate 100",
                CategoriaCarteEnumeration.GUADAGNA, this, 0, 100, null));
        /* 13 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO,
                "Qualcuno vi ha rubato i dati della carta! Pagatelo 80 per farveli restituire",
                CategoriaCarteEnumeration.PAGA, this, 80, 0, null));
        /* 14 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO,
                "Vi e' caduto un meteorite sulla casa! Dovete pagare 75 per le riparazioni",
                CategoriaCarteEnumeration.PAGA, this, 75, 0, null));
        /* 15 */imprevisti.add(new Carta(TipoCartaEnumeration.IMPREVISTO,
                "Vanno pagate le tasse Universitarie! Pagate 45", CategoriaCarteEnumeration.PAGA, this,
                45, 0, null));

        // mischia delle carte
        Collections.shuffle(imprevisti);

        return imprevisti;
    }
    /* metodo per la creazione delle lista di imprevisti */

    /* metodo per la creazione delle lista di probabilità */
    public List<Carta> creaListaProbabilita() {
        List<Carta> probabilita = new ArrayList<>(15);

        /* 1 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA, "Versate 50 per beneficenza",
                CategoriaCarteEnumeration.PAGA, this, 50, 0, null));
        /* 2 */probabilita.add(
                new Carta(TipoCartaEnumeration.PROBABILITA,
                        "Il DropShipping sta andando alla grande! Guadagnate 80",
                        CategoriaCarteEnumeration.GUADAGNA, this, 0, 80, null));
        /* 3 */probabilita
                .add(new Carta(TipoCartaEnumeration.PROBABILITA, "La nonna ti regala 15 per il gelato",
                        CategoriaCarteEnumeration.GUADAGNA, this, 0, 15, null));
        /* 4 */probabilita
                .add(new Carta(TipoCartaEnumeration.PROBABILITA,
                        "C'e' un guasto alla CPU e va sostituita! Pagate 35",
                        CategoriaCarteEnumeration.PAGA, this, 35, 0, null));
        /* 5 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA, "Potrete uscire gratis di prigione",
                CategoriaCarteEnumeration.ESCIDIPRIGIONE, this, 0, 0, null));
        /* 6 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA, "Andate fino al Parcheggio gratuito",
                CategoriaCarteEnumeration.SPOSTATI, this, 0, 0,
                this.getTabellone().getCaselleTabellone()
                        .get(this.getPosizioneParcheggioGratuito(this))));
        /* 7 */probabilita.add(
                new Carta(TipoCartaEnumeration.PROBABILITA,
                        "E' arrivata la tassa della borsa di studio. Guadagnate 60",
                        CategoriaCarteEnumeration.GUADAGNA, this, 0, 60, null));
        /* 8 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA,
                "Andate fino al Via ma non riscuotete l'accredito", CategoriaCarteEnumeration.SPOSTATI,
                this,
                0, 0,
                this.getTabellone().getCaselleTabellone().get(0)));
        /* 9 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA,
                "Hai trovato una moneta d'oro. Vendendola ne ricavi un guadagno di 80",
                CategoriaCarteEnumeration.GUADAGNA, this, 0, 80, null));
        /* 10 */probabilita
                .add(new Carta(TipoCartaEnumeration.PROBABILITA,
                        "Lancia un dado: guadagna 10 per ogni punto sul dado",
                        CategoriaCarteEnumeration.GUADAGNAINBASEALDADO, this, 0, 0, null));
        /* 11 */probabilita.add(
                new Carta(TipoCartaEnumeration.PROBABILITA,
                        "Paga 20 per ogni Proprieta' senza edifici che possiedi",
                        CategoriaCarteEnumeration.PAGAPERPROPRIETASENZAEDIFICI, this, 0, 0,
                        null));
        /* 12 */probabilita.add(
                new Carta(TipoCartaEnumeration.PROBABILITA, "E' Natale! Tutti i giocatori guadagnano 30",
                        CategoriaCarteEnumeration.TUTTIIGIOCATORIGUADAGNANO, this, 0, 0, null));
        /* 13 */probabilita
                .add(new Carta(TipoCartaEnumeration.PROBABILITA,
                        "Ci sono delle spese mediche da coprire. Pagate 35",
                        CategoriaCarteEnumeration.PAGA, this, 35, 0, null));
        /* 14 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA,
                "Ricevete un regalo da una fondazione anonima. Guadagnate 60",
                CategoriaCarteEnumeration.GUADAGNA, this,
                0, 60, null));
        /* 15 */probabilita.add(new Carta(TipoCartaEnumeration.PROBABILITA,
                "Complimenti! Il vostro animaletto domestico ha vinto il concorso di bellezza. Vincete un premio di 40",
                CategoriaCarteEnumeration.GUADAGNA, this, 0, 40, null));

        // mischia delle carte
        Collections.shuffle(probabilita);

        return probabilita;
    }
    /* metodo per la creazione delle lista di probabilità */

    /* Override metodo toString di Object */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Numero turni Partita %s: %s", this.getNomePartita(), this.getTurniPartita()));
        sb.append("\n");
        sb.append(String.format("Stato Giocatori: "));
        sb.append("\n");
        for (Player p : this.getListaGiocatori()) {
            sb.append(String.format("\tNome Giocatore: %s Bilancio: %s Posizione: %s",
                    p.getNomeGiocatore(),
                    p.getBilancio(), p.getPosizioneGiocatore().getNomeCasella()));
            sb.append("\n");
        }
        return sb.toString();
    }
    /* Override metodo toString di Object */
}