---
title: Testing
permalink: /testing
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/implementation">Implementazione</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/deployment">Deployment</a></div>
</div>
<br/>

Il testing di un sistema così complesso ha svolto un ruolo centrale nel processo di sviluppo del progetto. Non potersi assicurare
il corretto comportamento del sistema rallenterebbe se non direttamente impedirebbe il corretto deployment della soluzione sulle
macchine del cliente, in quanto non sarebbe possibile determinare il funzionamento atteso nemmeno nelle condizioni che si considerano
attese di base. Quanto più è grande il sistema, quanto più è facile che un errore in un microservizio si propaghi in tutti gli altri,
indipendentemente dal grado di disaccoppiamento degli stessi, in quanto le comunicazioni tra essi sono sempre possibili. Non basta
quindi testare la correttezza interna di ogni microservizio, ma anche quella delle comunicazioni tra gli stessi, cioè di come
questi si integrano tra di loro.

## Unit testing

È stato fatto unit testing per tutte le entità di dominio, indipendentemente che esse fossero value object, entity o service.
L'unica eccezione sono state le repository e i service che necessitavano di utilizzare il data layer per far persistere o ottenere
dei dati, che sono stati testati separatamente tramite degli integration test. Con la copertura di test totale sul dominio ci si
è anche assicurati che ciascun elemento rispettasse la sua definizione all'interno dello stesso. Il test è avvenuto utilizzando
la piattaforma di test preferita per scala, ovvero "scalatest". Inoltre, sono state testate le _route_ accessibili attraverso gli
"adapter" del microservizio per le _request_ HTTP, dato che Akka HTTP fornisce un "test kit" pensato proprio per simulare i client
HTTP. In questo modo si è osservato in fase di test se le _route_ delegano correttamente i compiti ai diversi attori e se sono
capaci di gestire correttamente le risposte che ricevono, siano esse di successo o di fallimento.

Ciò che non è stato testato sono stati i comandi per i diversi attori, così come i _marshaller_ e gli _unmarshaller_ per i messaggi
HTTP. Questo perché il loro uso è stato estensivo negli altri test, perciò non si pè ritenuto necessario dover testare separatamente
queste classi.

## Integration testing

Sono state utilizzate tecniche di integration testing per tutti gli attori che sono stati implementati nei diversi microservizi.
Questo perché, come già detto nei capitoli precedenti, sono gli attori a racchiudere i flussi di controllo necessari a gestire i
messaggi provenienti dagli "adapter" e a fornire adeguata risposta. Per questo motivo, per assicurarsi che questi svolgessero i
propri compiti in maniera esatta, è stato necessario dover lanciare diverse componenti del sistema all'interno del singolo test.
Alternativamente, si sarebbe potuto iniettare dei mock al posto delle istanze effettive e verificare che le chiamate effettuate
su di essi fossero quelle attese. Questo approccio però sarebbe stato problematico in quanto avrebbe esplicitamente richiesto di
modificare il codice degli attori, che creano autonomamente le classi che utilizzano per accedere ai diversi servizi. In secondo
luogo, i mock non avrebbero potuto rispondere come avrebbero invece fatto i servizi come il message broker o l'istanza di Ditto.
In questo modo, i test così realizzati danno maggiori garanzie, in quanto effettuati utilizzando i veri componenti che saranno
poi messi in esecuzione anche quando il sistema sarà in produzione.

Per lanciare i componenti del sistema in fase di test è stato utilizzato il framework "testcontainers-scala", capace di lanciare
in esecuzione e terminare dei container Docker utilizzando una configurazione minima. La sua integrazione con scalatest permette
di avviarli prima dell'esecuzione dei test e di terminarli una volta che questi sono stati eseguiti, in maniera totalmente
dichiarativa e senza entrare nei dettagli del funzionamento interno del framework. In questo modo sono stati avviati il message
broker, ed eventualmente il database PostgreSQL, quando occorreva testare l'attore interfaccia verso RabbitMQ, solamente il
database quando occorreva testare gli attori "server", il repository e i service che ne facevano uso.

È stato pensato di utilizzare testcontainers-scala anche per dispiegare i container di Eclipse Ditto perché supporta anche
configurazioni sotto forma di file di configurazione di "docker-compose". Purtroppo però il deployment richiedeva troppo tempo per
poter essere completato con successo e richiedeva troppo tempo perché la pipeline di CI/CD completasse in tempo ragionevole, oltre
a prendere troppe risorse sulle macchine usate per il test. Per questo i test sono stati realizzati presupponendo che tutti i
servizi necessari, quindi inclusi anche quelli di Eclipse Ditto, siano già attivi e funzionanti sulla macchina di test.

<br/>
<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/implementation">Implementazione</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/deployment">Deployment</a></div>
</div>
<br/>
