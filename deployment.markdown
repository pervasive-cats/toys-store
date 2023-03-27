---
title: Deployment
permalink: /deployment
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/testing">Testing</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/">Home</a></div>
</div>
<br/> 

Il deployment è stato fatto realizzando uno _script_ Bash apposito. Questo script per prima cosa lancia l'istanza di Eclipse Ditto
usando la sua configurazione base e si mette in attesa che tutti i container siano lanciati e diventino "healthy", ovvero pronti
per essere utilizzati. Da riga di comando devono essere specificati nome utente e password dell'utente che accederà al servizio
Ditto, per evitare di tracciare sulla repository informazioni sensibili, e stessa cosa andrà fatta per tutte le altre coppie di
username e password che sono utilizzate nel file di configurazione.

Dopodiché, lo _script_ genera il file di configurazione adatto per ciascun microservizio, specificando nome utente, password, nome
del database e nome dell'host a cui si trova il DBMS che il microservizio utilizzerà come implementazione del suo data layer. Nel
file di configurazione vengono specificati anche la porta mediante la quale il sistema host potrà accedere al container del
microservizio una volta che sarà in esecuzione e il nome utente e la password dell'utente che accederà al server RabbitMQ. Se il
microservizio deve gestire anche i Digital Twin di alcune _thing_, come "carts" e "stores", nel file vengono inseriti ance nome
utente e password dell'utente del servizio Ditto. Il file di configurazione viene facilmente generato, essendo un file in formato
HOCON, un'estensione di JSON fatta da Lightbend.

Fatto questo, è possibile lanciare il file di configurazione di Docker compose che si preoccuperà di lanciare tutti i componenti
mancanti. Compose infatti per prima cosa lancerà il container di RabbitMQ con username e password decisi in precedenza. Dovranno
essere specificati come variabili d'ambiente, così come quelle dei diversi DBMS lanciati come servizi, perciò queste verranno
specificate al lancio del comando nello _script_ Bash. Questo non è un problema perché abbiamo ancora accesso a questi valori
perché, come detto in precedenza, saranno specificate da riga di comando dall'utente che lancia lo _script_. L'immagine di RabbitMQ
è disponibile su DockerHub, perciò non c'è bisogno di crearla. Viene esposta la porta necessaria ai microservizi per accedere al server,
che è quella di default per il message broker. Dopodiché verranno lanciati i servizi che fungono da data layer, usando un'immagine
apposita definita in ciascuna delle repository dei microservizi. Questo perché occorre sì utilizzare l'immagine di base di
PostgreSQL, però è necessario passare anche lo _script_ SQL di configurazione di ciascun _database_. Il "context" per il "build"
dell'immagine è quindi la cartella della repository che contiene lo _script_. Viene esposta la porta di default per il DBMS per
permettere l'accesso ai microservizi.

Da ultimo, vengono lanciati i container dei microservizi una volta create le loro immagini a partire dalla cartella della
_repository_ di ciascuno. Ognuna infatti contiene un file di configurazione Docker che non fa altro che copiare nel container il
file di configurazione generato dallo _script_ Bash e scaricare il file JAR corrispondente all'ultima versione del microservizio
così come si trova su GitHub, che andrà poi messo in esecuzione al lancio del container. Ognuno dei container dei microservizi si
trova sia sulla rete di default costruita da Docker compose, in modo tale da poter esporre una porta verso l'host e renderlo
accessibile, sia su quella dei container di Ditto, così da poter accedere ai loro servizi. Quest'ultima rete è infatti dichiarata
nel file di configurazione Docker compose come "external".

Inoltre, mentre il sistema veniva realizzato, è stato richiesto dal cliente di realizzare delle demo che mostrassero il
funzionamento del sistema. Queste demo sono pensate per essere messe in esecuzione su di una macchina di test, non di produzione,
perciò che non necessariamente, anzi, sicuramente non possiede le prestazioni necessarie per mettere in piedi il sistema.
Certamente la macchina dovrà essere in grado di far funzionare una versione ridotta dei servizi utilizzati. Per questo motivo, si
è deciso di creare una versione semplificata dello _script_ Bash per la demo, non troppo diversa da quello originale.

Lo _script_ per la demo prende come input da riga di comando gli stessi dello _script_ originale e lancia i servizi di Eclipse
Ditto in maniera identica al precedente. Dopodiché, però, lancia direttamente il container del server RabbitMQ, aspettando che nel
suo log compaia la stampa che indica che l'avvio è stato completato con successo. In seguito, vengono creati allo stesso modo come
in precedenza i file di configurazione per ciascun microservizio, a cui vengono aggiunte le informazioni su Ditto per tutti e soli
quei microservizi che ne fanno uso. L'unica differenza tra le informazioni inserite in questo caso e quelle inserite nel file
corrispondente per il _deployment_ in produzione è che in questo caso l'host indicato da ciascun servizio è sempre "localhost",
perché tutti eseguiranno sulla macchina indicata per la demo.

Da ultimo, viene usato il file di Docker compose per la demo, che lancia solo i container dei microservizi in modalità di rete
"host". Questo significa che l'intero stack di rete utilizzato da questi è lo stesso dell'host che le mette in esecuzione, perciò
non è necessario esporre nessuna porta o connettersi a reti di altri servizi lanciati da Docker nella stessa macchina: i container
sono direttamente esposti come un qualsiasi altro processo in esecuzione che apre una _socket_. Questo è necessario perché in questo
modo è possibile accedere al _database_ PostgreSQL locale che agisce come surrogato dei diversi container "Postgres" che fungevano
da data layer per i diversi microservizi. In questo modo si alleggeriscono i requisiti dell'_host_ da usare per la demo, a costo
di minor sicurezza e disaccoppiamento, anche se in fase di prova del sistema queste ultime proprietà non sono desiderate.

<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/testing">Testing</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/">Home</a></div>
</div>
<br/>
