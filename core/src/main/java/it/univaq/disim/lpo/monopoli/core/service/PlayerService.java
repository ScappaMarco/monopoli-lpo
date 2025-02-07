package it.univaq.disim.lpo.monopoli.core.service;

import java.util.List;

import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
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

public interface PlayerService <T extends Player> {
    public ModeEnumeration getMode(Partita partita);
    public ActionEnumeration getAzione(Partita partita);
    public AzioniCasellaEnumeration getAzioneCasella();
    public AlbergoOCasaEnumeration getCasaOAlbergo(Partita partita);
    public void abbandona(Partita partita);
    public List<Integer> lanciaDadi(Partita partita);
    public void muoviti(Partita partita) throws GiocatoreInPrigioneException;
    public CasellaAcquistabile vendiCasellaAcquistabile(Partita partita) throws CategoriaCasellaVuotaException;
    public Proprieta selezionaProprieta (Partita partita) throws CategoriaCasellaVuotaException;
    public void costruisciCasa(Partita partita, Proprieta proprieta) throws ProprietaConTutteLeCaseException, SerieCompletaException, SaldoInsufficienteException;
    public void costruisciAlbergo(Partita partita, Proprieta proprieta) throws ProprietaConAlbergoException, ProprietaConPocheCaseException, SerieCompletaException, SaldoInsufficienteException;
    public void vendiCostruzione(Partita partita, Proprieta proprieta) throws ProprietaSenzaCostruzioniException;
    public boolean decidiEsitoScambio(Partita partita, Player giocatore, CasellaAcquistabile casellaDaScambiare, CasellaAcquistabile casellaDaRicevere);
    public void scambioAccettato(Partita partita, Player giocatore, CasellaAcquistabile casellaDaScambiare, CasellaAcquistabile casellaDaricevere);
    public void tentaDiUscireDiPrigione(Partita partita) throws SaldoInsufficienteException;
    public void visualizzaCasellePossedute(Partita partita);
    public void esciDiPrigione(Partita partita);
}
