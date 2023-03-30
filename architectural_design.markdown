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
durante l'implementazione del sotto-sistema associato. Ultimo, ma non per questo meno importante, sono le "inbound communications"
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
economico, con un implementazione che deve sottostare fortemente alle legislazioni dei paesi in cui questi sistemi operano. Allo
stesso tempo, non è un sistema particolarmente importante per il business perché non è ciò che di innovativo questo sistema porta,
non è ciò che è utile incrementare i ricavi o ridurre i costi di esercizio del committente. Senza dubbio, questo dominio è un
"generic domain" ed ha senso che sia _outsourced_, creando una semplice interfaccia per permettere di comunicare tra il suo sistema
e il sistema realizzato.

Incontriamo poi i due domini "utenti" e "prodotti", che sono molto simili tra loro. Ci aspettiamo che infatti i loro modelli siano
facili da realizzare, non a caso infatti saranno delle interfacce su un "data layer" che mantiene le informazioni su questi due
domini, con una limitata capacità di elaborazione. Il loro modello sarà quindi esprimibile tramite metodi di modellazione più
semplici come quello "entity-relationship" tipico dei _database_ relazionali. Sono relativamente poco importanti perché comunque
trattenendo unicamente dei dati, non è qui che avviene la computazione che rende il sistema "smart". Rimangono perciò dei "core
domain", ma sono solamente "supporting". Non ci si aspettano che siano degli "hidden core" data la loro più o meno chiara
modellazione. Il dominio "prodotti" è leggermente più importante di quello "utenti" data la sua particolarità e non presenza in
tutti i sistemi.

Successivamente sono presenti i due sotto-domini "negozi" e "carrelli", che sono ancora più importanti dei due precedenti per
quanto riguarda il business. Questi due infatti sono domini unici al problema che si sta cercando di risolvere.

## Bounded context Utenti

## Bounded context Prodotti

## Bounded context Carrelli

## Bounded context Negozi

## Bounded context Shopping

## Bounded context Pagamenti

<br/>
<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/requirements">Requisiti</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/detailed_design">Design di dettaglio</a></div>
</div>
<br/>
