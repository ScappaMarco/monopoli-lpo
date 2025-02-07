package it.univaq.disim.lpo.monopoli.core.datamodel;

/*CasellaAcquistabile: entità figlia di casella che gestisce le caselle che possno essere comprate, ha tre figli: Proprieta, Servizio e Stazione*/
public abstract class CasellaAcquistabile extends Casella{
    private int prezzoAcquisto;
    private int tassa;
    private int prezzoVendita;
    private Player proprietario;

    public CasellaAcquistabile(int prezzoAcquisto, int tassa, int prezzoVendita, String nomeCasella) {
        super(nomeCasella);
        this.prezzoAcquisto = prezzoAcquisto;
        this.tassa = tassa;
        this.prezzoVendita = prezzoVendita;
    }

    /*metdo get e set per il prezzo di acquisto della proprietà*/
    public int getPrezzoAcquisto() {
        return this.prezzoAcquisto;
    }

    public void setPrezzoAcquisto(int prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }
    /*metdo get e set per il prezzo di acquisto della proprietà*/

    /*metodi get e set per il prezzo della tassa*/
    public int getTassa() {
        return this.tassa;
    }

    public void setTassa(int tassa) {
        this.tassa = tassa;
    }
    /*metodi get e set per il prezzo della tassa*/

    /*metodi get e set per il prezzo di vendita*/
    public int getPrezzoVendita() {
        return this.prezzoVendita;
    }

    public void setPrezzoVendita(int prezzoVendita) {
        this.prezzoVendita = prezzoVendita;
    }
    /*metodi get e set per il prezzo di vendita*/

    /*metodi get e set per il proprietario */
    public Player getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(Player proprietario) {
        this.proprietario = proprietario;
    }
    /*metodi get e set per il proprietario */
}