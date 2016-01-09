/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Hashtable;

/**
 * Classe per la generazione di anomalia dato un certo numero di argomento
 *
 * @author root
 */
public class Anomalia {

    /**
     * Riferimento linea file xml presentante anomalia
     */
    private Integer Riferimento_Linea;
    /**
     * Messaggio di errore da visualizzare all'utente per il campo indicato
     */
    private String Messaggio_Errore;
    private String Tipo_anomalia;

    /**
     * @return the Riferimento_Linea
     */
    public Integer getRiferimento_Linea() {
        return Riferimento_Linea;
    }

    /**
     * @param Riferimento_Linea the Riferimento_Linea to set
     */
    public void setRiferimento_Linea(Integer Riferimento_Linea) {
        this.Riferimento_Linea = Riferimento_Linea;
    }

    /**
     * @return the Messaggio_Errore
     */
    public String getMessaggio_Errore() {
        return Messaggio_Errore;
    }

    /**
     * @param Messaggio_Errore the Messaggio_Errore to set
     */
    public void setMessaggio_Errore(String Messaggio_Errore) {
        this.Messaggio_Errore = Messaggio_Errore;
    }

    /**
     * @return the Tipo_anomalia
     */
    public String getTipo_anomalia() {
        return Tipo_anomalia;
    }

    /**
     * @param Tipo_anomalia the Tipo_anomalia to set
     */
    public void setTipo_anomalia(String Tipo_anomalia) {
        this.Tipo_anomalia = Tipo_anomalia;
    }

    /**
     * Enumeration per gestire il tipo di anomalia Q(Quadratura), V(Validazione)
     */
    public enum t_a {

        Q, V;
    };
    @SuppressWarnings("UseOfObsoleteCollectionType")
    Hashtable<String, String> numbers
            = new Hashtable<String,String>();

    public Anomalia() {
        Riferimento_Linea = null;
        Messaggio_Errore = null;
        Tipo_anomalia = null;
        numbers.put("Q", "Anomalia di quadratura");
        numbers.put("V", "Anomalia di validazione");
    }

    /**
     * Verifica validità anomalia
     */
    public boolean check_validity_tipo_anomalia(String tipo_anomalia) {
        boolean valido = false;
        for (t_a d : t_a.values()) {
            if (d.toString().equals(tipo_anomalia)) {
                valido = true;
            }

        }
        return valido;
    }

    /**
     * Restituisce il testo completo dell'anomalia dato il suo codice
     */
    public String get_Hash_value(String get) {
        String n = numbers.get(get);
        return n;
    }

    /**
     * Metodo per creare un anomalia dato un campo, un tipo di anomalia, un
     * riferimento a un numero linea, un messaggio da visualizzare all'utente,
     * il valore che dovrebbe essere, il valore che risulta effettivamente
     *
     * @param campo
     * @param tipo_anomalia
     * @param rif_lin
     * @param error_message
     * @param spieg
     * @param campo_calc
     * @param campo_dovrebbe
     * @return
     */
    public String Crea_Anomalia(String campo, String tipo_anomalia, Integer rif_lin, String error_message, String spieg, Double campo_calc, Double campo_dovrebbe) {
        String anomalia = null;
        if (check_validity_tipo_anomalia(tipo_anomalia)) {
            String mess = get_Hash_value(tipo_anomalia);
            anomalia = mess + "    " + campo + "      " + rif_lin + "       " + error_message;
        }
        return anomalia;

    }

    /**
     *
     * @param campo1
     * @param campo2
     * @param tipo_anomalia
     * @param rif_lin
     * @param error_message
     * @param spieg
     * @param campo
     * @param campo3
     * @return
     */
    public String Crea_Anomalia(String campo1, String campo2, Double campo, Double campo3) {
        String anomalia = null;

        anomalia = "Errore di quadratura tra " + campo1 + " e " + campo2 + "    il campo " + campo1 + "risulta" + campo + "mentre il campo"
                + campo2 + "risulta" + campo3;
        return anomalia;
    }

    public String Crea_Anomalia(String campo, String tipo_anomalia, String error_message, Double campo_e, Double campo_dovrebbe) {
        String anomalia = null;
        if (check_validity_tipo_anomalia(tipo_anomalia)) {
            String mess = get_Hash_value(tipo_anomalia);
            anomalia = mess + "       " + campo + "       " + error_message + "   il campo " + campo + " dovrebbe essere " + campo_dovrebbe + " ma risulta " + campo_e;
        }
        return anomalia;
    }

}
