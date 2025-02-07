package it.univaq.disim.lpo.monopoli.core.service;

import org.fusesource.jansi.Ansi;

import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaDiProprietaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.CasellaGiaPossedutaException;
import it.univaq.disim.lpo.monopoli.core.service.exception.SaldoInsufficienteException;

public interface CasellaAcquistabileService<T extends CasellaAcquistabile> extends CasellaService<T> {
    public void aggiornaTassa(Partita partita, Player player, T c);

    public void acquistaCasella(Partita partita, T casellaAcquistabile)
            throws SaldoInsufficienteException;

    public void aggiornamentoScambioGiocatoreDiTurno(Partita partita, Player player, T t);

    public void aggiornamentoScambioPlayer(Partita partita, Player player, T t);

    public void vendi(Partita partita, T t);

    public void visualizzaInformazioni (Partita partita, T t);

    public default boolean verificaSeLibera(Partita partita, CasellaAcquistabile casellaAcquistabile)
            throws CasellaGiaPossedutaException, CasellaDiProprietaException{
        if (casellaAcquistabile.getProprietario() != null && !(casellaAcquistabile.getProprietario().equals(partita.getGiocatoreDiTurno()))) {
            throw new CasellaGiaPossedutaException(
                    "La Casella " + casellaAcquistabile.getNomeCasella() + " e' gia' posseduta da un Giocatore");
        }
        if(casellaAcquistabile.getProprietario() != null && casellaAcquistabile.getProprietario().equals(partita.getGiocatoreDiTurno())) {
            throw new CasellaDiProprietaException("La Casella " + casellaAcquistabile.getNomeCasella() + " e' di tua Proprieta'");
        }


        System.out.println("La Casella " + casellaAcquistabile.getNomeCasella() + " non appartiene a nessun Giocatore");
        return true;
    }
    
    public default void pagaTassa(Partita partita, CasellaAcquistabile casellaAcquistabile) throws SaldoInsufficienteException {
        if(partita.getGiocatoreDiTurno().getBilancio() < casellaAcquistabile.getTassa()) {
            throw new SaldoInsufficienteException("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore() + " non ha abbastanza finanze per pagare la tassa");
        }
        System.out.println(Ansi.ansi().fg(partita.getGiocatoreDiTurno().getColoreGiocatore())
                .a("Il Giocatore " + partita.getGiocatoreDiTurno().getNomeGiocatore()
                        + " si trova su una casella posseduta dal Giocatore "
                        + casellaAcquistabile.getProprietario().getNomeGiocatore() + " e dovra' pagare una tassa di "
                        + casellaAcquistabile.getTassa())
                .reset());
        partita.getGiocatoreDiTurno().setBilancio(partita.getGiocatoreDiTurno().getBilancio() - casellaAcquistabile.getTassa());
        casellaAcquistabile.getProprietario().setBilancio(casellaAcquistabile.getProprietario().getBilancio() + casellaAcquistabile.getTassa());
    }
}