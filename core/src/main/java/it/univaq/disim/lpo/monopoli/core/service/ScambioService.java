package it.univaq.disim.lpo.monopoli.core.service;

import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.service.exception.CategoriaCasellaVuotaException;

public interface ScambioService {
    public Player scegliGiocatoreScambio(Partita partita);
    public CasellaAcquistabile scegliCasellaDaScambiare(Partita partita, Player player) throws CategoriaCasellaVuotaException;
    public CasellaAcquistabile scegliCasellaDaRicevere(Partita partita, Player player) throws CategoriaCasellaVuotaException;
}
