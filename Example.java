
package rename;

import java.util.List;
import javax.xml.xpath.XPathExpressionException;
/**
 *
 * @author luca
 */
public class Example {
  public static void main(String[] argv) throws XPathExpressionException{             
        String file_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<p:FatturaElettronica versione=\"1.1\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:p=\"http://www.fatturapa.gov.it/sdi/fatturapa/v1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
"  <FatturaElettronicaHeader>\n" +
"    <DatiTrasmissione>\n" +
"      <IdTrasmittente>\n" +
"        <IdPaese>IT</IdPaese>\n" +
"        <IdCodice>01234567890</IdCodice>\n" +
"      </IdTrasmittente>\n" +
"      <ProgressivoInvio>00001</ProgressivoInvio>\n" +
"      <FormatoTrasmissione>SDI11</FormatoTrasmissione>\n" +
"      <CodiceDestinatario>AAAAAA</CodiceDestinatario>\n" +
"      <ContattiTrasmittente/>\n" +
"    </DatiTrasmissione>\n" +
"    <CedentePrestatore>\n" +
"      <DatiAnagrafici>\n" +
"        <IdFiscaleIVA>\n" +
"          <IdPaese>IT</IdPaese>\n" +
"          <IdCodice>01234567890</IdCodice>\n" +
"        </IdFiscaleIVA>\n" +
"        <Anagrafica>\n" +
"          <Denominazione>SOCIETA' ALPHA SRL</Denominazione>\n" +
"        </Anagrafica>\n" +
"        <RegimeFiscale>RF19</RegimeFiscale>\n" +
"      </DatiAnagrafici>\n" +
"      <Sede>\n" +
"        <Indirizzo>VIALE ROMA 543</Indirizzo>\n" +
"        <CAP>07100</CAP>\n" +
"        <Comune>SASSARI</Comune>\n" +
"        <Provincia>SS</Provincia>\n" +
"        <Nazione>IT</Nazione>\n" +
"      </Sede>\n" +
"    </CedentePrestatore>\n" +
"    <CessionarioCommittente>\n" +
"      <DatiAnagrafici>\n" +
"        <CodiceFiscale>09876543210</CodiceFiscale>\n" +
"        <Anagrafica>\n" +
"          <Denominazione>AMMINISTRAZIONE BETA</Denominazione>\n" +
"        </Anagrafica>\n" +
"      </DatiAnagrafici>\n" +
"      <Sede>\n" +
"        <Indirizzo>VIA TORINO 38-B</Indirizzo>\n" +
"        <CAP>00145</CAP>\n" +
"        <Comune>ROMA</Comune>\n" +
"        <Provincia>RM</Provincia>\n" +
"        <Nazione>IT</Nazione>\n" +
"      </Sede>\n" +
"    </CessionarioCommittente>\n" +
"  </FatturaElettronicaHeader>\n" +
"  <FatturaElettronicaBody>\n" +
"    <DatiGenerali>\n" +
"      <DatiGeneraliDocumento>\n" +
"        <TipoDocumento>TD01</TipoDocumento>\n" +
"        <Divisa>EUR</Divisa>\n" +
"        <Data>2014-12-18</Data>\n" +
"        <Numero>123</Numero>\n" +
"        <Causale>LA FATTURA FA RIFERIMENTO AD UNA OPERAZIONE AAAA BBBBBBBBBBBBBBBBBB CCC DDDDDDDDDDDDDDD E FFFFFFFFFFFFFFFFFFFF GGGGGGGGGG HHHHHHH II LLLLLLLLLLLLLLLLL MMM NNNNN OO PPPPPPPPPPP QQQQ RRRR SSSSSSSSSSSSSS</Causale>\n" +
"        <Causale>SEGUE DESCRIZIONE CAUSALE NEL CASO IN CUI NON SIANO STATI SUFFICIENTI 200 CARATTERI AAAAAAAAAAA BBBBBBBBBBBBBBBBB</Causale>\n" +
"      </DatiGeneraliDocumento>\n" +
"      <DatiOrdineAcquisto>\n" +
"        <RiferimentoNumeroLinea>1</RiferimentoNumeroLinea>\n" +
"        <IdDocumento>66685</IdDocumento>\n" +
"        <NumItem>1</NumItem>\n" +
"        <CodiceCUP>123abc</CodiceCUP>\n" +
"	<CodiceCIG>456def</CodiceCIG>\n" +
"      </DatiOrdineAcquisto>\n" +
"      <DatiContratto>\n" +
"	<RiferimentoNumeroLinea>1</RiferimentoNumeroLinea>\n" +
"	<IdDocumento>123</IdDocumento>\n" +
"	<Data>2012-09-01</Data>\n" +
"	<NumItem>5</NumItem>\n" +
"	<CodiceCUP>123abc</CodiceCUP>\n" +
"	<CodiceCIG>456def</CodiceCIG>\n" +
"      </DatiContratto>\n" +
"      <DatiConvenzione>\n" +
"	<RiferimentoNumeroLinea>1</RiferimentoNumeroLinea>\n" +
"	<IdDocumento>123</IdDocumento>\n" +
"	<NumItem>5</NumItem>\n" +
"	<CodiceCUP>123abc</CodiceCUP>\n" +
"	<CodiceCIG>456def</CodiceCIG>\n" +
"      </DatiConvenzione>\n" +
"      <DatiRicezione>\n" +
"	<RiferimentoNumeroLinea>1</RiferimentoNumeroLinea>\n" +
"	<IdDocumento>123</IdDocumento>\n" +
"	<NumItem>5</NumItem>\n" +
"	<CodiceCUP>123abc</CodiceCUP>\n" +
"	<CodiceCIG>456def</CodiceCIG>\n" +
"      </DatiRicezione>\n" +
"      <DatiTrasporto>\n" +
"	<DatiAnagraficiVettore>				\n" +
"	  <IdFiscaleIVA>\n" +
"	    <IdPaese>IT</IdPaese>\n" +
"	    <IdCodice>24681012141</IdCodice>\n" +
"	  </IdFiscaleIVA>\n" +
"    	  <Anagrafica>\n" +
"	    <Denominazione>Trasporto spa</Denominazione>\n" +
"	  </Anagrafica>\n" +
"	</DatiAnagraficiVettore>\n" +
"	<DataOraConsegna>2012-10-22T16:46:12.000+02:00</DataOraConsegna>\n" +
"      </DatiTrasporto>\n" +
"    </DatiGenerali>\n" +
"    <DatiBeniServizi>\n" +
"      <DettaglioLinee>\n" +
"        <NumeroLinea>1</NumeroLinea>\n" +
"        <Descrizione>LA DESCRIZIONE DELLA FORNITURA PUO' SUPERARE I CENTO CARATTERI CHE RAPPRESENTAVANO IL PRECEDENTE LIMITE DIMENSIONALE. TALE LIMITE NELLA NUOVA VERSIONE E' STATO PORTATO A MILLE CARATTERI</Descrizione>\n" +
"        <Quantita>5.00</Quantita>\n" +
"        <PrezzoUnitario>1.00</PrezzoUnitario>\n" +
"        <PrezzoTotale>5.00</PrezzoTotale>\n" +
"        <AliquotaIVA>22.00</AliquotaIVA>\n" +
"      </DettaglioLinee>\n" +
"      <DatiRiepilogo>\n" +
"        <AliquotaIVA>22.00</AliquotaIVA>\n" +
"        <ImponibileImporto>6.00</ImponibileImporto>\n" +
"        <Imposta>1.10</Imposta>\n" +
"        <EsigibilitaIVA>I</EsigibilitaIVA>\n" +
"      </DatiRiepilogo>\n" +
"    </DatiBeniServizi>\n" +
"    <DatiPagamento>\n" +
"      <CondizioniPagamento>TP01</CondizioniPagamento>\n" +
"      <DettaglioPagamento>\n" +
"        <ModalitaPagamento>MP01</ModalitaPagamento>\n" +
"        <DataScadenzaPagamento>2015-01-30</DataScadenzaPagamento>\n" +
"        <ImportoPagamento>6.10</ImportoPagamento>\n" +
"      </DettaglioPagamento>\n" +
"    </DatiPagamento>\n" +
"  </FatturaElettronicaBody>\n" +
"</p:FatturaElettronica>";
      AnomalieQuadratura anomalie = new AnomalieQuadratura(file_xml);
                anomalie.verifica_quadratura_imponibile();
                anomalie.verifica_quadratura_iva();
                anomalie.verifica_quadratura_prezzo_unitario_prezzototale();   
                List<String> lista_anomalie= anomalie.return_lista_anomalie();
                for(String anomalia:lista_anomalie){
                    System.out.println(anomalia);
                    
                }
  }
    
}
