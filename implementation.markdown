---
title: Implementazione
permalink: /implementation
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/devops">Devops</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/testing">Testing</a></div>
</div>
<br/>

Per l'implementazione della soluzione proposta sono state utilizzate svariate tecnologie nelle diverse fasi del progetto. Si è
fatto uso di due middleware: "Eclipse Ditto", per la realizzazione dei digital twin, e "RabbitMQ", per la comunicazione
asincrona e message-oriented dei microservizi. Per permettere comunicazione asincrona è stata utilizzata anche la tecnologia delle
websocket, che permettono la creazione di canali full-duplex tra client e server. Inoltre, sia durante il testing che il
deployment, è stata utilizzata la tecnologia per la creazione di container "Docker", in modo da avere degli ambienti di esecuzione
sempre riproducibili. Da ultimo, ma non per questo meno importante, è stata utilizzata la libreria ad attori "Akka", con la sua
estensione per creare server web e websocket.

## Eclipse Ditto

Eclipse Ditto è un middleware rilasciato dalla Eclipse Foundation per la realizzazione di digital twin. Ditto permette di mettere
in comunicazione dispositivi fisici, i "physical twin", con il software che è la loro rappresentazione digitale, il loro "digital
twin", appunto. Ditto fa questo creando dei canali di comunicazione tra le due controparti, memorizzando al suo interno le
informazioni sui diversi digital twin che deve gestire. Le informazioni che trattiene per ognuno di essi sono composte dagli
attributi del twin, cioè le sue proprietà, assieme alle "feature", cioè le caratteristiche fisiche o virtuali di cui si compone,
che possono quindi essere altri dispositivi, moduli di funzionalità software e simili. Nel progetto, ci si è limitati all'uso
degli attributi, in quanto le _thing_ che era necessario modellare erano semplici nella composizione.

I canali di comunicazione tra physical e digital twin sono per loro natura asincroni e message-oriented, in quanto ogni _thing_ è
dotata di una "inbox", per i messaggi che sono diretti verso il device, e di una "outbox", per i messaggi che sono diretti verso
la componente software. Il loro invio e la loro ricezione sono a carico di Ditto, che offre la possibilità di registrare delle
_callback_ nel momento nel quale tali messaggi sono stati ricevuti. Questo meccanismo è stato sfruttato appieno per rendere le due
controparti perfettamente in grado di comunicare tra di loro ed effettuare il cosiddetto "shadowing" e fare in modo che tutta la
computazione venga eseguita dalla componente virtuale.

Ditto supporta anche un meccanismo di gestione delle policy per regolare chi ha accesso ai diversi digital twin e per fare cosa,
ma in questo progetto non si è voluto approfondire questo aspetto. Dopotutto, si presume che il sistema venga rilasciato in un
ambiente completamente controllato dall'azienda che lo ha commissionato, per cui i rischi legati alla sicurezza delle _thing_
sono minimi.

Un'altra caratteristica di interesse di questo middleware è il supporto allo standard "Web of Things" così come presentato dal
"World Wide Web Consortium". Questo standard ha lo scopo di presentare un modello standardizzato per la modellazione delle istanze
e delle classi di _thing_ sfruttando concetti come quelli di "risorsa", "link" e "form" che sono tipici del web. Questo sia per
facilitare l'interoperabilità delle diverse applicazioni, middleware e componenti, che possono avere un linguaggio comune e basato
su metafore note per dialogare tra loro, sia per permettere l'arricchimento dei sistemi con funzionalità possibili solamente
tramite l'uso di grafi di dati strutturati semanticamente e knowledge systems, come ad esempio il "discovering" delle _thing_.

Per questo motivo, sono stati prima realizzati i "Thing Model" per i diversi _digital twin_ e poi sono stati utilizzati nella
costruzione di questi ultimi mediante Eclipse Ditto. Il loro supporto è limitato, ma sufficiente per generare in automatico gli
attributi. Seguendo la documentazione ufficiale, le "action affordances" possono essere modellate tramite i messaggi inviati al
device, mentre le "event affordances" tramite i messaggi inviati dal device.

Qui di seguito dei frammenti del "Thing Model" dei sistemi antitaccheggio, definito in formato "JSON Linked Data" come da specifica
"Web-of-Things".

```{json}
"@context": "https://www.w3.org/2019/wot/td/v1",
"title": "AntiTheftSystem",
"@type": "tm:ThingModel",
"base": "http://localhost:8080/api/2/things/",
"description": "The anti-theft system in its store.",
"securityDefinitions": {
    "nosec_sc": {
        "scheme": "nosec"
    }
},
"security": "nosec_sc",
"uriVariables": {
    "storeId": {
      "title": "storeId",
      "description": "The id of the store the anti-theft system is in.",
      "type": "integer",
      "minimum": 0
    }
},
"properties": {
    "storeId": {
        "title": "storeId",
        "observable": false,
        "readOnly": true,
        "description": "The id of store the anti-theft system is in.",
        "type": "integer",
        "minimum": 0,
        "forms": [
            {
                "op": [
                    "readproperty"
                ],
                "href": "io.github.pervasivecats:antiTheftSystem-{storeId}/attributes/storeId"
            }
        ]
    }
},
```

In "uriVariables" vengono definite le variabili che appaiono negli URI presenti nel "Thing Model", come ad esempio nell'URI che
identifica univocamente la _thing_ come risorsa e che perciò rappresenta l'identificatore univoco di ogni "Thing Description".
Il formato di tutti gli URI è stato scelto perché combaciasse con la nozione di "thing id" e di "namespace" scelte da Ditto, in
modo tale da essere quanto più possibile aderenti alle specifiche del middleware scelto. Tutte le variabili sono anche proprietà
della _thing_ e per questo motivo sono state replicate tra le "properties affordances" del "Thing Model". Il gruppo delle
"properties" definisce quindi le informazioni che costituiscono le proprietà della _thing_. Sono state modellate come non osservabili,
in quanto si è deciso di voler utilizzare sempre delle "event affordances" per modellare delle sorgenti di eventi, e immutabili,
perché per modificare le proprietà è necessario passare attraverso le "action affordances" presenti nella "Thing Description".
Nel blocco "forms" di ogni proprietà viene descritto l'_endpoint_ grazie al quale potervi accedere tramite un URI.

```{json}
"actions": {
    "raiseAlarm": {
        "title": "raiseAlarm",
        "description": "Raises the anti-theft alarm, which will emit a sound for a certain amount of time.",
        "forms": [
            {
                "op": [
                    "invokeaction"
                ],
                "href": "io.github.pervasivecats:antiTheftSystem-{storeId}/messages/inbox/raiseAlarm",
                "contentType": "none/none",
                "response": {
                    "contentType": "application/json"
                }
            }
        ],
        "safe": false,
        "idempotent": false,
        "input": {},
        "output": {
            "oneOf": [
                {
                    "type": "object",
                    "required": [
                        "error",
                        "result"
                    ],
                    "properties": {
                        "result": {
                            "type": "integer",
                            "const": 1
                        },
                        "error": {
                            "type": "null"
                        }
                    }
                },
                {
                    "type": "object",
                    "required": [
                        "error",
                        "result"
                    ],
                    "properties": {
                        "result": {
                            "type": "null"
                        },
                        "error": {
                            "type": "object",
                            "required": [
                                "type",
                                "message"
                            ],
                            "properties": {
                                "type": {
                                    "type": "string",
                                    "enum": [
                                        "AlarmAlreadyRaised",
                                        "AlarmNotRaised"
                                    ]
                                },
                                "message": {
                                    "type": "string"
                                }
                            }
                        }
                    }
                }
            ]
        }
    }
},
```

Le azioni che è possibile compiere su di una _thing_ vengono definite tramite rispettivamente il blocco "actions". Per scelta,
tutte le "action affordances" non sono "safe", perché vanno in una certa misura a variare lo stato interno della _thing_, e non
sono "idempotent", perché effettuare più volte la stessa azione con lo stesso input può portare a risultati differenti,
dipendentemente dallo stato interno della _thing_. Gli input che sono forniti all'azione e i suoi output sono sempre in formato
JSON, a meno che l'azione non debba ricevere nessun input, in tal caso il formato non viene definito. L'output segue sempre il
formato dei messaggi usati in ogni punto del sistema, in modo tale che possa sempre essere chiaro al mittente se l'operazione è
andata a buon fine, e il risultato è stato generato, o se si è verificato un errore. In tal caso viene specificato tipo dell'errore
e una spiegazione in linguaggio naturale sull'errore. Nel blocco "forms" di ogni azione viene descritto l'_endpoint_ grazie al
quale poterla invocare tramite un URI, oltre ai già citati formati per l'input e l'output della stessa.

```{json}
"events": {
    "itemDetected": {
        "title": "itemDetected",
        "description": "The anti-theft alarm has detected an item exiting the store",
        "forms": [
            {
                "op": [
                    "subscribeevent"
                ],
                "href": "io.github.pervasivecats:antiTheftSystem-{storeId}/messages/outbox/itemDetected",
                "contentType": "application/json"
            }
        ],
        "data": {
            "type": "object",
            "required": [
                "catalogItemId",
                "itemId"
            ],
            "properties": {
                "catalogItemId": {
                    "type": "integer",
                    "minimum": 0
                },
                "itemId": {
                    "type": "integer",
                    "minimum": 0
                }
            }
        }
    }
}
```

Gli eventi che la _thing_ è capace di generare vengono definiti tramite il blocco "events". Per ogni evento è specificato il
formato dei dati dello stesso, che per scelta è sempre quello JSON. In più, vengono specificate le proprietà che costituiscono
l'evento e che ci si deve aspettare di ricevere ogniqualvolta si viene notificati di quello specifico evento. Nel blocco "forms"
di ogni evento viene descritto l'_endpoint_ grazie al quale potersi registrare per la ricezione invocare tramite un URI, oltre al
già citato formato dello stesso.

## RabbitMQ

RabbitMQ è il più celebre, nonché rodato middleware per la comunicazione asincrona tra sistemi distribuiti. Essendo pensato per
questo tipo di sistemi, è per sua natura basato sullo scambio di messaggi tra le parti. L'architettura su cui è basato è quella
conosciuta con il nome di "message broker": un componente centrale si propone come elemento capace di effettuare il _dispatching_
dei messaggi tra chi li invia e chi li riceve, garantendo la disintermediazione tra queste due componenti. Per la precisione, chi
invia messaggi è detto "publisher" e chi li riceve è detto "subscriber", perché di norma i messaggi sono assegnati ad un
determinato "topic", cioè argomento, a cui un subscriber può decidere di iscriversi. In tal caso riceverà tutti i messaggi che
sono associati a quello specifico argomento. Il message broker trattiene i messaggi su specifiche code, come in un qualsiasi
canale per la comunicazione message-passing asincrono. In particolar modo RabbitMQ definisce il concetto di "exchange", che
incapsula la politica di _dispatching_ dei messaggi sulle diverse code a cui è associato.

La comunicazione tra una coppia di microservizi avviene definendo per ciascuna di esse una coppia di code, una per i messaggi
che transitano in una direzione e una per quelli nell'altra. Non essendoci la possibilità di fare "receive" con _pattern matching_
sulle proprietà del messaggio, l'unico modo è definire code diverse associate a topic diversi, o per meglio dire a "routing key"
diverse, usando la nomenclatura di RabbitMQ. Ogni microservizio ha associato un exchange, a cui sono associate tutte le code su cui
può inviare messaggi. La routing key è utilizzata per capire qual è la coda del mittente, il quale sarà l'unico subscriber per
quella coda. I messaggi sono utilizzati per rappresentare gli eventi che i microservizi devono notificarsi tra loro, in quanto più
facilmente modellabili in questo modo anziché tramite _request_ HTTP. Queste sono comunque utilizzate per implementare comandi e
query che i microservizi si fanno tra loro.

Benché sia un middleware per la comunicazione asincrona, questo non vuol dire che non sia supportata una comunicazione che segue
il paradigma "remote procedure call", anche se sarebbe più corretto dire "request-response", dato che RPC, nella sua forma
tradizionale, richiede che la richiesta sia bloccante finché la risposta non è ricevuta. In questo caso non è possibile, dato che
è sempre e solo possibile registrare _callback_ che verranno invocate alla ricezione dei messaggi. In questo modo sono stati
implementati gli scambi di messaggi tra microservizi: ogni volta che un messaggio viene inviato, chi lo riceve deve
rispondere indicando il successo della ricezione o il fallimento. Deve farlo su di una coda che il mittente ha designato per la
risposta, riutilizzando l'identificatore di correlazione del primo messaggio così che il mittente possa effettuare
l'associazione tra richiesta e risposta. In caso di fallimento può essere ritentato l'invio del messaggio stesso. Questo permette
di garantire una semantica di comunicazione tipo "exactly once" a livello applicativo e il "Quality of Service" che ci si
aspetterebbe da un sistema che presuppone la comunicazione su di una rete che non è soggetta a perdite di messaggi.

Questo però non basta per garantire la semantica desiderata, sono state infatti utilizzate in aggiunta altre opzioni che RabbitMQ
mette a disposizione, come il fatto che tutte le code e tutti gli exchange sono "durable". Questo significa che, in caso di
fallimento del server del middleware, verranno ricreati senza bisogno di azioni aggiuntive. Inoltre, tutti i messaggi hanno come
"delivery mode" l'opzione "persistent", il che significa che quando sono piazzati in una coda, e questa è di tipo "durable", possono
essere completamente recuperati anche in caso di fallimento del server. Il costo è un maggiore uso del disco da parte del
middleware, cosa che però non preoccupa. Da ultimo, per tutti i messaggi l'invio è "mandatory", cioè nel caso in cui non fosse
trovata nessuna coda in cui depositare il messaggio da parte dell'exchange questo viene rimandato al publisher e non scartato e in
particolare è stato deciso di inserirlo in una coda per le "dead letter", così che possa almeno esserci traccia nei log del
messaggio perso. Anche in ogni altro caso di impossibilità di invio la coda per le "dead letter" è utilizzata per parcheggiare i
messaggi che non possono stare altrove.

## Akka

Akka è un framework per la programmazione ad attori, che permette di gestire tutto il loro ciclo di vita, dalla creazione alla
loro terminazione. Un sistema ad attori è fatto da un "root actor", o attore guardiano, che rappresenta il sistema stesso ed è
responsabile di creare tutti gli altri attori e dare il via al sistema. La terminazione di questo attore comporta la terminazione
del sistema stesso.

In Akka, la comunicazione tra attori è permessa secondo due tipi di pattern: "fire and forget", ovverosia l'invio di un messaggio
senza attendere una risposta o una conferma di avvenuta ricezione, rappresentata dall'operatore "tell", e "request-response",
dove l'attore una volta inviato il messaggio genera una _future_ che completerà con il messaggio di risposta, che può essere
utilizzata per continuare la computazione. L'uso della _future_ permette di supportare quest'ultimo pattern senza bloccarsi in
attesa della risposta. Per quanto riguarda il cambiamento di comportamento, ogni attore specifica il proprio mediante una funzione
che effettua _pattern matching_ sui messaggi ricevuti e restituisce il corrispondente "behavior", cioè il comportamento da
adottare da lì in poi. L'attore può decidere a quel punto di mantenere lo stesso oppure modificarlo in relazione a quanto accaduto.

In questo progetto, Akka è stato utilizzato ovunque fosse necessario utilizzare gli attori, ovvero nella componente attiva di
ciascun microservizio, quella che gestisce i flussi di controllo dell'applicazione. In primo luogo, è stato creato un attore
root per ogni microservizio, così come il framework richiede. Questo non fa altro che fare spawn di tutti gli altri attori nella
giusta sequenza, facendo in modo che le dipendenze tra questi siano soddisfatte, per poi passare ad un comportamento "vuoto",
ovvero che attende solamente il segnale di terminazione per terminare l'intero sistema.

Ogni "adapter" del microservizio che gli permette di dialogare con l'esterno è gestito da uno o più attori, eccezion fatta per
quello verso il _database_, dato che il framework utilizzato per connettersi ad esso utilizzava già un metodo proprio per farlo.
Esiste perciò un attore che gestisce le comunicazioni verso il message broker, verso RabbitMQ, che non farà altro che mappare i
messaggi in arrivo sulle diverse code in comandi per l'attore che invierà mediante una "tell". L'attore potrà poi effettuare
l'invio di messaggi tramite l'exchange adeguato come parte del proprio behavior. A questo attore è stato dato il compito di
gestire gli eventi in generale, perciò riceve anche i comandi ottenuti come trasformazione dei messaggi derivanti dalle websocket
aperte dal server web. Esiste un attore per dialogare con Eclipse Ditto, che, analogamente a quello per RabbitMQ, si registra
per la ricezione dei messaggi di interesse e li trasforma in comandi per l'attore, il quale potrà manipolare i digital twin e
rispondere a questi messaggi con altri come parte del proprio behavior.

Ultimo, ma non per questo meno importante, la gestione delle comunicazioni via HTTP. È stato deciso di utilizzare un'estensione di
Akka, denominata Akka HTTP, per la creazione dei server web necessari. Questo è stato fatto perché questo framework è capace di
supportare naturalmente gli attori di Akka senza nessun tipo di mapping ulteriore tramite il suo DSL. Questo significa che con
estrema facilità ogni _request_ HTTP che proviene da un client, sia esso l'applicazione o la dashboard, viene trasformata nel
comando per un attore apposito e inoltrata come messaggio mediante una "ask". In questo modo, è possibile inviare una _response_ HTTP
in ogni caso, gestendo correttamente un possibile timeout, e proseguendo l'elaborazione della _request_ quando l'attore responsabile
del "server" ha compiuto il proprio compito e completata la _future_ relativa all'operazione "ask". Akka HTTP fornisce inoltre
supporto diretto per la serializzazione e la de-serializzazione dei _body_ dei messaggi HTTP tramite la libreria "spray-json",
richiedendo di implementare i _marshaller_ e gli _unmarshaller_, in quanto il loro uso è applicato in automatico.

## Websocket

Websocket è un protocollo di comunicazione tra client e server che permette di stabilire dei canali full-duplex, dove i messaggi
possono quindi essere inviati dal client al server, ma anche viceversa, senza che una delle due parti nello specifico debba
iniziare la comunicazione come nei protocolli "request-response". È un protocollo basato su HTTP, il che lo rende particolarmente
comodo per essere utilizzato dai client e dai server web, ma in generale anche da tutte quelle applicazioni che utilizzano client
o server HTTP.

Il tipo di connessioni che questo protocollo stabilisce lo rende particolarmente adatto per modellare sorgenti di eventi. Un
protocollo request-response infatti non potrebbe modellare adeguatamente questo concetto, in quanto capace solamente di inviare
messaggi "one shot". Ogni volta che un nuovo evento è generato, occorrerebbe aprire una nuova connessione, inviare il messaggio e
poi chiuderla, senza considerare il fatto che solamente una delle due parti interessate potrebbe generare gli eventi e l'altra
parte solo riceverli.

Per questo motivo, si è adottato il protocollo websocket ogniqualvolta un'interfaccia come l'applicazione o la dashboard,
rappresentata da un client, doveva notificare un evento ad un microservizio. I comandi e le query, invece, che possono essere
modellati utilizzando il più classico concetto di richiesta, sono stati implementati attraverso delle più semplici _request_ HTTP.

Non è stato necessario utilizzare nessuna tecnologia aggiuntiva rispetto a quelle già discusse in precedenza, il framework "Akka
HTTP" già includeva la possibilità di gestire websocket come parte di un web server o di un web client. Il paradigma per la
gestione di ciascuna di esse è quello di programmazione reattiva, dove perciò ciascun messaggio inviato lungo il canale è trattato
come un evento di un "cold observable" ed è possibile definire degli "observer" capaci di manipolare i messaggi in arrivo.

## Docker

Docker è una tecnologia per creare dei runtime environment, conosciuti con il nome di "container", per l'esecuzione di software
in maniera riproducibile su diverse piattaforme. Gli ambienti che costruisce sono più leggeri di equivalenti macchine virtuali,
in quanto condividono una maggior parte dello stack di esecuzione con il sistema operativo sottostante. Questo permette di
"impacchettare" il software realizzato assieme a tutte le sue dipendenze e fare delivery o deployment di tutto l'ambiente sulle
macchine di destinazione senza il rischio che il sistema non funzioni perché viene eseguito in un contesto inadatto.

Docker permette di creare delle "immagini", ovvero delle sequenze di istruzioni che servono per costruire i container, attraverso
degli appositi file di configurazione detti "Dockerfile" in un linguaggio proprio. L'utilità di Docker risiede anche nel fatto che
esiste una repository pubblica, detta DockerHub, di immagini da dove è possibile scaricare immagini già pronte per creare i container
di interesse a fronte di una configurazione minima. Inoltre, è possibile anche caricare le proprie immagini per condividerle
pubblicamente. Una volta creato il container, Docker offre la possibilità di poterlo gestire completamente: permette di fermarlo,
"ucciderlo", controllare i log prodotti, aprire delle porte per poter accedere al container e così via.

Infine, Docker offre la possibilità di orchestrare l'avvio e lo spegnimento di più container in una volta sola attraverso la sua
estensione "compose". È possibile infatti lanciare più "servizi", ovvero container, alla volta, esprimendo dipendenze tra essi,
facendo in modo che comunichino su di una rete virtuale propria ed isolata, abbiano a disposizione come volumi delle parti di
filesystem ad essi dedicate. Questo è utile nel momento nel quale un sistema è composto di più componenti che devono essere lanciate
per poterlo fare funzionare. Anche in questo caso è necessario specificare tutte queste configurazioni in un file apposito che usa
il formato YAML denominato "docker-compose".

In questo progetto Docker è stato utilizzato per effettuare il deployment del sistema. È stato creato un file di configurazione
per Docker compose che si preoccupa innanzitutto di creare un'immagine "eclipse-temurin", quindi contenente una JVM, per ciascuno
dei microservizi, che altro non sono che applicazioni Java. Dopodiché, si preoccupa di creare un'immagine "postgres" per ciascuno
dei _database_ utilizzati dai microservizi come data layer, configurandoli nel modo corretto per ciascuno di essi mediante uno
script SQL di inizializzazione. Una volta create le immagini, crea in sequenza un container per ciascun database, poi il container
dall'immagine di RabbitMQ presente su DockerHub e infine un container per ciascun microservizio. Le uniche porte esposte sono
quelle dei microservizi per renderli accessibili localmente, tutte le altre sono interne alla rete che compose crea ad hoc per i
container. Si dà per scontato che i container di Eclipse Ditto siano già stati dispiegati ed accessibili su una propria rete
utilizzando la loro specifica configurazione. In questo modo i container dei microservizi possono essere attaccati a quella rete
per accedere ai servizi di Ditto.

Per il deployment della demo viene seguita una procedura simile, ma semplificata. Si assume infatti che tutti i sottosistemi
utilizzati dai microservizi siano già attivi e funzionanti sulla macchina locale al loro avvio, ovvero i database, contenuti in
questo caso tutti nello stesso DBMS PostgreSQL, il server RabbitMQ e il middleware Ditto. Docker compose non fa altro che
costruire le immagini e lanciare i container associati dei tre microservizi, connettendoli alla rete "host" e non ad una propria
per rendere accessibile i servizi elencati in precedenza. In questo caso non è quindi necessario aprire nessuna porta verso l'esterno
in quanto i container sono già visibili sulla rete host.

<br/>
<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/devops">Devops</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/testing">Testing</a></div>
</div>
<br/>
