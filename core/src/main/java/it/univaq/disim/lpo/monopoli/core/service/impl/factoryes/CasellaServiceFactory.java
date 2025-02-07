package it.univaq.disim.lpo.monopoli.core.service.impl.factoryes;

import it.univaq.disim.lpo.monopoli.core.datamodel.Casella;
import it.univaq.disim.lpo.monopoli.core.datamodel.CasellaConCarta;
import it.univaq.disim.lpo.monopoli.core.datamodel.ParcheggioGratuito;
import it.univaq.disim.lpo.monopoli.core.datamodel.PrigioneETransito;
import it.univaq.disim.lpo.monopoli.core.datamodel.Proprieta;
import it.univaq.disim.lpo.monopoli.core.datamodel.Servizio;
import it.univaq.disim.lpo.monopoli.core.datamodel.Stazione;
import it.univaq.disim.lpo.monopoli.core.datamodel.Tassa;
import it.univaq.disim.lpo.monopoli.core.datamodel.VaiInPrigione;
import it.univaq.disim.lpo.monopoli.core.datamodel.Via;
import it.univaq.disim.lpo.monopoli.core.service.CasellaService;
import it.univaq.disim.lpo.monopoli.core.service.impl.CasellaConCartaServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.ParcheggioGratuitoServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.PrigioneETransitoServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.ProprietaServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.ServizioServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.StazioneServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.TassaServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.VaiInPrigioneServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.ViaServiceImpl;

public class CasellaServiceFactory {
    private static final ProprietaServiceImpl proprieta = new ProprietaServiceImpl();
    private static final ServizioServiceImpl servizio = new ServizioServiceImpl();
    private static final StazioneServiceImpl stazione = new StazioneServiceImpl();
    private static final ViaServiceImpl via = new ViaServiceImpl();
    private static final ParcheggioGratuitoServiceImpl parcheggioGratuito = new ParcheggioGratuitoServiceImpl();
    private static final PrigioneETransitoServiceImpl prigioneETransito = new PrigioneETransitoServiceImpl();
    private static final VaiInPrigioneServiceImpl vaiInPrigione = new VaiInPrigioneServiceImpl();
    private static final CasellaConCartaServiceImpl casellaConCarta = new CasellaConCartaServiceImpl();
    private static final TassaServiceImpl tassa = new TassaServiceImpl();
    
    public static <T extends Casella> CasellaService <? extends Casella> getCasellaService(Class<T> clazz) {
        if(clazz == Proprieta.class) {
            return proprieta;
        }
        if(clazz == Servizio.class) {
            return servizio;
        }
        if(clazz == Stazione.class) {
            return stazione;
        }
        if(clazz == Via.class) {
            return via;
        }
        if(clazz == ParcheggioGratuito.class) {
            return parcheggioGratuito;
        }
        if(clazz == PrigioneETransito.class) {
            return prigioneETransito;
        }
        if(clazz == VaiInPrigione.class) {
            return vaiInPrigione;
        }
        if(clazz == CasellaConCarta.class) {
            return casellaConCarta;
        }
        if(clazz == Tassa.class) {
            return tassa;
        }
        return null;
    }
}
