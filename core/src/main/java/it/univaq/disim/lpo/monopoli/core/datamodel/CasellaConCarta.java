package it.univaq.disim.lpo.monopoli.core.datamodel;

/*CasellaConCarta: entità figlia di CasellaNonAcquistabile che gestisce le caselle in cui verranno pescate delle carte*/
public class CasellaConCarta extends CasellaNonAcquistabile {
    private TipoCartaEnumeration tipoCarta;
    
    public CasellaConCarta(String nomeCasella, TipoCartaEnumeration tipoCarta) {
        super(nomeCasella);
        this.tipoCarta = tipoCarta;
    }

    /*metodi get e set per il tipo della carta*/
    public TipoCartaEnumeration getTipoCarta() {
        return this.tipoCarta;
    }

    public void setTipoCarta(TipoCartaEnumeration tipoCarta) {
        this.tipoCarta = tipoCarta;
    }
    /*metodi get e set per il tipo della carta*/
}