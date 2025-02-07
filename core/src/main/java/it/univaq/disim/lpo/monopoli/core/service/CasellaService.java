package it.univaq.disim.lpo.monopoli.core.service;

import it.univaq.disim.lpo.monopoli.core.datamodel.Casella;
import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.service.enumerations.AzioniCasellaEnumeration;

public interface CasellaService<T extends Casella> {
      public AzioniCasellaEnumeration getAzioneCasella(Partita partita, PlayerService playerService);
}
