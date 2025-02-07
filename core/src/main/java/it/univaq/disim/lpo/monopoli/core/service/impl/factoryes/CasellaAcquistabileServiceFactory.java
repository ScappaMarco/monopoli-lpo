package it.univaq.disim.lpo.monopoli.core.service.impl.factoryes;

import it.univaq.disim.lpo.monopoli.core.datamodel.Casella;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaAcquistabile;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.datamodel.Servizio;
import it.univaq.disim.lpo.monopoli.core.datamodel.Stazione;
import it.univaq.disim.lpo.monopoli.core.service.CasellaAcquistabileService;
import it.univaq.disim.lpo.monopoli.core.service.impl.ProprietaServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.ServizioServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.StazioneServiceImpl;

public class CasellaAcquistabileServiceFactory {
    private static final ProprietaServiceImpl proprieta = new ProprietaServiceImpl();
    private static final ServizioServiceImpl servizio = new ServizioServiceImpl();
    private static final StazioneServiceImpl stazione = new StazioneServiceImpl();
    
    public static <T extends Casella> CasellaAcquistabileService<? extends CasellaAcquistabile> getCasellaAcquistabileService(Class<T> clazz) {
        if(clazz == Proprieta.class) {
            return proprieta;
        }
        if(clazz == Stazione.class) {
            return stazione;
        }
        if(clazz == Servizio.class) {
            return servizio;
        }
        return null;
    }
}