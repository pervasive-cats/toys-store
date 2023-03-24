---
title: Analisi dei requisiti
permalink: /requirements
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/">Home</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/architectural_design">Design Architetturale</a></div>
</div>
<br/>

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

![Domain storytelling relativo all'acquisto di un prodotto](/toys-store/assets/images/DS1.jpg)

L'acquisto di un prodotto inizia con l'ingresso del cliente, il quale comunica così al sistema negozio la sua presenza al suo
interno. Tale sistema conferma poi al cliente l'avvio della sua procedura d'acquisto.

Per poter proseguire, il cliente deve scannerizzare un carrello _smart_ utilizzando il codice identificativo posto sopra di esso,
comunicandolo al sistema negozio. Il sistema comunica l'avvenuta associazione al cliente, che può così muovere il carrello.

A questo punto il cliente può aggirarsi liberamente per il negozio, facendo il suo acquisto. Per ottenere le informazioni che gli
servono su di un prodotto nel catalogo del negozio, gli basterà scannerizzare un codice identificativo posto su di esso e
comunicarlo al sistema negozio, il quale risponderà con tutte le informazioni necessarie.

Se il cliente è convinto della sua scelta, potrà mettere il prodotto nel suo carrello fisico, così da aggiungerlo anche al suo
carrello virtuale. Il cliente prima solleverà il prodotto da una scaffalatura _smart_, che a sua volta invierà una notifica di
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

![Domain storytelling relativo al mettere via un prodotto nel carrello](/toys-store/assets/images/DS2.jpg)

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
il prodotto nella corretta scaffalatura _smart_ e in seguito notificare il sistema negozio che il prodotto è stato rimesso al suo
posto.

![Domain storytelling relativo allo spegnimento di un dispositivo, della gestione del post-vendita, della modifica dell'allestimento e dei carrelli in negozio](/toys-store/assets/images/DS3.jpg)

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

![Domain storytelling relativo al riconoscimento di un furto, alle analisi di marketing e all'aggiornamento dei listini di vendita](/toys-store/assets/images/DS4.jpg)

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

Sulla base del domain storytelling effettuato in precedenza, sono emersi tre attori principali all'interno del sistema, ovverosia
il cliente, il responsabile di negozio e l'amministrazione. Questi tre possono interagire attraverso interfacce diverse del sistema
negozio, quando vogliono utilizzare le sue funzionalità. È stato deciso che un cliente può interagire con un'applicazione installata
su di un dispositivo proprio, oppure fornito dal negozio, per quanto riguarda la gestione del suo processo di acquisto. Il
responsabile di negozio, così come l'amministrazione, possono interagire con il negozio attraverso un'apposita _dashboard_. Inoltre,
le altre interfacce utilizzabili dagli attori del sistema sono quelle relative ai vari _smart device_ ovvero il carrello, la
scaffalatura, il sistema di restituzione e il sistema antitaccheggio.

Per ciascuna interfaccia è stato realizzato un apposito diagramma UML dei casi d'uso della stessa.

![Diagramma dei casi d'uso dell'applicazione](/toys-store/assets/images/Applicazione.png)

Un cliente, utilizzando l'applicazione, vuole poter:

* effettuare la registrazione nel sistema;
  * i dati che è necessario fornire sono nome utente, email, nome, cognome e password;
  * ogni cliente è identificato dalla sua email.
* effettuare la de-registrazione dal sistema;
  * per poter effettuare la de-registrazione dal sistema è necessario specificare la password.
* fare _login_ una volta registratosi;
  * i dati che è necessario fornire sono email e password.
* fare _logout_ una volta fatto _login_;
* modificare i dati inseriti in fase di registrazione, così come la propria _password_, una volta fatto _login_;
  * in caso di modifica della password è necessario indicare anche la vecchia password.
* scansionare il codice identificativo di un carrello;
  * il codice di un carrello è univoco per negozio.
* associare il carrello di cui ha appena scansionato il codice identificativo;
* scansionare il codice identificativo di un prodotto;
  * il codice di un prodotto è univoco per prodotto in catalogo, il quale ha un codice univoco per negozio.
* visualizzare i dati relativi al prodotto in catalogo per cui ha appena scansionato il codice identificativo del prodotto
ad esso associato;
* visualizzare il contenuto del proprio carrello;
* visualizzare gli acquisti già effettuati;
* effettuare l'ingresso in negozio;
* effettuare l'uscita dal negozio, che comprende il blocco del carrello associato al cliente e l'effettuazione del pagamento;
  * l'effettuazione del pagamento coinvolgerà il sistema di pagamento;
  * l'effettuazione del pagamento può avvenire con successo o con fallimento.

![Diagramma dei casi d'uso della dashboard lato responsabile di negozio](/toys-store/assets/images/Dashboard_responsabile.png)

Un responsabile di negozio, utilizzando la dashboard, vuole poter:

* effettuare la registrazione nel sistema;
  * i dati che è necessario fornire sono nome utente, codice del negozio di appartenenza, dati di un metodo di pagamento e password;
  * ogni responsabile di negozio è identificato dal suo nome utente.
* effettuare la de-registrazione dal sistema;
* fare _login_ una volta registratosi;
  * i dati che è necessario fornire sono nome utente e password.
* fare _logout_ una volta fatto _login_;
* modificare i dati inseriti in fase di registrazione, così come la propria _password_, una volta fatto _login_;
  * il nome utente non può essere modificato;
  * in caso di modifica della password è necessario indicare anche la vecchia password.
* visualizzare i carrelli presenti in negozio;
* sbloccare un carrello bloccato;
* bloccare un carrello sbloccato;
* annullare la procedura d'acquisto di un cliente, che comprende il blocco del carrello associato al cliente di cui si sta
annullando la procedura d'acquisto e il sollevamento dei prodotti in catalogo che sono associati ai prodotti parte del contenuto del
carrello del cliente;
  * il sollevamento dei prodotti in catalogo viene notificato al responsabile di negozio;
  * il successo dell'operazione viene notificato all'applicazione del cliente.
* rimuovere un prodotto dal carrello di un cliente, che comprende il sollevamento del prodotto in catalogo che è associato al prodotto che sta venendo rimosso;
  * il sollevamento del prodotto in catalogo viene notificato al responsabile di negozio;
  * il successo dell'operazione viene notificato all'applicazione del cliente.
* associare un carrello ad un cliente;
  * il successo dell'operazione viene notificato all'applicazione del cliente.
* aggiungere un prodotto al carrello di un cliente, che comprende la precedente visualizzazione del prodotto in catalogo associato
al prodotto che il responsabile vuole aggiungere;
  * il successo dell'operazione viene notificato all'applicazione del cliente.
* rimuovere un carrello;
* aggiungere un carrello;
* visualizzare l'allestimento del negozio;
* aggiungere, modificare o rimuovere una gondola;
  * il codice identificativo di una gondola è univoco per negozio.
* aggiungere, modificare o rimuovere una scaffalatura;
  * il codice identificativo di una scaffalatura è univoco per gondola.
* aggiungere, modificare o rimuovere uno scaffale;
  * il codice identificativo di uno scaffale è univoco per scaffalatura.
* aggiungere, modificare o rimuovere una fila di prodotti;
  * il codice identificativo di una fila di prodotti è univoco per scaffale.
* visualizzare la procedura d'acquisto di un cliente;
* ricevere una notifica ogni volta viene restituito un prodotto;
* ricollocare al proprio posto un prodotto restituito;
* ricollocare al proprio posto un prodotto in catalogo sollevato;
* visualizzare i prodotti in catalogo sollevati;
* visualizzare i prodotti restituiti;
* aggiungere o rimuovere un prodotto nel negozio;
* aggiungere o rimuovere un acquisto;
* aggiungere o rimuovere un pagamento;
* visualizzare i pagamenti di ciascun cliente;
* visualizzare gli acquisti di ciascun cliente;
* ricevere una notifica ogni volta che un prodotto in catalogo è stato sollevato.

![Diagramma dei casi d'uso della dashboard lato amministrazione](/toys-store/assets/images/Dashboard_amministrazione.png)

L'amministrazione, utilizzando la dashboard, vuole poter:

* fare _login_;
  * i dati che è necessario fornire sono nome utente e password;
* fare _logout_ una volta fatto _login_;
* modificare la propria _password_ una volta fatto _login_;
  * in caso di modifica della password è necessario indicare anche la vecchia password.
* visualizzare gli acquisti suddivisi per negozio;
* visualizzare gli acquisti per intervallo di date;
* visualizzare i pagamenti per intervallo di date;
* aggiungere, modificare o rimuovere una tipologia di prodotto;
* aggiungere, modificare o rimuovere un prodotto in catalogo;
  * il codice di un prodotto in catalogo è univoco per negozio;
  * ogni prodotto in catalogo è associato ad una sola tipologia di prodotto.

![Diagramma dei casi d'uso del carrello](/toys-store/assets/images/Carrello.png)

Un cliente, utilizzando un carrello, vuole poter:

* appoggiare un prodotto nel carrello;
  * sia la sua applicazione che la _dashboard_ verranno aggiornate con il nuovo dato qualora un cliente o un responsabile di
  negozio facciano richiesta di visualizzare i dati aggiornati;
  * questa operazione potrebbe scatenare l'allarme del carrello, nel caso in cui non sia associato ad un cliente.
* muovere il carrello;
  * questa operazione potrebbe scatenare l'allarme del carrello, nel caso in cui sia bloccato.

![Diagramma dei casi d'uso del sistema di restituzione](/toys-store/assets/images/Sistema_restituzione.png)

Un cliente, utilizzando il sistema di restituzione, vuole poter:

* inserire un prodotto nel sistema di restituzione;
* restituire un prodotto, dopo averlo inserito;
  * sia la sua applicazione che la _dashboard_ verranno aggiornate con il nuovo dato qualora un cliente o un responsabile di
  negozio facciano richiesta di visualizzare i dati aggiornati;
  * questa operazione richiede che il cliente confermi di voler restituire il prodotto inserito.

![Diagramma dei casi d'uso del sistema antitaccheggio](/toys-store/assets/images/Sistema_antitaccheggio.png)

Un cliente, utilizzando il sistema antitaccheggio, vuole poter:

* avvicinare ad esso un prodotto;
  * questa operazione potrebbe scatenare l'allarme dell'antitaccheggio, nel caso in cui il prodotto non sia un prodotto nel carrello
    di un cliente, il che comprende anche che venga avvisato il responsabile di negozio.

![Diagramma dei casi d'uso della scaffalatura](/toys-store/assets/images/Scaffalatura.png)

Un cliente, utilizzando una scaffalatura, vuole poter:

* sollevare un prodotto in catalogo dalla sua fila di prodotti.

## Event storming

L'event storming, avvenuto in seguito al domain storytelling iniziale e alla raccolta dei requisiti funzionali mediante i diagrammi
dei casi d'uso, necessariamente ha seguito la traccia da questi già segnata. L'obiettivo da raggiungere con questa tecnica è stato
infatti quello di riordinare ulteriormente le informazioni già raccolte in senso temporale. Questo ha significato cercare di
raccogliere tutte le _story_ in un unica grande sequenza di eventi che si sussegue, mostrando però anche le possibili ramificazioni
e gli eventi generati da attori che portano avanti le proprie azioni in parallelo.

La sequenza principale non poteva che essere quella inerente agli eventi di un cliente che interagisce con il sistema negozio, in
quanto attore principale, nonché la sequenza più lunga di tutte, sulla quale si appoggiando la maggior parte delle funzionalità
del sistema.

La versione di event storming messa in pratica cerca di rispettare i colori che sono parte della definizione originale di questa
tecnica, pur però cercando di venire incontro alle persone con problemi di daltonismo. Per questo motivo, è stato deciso di
correggerla come segue:

* arancione: domain event;
* giallo chiaro: actor o agent;
* blu: system;
* giallo ocra: boundary;
* azzurro: arrow vote;
* rosa: hotspot;
* verde: opportunity.

Inoltre, non era possibile utilizzare foglietti adesivi di diverse dimensioni perché il software Miro utilizzato non permetteva
di averli a disposizione. Per questo motivo si sono utilizzati quelli quadrati tradizionali eccetto che per i boundary, sostituiti
da rettangoli stretti ed alti.

![Prima parte dell'event storming](/toys-store/assets/images/ES1.jpg)

La sequenza di eventi inizia con il cliente che si registra al sistema e poi effettua il login mediante l'applicazione. Una volta
fatto login, può decidere anche di modificare i dati che ha inserito. La stessa cosa può fare un responsabile di negozio, solamente
che anziché utilizzare un'applicazione, dovrà utilizzare la dashboard apposita. Tutte queste operazioni, che contraddistinguono la
fase iniziale, sono di pertinenza del bounded context "utenti", che gestirà i dati inerenti agli utenti del sistema, come
suggerisce il nome. Fa eccezione la registrazione del cliente che interessa anche il bounded context "pagamenti" perché dovrà gestire
le informazioni sui metodi di pagamento del cliente.

Una volta "inizializzati" i dati del cliente, che ora fa parte del sistema, può entrare in negozio e far iniziare il suo processo
d'acquisto, che viene gestito dal bounded context "shopping".
Questo sarà segnalato al sistema tramite l'applicazione, la quale permetterà quindi al cliente di scannerizzare il
codice di un carrello ed associarlo ad egli stesso. Una volta che il cliente riceverà conferma dell'associazione, potrà liberamente
muovere il carrello per il negozio e spostarlo dove preferisce. In caso il cliente muovesse o inserisse prodotti all'interno del
carrello senza prima averlo associato, l'allarme di quest'ultimo verrebbe fatto suonare per ricordare al cliente la corretta
procedura da seguire per fare il proprio acquisto. In caso di problemi, anche il responsabile di negozio può associare un carrello
ad un cliente. Di tutte queste operazioni se ne occuperà il bounded context "carrelli", deputato appunto alla gestione dei carrelli.

A questo punto, il cliente che si aggira per il negozio ha la facoltà di sollevare i prodotti dalle scaffalature, cosa di cui il
responsabile di negozio viene immediatamente allertato sulla dashboard. Questo è un punto di forza per la soluzione, perché così a
fine giornata, quando il negozio chiude, è più semplice capire quali prodotti sono stati sollevati e quindi possono essere in giro
per il negozio da rimettere a posto. Di questo si occupa il bounded context "negozi", perché gestisce tutti i sottosistemi legati
ad uno specifico negozio. Con il prodotto in mano, il cliente può scansionare il suo codice identificativo tramite l'applicazione
per ricevere maggiori informazioni su di esso. Questo viene invece gestito dal bounded context "prodotti", che gestisce tutte le
informazioni su ciò che è in vendita nei negozi. Questa fase si conclude con l'inserimento del prodotto nel carrello. Se il cliente
deciderà di non farlo, quando si avvicinerà al sistema antitaccheggio questo rileverà il prodotto non nel carrello e suonerà il suo
allarme. Anche la gestione di questo sottosistema è affidata al bounded context "negozi".

In ogni momento, da quando il cliente ha avviato la sua procedura d'acquisto, il responsabile di negozio può decidere di cancellarla
dalla sua dashboard. Questo viene gestito dal bounded context responsabile della procedura di acquisto del cliente, ovverosia
"shopping". In caso il responsabile annullasse la procedura, il carrello del cliente viene bloccato e tutti i prodotti al suo interno
passano ad essere semplicemente sollevati e non presenti in nessun carrello. Di questo sarà notificato il responsabile di negozio,
che potrà vedere quali sono e rimetterli a posto immediatamente. Tutte queste operazioni sono responsabilità del bounded context
"prodotti", eccetto bloccare il carrello, che è responsabilità di "carrelli".

![Seconda parte dell'event storming](/toys-store/assets/images/ES2.jpg)

Una volta inserito il prodotto all'interno del carrello, il carrello può identificare il prodotto. Queste operazioni sono gestite
dal bounded context "carrelli". Quando il sistema viene a conoscenza di un nuovo prodotto inserito nel carrello, lo aggiunge al
contenuto del carrello del cliente, che potrà richiedere di visualizzarlo e averlo disponibile mediante la sua applicazione. La
gestione dello stato della procedura d'acquisto, così come del contenuto del carrello, è affidata al bounded context "shopping".

Una volta che un prodotto è stato inserito nel carrello, è possibile per il cliente rimuoverlo utilizzando il sistema di restituzione.
Egli non dovrà fare altro che inserirlo al suo interno, confermare il fatto di volerlo rimuovere e questo verrà notificato sia
al cliente mediante l'applicazione che al responsabile di negozio mediante la dashboard. A questo punto, il responsabile potrà
visualizzare quali prodotti sono da rimettere a posto e farlo, andandoli a prendere da dentro il sistema di restituzione. Una volta
rimessi a posto, utilizzerà la dashboard per segnalare il fatto che il compito è stato concluso. Utilizzare questo metodo permette
di semplificare il lavoro del responsabile di negozio, che può sia sapere più facilmente quali prodotti vanno rimessi a posto, sia
effettuare l'operazione stessa con più facilità. La gestione del sistema di restituzione, così come di tutte le operazioni che
permette, sono di pertinenza del bounded context "negozi".

In caso di problemi, il responsabile di negozio può sempre decidere di aggiungere o rimuovere manualmente un prodotto dal carrello
di un cliente mediante la dashboard. La rimozione comporta il passaggio allo stato di sollevato e la generazione della relativa
notifica. Questo viene gestito dal bounded context "prodotti".

L'ultima fase della sequenza si ha nel momento nel quale il cliente, soddisfatto del suo acquisto, decide di uscire dal negozio.
In tal caso la procedura d'acquisto termina e il carrello del cliente viene bloccato, unica operazione ad opera del bounded
context "carrelli". Il sistema aspetta poi un certo lasso di tempo, durante il quale il cliente ha ancora la facoltà di rientrare
in negozio e riprendere il suo acquisto, per poi lanciare il pagamento. Questo passaggio tra i diversi stati della procedura di
acquisto del cliente sono gestiti dal bounded context "shopping".

L'intero processo termina con il pagamento dell'acquisto fatto, che può avere successo o fallire. Questo implica necessariamente
una problematica da risolvere: occorre procurarsi un sistema di pagamento elettronico tramite il quale gestire le transazioni
monetarie. Questo però permette un'esperienza di acquisto molto più flessibile per l'utente, che è il punto di forza fondamentale
che motiva l'intero sistema. I pagamenti saranno chiaramente gestiti dal bounded context responsabile, ovvero "pagamenti".

Una volta che la procedura di acquisto è terminata, il cliente può anche fare logout dall'applicazione o addirittura de-registrarsi.
Entrambe le operazioni sono gestite dal bounded context "users". Il responsabile di negozio può decidere di sbloccare un carrello
per rimetterlo a posto e poi bloccarlo nuovamente, utilizzando il bounded context "carrelli". Finiti i suoi compiti, anche un
responsabile di negozio può fare logout o anche de-registrarsi, utilizzando la dashboard che farà gestire queste operazioni al
bounded context "utenti".

![Terza parte dell'event storming](/toys-store/assets/images/ES3.jpg)

Da ultimo sono stati raccolti tutti gli eventi relativi ad altre operazioni che il responsabile e l'amministrazione possono fare
mediante la dashboard.

Un responsabile può aggiungere, rimuovere o visualizzare i carrelli in negozio mediante il bounded context
"carrelli". Un responsabile può aggiungere un nuovo prodotto al negozio oppure rimuoverlo mediante il bounded context "prodotti",
può modificare o visualizzare l'allestimento del negozio grazie a "negozi". È però bene chiarire se e in che modo il responsabile
può modificare l'allestimento del negozio mentre questo è aperto. Da ultimo, il responsabile può modificare e visualizzare gli
acquisti e i pagamenti effettuati da qualsiasi cliente, operazioni la cui responsabilità ricade sul bounded context "pagamenti".

L'amministrazione può fare login ed eventualmente modificare i suoi dati, operazioni gestite come in ogni altro caso dal bounded
context "utenti". Attraverso quello denominato "prodotti" può aggiungere, rimuovere o modificare le informazioni relative ai
prodotti in catalogo e alle tipologie di prodotti. Tramite le operazioni del bounded context "pagamenti" l'amministrazione può
visualizzare gli acquisti per negozio, ma anche acquisti e pagamenti per intervallo di date. Questo permette al reparto marketing
aziendale di raccogliere maggiori e migliori dati sulle intenzioni di acquisto dei clienti. Infine, sempre tramite il bounded
context "utenti", l'amministrazione può fare logout.

Un altro punto saliente che dovrà essere meglio identificato in fase di progettazione del sistema è quali sono le responsabilità
precise del carrello e del sistema di restituzione, cosa possono fare e che cosa invece è responsabilità del cliente o del sistema.

## Ubiquitous Language

L'ubiquitous language è stato realizzato sotto forma di mappa concettuale, per poter evidenziare i collegamenti tra i concetti,
oltre che le loro definizioni, in maniera tale da poter meglio esemplificare il contesto in cui ciascuno di essi è collocato.
I concetti che sono stati inseriti sono tutti i sostantivi rilevanti che sono emersi durante le fasi del _knowledge crunching_ e
per cui si è ritenuto che avere una definizione chiara ed esplicita, collocata nel contesto di utilizzo del concetto, fosse
fondamentale. Solo in questo modo è stato possibile costruire un linguaggio, conosciuto sia dagli esperti di dominio che dai
membri del _team_ di progetto, universale e quindi anche onnipresente, in ciascuno degli artefatti prodotti a seguito dell'analisi
dei requisiti. Particolare importanza rivestono gli stati dei singoli concetti, che non possono essere modellati come concetti a
sé stanti, ma comunque permettono di portare meglio in luce i loro aspetti dinamici.

Inoltre, poiché necessariamente alcune parti del sistema devono essere realizzate in lingua inglese, per facilitare il processo
di sviluppo in un contesto potenzialmente internazionale, si è deciso di realizzare un dizionario delle traduzioni dall'italiano
all'inglese. In questo modo, ad esempio, è possibile utilizzare nel codice i concetti dell'ubiquitous language, garantendogli la
sua totale potenza espressiva, senza però penalizzare la qualità del codice. Sono state prese in considerazione tutte quelle
parole che hanno particolare rilevanza nel dominio di riferimento, ad esempio perché sono specifiche di quel dominio o sono
parole di uso generico ma che in quello specifico dominio hanno una sfumatura di significato ben precisa. Questo significa che
parole generiche che sono usate in maniera generale nel parlare comune o anche nel gergo informatico sono state escluse. Le parole
possono essere anche locuzioni, nel caso in cui una sola parola non fosse bastata ad esprimere un concetto, ma in ogni caso ogni
elemento del dizionario svolge sempre il ruolo grammaticale di sostantivo o di verbo, in quanto le altre forme possono essere
derivate da queste. Fanno eccezione gli aggettivi i quali non vengono mai utilizzati nelle definizioni dell'ubiquitous language
come sostantivo o verbo. Come è possibile vedere su Miro, infatti, ogni definizione riporta delle parole in grassetto, le quali
sono tutte e sole le parole che compongono il dizionario.

Qui di seguito si riporta la mappa concettuale così come presente su Miro, spogliata per motivi di semplicità dalle definizioni di
ciascun concetto. Queste sono riportate in seguito in una tabella.

![Mappa concettuale delle entità dell'Ubiquitous Language](/toys-store/assets/images/UL.jpg)

|Concetto|Definizione|
|---|---|
|Prezzo|Il prezzo di un prodotto in catalogo è determinato da una valuta e da una quantità della stessa per i quali è possibile comprarlo.|
|Tipologia Prodotto|Un tipo di prodotto in catalogo che può essere comprato in uno o più negozi. La tipologia è rappresentata da un nome della tipologia e da una descrizione. È rappresentata da un identificatore univoco.|
|Prodotto In Catalogo|Un prodotto in catalogo in uno specifico negozio, di cui è possibile comprarne più copie. Possiede una specifica tipologia di prodotto ed ha un prezzo determinato dal negozio in cui è venduto. È rappresentato da un identificatore univoco per negozio.|
|Prodotto In Catalogo - A Posto|Il prodotto in catalogo è regolarmente collocato nella sua posizione all'interno della fila di prodotti.|
|Prodotto In Catalogo - Sollevato|Il prodotto in catalogo è stato sollevato da una fila di prodotti.|
|Prodotto|Un'entità la cui categoria è un prodotto in catalogo. È rappresentata da un identificatore univoco e possiede uno stato.|
|Prodotto - A Posto|Il prodotto è regolarmente collocato nella sua posizione all'interno della fila di prodotti.|
|Prodotto - In Carrello|Il prodotto è stato aggiunto in un carrello, diventerà parte del contenuto del carrello di un cliente.|
|Prodotto - Restituito|Il prodotto è stato restituito attraverso il sistema di restituzione e sarà rimosso dal contenuto del carrello di un cliente.|
|Contenuto Del Carrello|L'insieme dei prodotti che il cliente sta comprando durante la sua procedura d'acquisto, a ognuno dei quali è associato un conteggio. Sarà possibile aggiungere altri prodotti dopo averli inseriti nel proprio carrello, ma anche rimuoverli dopo averli restituiti.|
|Procedura di Acquisto|La procedura che segue un cliente, comprando in negozio. Questa procedura, attraverso la modifica del suo stato, modifica il contenuto del carrello dell'utente. Permette inoltre l'acquisto dei prodotti.|
|Procedura di Acquisto - Non Iniziato|Il cliente non risulta essere entrato in un negozio.|
|Procedura di Acquisto - Iniziando|Il cliente risulta essere entrato in un negozio.|
|Procedura di Acquisto - Iniziato|Il cliente ha sbloccato un carrello e sta comprando.|
|Procedura di Acquisto - Terminando|Il cliente è uscito dal negozio dopo avere effettuato l'acquisto, si attende di raggiungere il timeout per effettuare il pagamento.|
|Procedura di Acquisto - Terminato|La procedura d'acquisto è terminata e si sta effettuando il pagamento dell'acquisto effettuato. Al termine di quest'ultimo, la procedura d'acquisto tornerà al suo stato iniziale.|
|Costo|Il costo di un prodotto in catalogo è determinato da una valuta e da una quantità della stessa per i quali è possibile comprarlo.|
|Acquisito|L'insieme dei prodotti in catalogo che il cliente è intenzionato a comprare, per i quali sia possibile far pagare al cliente il loro costo tramite un pagamento. Viene effettuato in corrispondenza di un dato timestamp in un dato negozio ed ha un identificatore univoco.|
|Pagamento|Il pagamento del cliente in cambio dei prodotti in catalogo appartenenti al corrispondente acquisto. È composto di più stati, da un timestamp e da un identificatore univoco.|
|Pagamento - Iniziato|Stato del pagamento quando questo è iniziato.|
|Pagamento - Terminato|Stato del pagamento quando quest'ultimo è terminato.|
|Pagamento - Terminato - Successo|Stato della terminazione quando questa è avvenuta con successo.|
|Pagamento - Terminato - Fallimento|Stato della terminazione quando questa è avvenuta con un fallimento.|
|Carrello|L'entità utilizzata dal cliente per aggiungere un prodotto al suo contenuto del carrello. Possiede un identificatore per negozio, uno stato ed un allarme. Può essere mosso e associato ad un cliente tramite una scansione del suo identificatore.|
|Carrello - Bloccato|Lo stato del carrello mentre è bloccato. Non può essere mosso, né si possono aggiungervi prodotti, pena l'attivazione del suo allarme.|
|Carrello - Sbloccato|Lo stato del carrello dopo che il responsabile del negozio lo ha sbloccato. Può essere mosso da un cliente o da un responsabile di negozio, ma non vi si possono aggiungere prodotti, pena l'attivazione del suo allarme.|
|Carrello - Associato|Lo stato del carrello dopo che è stato associato al cliente tramite l'applicazione. Può essere mosso da un cliente o da un responsabile di negozio, mentre solo il cliente a cui è stato associato può inserirvi dei prodotti.|
|Negozio|L'entità costituita da uno specifico allestimento di gondole, di un sistema antitaccheggio, un insieme di carrelli e un sistema di restituzione. Ogni negozio ha un identificatore univoco.|
|Responsabile di Negozio|Un utente del sistema capace di amministrare un negozio e i processi di acquisto al suo interno attraverso una dashboard. Deve prima registrarsi e poi potrà de-registrarsi.|
|Utente|Un'entità identificata capace accedere al sistema. È dotato di nome utente che lo rappresenta e di una password per l'accesso, che può cambiare e verrà verificata per l'accesso. La password viene fornita in chiaro e poi criptata.|
|Cliente|Un utente del sistema che ha la possibilità di iniziare e terminare un processo di acquisto tramite un'apposita applicazione. Tra i suoi dati figurano il suo nome, il suo cognome e la sua email. Deve prima registrarsi e poi potrà de-registrarsi.|
|Carta di Pagamento|I dati associati ad un cliente che permettono il pagamento durante un processo d'acquisto per il proprio contenuto del carrello. Sono costituiti dal numero della carta, proprietario della carta e dal codice di sicurezza.|
|Amministrazione|Un utente del sistema dotato di maggiori privilegi di un responsabile di negozio, utilizzabile dall'amministrazione dell'azienda per raccogliere dati e svolgere compiti che richiedono maggior privilegio sulla dashboard.|
|Fila di Prodotti|Una fila di prodotti in catalogo disposta su di uno scaffale, contenente un certo conteggio degli stessi. Ogni fila di prodotti ha un identificatore univoco.|
|Scaffale|Un gruppo di file di prodotti, parte di una scaffalatura. Ogni scaffale ha un identificatore univoco.|
|Scaffalatura|Un gruppo di scaffali posti verticalmente, parte di una gondola. Ogni scaffalatura ha un identificatore univoco.|
|Gondola|Un gruppo di scaffalature raggruppate per area tematica o categoria merceologica dei prodotti contenuti al loro interno, costituiscono l'allestimento di un negozio. Ogni gondola ha un identificatore univoco.|
|Sistema Antitaccheggio|Il sistema, parte del negozio, capace di impedire furti di prodotti sollevati, perciò non inseriti in nessun carrello. Possiede un allarme che può essere attivato quando un prodotto sollevato viene rilevato dal sistema.|
|Sistema di Restituzione|Il sistema, parte del negozio, che permette la restituzione dei prodotti per rimuoverli dal contenuto del carrello di un cliente. Verranno posti in un luogo che permetterà ai responsabili di negozio di rimetterli a posto.|

Qui di seguito, invece, il dizionario delle traduzioni di ciascun concetto dall'italiano all'inglese.

|Italiano                |Inglese           |
|------------------------|------------------|
|A posto                 |In place          |
|Accedere                |Login             |
|Acquisto                |Purchase          |
|Allarme                 |Alarm             |
|Allestimento            |Layout            |
|Amministrazione         |Administration    |
|Annullare               |Cancel            |
|Applicazione            |App               |
|Associare               |Associate         |
|Attivare                |Trigger           |
|Bloccare                |Lock              |
|Carrello                |Cart              |
|Carta di pagamento      |Payment card      |
|Categoria               |Kind              |
|Cliente                 |Customer          |
|Codice di sicurezza     |Card security code|
|Cognome                 |Last name         |
|Comprare                |Buy               |
|Conteggio               |Count             |
|Contenuto del carrello  |Cart contents     |
|Costo                   |Cost              |
|Criptare                |Encrypt           |
|Dashboard               |Dashboard         |
|De-registrare           |Unregister        |
|Descrizione             |Description       |
|Email                   |Email             |
|Entrare                 |Enter             |
|Fallire                 |Fail              |
|Fila di prodotti        |Items row         |
|Gondola                 |Shelving group    |
|Identificatore          |Id                |
|In carrello             |In cart           |
|In chiaro               |Plain             |
|Iniziare                |Start             |
|Inserire                |Insert            |
|Muovere                 |Move              |
|Negozio                 |Store             |
|Nome                    |First name        |
|Nome della tipologia    |Name              |
|Nome utente             |Username          |
|Numero della carta      |Card number       |
|Pagare                  |Pay               |
|Password                |Password          |
|Prezzo                  |Price             |
|Procedura di acquisto   |Shopping process  |
|Prodotto                |Item              |
|Prodotto in catalogo    |Catalog item      |
|Proprietario della carta|Cardholder        |
|Quantità                |Amount            |
|Raggiungere             |Reach             |
|Registrare              |Register          |
|Responsabile di negozio |Store Manager     |
|Restituire              |Return            |
|Rilevare                |Detect            |
|Rimettere a posto       |Put in place      |
|Sbloccare               |Unlock            |
|Scaffalatura            |Shelving          |
|Scaffale                |Shelf             |
|Scansione               |Scan              |
|Sistema antitaccheggio  |Anti-theft system |
|Sistema di restituzione |Drop system       |
|Sollevare               |Lift              |
|Successo                |Success           |
|Terminare               |Terminate         |
|Timestamp               |Timestamp         |
|Timeout                 |Timeout           |
|Tipologia di prodotto   |Item category     |
|Uscire                  |Leave             |
|Utente                  |User              |
|Valuta                  |Currency          |
|Verificare              |Verify            |

## Requisiti Non Funzionali

In aggiunta ai requisiti funzionali elencati precedentemente, così come estrapolati dal processo di knowledge crunching,
sono stati individuati i seguenti requisiti non funzionali:

* Il sistema deve essere sufficientemente modulare in modo tale da essere possibile riusare i suoi componenti in
contesti diversi senza problemi;
  * Non ci devono essere dipendenze tra le classi dei componenti, ma solo verso interfacce liberamente re-implementabili;
  * Non ci devono essere dipendenze da interfacce di livello di dominio verso interfacce di livello applicativo o di
    livello dati, possono solo essere eventualmente presenti tra le loro implementazioni;
  * Non ci devono essere dipendenze da entità di livello dati verso entità di livello applicativo;
  * I componenti del sistema devono essere quanto meno possibile dipendenti tra di loro, devono poter essere implementati
    indipendentemente, rilasciati indipendentemente, devono fallire indipendentemente tra loro;
* Il sistema non deve mai interrompersi qualora si verifichi un errore, deve invece restituire un messaggio di errore;
* Le _user interfaces_ del sistema devono essere fluide, cioè non presentare _stuttering_ o _freezing_;
* Il sistema deve poter gestire un carico di almeno una cinquantina di utenti senza subire fallimenti dovuti a sovraccarico;
* La copertura degli _statements_ e dei _branches_ nella valutazione della _coverage_ deve essere pari o superiore all'80%;
* La qualità del codice deve essere considerata adeguata secondo lo strumento "Sonarcloud".

## Requisiti Implementativi

In aggiunta ai requisiti funzionali elencati precedentemente, così come estrapolati dal processo di knowledge crunching,
sono stati individuati i seguenti requisiti implementativi:

* L'implementazione dell'applicazione deve essere fatta nel linguaggio "scala", con compatibilità garantita con la versione 3.1.3;
* Deve essere sfruttato lo strumento di _build automation_ "scala build tool" versione 1.8.0 o successive per automatizzare la
  compilazione, il _testing_ e il _deployment_ degli artefatti rilasciati;
* Devono essere applicati i pattern tattici del _Domain Driven Design_ durante la progettazione e l'implementazione del sistema;
* La repository deve essere gestita attraverso il D.V.C.S. "git" e mantenuta sul servizio di _hosting_ "GitHub";
* Deve essere sfruttata la tecnologia offerta da "GitHub" per effettuare _continuous integration_ e _continuous deployment_,
  ovvero "GitHub Actions";
* Il _versioning_ degli artefatti deve avvenire in maniera automatica attraverso lo strumento "semantic release", ciò implica il
  fatto che i numeri di versione vengono scelti secondo la strategia "semantic versioning".

<br/>
<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/">Home</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/architectural_design">Design Architetturale</a></div>
</div>
<br/>
