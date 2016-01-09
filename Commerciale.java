

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author root
 */
public class Commerciale {

    /**
     * classe per valorizzare gli importi nel pannello commerciale accanto alle
     * anomalie 14.30 -> 23 giugno 2015 by Luca Traversari
     */

    private Double arrontodamento_di_testa;
    private Double abbuono_di_testa;
    private Double somma_sconti_di_dettaglio;
    private Double somma_imponibili;
    private Double somma_spese_accessorie;
    private Double somma_iva_riepilogo;
    private Double totale;

    private DocumentBuilderFactory factory
            = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = null;
    private NodeList node = null;
    private NodeList node2 = null;

    private Document doc = null;
    private String file_xml_importato = null;
    private XPath xPath = null;
    private XPath xpath2 = null;

    public Commerciale(String file_xml_xml) {

//NodeList node2 = null;
//NodeList node3 = null;
//NodeList node4 = null;
//NodeList node5 = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Commerciale.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Errore file vuoto");
        }

        file_xml_importato = file_xml_xml;
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(file_xml_importato);
        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Commerciale.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            doc = builder.parse(input);
        } catch (SAXException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

    }

    public NodeList get_righe_dettaglio() {

        setxPath(XPathFactory.newInstance().newXPath());
        NodeList righe_dettaglio = null;
        String expression = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        try {
            righe_dettaglio = (NodeList) (getxPath().compile(expression).evaluate(getDoc(), XPathConstants.NODESET));
        } catch (XPathExpressionException ex) {
        }
        return righe_dettaglio;

    }

    public Double round(Double number) {
        double numero;
        numero = Math.pow(10, 2);
        numero = Math.round(numero * number) / numero;
        return numero;
    }

    public Double somma_sconti_dettaglio(String dett) {
        setxPath(XPathFactory.newInstance().newXPath());
        NodeList righe_dettaglio = null;
        Double somma = 0.00;
        String expression = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee[NumeroLinea=" + dett + "]/ScontoMaggiorazione";
        System.out.println("Expression" + expression);
        try {
            righe_dettaglio = (NodeList) (getxPath().compile(expression).evaluate(getDoc(), XPathConstants.NODESET));
        } catch (XPathExpressionException ex) {
        }
        for (int i = 0; i < righe_dettaglio.getLength(); i++) {
            somma = 0.00;
            Node nNode = righe_dettaglio.item(i);
            System.out.println("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getElementsByTagName("Importo").getLength() > 0 && eElement.getElementsByTagName("Percentuale").getLength() == 0) {
                    somma += Double.parseDouble(eElement.getElementsByTagName("Importo").item(0).getTextContent().trim());
                }
                if (eElement.getElementsByTagName("Importo").getLength() == 0) {
                    if (eElement.getElementsByTagName("Percentuale").getLength() > 0) {
                        somma += round(Double.parseDouble(eElement.getElementsByTagName("Percentuale").item(0).getTextContent().trim()));
                    }

                }
                if (eElement.getElementsByTagName("Importo").getLength() > 0 && eElement.getElementsByTagName("Percentuale").getLength() > 0) {
                    somma += Double.parseDouble(eElement.getElementsByTagName("Importo").item(0).getTextContent().trim());
                }
            }
        }
        return somma;
    }

    public Double return_arrotondamento_di_testa() {
        setxPath(XPathFactory.newInstance().newXPath());
        String abb = null;
        Double abbuo = 0.00;

        String expression = "FatturaElettronica/FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento";
        try {

            this.setNode2((NodeList) getxPath().compile(expression).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {

        }

        for (int i = 0; i < getNode2().getLength(); i++) {
            Node nNode = getNode2().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                /* System.out.println("Arrotondamento : " 
                 + eElement
                 .getElementsByTagName("Arrotondamento")
                 .item(0)
                 .getTextContent());*/
                if (eElement.getElementsByTagName("Arrotondamento").getLength() >= 1) {
                    abb = eElement.getElementsByTagName("Arrotondamento").item(0).getTextContent().trim();
                    abbuo = Double.parseDouble(abb);
                }

            }
        }
        return abbuo;

    }

    public Double return_abbuono_di_testa() {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;

        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/ScontoMaggiorazione";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
        

        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                System.out.println("Importo: "
                        + eElement2
                        .getElementsByTagName("Importo")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("Importo").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("Importo").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("Tipo").item(0).getTextContent().trim();
                    if ("SC".equals(sc)) {
                        arro = abs(Double.parseDouble(arr));
                    }
                }
            }
        }
        return arro;
    }

    public Double return_maggiorazione_di_testa() {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;

        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/ScontoMaggiorazione";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
           
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                System.out.println("Importo: "
                        + eElement2
                        .getElementsByTagName("Importo")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("Importo").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("Importo").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("Tipo").item(0).getTextContent().trim();
                    if ("MG".equals(sc)) {
                        arro = abs(Double.parseDouble(arr));
                    }
                }
            }
        }
        return arro;
    }

    public Double return_somma_imponibili() {
        XPath xpath3 = XPathFactory.newInstance().newXPath();
        String prezzo_totale = null;
        Double prezzo_tot = 0.00;

        String expression2 = "sum(FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/PrezzoTotale)";
        try {
            prezzo_totale = xpath3.compile(expression2).evaluate(getDoc());

        } catch (XPathExpressionException ex) {
          
        }

        prezzo_tot = Double.parseDouble(prezzo_totale);

        return prezzo_tot;

    }

    public Double return_somma_dett_per_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        Double tot = 0.00;
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
           
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;
                System.out.println("Aliquota che viene passata" + al);

                System.out.println("PrezzoTotale "
                        + eElement2
                        .getElementsByTagName("PrezzoTotale")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("PrezzoTotale").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("PrezzoTotale").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sca = Double.parseDouble(sc);
                    if (al.equals(sca)) {
                        arro = Double.parseDouble(arr);
                        tot += arro;
                    }
                }
            }
        }
        return tot;
    }

    public Double return_somma_iva_dett_per_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        Double tot = 0.00;
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;
                System.out.println("Aliquota che viene passata" + al);

                System.out.println("PrezzoTotale "
                        + eElement2
                        .getElementsByTagName("PrezzoTotale")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("PrezzoTotale").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("PrezzoTotale").item(0).getTextContent().trim();

                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sca = Double.parseDouble(sc);
                    Double iva_dett = 0.00;
                    if (al.equals(sca)) {
                        arro = Double.parseDouble(arr);
                        iva_dett = arro * sca / 100;
                        tot += iva_dett;
                    }
                }
            }
        }
        return round(tot);
    }

    public List<Double> return_lista_dett_per_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        List<Double> list = new ArrayList<Double>();
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
           
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                System.out.println("PrezzoTotale "
                        + eElement2
                        .getElementsByTagName("PrezzoTotale")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("PrezzoTotale").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("PrezzoTotale").item(0).getTextContent().trim();

                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sco = Double.parseDouble(sc);
                    if (sco.equals(al)) {
                        arro = Double.parseDouble(arr);
                        list.add(arro);
                    }
                }
            }
        }
        return list;
    }

    public Double return_somma_imponibili_riepilogo() {
        XPath xpath3 = XPathFactory.newInstance().newXPath();
        String prezzo_totale = null;
        Double prezzo_tot = 0.00;

        String expression2 = "sum(FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo/ImponibileImporto)";
        try {
            prezzo_totale = xpath3.compile(expression2).evaluate(getDoc());

        } catch (XPathExpressionException ex) {
           
        }

        prezzo_tot = Double.parseDouble(prezzo_totale);

        return prezzo_tot;

    }

    public Double return_somma_sconti_dettaglio() throws XPathExpressionException {
        XPath xpath3 = XPathFactory.newInstance().newXPath();
        String prezzo_totale = null;
        Double prezzo_tot = 0.00;

        StringBuilder expression = new StringBuilder();
        expression.append("sum(FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/ScontoMaggiorazione[Tipo='SC']/Importo)");
        prezzo_totale = xpath3.compile(expression.toString()).evaluate(getDoc());

        prezzo_tot = Double.parseDouble(prezzo_totale);

        return prezzo_tot;

    }

    public Double return_somma_maggiorazioni_dettaglio() throws XPathExpressionException {
        XPath xpath3 = XPathFactory.newInstance().newXPath();
        String prezzo_totale = null;
        Double prezzo_tot = 0.00;

        StringBuilder expression = new StringBuilder();
        expression.append("sum(FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/ScontoMaggiorazione[Tipo='MG']/Importo)");
        prezzo_totale = xpath3.compile(expression.toString()).evaluate(getDoc());

        prezzo_tot = Double.parseDouble(prezzo_totale);

        return prezzo_tot;

    }

    public Double return_somma_imponibili_riepilogo_per_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        Double tot = 0.00;
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {

        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                System.out.println("ImponibileImporto "
                        + eElement2
                        .getElementsByTagName("ImponibileImporto")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("ImponibileImporto").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("ImponibileImporto").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sca = Double.parseDouble(sc);

                    if (sca.equals(al)) {
                        arro = Double.parseDouble(arr);
                        tot += arro;
                    }
                }
            }
        }
        return tot;
    }

    public List<Double> return_lista_imponibili_riepilogo_per_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        List<Double> list = new ArrayList<Double>();
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
          
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                System.out.println("ImponibileImporto "
                        + eElement2
                        .getElementsByTagName("ImponibileImporto")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("ImponibileImporto").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("ImponibileImporto").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sconto = Double.parseDouble(sc);
                    if (sconto.equals(al)) {
                        arro = Double.parseDouble(arr);
                        list.add(arro);
                    }
                }
            }
        }
        return list;
    }

    public Double totale_spese_accessorie() {
        XPath xpath3 = XPathFactory.newInstance().newXPath();
        String prezzo_totale = null;
        Double prezzo_tot = 0.00;

        String expression3 = "sum(FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo/SpeseAccessorie)";
        try {
            prezzo_totale = xpath3.compile(expression3).evaluate(getDoc());
            System.out.println("merda");

        } catch (XPathExpressionException ex) {
          
        }

        prezzo_tot = Double.parseDouble(prezzo_totale);

        return prezzo_tot;

    }

    /**
     *
     * @param al
     * @return
     */
    public Double return_somma_spese_accessorie_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        Double tot = 0.00;
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
         
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                if (eElement2.getElementsByTagName("SpeseAccessorie").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("SpeseAccessorie").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sca = Double.parseDouble(sc);

                    if (sca.equals(al)) {
                        arro = Double.parseDouble(arr);
                        tot += arro;
                    }
                }
            }
        }
        return tot;
    }

    /*Gestione___Quadratura_IVA*/
    public List<String> check_quadratura_scontomaggiorazione_dettaglio(Double n_linea, Double prezzo_totale, Double toto) throws XPathExpressionException {

        /* per ogni sconto o maggiorazione su quel dettaglio
         se è presente importo e %, effettuo il confronto e verifico se non è corretto -> anomalia
         sommo pertanto gli importi sconti calcolati con la percentuale corretta da U-GOV
         se è presente solo importo, sommo gli importi e confronto con il totale.
         se è presente solo percentuale, effettuo il confronto e verifico se non è corretto -> anomalia
         sommo pertanto gli importi che ho calcolato e li confronto con il totale.
         la stessa cosa per la maggiorazioni solo con i segni invertiti
         */
        List<String> lista_anomalie = new ArrayList<String>();
        setXpath2(XPathFactory.newInstance().newXPath());
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee[NumeroLinea=" + n_linea + "]/ScontoMaggiorazione";
        NodeList nodo = ((NodeList) getxPath().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        Double somma_maggiorazione = 0.00;
        Double somma_sconto = 0.00;
        Double conta = 0.00;
        Double totale_calcolato = prezzo_totale;

        for (int i = 0; i < nodo.getLength(); i++) {
            conta++;
            System.out.println("\nCurrent Element :"
                    + nodo.item(i).getNodeName());
            Element eElement2 = (Element) nodo.item(i);
            if (eElement2.getElementsByTagName("Importo").getLength() >= 1
                    && eElement2.getElementsByTagName("Percentuale").getLength() >= 1) {
                String per = eElement2.getElementsByTagName("Percentuale").item(0).getTextContent().trim();
                Double perc = round(Double.parseDouble(per));
                System.out.println("Percentuale" + perc);

                String imp = eElement2.getElementsByTagName("Importo").item(0).getTextContent().trim();

                Double impo = round(Double.parseDouble(imp));
                System.out.println("Importo" + imp);

                String tipo = eElement2.getElementsByTagName("Tipo").item(0).getTextContent().trim();
                System.out.println("Tipologia" + tipo);
                Double impo_calc = round((totale_calcolato * perc) / 100);
                System.out.println("Totale calcolato" + totale_calcolato);
                System.out.println("Importo calcolato" + impo_calc);
                if ("SC".equals(tipo)) {

                    somma_sconto += impo_calc;
                    totale_calcolato -= impo_calc;
                }
                if ("MG".equals(tipo)) {
                    somma_sconto += impo_calc;
                    totale_calcolato += impo_calc;
                }
                if (!impo_calc.equals(impo)) {
                    if ("SC".equals(tipo)) {
                        lista_anomalie.add(" Riga dettaglio numero " + n_linea
                                + "Errore di quadratura tra percentuale e importo sconto nel file" + "l'importo dello sconto dovrebbe essere" + impo_calc + " ma invece risulta" + impo);

                    }
                    if ("MG".equals(tipo)) {
                        lista_anomalie.add("Riga dettaglio numero" + n_linea + "Errore di quadratura tra percentuale e importo maggiorazione nel file" + "l'importo della maggiorazione dovrebbe essere" + impo_calc + " ma invece risulta" + impo);

                    }
                }
            }
            if (eElement2.getElementsByTagName("Importo").getLength() >= 1
                    && eElement2.getElementsByTagName("Percentuale").getLength() == 0) {

                String imp = eElement2.getElementsByTagName("Importo").item(0).getTextContent().trim();
                Double impo = round(Double.parseDouble(imp));
                String tipo = eElement2.getElementsByTagName("Tipo").item(0).getTextContent().trim();

                if ("SC".equals(tipo)) {

                    totale_calcolato -= impo;
                }
                if ("MG".equals(tipo)) {
                    totale_calcolato += impo;
                }

            }
            if (eElement2.getElementsByTagName("Importo").getLength() == 0
                    && eElement2.getElementsByTagName("Percentuale").getLength() == 1) {

                String imp = eElement2.getElementsByTagName("Percentuale").item(0).getTextContent().trim();
                Double impo = round(Double.parseDouble(imp));
                Double impoperc = round((totale_calcolato * impo) / 100);
                String tipo = eElement2.getElementsByTagName("Tipo").item(0).getTextContent().trim();

                if ("SC".equals(tipo)) {

                    totale_calcolato -= impoperc;
                }
                if ("MG".equals(tipo)) {
                    totale_calcolato += impoperc;
                }
            }

        }

        if (!(round(totale_calcolato)).equals(round(toto))) {
            lista_anomalie.add("Riga dettaglio numero "
                    + n_linea + "Sconto: Errore di quadratura sul totale Prezzo Unitario* Quantità -sconti +maggiorazioni farebbe" + round(totale_calcolato)
                    + " ma nel file è indicato " + round(toto));
        }
        System.out.println("ciao" + conta);
        return lista_anomalie;
    }

    public List<String> check_prezzo_unitario_prezzo_totale() throws XPathExpressionException {

        /* Controllo di quadratura tra prezzo unitario e prezzo totale nel file */
        /* Per ogni riga 
   
        
         dettaglio del file xml
         prendere la quantità (se non è indicata viene implicitamente considerata 1)
         if(presente_sconto o maggiorazione di dettaglio)
         {
         verifica_quddratrua_scontomaggiorazione_dettaglio(int n_linea){
            
         }
         else{
         controllo tra prezzo unitario e prezzo quantità e confronto con il prezzo totale.
         se il prezzo totale differisce errore di quadratura tra prezzo unitario e prezzo 
         totale del file
         }
         }
        
       
        
         */
        setXpath2(XPathFactory.newInstance().newXPath());
        List<String> anomalie = new ArrayList<String>();
        String pu;
        Double put;
        put = null;
        Double quantita;
        quantita = null;
        String quantitat;
        quantitat = null;
        Double tot;
        tot = 0.00;
        String totale = null;
        Double totale2 = null;
        Double lineadett = null;
        String lindett = null;
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
           
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;
                pu = eElement2.getElementsByTagName("PrezzoUnitario").item(0).getTextContent().trim();
                put = Double.parseDouble(pu);
                if (eElement2.getElementsByTagName("Quantita").getLength() >= 1) {
                    quantitat = eElement2.getElementsByTagName("Quantita").item(0).getTextContent().trim();
                    quantita = Double.parseDouble(quantitat);
                } else {
                    quantita = 1.00;
                }
                tot = round(quantita * put);
                totale = eElement2.getElementsByTagName("PrezzoTotale").item(0).getTextContent().trim();
                totale2 = Double.parseDouble(totale);
                lindett = eElement2.getElementsByTagName("NumeroLinea").item(0).getTextContent().trim();
                lineadett = Double.parseDouble(lindett);
                if (eElement2.getElementsByTagName("ScontoMaggiorazione").getLength() == 0) {
                    if (!(totale2.equals(tot))) {
                        anomalie.add("Riga dettaglio numero" + lineadett + "Errore di Quadratura tra prezzo unitario e prezzo totale"
                                + "nel file " + "Il prezzo totale dovrebbe essere " + tot + "ma risulta" + totale2);
                    }

                }
                if (eElement2.getElementsByTagName("ScontoMaggiorazione").getLength() >= 1) {

                    anomalie.addAll(check_quadratura_scontomaggiorazione_dettaglio(lineadett, tot, totale2));
                }

            }
        }
        return anomalie;

    }

    public List<String> check_iva() throws XPathExpressionException {
        Anomalia an = new Anomalia();
        XPath xpath4 = XPathFactory.newInstance().newXPath();
        List<String> lista_anomalie = new ArrayList<String>();
        String expression3;
        expression3 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo";
        String expression4;
        expression4 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        List<Double> lista_aliquote = this.return_lista_aliquote();
        List<Double> lista_aliquote2 = this.return_lista_aliquote_dett();

        NodeList nodonodo = (NodeList) xpath4.compile(expression3).evaluate(getDoc(), XPathConstants.NODESET);
        XPath xpath5 = XPathFactory.newInstance().newXPath();
        NodeList nodonodo2 = (NodeList) xpath5.compile(expression4).evaluate(getDoc(), XPathConstants.NODESET);
        System.out.println("Lunghezza nodono2" + nodonodo2.getLength());
        Double somma_iva_riepilogo_calcolata = 0.00;
        Double somma_iva_riepilogo_file = 0.00;
        Double somma_iva_dettaglio = 0.00;
        int n_riepilogo_per_aliquota = 0;
        for (Double aliquota : lista_aliquote) {
            somma_iva_dettaglio = 0.00;
            somma_iva_riepilogo_file = 0.00;
            somma_iva_riepilogo_calcolata = 0.00;
            n_riepilogo_per_aliquota++;
            System.out.println("Analisi aliquota IVA"
                    + aliquota);
            for (int i = 0; i < nodonodo.getLength(); i++) {
                Node nNode2 = getNode().item(i);
                System.out.println("\nCurrent Element :"
                        + nNode2.getNodeName());
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement2 = (Element) nNode2;
                    String aliquota_trov = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double aliquota_trova = Double.parseDouble(aliquota_trov);
                    if (aliquota_trova.equals(aliquota)) {

                        n_riepilogo_per_aliquota++;
                        String imponibile = eElement2.getElementsByTagName("ImponibileImporto").item(0).getTextContent().trim();
                        Double imponibilo = Double.parseDouble(imponibile);
                        String imposta = eElement2.getElementsByTagName("Imposta").item(0).getTextContent().trim();
                        Double imposto = Double.parseDouble(imposta);
                        Double imponibilo_arr = Math.round(imponibilo * Math.pow(10, 2)) / Math.pow(10, 2);
                        Double imposto_arr = Math.round(imposto * Math.pow(10, 2)) / Math.pow(10, 2);
                        Double imposta_calcolata = imponibilo_arr * aliquota / 100;
                        Double imposta_calcolata_arr = Math.round(imposta_calcolata * Math.pow(10, 2)) / Math.pow(10, 2);
                        /* verifica congruità imponibile e imposta */
                        somma_iva_riepilogo_calcolata += round(imposta_calcolata_arr);
                        somma_iva_riepilogo_file += round(imposto_arr);
                        if (!imposto_arr.equals(imposta_calcolata_arr)) {

                            String anomalia = an.Crea_Anomalia("Riepilogo iva  " + aliquota + "numero" + n_riepilogo_per_aliquota, "Q", "Errore di quadratura dell'iva sul riepilogo", imposto_arr, imposta_calcolata);
                            lista_anomalie.add(anomalia);
                            System.out.println(anomalia);
                        }
                        System.out.println("Riepilogo aliquota" + aliquota);
                        System.out.println("IVa corretta" + somma_iva_riepilogo_calcolata);
                        System.out.println("IVa del file" + somma_iva_riepilogo_file);
                    }
                    /*aggiungere somma delle ive del riepilogo dell'importo calcolato da U-GOv e confrontarlo
                     con la somma delle ive del riepilogo relativo. 
                     Se non è uguale errore di quadratura sul riepilogo IVA */
                }

            }
            /*inizio ciclo righe dettaglio
             Per ogni riga dettaglio prendo le righe dell'aliquota in questione
             e per ogni riga calcola imponibile * iva /100. 
             Ottengo pertanto anche in questa una somma delle iva dettaglio.
             */

            for (int i = 0; i < nodonodo2.getLength(); i++) {

                Node nNode3 = nodonodo2.item(i);
                System.out.println("\nCurrent Element :"
                        + nNode3.getNodeName());
                if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement3 = (Element) nNode3;
                    String aliquota_trova = eElement3.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double aliquota_trovat = Double.parseDouble(aliquota_trova);
                    if (aliquota_trovat.equals(aliquota)) {
                        String prezzo_totale = eElement3.getElementsByTagName("PrezzoTotale").item(0).getTextContent().trim();
                        Double totale_d = Double.parseDouble(prezzo_totale);

                        somma_iva_dettaglio += totale_d * aliquota / 100;
                        System.out.println("iva_dettaglio " + somma_iva_dettaglio);
                    }
                }
            }
            Double spese_accessorie = 0.00;
            spese_accessorie = this.return_somma_spese_accessorie_aliquota(aliquota);
            System.out.println(spese_accessorie);
            somma_iva_dettaglio += round(spese_accessorie * aliquota / 100);
            System.out.println(round(spese_accessorie * aliquota / 100));
            if (!(round(somma_iva_dettaglio).equals(round(somma_iva_riepilogo_file)))) {
                System.out.println(""
                        + "Per l'aliquota " + aliquota
                        + " l'iva dei dettagli e l'iva del riepilogo non coincide "
                        + "    l'iva nei dettagli risulta di   " + round(somma_iva_dettaglio)
                        + "mentre l'iva del riepilogo risulta    " + round(somma_iva_riepilogo_calcolata));
                // String anomalia2 = an.Crea_Anomalia("IVA dettaglio ","Iva riepilogo"+aliquota,round(somma_iva_dettaglio),round(somma_iva_riepilogo_calcolata));
                StringBuilder appendo = new StringBuilder();
                appendo = appendo.append("Per l'aliquota ").append(aliquota).append(" l'iva dei dettagli e l'iva del riepilogo non coincide " + "    l'iva nei dettagli risulta di   ").append(round(somma_iva_dettaglio)).append("mentre l'iva del riepilogo risulta    ").append(round(somma_iva_riepilogo_file));
                lista_anomalie.add(appendo.toString());

            }
            /* usciti dai due cicli interni (uno riepilogo, uno dettaglio)
             confronto le somme ottenute delle ive dei dettagli
             con quelle dei riepiloghi relativi.
             Se non coincidono Riepilogo aliquota tal dei tali,
             Errore di Quadratura, Il riepilogo iva non coincide con quello 
             indicato nei dettagli. L'importo è ma dovrebbe essere ----
                
                    
             */
            System.out.println("Riepilogo iva " + aliquota);
            System.out.println("Totale iva  aliquotacalcolata " + somma_iva_riepilogo_calcolata);
            System.out.println("Totale iva file" + somma_iva_riepilogo_file);
            /*if(!somma_iva_riepilogo_calcolata.equals(somma_iva_riepilogo_file)){
             StringBuilder appendo = new StringBuilder();
             appendo= appendo.append("Per l'aliquota ").append(aliquota).append(" l'iva dei dettagli e l'iva del riepilogo non coincide " + "    l'iva nei dettagli risulta di   ").append(round(somma_iva_dettaglio)).append("mentre l'iva del riepilogo risulta    ").append(round(somma_iva_riepilogo_calcolata));
             lista_anomalie.add(appendo.toString());
             }*/
        }

        for (Double aliquote21 : lista_aliquote2) {

            if (!lista_aliquote.contains(aliquote21)) {
                lista_anomalie.add("E' presente "
                        + "una aliquota iva "
                        + "presente solo nei "
                        + "dettagli ma non nel riepilogo "
                        + "aliquota" + aliquote21);

            }
        }
        return lista_anomalie;
    }
    /*
     }       
            
     per ogni aliquota (commerciale.select distint(aliquote)
     prendi la lista del riepilogo per aliquota. Per ogni riga calcoli imponibileimporto*aliquota/100. 
     per ogni riga verifica se coincide con il riepilogo iva indicato per quella riga. Se non coincide
     Imposta,Q,Errore di quadratura sull'imposta del riepologo,dovrebbe_essere,è. 
     Per ogni riga dettaglio dell'aliquota in oggetto calcolo l'IVA. Se iva dettagli
     non coincide con iva del riepilogo, AIAIAI
     return null;
     */

    public Double return_somma_iva_riepilogo() {
        XPath xpath3 = XPathFactory.newInstance().newXPath();
        String prezzo_totale = null;
        Double prezzo_tot = 0.00;

        String expression3 = "sum(FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo/Imposta)";
        try {
            prezzo_totale = xpath3.compile(expression3).evaluate(getDoc());
            System.out.println("merda");

        } catch (XPathExpressionException ex) {
        }

        prezzo_tot = Double.parseDouble(prezzo_totale);

        return prezzo_tot;
    }

    public Double return_somma_iva_riepilogo_per_aliquota(Double al) {
        setXpath2(XPathFactory.newInstance().newXPath());
        String arr = null;
        Double arro = 0.00;
        Double tot = 0.00;
        String expression2 = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo";
        try {
            this.setNode((NodeList) getXpath2().compile(expression2).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
        
        }
        for (int i = 0; i < getNode().getLength(); i++) {
            Node nNode2 = getNode().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode2.getNodeName());
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;

                System.out.println("Imposta "
                        + eElement2
                        .getElementsByTagName("Imposta")
                        .item(0)
                        .getTextContent());
                if (eElement2.getElementsByTagName("Imposta").getLength() >= 1) {

                    arr = eElement2.getElementsByTagName("Imposta").item(0).getTextContent().trim();
                    String sc = eElement2.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                    Double sca = Double.parseDouble(sc);
                    if (al.equals(sca)) {
                        arro = Double.parseDouble(arr);
                        tot += arro;
                    }
                }
            }
        }
        return tot;
    }

    public Double return_totale() {
        this.setTotale(this.return_somma_imponibili() + this.return_somma_iva_riepilogo());
        if (this.return_arrotondamento_di_testa() != null) {
            this.setTotale((this.getTotale() + this.return_arrotondamento_di_testa()));
            //-this.return_abbuono_di_testa();
        }
        if (this.return_abbuono_di_testa() != null) {
            this.setTotale((this.getTotale() - abs(this.return_abbuono_di_testa())));
        }
        if (this.return_maggiorazione_di_testa() != null) {
            this.setTotale((this.getTotale() + abs(this.return_maggiorazione_di_testa())));
        }
        return this.getTotale();
    }

    public List<Double> return_lista_aliquote() {
        setxPath(XPathFactory.newInstance().newXPath());
        Double aliquota = 0.00;
        String aliquota1 = null;
        List<Double> aliquote = new ArrayList<Double>();

        String expression = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo";
        try {
            this.setNode2((NodeList) getxPath().compile(expression).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
          
        }
        for (int i = 0; i < getNode2().getLength(); i++) {
            Node nNode = getNode2().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                System.out.println("AliquotaIVA : "
                        + eElement
                        .getElementsByTagName("AliquotaIVA")
                        .item(0)
                        .getTextContent());
                aliquota1 = eElement.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                aliquota = Double.parseDouble(aliquota1);
                if (!(aliquote.contains(aliquota))) {
                    aliquote.add(aliquota);
                }
            }
        }

        return aliquote;
    }

    public List<Double> return_lista_aliquote_dett() {
        setxPath(XPathFactory.newInstance().newXPath());
        Double aliquota = 0.00;
        String aliquota1 = null;
        List<Double> aliquote = new ArrayList<Double>();

        String expression = "FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee";
        try {
            this.setNode2((NodeList) getxPath().compile(expression).evaluate(getDoc(), XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
           
        }
        for (int i = 0; i < getNode2().getLength(); i++) {
            Node nNode = getNode2().item(i);
            System.out.println("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                System.out.println("AliquotaIVA dettaglio: "
                        + eElement
                        .getElementsByTagName("AliquotaIVA")
                        .item(0)
                        .getTextContent());
                aliquota1 = eElement.getElementsByTagName("AliquotaIVA").item(0).getTextContent().trim();
                aliquota = Double.parseDouble(aliquota1);
                if (!(aliquote.contains(aliquota))) {
                    aliquote.add(aliquota);
                }
            }
        }

        return aliquote;
    }

    /**
     * @return the arrontodamento_di_testa
     */
    public Double getArrontodamento_di_testa() {
        return arrontodamento_di_testa;
    }

    /**
     * @param arrontodamento_di_testa the arrontodamento_di_testa to set
     */
    public void setArrontodamento_di_testa(Double arrontodamento_di_testa) {
        this.arrontodamento_di_testa = arrontodamento_di_testa;
    }

    /**
     * @return the abbuono_di_testa
     */
    public Double getAbbuono_di_testa() {
        return abbuono_di_testa;
    }

    /**
     * @param abbuono_di_testa the abbuono_di_testa to set
     */
    public void setAbbuono_di_testa(Double abbuono_di_testa) {
        this.abbuono_di_testa = abbuono_di_testa;
    }

    /**
     * @return the somma_sconti_di_dettaglio
     */
    public Double getSomma_sconti_di_dettaglio() {
        return somma_sconti_di_dettaglio;
    }

    /**
     * @param somma_sconti_di_dettaglio the somma_sconti_di_dettaglio to set
     */
    public void setSomma_sconti_di_dettaglio(Double somma_sconti_di_dettaglio) {
        this.somma_sconti_di_dettaglio = somma_sconti_di_dettaglio;
    }

    /**
     * @return the somma_imponibili
     */
    public Double getSomma_imponibili() {
        return somma_imponibili;
    }

    /**
     * @param somma_imponibili the somma_imponibili to set
     */
    public void setSomma_imponibili(Double somma_imponibili) {
        this.somma_imponibili = somma_imponibili;
    }

    /**
     * @return the somma_spese_accessorie
     */
    public Double getSomma_spese_accessorie() {
        return somma_spese_accessorie;
    }

    /**
     * @param somma_spese_accessorie the somma_spese_accessorie to set
     */
    public void setSomma_spese_accessorie(Double somma_spese_accessorie) {
        this.somma_spese_accessorie = somma_spese_accessorie;
    }

    /**
     * @return the somma_iva_riepilogo
     */
    public Double getSomma_iva_riepilogo() {
        return somma_iva_riepilogo;
    }

    /**
     * @param somma_iva_riepilogo the somma_iva_riepilogo to set
     */
    public void setSomma_iva_riepilogo(Double somma_iva_riepilogo) {
        this.somma_iva_riepilogo = somma_iva_riepilogo;
    }

    /**
     * @return the totale
     */
    public Double getTotale() {
        return totale;
    }

    /**
     * @param totale the totale to set
     */
    public void setTotale(Double totale) {
        this.totale = totale;
    }

    /**
     * @return the factory
     */
    public DocumentBuilderFactory getFactory() {
        return factory;
    }

    /**
     * @param factory the factory to set
     */
    public void setFactory(DocumentBuilderFactory factory) {
        this.factory = factory;
    }

    /**
     * @return the builder
     */
    public DocumentBuilder getBuilder() {
        return builder;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(DocumentBuilder builder) {
        this.builder = builder;
    }

    /**
     * @return the node
     */
    public NodeList getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(NodeList node) {
        this.node = node;
    }

    /**
     * @return the node2
     */
    public NodeList getNode2() {
        return node2;
    }

    /**
     * @param node2 the node2 to set
     */
    public void setNode2(NodeList node2) {
        this.node2 = node2;
    }

    /**
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     * @return the file_xml_importato
     */
    public String getFile_xml_importato() {
        return file_xml_importato;
    }

    /**
     * @param file_xml_importato the file_xml_importato to set
     */
    public void setFile_xml_importato(String file_xml_importato) {
        this.file_xml_importato = file_xml_importato;
    }

    /**
     * @return the xPath
     */
    public XPath getxPath() {
        return xPath;
    }

    /**
     * @param xPath the xPath to set
     */
    public void setxPath(XPath xPath) {
        this.xPath = xPath;
    }

    /**
     * @return the xpath2
     */
    public XPath getXpath2() {
        return xpath2;
    }

    /**
     * @param xpath2 the xpath2 to set
     */
    public void setXpath2(XPath xpath2) {
        this.xpath2 = xpath2;
    }
}
