---
title: Home
---

## Introduzione

Toys Store è un'azienda leader nel settore della vendita di giocattoli e vorrebbe replicare il progetto di negozio _smart_ messo
in piedi da Amazon, collocandolo nel proprio contesto di vendita. Il negozio "intelligente" di Amazon, denominato "Amazon
GO", permette agli utenti di avere un'esperienza di acquisto più veloce e comoda, eliminando la presenza delle casse in uscita dal
negozio. Ogni cliente può infatti prendere dagli scaffali i prodotti di proprio interesse e portarli via con sé: il sistema
automaticamente riconosce chi ha effettuato l’acquisto e quali prodotti ha acquistato, addebitando il pagamento corretto
all’utente corretto.

Per ottenere questo, il sistema avrà carrelli, scaffali, sistemi antifurto ed altri sistemi utili al funzionamento del negozio
dotati di sensori ed attuatori, spostando la computazione negli oggetti, rendendolo per questo un sistema IoT di _pervasive computing_.
Questo permette di ridurre e ripensare la componente umana nei negozi, in particolar modo il suo ruolo, senza però eliminarla
completamente.

La _software house_ che si è occupata dello sviluppo della soluzione è stata "Pervasive Cats S.r.l.s." e questo sito funge sia
strumento per la disseminazione dei risultati, sia da corpus di conoscenze accumulate durante il progetto, in modo da non disperdere
la conoscenza accumulata sul dominio in un'ottica di miglioramento ed estensione del progetto stesso. Le informazioni sono presentate
in un ordine che è tradizionale per lo sviluppo software: dapprima i requisiti e la loro raccolta, poi la discussione delle scelte
di design per il progetto e infine i dettagli di implementazione più rilevanti e il _testing_. Particolare interesse è rivolto
anche alle _operations_,ormai inscindibilmente legate al processo di sviluppo e per questo da considerarsi parte integrante dello
stesso.

## Capitoli

1. [Analisi dei requisiti](/requirements)
    1. [Requisiti](/requirements#requisiti)
    2. [Domain storytelling](/requirements#domain-storytelling)
    3. [Casi d'uso](/requirements#casi-duso)
    4. [Event storming](/requirements#event-storming)
    5. [Ubiquitous Language](/requirements#ubiquitous-language)
2. [Design architetturale](/architectural_design)
    1. [Bounded context](/architectural_design#bounded-context)
    2. [Architettura a microservizi](/architectural_design#architettura-a-microservizi)
    3. [Digital twin](/architectural_design#digital-twin)
    4. [Attori](/arcitectural_design#attori)
3. [Design di dettaglio](/detailed_design)
    1. [Microservizio "Utenti"](/detailed_design#microservizio-utenti)
    2. [Microservizio "Prodotti"](/detailed_design#microservizio-prodotti)
    3. [Microservizio "Carrelli"](/detailed_design#microservizio-carrelli)
    4. [Microservizio "Negozi"](/detailed_design#microservizio-negozi)
    5. [Microservizio "Shopping"](/detailed_design#microservizio-shopping)
    6. [Microservizio "Pagamenti"](/detailed_design#microservizio-pagamenti)
4. [Implementazione](/implementation)
    1. [Eclipse Ditto](/implementation#eclipse-ditto)
    2. [RabbitMQ](/implementation#rabbitmq)
    3. [Websocket](/implementation#websocket)
    4. [Docker](/implementation#docker)
    5. [Akka](/implementation#akka)
5. [Testing](/testing)
    1. [Unit test](/testing#unit-test)
    2. [Integration test](/testing#integration-test)
6. [Devops](/devops)
    1. [Metodo di sviluppo](/devops#metodo-di-sviluppo)
    2. [Organizzazione delle repository](/devops#organizzazione-delle-repository)
    3. [Build system](/devops#build-system)
    4. [Continuous Integration](/devops#continuous-integration)
    5. [Continuous Deployment](/devops#continuous-deployment)
