---
title: Design architetturale
permalink: /architectural_design
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/requirements">Requisiti</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/detailed_design">Design di dettaglio</a></div>
</div>
<br/>

Una volta completata la fase di analisi dei requisiti, si è passati a stilare l'architettura del sistema. Questa operazione è stata
facilitata dal fatto che le tecniche utilizzate per la raccolta dei requisiti, nella fattispecie lo "event storming", aiutano a
delineare i potenziali "bounded context" che compongono il dominio della soluzione.

I bounded context che sono emersi sono quindi i seguenti:

* utenti, pensato per gestire i dati che riguardano tutti i tipi di utenti del sistema, incluso i meccanismi di login, registrazione
  e de-registrazione;
* prodotti, pensato per gestire i dati dei diversi tipi di prodotti, il loro stato, nonché i cataloghi dei diversi negozi;
* carrelli, pensato per gestire lo stato dei carrelli dei negozi, incluso i meccanismi di blocco, sblocco e associazione ai clienti;
* negozi, pensato per gestire tutti i sistemi interni al singolo negozio, assieme all'allestimento dello stesso;
* shopping, pensato per gestire lo stato della procedura d'acquisto del cliente e poter quindi "governare" gli altri bounded context;
* pagamenti, pensato per gestire i dati sullo storico degli acquisti e dei pagamenti effettuati dai diversi clienti.

Prima di dettagliare ciascun bounded context, ci si è preoccupati di portare a termine la fase di "domain charting". Questo è stato
utile per definire un indice di "importanza" dei diversi sotto-domini, assumendo che ciascun bounded context facesse riferimento
ad un dominio analogo. Il domain charting infatti permette di mettere in rapporto la complessità del sotto-dominio con la sua
importanza per il business, cioè il suo _business value_. In questo modo è possibile dare maggiore priorità a quei domini che sono
più importanti per il business la cui complessità è ridotta, in un ottica di poter consegnare "early and often", in un'ottica non
dissimile dai principi di sviluppo della metodologia "agile". Viceversa, i domini che non sono particolarmente interessanti si può
pensare di fare _outsourcing_ della loro implementazione a terzi, magari acquistando soluzioni "off-the-shelf" che necessitano un
processo di _customizing_ minimo. Questo appare del tutto evidente quando, una volta inseriti i sotto-domini nel grafico, poi
questi vengono classificati nelle diverse categorie.

Per ognuno dei bounded context è stato realizzato uno schema capace di identificare le sue proprietà fondamentali. Oltre ad una
breve descrizione, per ogni bounded context è stata indicata la sua "classificazione strategica", ovvero che tipo di bounded
context è in relazione alla strategia che il business sta adottando per questo progetto. Sarà perciò utile indicare, oltre
all'indice di "importanza" come dedotto dal domain charting, il tipo di utilità che il business trova nello specifico bounded
context - è ciò che genera introiti, è ciò che mantiene _engaged_ gli stakeholder e simili - e chi realizzerà la sua implementazione.
Si indica inoltre per ogni bounded context il ruolo che svolge il suo dominio in relazione al sistema nella sua interezza, gli
elementi dello "ubiquitous language" che sono legati al sotto-dominio del bounded context e che servono per descriverlo e le
decisioni di business, ovvero i vincoli sulle funzionalità offerte dal bounded context che dovranno essere tenute in considerazione
durante l'implementazione del sotto-sistema associato. Infine vi sono le "inbound communications"
e le "outbound communications", ovvero i comandi, le query e gli eventi che il bounded context riceve da sistemi esterni e invia
ad altri sistemi. Questi due elementi sono forse i più importanti perché vincolano come sarà strutturato il context mapping
effettuato successivamente, sia perché per ciascuna delle coppie sistema esterno - sistema interno è necessario indicare il tipo
di relazione di context mapping esiste tra i due, sia perché i diversi messaggi scambiati daranno necessariamente origine a metodi
propri delle interfacce dei sistemi implementati.

La fase di design architetturale è stata svolta in presenza di soli componenti del team di progetto interni a Pervasive Cats,
ovvero un project manager, il software architect e il team di sviluppo. I primi due hanno potuto riportare le informazioni apprese
durante la fase precedente ai restanti membri del team, aiutati dalla presenza dello "ubiquitous language" e potendo quindi parlare
nella "lingua" dei _domain experts_ e della restante documentazione prodotta che illustra le funzionalità, i casi d'uso e i restanti
requisiti che contraddistinguono la soluzione.

## Domain charting

Il diagramma emerso dalla fase di domain charting è stato il seguente.

![Grafico di domain charting emerso per il sistema](/toys-store/assets/images/DC.jpg)

Come si può osservare, il primo sotto-dominio che incontriamo da sinistra verso destra è quello di "pagamenti". Sistemi di pagamento
elettronico sono molto difficili da realizzare, in quanto devono modellare dei concetti principalmente appartenenti al mondo
economico, con un'implementazione che deve sottostare fortemente alle legislazioni dei paesi in cui questi sistemi operano. Allo
stesso tempo, non è un sistema particolarmente importante per il business perché non è ciò che di innovativo questo sistema porta,
non è ciò che è utile incrementare i ricavi o ridurre i costi di esercizio del committente. Senza dubbio, questo dominio è un
"generic domain" ed ha senso che sia _outsourced_, creando una semplice interfaccia per permettere di comunicare tra il suo sistema
e il sistema realizzato.

Incontriamo poi i due domini "utenti" e "prodotti", che sono molto simili tra loro. Ci aspettiamo che infatti i loro modelli siano
facili da realizzare, non a caso infatti saranno delle interfacce su un "data layer" che mantiene le informazioni su questi due
domini, con una limitata capacità di elaborazione. Il loro modello sarà quindi esprimibile tramite metodi di modellazione più
semplici come quello "entity-relationship" tipico dei _database_ relazionali. Sono relativamente poco importanti perché comunque
trattenendo unicamente dei dati, non è qui che avviene la computazione che rende il sistema "smart". Sono perciò dei "supporting"
domain; non ci si aspetta che siano degli "hidden core" data la loro più o meno chiara modellazione. Il dominio "prodotti" è
leggermente più importante di quello "utenti" data la sua particolarità e non presenza in tutti i sistemi.

Successivamente sono presenti i due sotto-domini "negozi" e "carrelli", che sono ancora più importanti dei due precedenti per
quanto riguarda il business. Questi due infatti sono domini unici al problema che si sta cercando di risolvere, che contengono i
sistemi "smart" che opereranno in negozio e che permettono al negozio di raggiungere il suo obiettivo di essere più rapido perché
concetto di carrello assieme ai suoi stati, mentre quella di "negozi" è comparabile a quella di "utenti". Questo perché un negozio
porta con sé molti più concetti da modellare, semplicemente a partire dal fatto che deve occuparsi di più tipi di sotto-sistemi
diversi, mentre "carrelli" si occupa solamente, appunto, dei carrelli. Questo ha per conseguenza che "carrelli" è un dominio
"supporting", mentre "negozi" è un dominio "core". Anche per "carrelli" non ci si aspetta che sia un "hidden core", mentre per
"negozi" non ci si aspetta sia uno "short term", in quanto essendo il tasso di innovazione del progetto molto elevato il tempo per
cui le _feature_ introdotte dal sistema siano replicate dalla concorrenza sia lungo.

Ultimo dominio è quello denominato "shopping", che si trova nell'angolo in alto a sinistra perché è il "decisive core domain" per
definizione. È il dominio più interessante per il business in assoluto, dato che è quello che abilita il cliente alla possibilità
di effettuare l'acquisto in negozio, trattenendo lo stato della sua procedura d'acquisto. Ha anche la complessità più elevata
perché deve interagire con la maggioranza degli altri domini, oltre che con le interfacce che sono l'applicazione e la dashboard.

## Bounded context Utenti

Nel diagramma successivo è illustrata la descrizione del bounded context "utenti".

![Diagramma che descrive il bounded context "utenti"](/toys-store/assets/images/Users_bc.jpg)

Questo bounded context è un "compliance enforcer" perché serve al sistema per funzionare, ma non ha nessun'altra utilità specifica
ed è "custom built" perché sarà realizzato internamente. Il ruolo del suo dominio è quello di "information holder" perché il suo
stato è unicamente determinato dalle informazioni che trattiene.

I messaggi che riceve sono unicamente dai _frontend_ dell'applicazione e della dashboard secondo quello che i casi d'uso hanno
definito in precedenza. Più interessanti sono i messaggi in uscita, in quanto in caso un cliente si de-registrasse bisognerebbe
notificare questo evento ai bounded context "shopping" e "pagamenti", al primo per eliminare la sua procedura d'acquisto mentre al
secondo per eliminare le informazioni sui metodi d'acquisto del cliente.

Le decisioni di business riguardano il login, che per i clienti avviene con email e password, mentre per responsabili di negozio e
per l'amministrazione avviene con nome utente e password. Il bounded context inoltre non deve mantenere le sessioni di login per
non essere inutilmente complicato, saranno i _frontend_ a gestire questa problematica.

## Bounded context Prodotti

Nel diagramma successivo è illustrata la descrizione del bounded context "prodotti".

![Diagramma che descrive il bounded context "prodotti"](/toys-store/assets/images/Items_bc.jpg)

Questo bounded context è un "compliance enforcer" perché serve al sistema per funzionare, ma non ha nessun'altra utilità specifica
ed è "custom built" perché sarà realizzato internamente. Il ruolo del suo dominio è quello di "structurer" perché di fatto questo
bounded context è un'interfaccia verso un data layer e basta, i _command_ e le _query_ che permette sono quelle che ci si
aspetterebbe da un data storage e nulla di più, senza particolari astrazioni oltre questo semplice livello.

I messaggi che riceve sono diversi, quelli più significativi sono gli eventi per segnalare che un prodotto o un prodotto in catalogo
sono stati rimessi a posto dalla dashboard, che sono il modo che il responsabile di negozio ha per segnalare il fatto che ha
compiuto il suo lavoro di ricollocamento degli elementi dell'allestimento del negozio. Per quanto riguarda i messaggi in uscita
invia solamente una _query_ verso il bounded context "shopping" per visualizzare quali processi d'acquisto contengono un dato
prodotto, questo perché non è possibile eliminare un prodotto se questo si trova nel contenuto del carrello di un qualche cliente.

Oltre alla regola di business appena indicata, per quanto riguarda la cancellazione di entità ad opera del bounded context, non è
possibile eliminare una categoria di prodotti finché esistono dei prodotti associati ad essa e non è possibile eliminare una
tipologia prodotto finché c'è una categoria prodotto che è associata ad essa. Questo per permettere la modifica dei dati in qualsiasi
momento, anche a negozio aperto, senza alterare la consistenza dei dati nel sistema. Inoltre, tramite la dashboard possono essere
fatte diverse operazioni, ma non tutte possono essere fatte da tutti gli utenti, alcune sono di competenza dell'amministrazione e
altre dei responsabili di negozio, come in accordo ai casi d'uso.

## Bounded context Carrelli

Nel diagramma successivo è illustrata la descrizione del bounded context "carrelli".

![Diagramma che descrive il bounded context "carrelli"](/toys-store/assets/images/Carts_bc.jpg)

Questo bounded context è un "engagement generator" perché non è ciò che porta al guadagno del business, però i clienti
vi interagiscono, ed è "custom built" perché sarà realizzato internamente. Il ruolo del suo dominio è
quello di "service provider" perché non è un semplice "information holder": con le informazioni che trattiene offre anche un
servizio, ovvero quello di gestire i carrelli e fare in modo che rispondano correttamente agli input che gli utenti danno loro.

I messaggi in ingresso più importanti sono infatti quelli che arrivano dall'interazione diretta dei clienti con il bounded context,
non mediata dal _frontend_ dell'applicazione, che avviene nel momento nel quale questi usano i carrelli. I due eventi per cui è
interessante venire notificati sono il fatto che il cliente può muovere il carrello e inserire al suo interno dei prodotti. In
risposta a tali eventi è possibile richiedere di attivare l'allarme del carrello tramite apposito _command_. Inoltre è possibile
richiedere al carrello sempre tramite _command_ di bloccarsi, sbloccarsi o associarsi ad uno specifico cliente. Tra i messaggi in
uscita abbiamo anche l'evento di aggiunta di un prodotto al carrello, che viene notificato sia a "shopping", per aggiornare lo
stato della procedura d'acquisto del cliente, e a "prodotti", per aggiornare il suo stato. A "shopping" viene anche notificato
l'evento di associazione di un carrello per aggiornare lo stato della procedura d'acquisto del cliente e permettergli di avviare
effettivamente il suo acquisto.

Tra le regole di business abbiamo il fatto che un carrello può essere utilizzato unicamente per rilevare l'incremento di peso,
quindi quando un nuovo prodotto viene aggiunto, non quando viene rimosso. La rimozione può avvenire solamente grazie al sistema di
restituzione dei prodotti. Inoltre, l'allarme verrà attivato solamente nel caso in cui il cliente inserisca prodotti al suo interno
mentre questo non è associato o venga mosso mentre è bloccato.

## Bounded context Negozi

Nel diagramma successivo è illustrata la descrizione del bounded context "negozi".

![Diagramma che descrive il bounded context "negozi"](/toys-store/assets/images/Stores_bc.jpg)

Questo bounded context è un "compliance enforcer" perché serve al sistema per funzionare, ma non ha nessun'altra utilità specifica
ed è "custom built" perché sarà realizzato internamente. Il ruolo del suo dominio è quello di "service provider" perché non è un
semplice "information holder", con le informazioni che trattiene offre anche un servizio, ovvero quello di gestire tutti i
sotto-sistemi presenti in negozio e fare in modo che rispondano correttamente agli input che gli utenti danno loro.

I messaggi in input più rilevanti sono quelli derivanti direttamente dall'interazione diretta del cliente con il bounded context
mediante i sotto-sistemi che quest'ultimo deve gestire. Infatti può arrivare un messaggio evento legato al fatto che un cliente ha
inserito un prodotto nel sistema di restituzione, al che il bounded context risponde con un _command_ che istruisce quest'ultimo
di mostrare le informazioni su quel prodotto al cliente, così che quest'ultimo possa confermare la sua scelta di restituirlo oppure
no. Un altro messaggio evento che il bounded context può ricevere è quindi quello di effettiva restituzione del prodotto. Possono
anche arrivare messaggi evento relativi al fatto che il sistema antitaccheggio ha rilevato un prodotto, al che il bounded context
può rispondere con un _command_ per attivare il suo allarme. Ultimo messaggio in input è l'evento di sollevamento di un prodotto in
catalogo come notificato da una scaffaleria. Sempre alla scaffaleria può essere notificato di aggiungere o rimuovere uno scaffale
e così una fila di prodotti, in accordo a quello che decide di fare un responsabile di negozio.

I messaggi che questo bounded context manda agli altri sono innanzitutto quelli che invia al bounded context "prodotti". Deve
infatti poter fare una _query_ per richiedere le informazioni su di un prodotto, per poterle inoltrare al sistema di restituzione.
A "prodotti" però inoltra anche gli eventi di restituzione di un prodotto e di sollevamento di un prodotto in catalogo, così che
il bounded context possa tenere traccia dello stato di questi ultimi. Infine, l'ultimo evento che inoltra è quello di restituzione
di un prodotto al bounded context "shopping", per fare in modo che questo lo rimuova dal contenuto del carrello del cliente relativo.

L'unica regola di business è quella per la quale l'allarme del sistema antitaccheggio si attiva nel momento nel quale un cliente
si avvicina ad esso con un prodotto, ma lo stato di questo prodotto non è "nel carrello".

## Bounded context Shopping

Nel diagramma successivo è illustrata la descrizione del bounded context "shopping".

![Diagramma che descrive il bounded context "shopping"](/toys-store/assets/images/Shopping_bc.jpg)

Questo bounded context è un "revenue generator" perché, essendo quello che permette al cliente di effettuare il suo acquisto in
negozio, mantenendo lo stato della procedura d'acquisto, è ciò che permette veramente a Toys Store di ottenere i ricavi desiderati.
A maggior ragione deve essere "custom built", quindi realizzato internamente. Il ruolo del suo dominio non può che essere "service
provider" dato che è deputato a fornire il servizio più importante di tutti.

Avendo già discusso dei suoi messaggi in ingresso, ci concentreremo su quelli in uscita. Il bounded context "shopping" può inviare
un _command_ a "carrelli" di bloccare il carrello associato ad un cliente, nel momento nel quale è chiaro che questo è uscito dal
negozio e non dovrà più utilizzarlo. Può inviare anche i _command_ per aggiungere un acquisto quando questo è stato effettuato e
per aggiungere un pagamento quando il lasso di tempo per effettuare il pagamento è scaduto. Da ultimo, può inviare a "prodotti" il
_command_ per rimuovere un prodotto, dato che quando questo è stato acquistato, non si trova più in negozio. Inoltre può inviare
anche gli eventi per notificare che un prodotto in catalogo è stato sollevato, quando viene rimosso forzatamente un prodotto dal
contenuto del carrello di un cliente, così come l'evento per cui il prodotto è stato aggiunto al suo carrello, quando accade.

L'unica decisione di business è quella per cui il pagamento avviene automaticamente all'uscita dal negozio, senza nessuna conferma,
perciò servirà che il bounded context tenga traccia di quali acquisti hanno dei pagamenti pendenti e quali no.

## Bounded context Pagamenti

Nel diagramma successivo è illustrata la descrizione del bounded context "pagamenti".

![Diagramma che descrive il bounded context "pagamenti"](/toys-store/assets/images/Payments_bc.jpg)

Questo bounded context è un "compliance enforcer", in quanto è necessario al sistema per funzionare, d'altronde senza poter
processare i pagamenti il cliente non potrebbe fare i propri acquisti, ma non ha nessun'altra utilità se non questa. Poiché di
sistemi capaci di processare i pagamenti ne esistono già molti in commercio e costruirsene uno proprio da zero è complesso sia da
un punto di vista di sviluppo che da un punto di vista legale, si è deciso che il sistema che lo implementerà sarà "off-the-shelf",
ovvero una soluzione già pronta in commercio ed acquistata così com'è, che verrà adeguata alle specifiche del problema. Il ruolo
del dominio a lui associato è quello di "interfacer", cioè di elemento che si pone come interfaccia verso un sistema esterno.

Gli unici messaggi in ingresso interessanti sono quelli relativi al sistema esterno che è il sistema di pagamento, che sarà
responsabilità di questo bounded context gestire. Questo sistema notificherà il bounded context con degli eventi se un dato
pagamento ha avuto successo oppure si è concluso con un fallimento. Questi due eventi saranno poi inoltrati al cliente mediante la
sua applicazione, in modo da dargli conferma dell'esito del suo acquisto.

<br/>
<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/requirements">Requisiti</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/detailed_design">Design di dettaglio</a></div>
</div>
<br/>
