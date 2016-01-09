/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author root Anomalie di quadratura Classe per la gestione delle quadrature
 *
 */
public class AnomalieQuadratura {

    /**
     * Tipo Anomalia
     */
    public static String tipo_anomalia = "Q";

    /**
     * @return the tipo_anomalia
     */
    public static String getTipo_anomalia() {
        return tipo_anomalia;
    }

    /**
     * @param aTipo_anomalia the tipo_anomalia to set
     */
    public static void setTipo_anomalia(String aTipo_anomalia) {
        tipo_anomalia = aTipo_anomalia;
    }
    /**
     * Stringa contenente il File XML Fattura PA
     */
    public String file_xml;
    /**
     * Lista di stringhe contenente le anomalie generate dalla classe in oggetto
     */
    public List<String> lista_anomalie;
    /**
     * Inizializzazione e instanziazione classe anomalia utile per combinare
     * stringhe relative alle anomalie
     */
    public Anomalia m = new Anomalia();
    public Commerciale comm;

    public AnomalieQuadratura(String file) {
        this.comm = new Commerciale(file);
        this.lista_anomalie = new ArrayList<String>();
        this.file_xml = file;

    }

    /**
     * Metodo per verificare la quadratura dell'imponibile rispetto alle righe
     * dettaglio sia per totale, che per aliquota
     *
     * @param number
     * @return List anomalie;
     */
    public Double round(Double number) {
        double numero;
        numero = Math.pow(10, 2);
        numero = Math.round(numero * number) / numero;
        return numero;
    }

    public List<String> verifica_quadratura_imponibile() {
        String an = "";
        Double somma_imponibili_dett = 0.00;
        somma_imponibili_dett = getComm().return_somma_imponibili();
        Double somma_imponibili_dett_arr = 0.00;
        somma_imponibili_dett_arr = round(somma_imponibili_dett);
        Double somma_imponibili_riep = getComm().return_somma_imponibili_riepilogo();
        Double somma_imponibili_riep_arr = round(somma_imponibili_riep);
        Double totale_spese_accessorie = getComm().totale_spese_accessorie();
        Double totale = somma_imponibili_dett_arr + totale_spese_accessorie;
        /* controllo quadratura tra totale imponibile e totale righe dettaglio */
        if (!(totale).equals(somma_imponibili_riep_arr)) {
            an = getM().Crea_Anomalia("Imponibile", "Q", "Errore di quadratura sul totale", somma_imponibili_riep_arr,
                    totale);
            //Andiamo a vedere pertanto le righe incriminate con il metodo
        }

        this.getLista_anomalie().add(an);
        /*stesso controllo di prima ma fatto per ogni aliquota del documento (prendendo dal riepilogo)*/
        List<Double> aliquote = getComm().return_lista_aliquote();

        String anomalia = null;
        int conta = 0;
        for (Double aliquote1 : aliquote) {
            conta++;
            System.out.println("Aliquota in esame" + aliquote1);
            Double somma_dett_al = getComm().return_somma_dett_per_aliquota(aliquote1.doubleValue());
            Double somma_dett_alo = round(somma_dett_al);
            System.out.println("Somma dettagli aliquota  " + somma_dett_al);
            Double somma_riep_al = getComm().return_somma_imponibili_riepilogo_per_aliquota(aliquote1.doubleValue());

            Double somma_riep_alo = round(somma_riep_al);
            Double spese_accessorie_iva = getComm().return_somma_spese_accessorie_aliquota(aliquote1);
            Double totale2 = somma_dett_alo + spese_accessorie_iva;
            System.out.println("Somma riepilogo aliquota   " + somma_riep_al);

            if (!totale2.equals(somma_riep_alo)) {
                anomalia = getM().Crea_Anomalia("Imponibile aliquota " + aliquote1, "Q", "Errore di quadratura sul totale riepilogo"
                        + "rispetto ai dettagli del file", somma_riep_alo, totale2);
                this.getLista_anomalie().add(anomalia);
            }

        }
        System.out.println("Contagiro" + conta);
        /*Controlla per riepilogo 
        
       
         */
        return this.getLista_anomalie();
    }

    public void verifica_quadratura_iva() throws XPathExpressionException {
        this.getLista_anomalie().addAll(getComm().check_iva());
    }
    /* public void verifica_quadratura_iva() {
     */

    public void verifica_quadratura_prezzo_unitario_prezzototale() throws XPathExpressionException {
        this.getLista_anomalie().addAll(getComm().check_prezzo_unitario_prezzo_totale());
    }

    /*
     List<Double> aliquote = getComm().return_lista_aliquote();
            
     String anomalia = null;
     int conta = 0;
     Double somma_iva_dettaglio=0.00;
     Double arr_somma_iva_dettaglio = 0.00;
     Double somma_iva_riepilogo = 0.00;
     Double arr_somma_iva_riepilogo = 0.00;
     Double somma_riepilogo_in_esame = 0.00;
     Double arr_somma_riepilogo_in_esame = 0.00;
     List <Double> riepilogo_per_aliquota = new ArrayList<>();
     List <Double> dettagli_per_aliquota = new ArrayList<>();

     for (Double aliquote1 : aliquote) {
     riepilogo_per_aliquota = getComm().return_lista_imponibili_riepilogo_per_aliquota(aliquote1);
     for(Double riepilogo:riepilogo_per_aliquota){
                    
     somma_iva_riepilogo +=riepilogo*aliquote1/100;
     if(somma_iva_riepilogo.equals)
      
      
      
     }
     arr_somma_iva_riepilogo = Math.ceil(somma_iva_riepilogo*100);
     arr_somma_iva_riepilogo /= 100;
                
     {
     List<Double> aliquote = getComm().return_lista_aliquote();
            
     List <String> anomalies = new ArrayList<>();
     String anomalia = null;
     for (Double aliquote1 : aliquote) {
     System.out.println("Aliquota in esame"+aliquote1);
     Double somma_dett_al = getComm().return_somma_dett_per_aliquota(aliquote1);
     Double somma_riep_al = getComm().return_somma_imponibili_riepilogo_per_aliquota(aliquote1);
     if(!somma_dett_al.equals(somma_riep_al)){
     anomalia = getM().Crea_Anomalia("Imponibile aliquota "+aliquote1,"Q", "Errore di quadratura sul totale riepilogo"
     + "rispetto ai dettagli del file",somma_riep_al,somma_dett_al);
     }
     }
     this.getLista_anomalie().add(anomalia);

                
                
                
                
                
                
                
                
                
               
     }
     
     
     /**
     *
     * @return
   
  
     /**
     * Restituisce la lista delle anomalie generate tramite la classe Anomalia 
     * @return 
     */
    public List<String> return_lista_anomalie() {
        return Collections.unmodifiableList(this.getLista_anomalie());
    }

    /**
     * @return the file_xml
     */
    public String getFile_xml() {
        return file_xml;
    }

    /**
     * @param file_xml the file_xml to set
     */
    public void setFile_xml(String file_xml) {
        this.file_xml = file_xml;
    }

    /**
     * @return the lista_anomalie
     */
    public List<String> getLista_anomalie() {
        return lista_anomalie;
    }

    /**
     * @param lista_anomalie the lista_anomalie to set
     */
    public void setLista_anomalie(List<String> lista_anomalie) {
        this.lista_anomalie = lista_anomalie;
    }

    /**
     * @return the m
     */
    public Anomalia getM() {
        return m;
    }

    /**
     * @param m the m to set
     */
    public void setM(Anomalia m) {
        this.m = m;
    }

    /**
     * @return the comm
     */
    public Commerciale getComm() {
        return comm;
    }

    /**
     * @param comm the comm to set
     */
    public void setComm(Commerciale comm) {
        this.comm = comm;
    }

}

