package it.univaq.disim.lpo.monopoli.core.service;

import java.io.IOException;

import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.service.exception.InizializzaPartitaException;

public interface PartitaService {
    public Player sorteggioInizioPartita(Partita partita);
    public void gioca (Partita partita);
    public Partita inizializzaPartita() throws InizializzaPartitaException;
    public void salvaPartita (Partita p, String filename) throws CloneNotSupportedException, IOException, ClassNotFoundException;
    public Partita caricaPartita (String filename) throws CloneNotSupportedException, IOException, ClassNotFoundException;
    public void generaTabellone(int numeroGiocatori, Partita partita);
    public void generaMazziImprevistiEProbabilita(Partita partita);
    public void passaTurno (Partita partita);
    public void setPosizioneInizialeGiocatori(Partita partita);
}
