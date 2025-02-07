package it.univaq.disim.lpo.monopoli.core;

import it.univaq.disim.lpo.monopoli.core.datamodel.Partita;
import it.univaq.disim.lpo.monopoli.core.service.PartitaService;
import it.univaq.disim.lpo.monopoli.core.service.exception.InizializzaPartitaException;
import it.univaq.disim.lpo.monopoli.core.service.impl.PartitaServiceImpl;

public class Runner {
    public static void main(String[] args) {
        PartitaService ps = new PartitaServiceImpl();
        try {
            Partita p = ps.inizializzaPartita();
            ps.gioca(p);
        } catch(InizializzaPartitaException e) {
            System.out.println("Va bene, giocheremo un'altra volta :)");
        }
    }
}