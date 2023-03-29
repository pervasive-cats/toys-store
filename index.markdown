---
title: Home
permalink: /
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
anche alle _operations_, ormai inscindibilmente legate al processo di sviluppo e per questo da considerarsi parte integrante dello
stesso.

## Indice dei contenuti

1. [Analisi dei requisiti](/toys-store/requirements)
    1. [Domain storytelling](/toys-store/requirements#domain-storytelling)
    2. [Casi d'uso](/toys-store/requirements#casi-duso)
    3. [Event storming](/toys-store/requirements#event-storming)
    4. [Ubiquitous Language](/toys-store/requirements#ubiquitous-language)
    5. [Requisiti Non Funzionali](/toys-store/requirements#requisiti-non-funzionali)
    6. [Requisiti Implementativi](/toys-store/requirements#requisiti-implementativi)
2. [Design architetturale](/toys-store/architectural_design)
    1. [Bounded context](/toys-store/architectural_design#bounded-context)
    2. [Architettura a microservizi](/toys-store/architectural_design#architettura-a-microservizi)
    3. [Digital twin](/toys-store/architectural_design#digital-twin)
    4. [Attori](/toys-store/arcitectural_design#attori)
3. [Design di dettaglio](/toys-store/detailed_design)
    1. [Microservizio Utenti](/toys-store/detailed_design#microservizio-utenti)
    2. [Microservizio Prodotti](/toys-store/detailed_design#microservizio-prodotti)
    3. [Microservizio Carrelli](/toys-store/detailed_design#microservizio-carrelli)
    4. [Microservizio Negozi](/toys-store/detailed_design#microservizio-negozi)
    5. [Microservizio Shopping](/toys-store/detailed_design#microservizio-shopping)
    6. [Microservizio Pagamenti](/toys-store/detailed_design#microservizio-pagamenti)
4. [Devops](/toys-store/devops)
    1. [Workflow](/toys-store/devops#workflow)
    2. [Organizzazione delle repository](/toys-store/devops#organizzazione-delle-repository)
    3. [Continuous Integration](/toys-store/devops#continuous-integration)
    4. [Continuous Deployment](/toys-store/devops#continuous-deployment)
5. [Implementazione](/toys-store/implementation)
    1. [Eclipse Ditto](/toys-store/implementation#eclipse-ditto)
    2. [RabbitMQ](/toys-store/implementation#rabbitmq)
    3. [Akka](/toys-store/implementation#akka)
    4. [Websocket](/toys-store/implementation#websocket)
    5. [Docker](/toys-store/implementation#docker)
6. [Testing](/toys-store/testing)
    1. [Unit testing](/toys-store/testing#unit-testing)
    2. [Integration testing](/toys-store/testing#integration-testing)
7. [Deployment](/toys-store/deployment)
