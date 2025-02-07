package it.univaq.disim.lpo.monopoli.core.datamodel;
import java.util.HashMap;
import java.util.Map;

/*PrigioneETransito: entità figlia di CasellaNonAcquistabile che gestisce due caselle nella stessa posizione: Prigione e Transito*/
public class PrigioneETransito extends CasellaNonAcquistabile {
    private Map<Player, Integer> giocatoriInPrigione;

    public PrigioneETransito(String nomeCasella) {
        super(nomeCasella);
        this.giocatoriInPrigione = new HashMap<>();
    }

    /*metodi per aggiungere e rimuovere un giocatore dalla prigione*/
    public <T extends Player> void aggiungiGiocatoreInPrigione(T g) {
        this.giocatoriInPrigione.put(g, g.getNumeroTurniInPrigione());
        g.setInPrigione(true);
    }

    public <T extends Player> void rimuoviGiocatoreInPrigione(T g) {
        this.giocatoriInPrigione.remove(g);
        g.setInPrigione(false);
    }
    /*metodi per aggiungere e rimuovere un giocatore dalla prigione*/
}