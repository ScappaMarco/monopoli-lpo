package it.univaq.disim.lpo.monopoli.core.datamodel;

/*Via: entità figlia di CasellaNonAcquistabile che gestisce il via*/
public class Via extends CasellaNonAcquistabile{
    private int prezzoAccredito;

    public Via(String nomeCasella, int prezzoAccredito) {
        super(nomeCasella);
        this.prezzoAccredito = prezzoAccredito;
    }

    /*metodi get e set per la somma di denaro che si riceve passando dal via*/
    public int getPrezzoAccredito() {
        return this.prezzoAccredito;
    }

    public void setPrezzoAccredito(int prezzoAccredito) {
        this.prezzoAccredito = prezzoAccredito;
    }
    /*metodi get e set per la somma di denaro che si riceve passando dal via*/
}