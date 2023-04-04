---
title: Design di dettaglio
permalink: /detailed_design
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/architectural_design">Design architetturale</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/devops">Devops</a></div>
</div>
<br/>

Una volta realizzati i bounded context canvas, si tratta di completarli con le relazioni di context mapping che li mettono in
relazione. Per questo motivo, si è entrati maggiormente nel dettaglio di ciascun bounded context andando a definire l'architettura
interna di ciascuno di essi: quali sono i suoi componenti, di che tipo sono le sue relazioni con gli altri e come queste vengono
realizzate. Lo strumento "context mapper" si è perciò rivelato doppiamente utile, oltre che definire la "context map" dura e pura,
ha permesso di specificare quali entities, quali value objects, quali services compongono la sua implementazione e com'è fatta
l'implementazione della loro interfaccia. In questo modo, è stato possibile tradurre direttamente i file di modellazione
dell'architettura in linguaggio scala, a cui poi è possibile far seguire la loro implementazione e il test.

Una volta strutturata l'architettura di ciascun bounded context, si è deciso quali paradigmi, quali classi di tecnologie adottare
per strutturare l'architettura del singolo bounded context. Si è deciso di utilizzare un'architettura a microservizi dove ogni
bounded context è un microservizio. Questi comunicano tra di loro utilizzando un message broker, ovvero un intermediario che fa
da canale di comunicazione dotato di una coda propria responsabile di ricevere i messaggi da chi li pubblica e inviarli a chi ha
richiesto di volerli ricevere. In questo modo nessuna comunicazione è bloccante, così come i migliori modelli per i sistemi
distribuiti indicano. La comunicazione tra i bounded context e le interfacce, ovvero l'applicazione del cliente e la dashboard
del responsabile di negozio e dell'amministrazione avviene tramite delle più tradizionali ReST API. Ogni microservizio è dotato di
un proprio "data layer", capace di far persistere e recuperare i dati necessari.

Per realizzare la logica di business dei diversi sotto-sistemi, ovvero i carrelli, le scaffalature, il sistema di restituzione e
quello antitaccheggio, si è utilizzato il paradigma dei "Digital Twin". In questo modo, avendo a disposizione delle rappresentazioni
puramente virtuali degli _asset_ fisici, queste possono interamente essere incapsulate e manipolate dai corrispondenti microservizi.
Per di più, essendo queste rappresentazioni quelle che contengono la totalità dello stato osservabile delle controparti fisiche,
oltre alla logica che permette l'attuazione del funzionamento previsto a partire dai dati provenienti dai sensori che compongono il
_physical asset_, non è necessario dover dividere il sistema a metà. Lo _asset_ fisico dovrà solamente preoccuparsi di mettere in
atto le azioni che gli possono essere impartite e notificare il microservizio di riferimento degli eventi predeterminati.

Al cuore di ogni microservizio, il suo flusso di controllo è gestito da un sistema ad attori. È stato deciso di utilizzare questo
paradigma perché concettualmente semplice e che ben si sposa con la modellazione di sistemi distribuiti, che sono sistemi fatti di
componenti che comunicano a scambio di messaggi, in modo non bloccante, con nessuna visione dello stato globale del sistema. Un
insieme di sistemi ad attori permette infatti proprio questo, ovvero di avere delle entità attive, appunto capaci di incapsulare
interamente un flusso di controllo, che comunicano inserendo messaggi nella _message queue_ l'uno dell'altro senza sapere
nient'altro che non siano gli identificatori degli altri attori con cui comunicare.

La fase di design di dettaglio, che prende in considerazione gli elementi più minuziosi, è stata svolta unicamente dal software
architect in concomitanza con il team di sviluppo, dato che non coinvolge più informazioni di organizzazione o comunque relativi
alla strategia aziendale per la conduzione del progetto. Al contrario, tutte le decisioni prese in questa fase coinvolgono
principalmente il come il codice verrà strutturato, competenze che non sono proprie di un project manager.

## Context mapping

Qui di seguito si può vedere la context map dei bounded context individuati.

![Context map dei bounded context individuati](/toys-store/assets/images/CM.png)

È bene innanzitutto ricordare come "shopping", qui denominato "ShoppingContext", sia il bounded context più importante di tutti.
Nel momento nel quale perciò "utenti" o "negozi", rispettivamente denominati "UsersContext" e "StoresContext" decidono di voler
interagire con il primo, necessariamente lo faranno attraverso un "Open Host Service" utilizzando un "Published Language", ovvero
il linguaggio definito mediante la combinazione di specifiche ReST API e per l'uso del _message broker_. In questo modo, il modello
del bounded context può rimanere isolato al suo interno e non essere intaccato da cambiamenti dei "downstream". Allo stesso modo
però questi due bounded context si proteggono dai cambiamenti nel linguaggio offerto da "shopping" isolando il proprio modello
mediante un "Anti-Corruption Layer", accettando risposte alle proprie richieste che verranno poi tradotte adeguatamente. Lo stesso
discorso può essere fatto per la relazione che intercorre tra "carrelli", "CartsContext", e "prodotti", così come quella tra
"negozi" e "prodotti". Questa modellazione è particolarmente importante quanto si tratta della relazione tra "pagamenti",
"PaymentsContext", e "shopping", ma anche tra "pagamenti" ed "utenti". Rappresentando infatti il primo un intermediario tra il
sistema esterno di pagamento e il sistema vero e proprio, necessariamente gli altri due bounded context devono proteggersi
mediante un "Anti-Corruption Layer" e il bounded context intermediario offrire un servizio ben definito mediante un linguaggio
altrettanto ben definito. Quelle tra "shopping" e "carrelli" e tra "shopping" e "prodotti" sono partnership perché necessariamente
l'uno utilizza i servizi dell'altro e viceversa, perciò necessariamente i bisogni dell'uno sono parte dei bisogni dell'altro e
viceversa, perciò il loro sviluppo deve avvenire in maniera coordinata.

Per quanto riguarda le interfacce come l'applicazione e la dashboard, non rappresentate nella context map, si è deciso che la
relazione che hanno con tutti i bounded context è quella "conformist-published language". Questo significa che il modello delle
interfacce si adegua sempre e comunque a quello dei bounded context, in quanto queste non ne hanno uno vero e proprio a cui
attingere e perciò sfruttano quello dei servizi che utilizzano. La relazione tra sistemi che permettono l'interazione diretta con
il cliente e i bounded context è sempre di tipo "partnership" perché questi sistemi possono sempre inviare eventi come notifiche
e ricevere _command_ che permettono di modificare il loro stato o fanno compiere loro un'azione.

Per quanto riguarda "UsersContext", è stato deciso di suddividerlo in tre "aggregate", uno per ciascuna tipologia di utente che
deve gestire. Conseguentemente, ogni aggregate è dotato di una sola "entity" che fa da "root" che rappresenta la tipologia di
utente. Infatti, non capita mai di dover gestire tipi diversi di utenti allo stesso modo, perciò possono anche essere separati.
Non avrebbe avuto senso suddividere ulteriormente gli utenti ciascuno nel proprio bounded context perché sono concetti a grana
troppo fine. Infatti, è presente un ulteriore aggregate che trattiene una entity astratta con gli elementi comuni a tutti gli
utenti. Inoltre, contiene anche il "service" per la gestione delle password, che necessariamente avviene tramite specifici algoritmi
pensati per essere sicuri, ma la cui effettiva implementazione è irrilevante ai fini della progettazione. Ognuna delle entity
concrete è poi dotata di un "repository" che si interfaccia con il data layer e presenta tutte le operazioni per manipolare i dati
relativi a ciascuno dei tipi di utenti.

Il bounded context "ItemsContext" contiene un aggregate per ciascuno dei componenti principali che riguardano i prodotti, ovvero
la tipologia di prodotto, il prodotto in catalogo e il prodotto vero e proprio. Ciascuno di questi viene poi rappresentato da una
entity che fa da root all'aggregate ed è associato ad una repository che contiene le operazioni di aggiunta, rimozione ed
aggiornamento, se possibili. Anche se esiste una relazione di tipo "part-of" tra questi tre concetti nell'ordine in cui sono stati
elencati, questo non vuol dire che vengano gestiti assieme, anzi, quando un'operazione riguarda una delle tre entità, questa non
ha ricaduta sulle altre, anche grazie ai vincoli per i quali la cancellazione di una tipologia di prodotto non è possibile se vi
è associato almeno un prodotto in catalogo e così via. Interessanti sono i "domain event" associati ai cambiamenti di stato dei
diversi concetti, che sono chiaramente eventi importanti per il dominio. A ciascun gruppo di eventi relativi allo stato di una
entity è associato un service che contiene gli _event handler_ per gli stessi, ovvero i metodi che rappresentano la logica di
gestione degli eventi corrispondenti.

Il bounded context "CartsContext" è simile ai precedenti, ma più semplice, in quanto è presente un solo concetto fondamentale: il
carrello, modellato da una entity che è root nel suo aggregate e con associata una repository capace di effettuare le operazioni
sui dati relativi a questa entity. I domain event presenti sono sia eventi di cui il bounded context viene notificato, per questo
esiste un service che raccoglie i loro _event handler_, sia eventi che questo genera e deve notificare ad altri. Infatti, se il
carrello è stato associato o vi è stato inserito un prodotto sono informazioni utili ad altri, se il carrello è stato mosso oppure
è stato inserito un prodotto di cui ancora non si sa che cosa sia, sono informazioni di pertinenza del bounded context. Sarà lui
infatti a decidere come gestire questi due eventi, in relazione alla logica del Digital Twin.

Il bounded context "StoresContext", come il precedente, si compone di una sola entity che fa da root al suo aggregate e ha associata
una repository. Questa però è deputata alle operazioni per l'unica informazione che è possibile trattenere per un negozio, ovvero
il suo allestimento. Tutti i suoi componenti sono poi modellati come "value object" interni all'aggregate, in quanto non hanno
alcun senso separati dall'allestimento in cui si trovano. Più importanti in questo bounded context sono i domain event. Eventi
come quello relativo all'inserimento di un prodotto nel sistema di restituzione, la restituzione effettiva del prodotto, il
rilevamento di un prodotto in catalogo sollevato e l'individuazione di un prodotto da parte del sistema antitaccheggio hanno un
corrispondente _event handler_ in un service in quanto arrivano dagli _asset_ fisici dei Digital Twin e perciò il bounded context
è tenuto a reagire, incapsulando in questi metodi la logica di gestione degli eventi. Alcuni di questi sono anche inviati dal
bounded context, ma senza nessuna operazione di trasformazione. Mentre invece l'evento di prodotto in catalogo sollevato
è generato dal bounded context stesso, come traduzione di quello del rilevamento del sollevamento, per questo non ha un metodo
associato. Anche se potrebbe sembrare che questo bounded context si possa separare in più parti, ad esempio una per sotto-sistema
che gestisce, non è stato ritenuto necessario perché questi sistemi sono talmente semplici che bastano pochi eventi per descrivere
completamente il loro comportamento. In più, anche se sono molti i _command_ che vengono inviati a questo bounded context, è solo
per permettere la completa gestione dell'allestimento e perciò non è utile separarli tra di loro.

Per quanto riguarda il bounded context "ShoppingContext" è anch'esso composto da una sola entity che è quella del processo
d'acquisto del cliente, che fa da root all'unico aggregate presente. Ogni sotto-entità ha il compito di gestire che cosa l'utente
può fare nel momento in cui il cliente si trova nello stato corrispondente e trattenere i suoi dati. Inoltre, è sempre possibile
terminarla anticipatamente mediante metodi appositi. Associata alla entity c'è una repository pensata per far persistere, modificare
e fare _query_ sui dati delle procedure d'acquisto. Tra i domain event sono principalmente presenti eventi che arrivano da altri
bounded context e servono per modificare lo stato della procedura d'acquisto del cliente, come quelli per il cliente entrato o
uscito dal negozio e per il carrello associato ad un cliente. Altri eventi di cui viene notificato è l'aggiunta o la rimozione di
un prodotto dal contenuto del carrello di un cliente, dato che lo stato della procedura d'acquisto è anche fatto dal contenuto
del carrello del cliente. Tutti questi hanno un _event handler_ in un service pensato appositamente allo scopo, così come l'evento
di cliente de-registrato, che ha un effetto anche su questo bounded context, solo in service differente. Alcuni di questi eventi
sono poi anche inviati ad altri bounded context, nel momento nel quale è questo a generarli. L'evento di sollevamento di un prodotto
in catalogo è però l'unico che viene solamente inviato e per questo non ne deve essere gestita la ricezione.

Ultimo, ma non per questo meno importante, è il bounded context "PaymentsContext". Questo si compone di tre entità, una per i
clienti, dato che deve occuparsi di trattenere le informazioni sui metodi di pagamento registrati, una per gli acquisti e una per
i pagamenti. Tutte e tre sono root del loro aggregate e sono associate a repository capaci di serializzare queste entità, ottenerle
e fare _query_ su di esse, dalle più semplici alle più complesse così come richiesto dalle funzionalità identificate. L'entità del
cliente possiede anche il domain event relativo alla sua de-registrazione, in quanto è necessario in tal caso eliminare i suoi dati
e questo verrà fatto grazie all'_event handler_ associato. Ultimi due eventi presenti sono quelli di pagamento completato con
successo e con fallimento, che vengono notificati dal sistema esterno di pagamento e vengono inoltrati all'applicazione del cliente.
Per questo motivo anche questi ultimi hanno un proprio _event handler_ associato e, come per il precedente, questi metodi sono
raccolti in un service apposito. Il fatto che questi eventi avvengano porterà al cambiamento dello stato del pagamento,
rappresentato dalle sotto-entità dell'entità "pagamento" stessa.

In tutti i bounded context, i value object sono sempre stati utilizzati per modellare tipi di dato semplici, atomici, che però
hanno regole di costruzione ben definite e non possono essere sostituiti da alternative più generiche come "intero" o "stringa".
Sono tutti quei tipi di dato che vengono utilizzati per definire una entity nella sua interezza. Le entity dotate di uno stato
sono sempre state modellate tramite una entity astratta che rappresenta il concetto più una serie di entity concrete che
rappresentano i diversi stati della stessa. Ogni "stato" possiede dunque i metodi per passare dall'uno all'altro, che rappresentano
le transizioni della _state machine_ corrispondente.

## Architettura a microservizi

## Digital twin

## Attori

<br/>
<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/architectural_design">Design architetturale</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/devops">Devops</a></div>
</div>
<br/>
