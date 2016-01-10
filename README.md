# CheckFepa
Utility per il controllo di quadratura delle fatture elettroniche
Vengono effettuati i seguenti controlli di quadratura
- controlli di quadratura tra imponibile e riepilogo
- controlli di quadratura tra prezzo unitario e prezzo totale per ogni riga dettaglio
- controlli di quadratura per aliquota iva tra l'imponibile e l'iva del riepilogo e l'imponibile e l'iva delle righe dettaglio
Esempio di utilizzo è contenuto nel file Example.java
La classe Anomalie di Quadratura prevede tre metodi relativi ai 3 controlli di quadratura e un metodo che restituisce le anomalie di quadratura. La lista restituita è di tipologia List. 
Tali classi Java possono essere integrate in qualsiasi gestionale che elabori le fatture elettroniche ricevute da SDI. E' utile mostrare queste anomalie all'utente che riceve una fattura elettronica per decidere di accettare o meno il documento.



