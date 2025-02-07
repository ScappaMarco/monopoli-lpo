package it.univaq.disim.lpo.monopoli.core.service.impl.factoryes;

import it.univaq.disim.lpo.monopoli.core.datamodel.Cpu;
import it.univaq.disim.lpo.monopoli.core.datamodel.Player;
import it.univaq.disim.lpo.monopoli.core.datamodel.Umano;
import it.univaq.disim.lpo.monopoli.core.service.PlayerService;
import it.univaq.disim.lpo.monopoli.core.service.impl.CpuServiceImpl;
import it.univaq.disim.lpo.monopoli.core.service.impl.UmanoServiceImpl;

public class PlayerServiceFactory {
    private static final UmanoServiceImpl umano = new UmanoServiceImpl();
    private static final CpuServiceImpl cpu = new CpuServiceImpl();
    
    public static <T extends Player> PlayerService<? extends Player> getPlayerService(Class<T> clast) {
        if(clast == Umano.class) {
            return umano;
        }
        if(clast == Cpu.class) {
            return cpu;
        }
        return null;
    }
}
