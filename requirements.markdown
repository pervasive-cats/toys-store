---
title: Analisi dei requisiti
permalink: /requirements
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/">Home</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/architectural_design">Design Architetturale</a></div>
</div>
<br/>

## Analisi dei requisiti

La raccolta dei requisiti è stata costituita principalmente dal processo di "knowledge crunching". Il progetto ha una componente
fortemente innovativa, per cui non esistono casi analoghi su cui potersi basare. A maggior ragione è perciò importante assicurarsi
con i _domain experts_ che tutto il team di progetto sia a conoscenza del dominio e dei suoi dettagli, in modo da evitare
incomprensioni e problemi in momenti più avanzati del processo di sviluppo, dove un errore è molto più costoso.

Il "knowledge crunching" è iniziato con una fase di "domain storytelling". Questa tecnica, che utilizza un linguaggio visuale di
facile comprensione per tutti gli _stakeholder_, indipendentemente dal fatto di avere un _background_ tecnico o meno, ha facilitato
la raccolta delle informazioni più importanti sul dominio. Questo proprio per le sue caratteristiche, che non richiedono conoscenza
pregressa per poterne comprendere il funzionamento, quindi velocizza e riduce il tasso di errore nell'estrapolazione dei requisiti.

Nel mentre si procedeva iterativamente ad esplorare le storie che illustravano il dominio, si è cercato di distillare i casi d'uso
più significativi mediante l'uso di un più tradizionale diagramma dei casi d'uso in UML. I suoi tecnicismi lo rendono più ostico
alla comprensione, però, forti della chiarezza ottenuta dal metodo precedente, i casi d'uso permettono di avere una visione più
schematica e più comoda per il _team_ di sviluppo delle funzionalità del sistema.

Dopodiché, si è affrontata una sessione di "event storming" per validare ulteriormente i casi d'uso ed estrapolare ancora più
conoscenza in merito al dominio. Grazie a questa tecnica, infatti, è possibile evidenziare le relazioni temporali tra gli eventi
che si susseguono nel sistema e quindi anche le relazioni tra le funzionalità, fino a prima solo vagamente indicate. Inoltre, l'uso
dello "event storming" permette una transizione più semplice alla fase di progettazione, in quanto permette di far emergere i
_bounded context_ di interesse, nonché di evidenziare punti critici o salienti da tenere conto durante la fase di pianificazione
dell'architettura.

Infine, durante l'intera fase di raccolta dei requisiti si è costruito mano a mano lo "ubiquitous language", la colonna portante
della conoscenza sull'intero progetto. La sua realizzazione sotto forma di mappa concettuale permette di capire quali concetti
sono distinti e quali sono identici, quali sono correlati tra loro, come e perché. Permette di contestualizzare ciascun concetto
e di osservare se sono presenti lacune nella conoscenza del dominio o se invece la comprensione su di un certo argomento è sufficiente
oppure no. Sicuramente la mancanza di un tale strumento impedirebbe una corretta esecuzione di tutte le fasi successive, il cui
linguaggio stesso dipende dallo "ubiquitous language".

## Domain storytelling

Il domain storytelling è stato portato avanti in presenza del software architect e di due project manager di Pervasive Cats, uno
dei quali ha svolto il ruolo di tecnografo, di due _domain expert_, ovvero il responsabile del settore _marketing_ e quello del
settore _sales_ di Toys Store, e infine il responsabile per il committente, ovvero un project manager nominato da Toys Store.

Il risultato dell'applicazione di questa tecnica è visibile dai seguenti schemi, così come estrapolati da Miro.

![Domain storytelling relativo all'acquisto di un prodotto](/toys-store/images/DS1.jpg)

L'acquisto di un prodotto inizia con l'ingresso del cliente, il quale comunica così al sistema negozio la sua presenza al suo
interno. Tale sistema conferma poi al cliente l'avvio della sua procedura d'acquisto.

Per poter proseguire, il cliente deve scannerizzare un carrello _smart_ utilizzando il codice identificativo posto sopra di esso,
comunicandolo al sistema negozio. Il sistema comunica l'avvenuta associazione al cliente, che può così muovere il carrello.

A questo punto il cliente può aggirarsi liberamente per il negozio, facendo il suo acquisto. Per ottenere le informazioni che gli
servono su di un prodotto nel catalogo del negozio, gli basterà scannerizzare un codice identificativo posto su di esso e
comunicarlo al sistema negozio, il quale risponderà con tutte le informazioni necessarie.

Se il cliente è convinto della sua scelta, potrà mettere il prodotto nel suo carrello fisico, così da aggiungerlo anche al suo
carrello virtuale. Il cliente prima solleverà il prodotto da una scaffaleria _smart_, che a sua volta invierà una notifica di
sollevamento al sistema negozio per motivi di sicurezza. Il sistema infatti inoltrerà questa notifica al responsabile di negozio,
che potrà controllare quali prodotti in catalogo sono stati sollevati. A questo punto il cliente inserirà il prodotto nel carrello
_smart_, il quale si occuperà di comunicare il codice identificativo del prodotto aggiunto al sistema negozio, che confermerà
l'operazione di aggiunta al cliente.

In qualsiasi momento, il cliente può controllare il contenuto del proprio carrello virtuale effettuando una richiesta al sistema
negozio, il quale risponderà con i dati adeguati.

La terminazione della procedura d'acquisto si ha quando il cliente esce dal negozio e comunica la sua posizione allo stesso.
Quest'ultimo risponde con una conferma di terminazione della procedura e attende un certo tempo. Dopodiché, scaduto il tempo
necessario, il sistema effettua il pagamento per il cliente con i dati che ha fornito, inviandogli una notifica di avvenuto
pagamento. A questo punto, il carrello _smart_ viene bloccato dal negozio per un successivo utilizzo. Se il cliente farà richiesta
degli acquisti effettuati potrà vedere tutto lo storico degli stessi, tra cui quello appena effettuato.

![Domain storytelling relativo al mettere via un prodotto nel carrello](/toys-store/images/DS2.jpg)

Un cliente potrebbe decidere di voler rimuovere un prodotto dal suo carrello, in tal caso è necessario utilizzare un sistema
apposito detto "sistema di restituzione". Il cliente dapprima alza il prodotto dal carrello, per inserirlo in tale sistema. Questo
visualizza una richiesta di conferma al cliente, che potrà rifiutare ed avere indietro il prodotto o approvare. In quest'ultimo
caso il sistema di restituzione invierà la notifica di un nuovo prodotto restituito al sistema negozio, il quale inoltrerà la
notifica al responsabile di negozio per ragioni di sicurezza. Se il responsabile visualizzerà i prodotti restituiti potrà vedere
anche quello appena inserito.

In caso di malfunzionamento del sistema di restituzione, il responsabile di negozio avrà la possibilità di restituire
manualmente il prodotto. Il cliente comunicherà il codice del prodotto al responsabile, il quale effettuerà la rimozione e riceverà
dal cliente stesso il prodotto fisico. Controllando i prodotti nel carrello del cliente, il responsabile noterà che il prodotto
non è più presente e, come nel caso precedente, per motivi di sicurezza il sistema genererà una notifica di prodotto restituito.

In ambedue i casi precedenti, dopo che il prodotto è stato restituito, il responsabile di negozio ha il compito di rimetterlo a
posto. Il responsabile ha la facoltà di vedere tutti i prodotti restituiti e, per sistemare ognuno di essi, deve prima collocare
il prodotto nella corretta scaffaleria _smart_ e in seguito notificare il sistema negozio che il prodotto è stato rimesso al suo
posto.

![Domain storytelling relativo allo spegnimento di un dispositivo, della gestione del post-vendita, della modifica dell'allestimento e dei carrelli in negozio](/toys-store/images/DS3.jpg)

In caso il dispositivo che il cliente utilizza per accedere al sistema negozio dovesse spegnersi o malfunzionare, sarà premura
del cliente notificare il responsabile di negozio. Costui verificherà il contenuto del carrello del cliente, per identificare
quest'ultimo e quindi il suo processo di acquisto ed osservare il suo stato attuale. A questo punto, il responsabile annullerà la
procedura d'acquisto e ne inizierà una nuova. Il cliente verrà dotato di un altro dispositivo, la quale comunicherà la presenza
all'interno del negozio al sistema permettendo il riavvio della procedura. Il responsabile di negozio potrà quindi associare il
carrello che stava usando il cliente in precedenza a quest'ultimo, così come potrà aggiungere all'interno del carrello tutti i
prodotti già scelti dal cliente.

Il responsabile di negozio ha anche la facoltà di visualizzare e modificare l'allestimento del negozio, in caso di eventi speciali
o semplicemente di ri-allestimenti periodici dello stesso.

Il responsabile di negozio può correggere eventuali errori incorsi nel sistema a proposito di acquisti o pagamenti erronei.
Egli può infatti visualizzare gli acquisti effettuati dai clienti, rimuovere quelli erronei e sostituirli con gli acquisti corretti.
Discorso analogo vale per i pagamenti dei singoli acquisti.

Infine, il responsabile di negozio si occupa anche della manutenzione dei carrelli _smart_. Qualora un carrello dovesse rompersi
o malfunzionare per un qualche motivo, il responsabile ha la facoltà di rimuoverlo dal sistema e di aggiungerne uno nuovo. In tal
caso, visualizzando tutti i carrelli in negozio, si potrebbe vedere come il vecchio non sia più presente, mentre il nuovo sì.
Poiché il nuovo carrello dovrà essere ricondotto al suo posto, così come un qualsiasi altro carrello abbandonato da un cliente,
il responsabile di negozio potrà sbloccarlo, ovvero rimuovere i vincoli alle ruote che lo tengono fermo per poterlo portare in giro,
senza però potervi aggiungere dei prodotti. Dopo averlo ricondotto al suo posto, potrà nuovamente bloccarlo come qualsiasi carrello
che si trova a posto.

![Domain storytelling relativo al riconoscimento di un furto, alle analisi di marketing e all'aggiornamento dei listini di vendita](/toys-store/images/DS4.jpg)

Altra "_story_" particolarmente importante è quella relativa al riconoscimento dei furti di prodotti. In caso un cliente si
rivelasse essere un malintenzionato, questo solleverebbe un prodotto dal suo scaffale, senza inserirlo nel suo carrello. Lo scaffale,
invariabilmente, invierebbe una notifica al sistema negozio, che la inoltrerebbe al responsabile di negozio, che potrebbe quindi
vedere quali prodotti sono al momento sollevati dagli scaffali. A questo punto il cliente, avvicinando il prodotto al sistema
antitaccheggio, lo fa rilevare da quest'ultimo. L'antitaccheggio quindi notifica il codice identificativo del prodotto al sistema
negozio, il quale capisce se quello è uno dei prodotti sollevati e non inseriti in nessun carrello e se quindi per questo sta
venendo rubato. Se così fosse, il sistema invia una notifica al sistema antitaccheggio di attivare il suo allarme, che viene quindi
attivato e sentito dal responsabile di negozio. Questo può quindi avvicinarsi al malintenzionato, recuperare il prodotto e metterlo
sullo scaffale adeguato e infine notificare il sistema che il prodotto è ora al suo posto.

Poiché il sistema è ricco di dati utili per effettuare analisi sugli acquisti effettuati dal cliente, il sistema permette
all'amministrazione di "Toys Store" di visualizzare gli acquisti effettuati dai diversi clienti suddivisi per negozio, così come
per intervallo di tempo. Inoltre, il sistema permette di visualizzare i pagamenti effettuati per gli acquisti per intervallo di
tempo.

Da ultimo, un altro aspetto interessante per l'amministrazione del committente è quello di poter aggiornare i listini di vendita
dei diversi negozi. L'amministrazione può infatti modificare una tipologia di prodotto già esistente, rimuoverne una già presente
o inserirne una nuova. Discorso analogo vale per i prodotti nel catalogo di ciascun negozio. Sono invece i responsabili di negozio
a dover aggiungere o rimuovere i prodotti nel sistema, in quanto i loro dati sono legati allo specifico negozio in cui si trovano.

## Casi d'uso

## Event storming

## Ubiquitous Language

## Requisiti

<br/>
<div>
    <div style="text-align: center"><a href="#analisi-dei-requisiti">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/">Home</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/architectural_design">Design Architetturale</a></div>
</div>
<br/>
