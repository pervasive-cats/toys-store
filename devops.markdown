---
title: Devops
permalink: /devops
---

<div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/detailed-design">Design di dettaglio</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/implementation">Implementazione</a></div>
</div>
<br/>

Una parte molto importante del processo di sviluppo durante il progetto ha riguardato la gestione delle _IT operations_, che non
poteva essere isolata in una fase a sé stante, dopo il _deployment_ del sistema. Si è infatti adottata la metodologia cosiddetta
"DevOps" dove lo sviluppo del software è andato di pari passo con la definizione delle operazioni da compiere a supporto dello
stesso. In questo modo è possibile facilitare lo sviluppo strutturando correttamente le operazioni e facilitare l'esecuzione delle
operazioni usando metodologie e strumenti di sviluppo che si prestano più facilmente allo scopo.

## Workflow

Il workflow adottato nel progetto è il cosiddetto "Trunk-Based Development", che differisce dal più tradizionale "Git Workflow" in
quanto riduce fortemente il numero di _branch_ creati e mantenuti attivi per favorire agilità e rapidità, elementi necessari per
la metodologia "continuous integration / continuous deployment".

Il classico "Git Workflow", o "GitFlow" in breve, richiede di avere un _branch_ principale, denominato "master" sul quale si
trovano le _release_ stabili e che, altrimenti, non viene mai toccato. Il secondo _branch_ più importante è "develop", così
chiamato perché è lì dove avviene tutto lo sviluppo. Da questo si dipartono i "feature branch", ognuno dei quali pensato per
realizzare una qualche funzionalità del sistema, che dovrà poi essere riunito con "develop". Da questo si dipartono anche i
"release branch", ovvero quei branch pensati solamente per effettuare un rilascio che "congelano" il codice e gli applicano le
ultime modifiche necessarie per renderlo idoneo al rilascio, unendolo poi con "master". Ultimo tipo di _branch_ sono i cosiddetti
"hotfix", pensati per risolvere _bug_ al volo, in produzione, che nascono quindi da "master" e si riuniscono ad esso, ma anche a
"develop" perché questi _bugfix_ devono essere integrati anche con il codice in sviluppo.

Nel "Trunk-Based Development" non c'è pressoché nulla di tutto questo: c'è un unico _branch_ denominato "master" o "trunk" da cui
si dipanano tutti gli altri. Tutti questi _branch_ devono essere di breve durata, ovvero massimo uno sviluppatore alla volta può
lavorarci e per un massimo di tre giorni. Quando questi vengono riuniti a "master", viene lanciata una nuova _release_. Se questa
può sembrare inizialmente un'enorme semplificazione, che non permette di avere controllo sul codice che sta venendo prodotto, in
realtà non lo è. È bene sottolineare il fatto che, così facendo, il codice è sempre pronto per un rilascio, non occorre mai dover
"congelare" il codice per approntarlo, è già pronto. Facendo sempre poche, piccole modifiche alla volta, non è più necessario
spendere tempo per assicurarsi che grandi _codebase_ siano pronte per la _release_, tanto più che, in un'ottica "CI/CD", questa
verifica deve essere svolta grazie ad un _workflow_ automatico. Allora, i "release" _branch_ perdono di senso, come pure gli
"hotfix", che servivano solo per avere una "via di fuga" più rapida per effettuare dei rilasci in produzione di emergenza, come
per i _bugfix_. Tutto diventa una semplice "feature" che viene periodicamente integrata e rilasciata su "master" e tutta la
burocrazia degli "human-in-the-loop" viene rimossa assieme a questi ultimi.

In questo progetto è stata adottata una versione leggermente differente, in accordo con lo strumento di _release_ adottato, ovvero
"semantic release". Questo strumento infatti costringe gli sviluppatori a non iniziare le proprie release dalla versione "0.1.0",
ma dalla "1.0.0", in quanto le versioni con zero come _major version_ hanno un sistema di _versioning_ non ben definito, anche
all'interno dello stesso schema di "semantic versioning". Poiché questo schema permette di appendere delle etichette alla versione,
lo strumento di _release_ suggerisce di utilizzare il _label_ "beta" assieme alla futura versione stabile che sarà rilasciata una
volta che la "fase beta" sarà terminata. Nel frattempo, mentre saranno rilasciate le "prerelease" secondo il Trunk-Based Development,
sarà incrementata un'ulteriore numero di versione finale che segue "beta". In questo modo, la prima _release_ in fase beta sarà
sempre "1.0.0-beta.1". Per rendere possibile questo, si è creato sempre un _branch_ che si biforcava da "master" subito all'inizio
della storia di ciascun _repository_ e veniva riunito una volta terminata la fase beta, generando il primo rilascio stabile. Da
quel momento in poi, sono stati permessi solo rilasci stabili in accordo con la metodologia scelta.

## Organizzazione delle repository

Per la gestione del progetto, è stata creata una "organizzazione" su GitHub accessibile a tutti gli sviluppatori e al software
architect che contiene tutte le _repository_, denominata "Pervasive Cats". In questo modo è possibile gestire in maniera più
semplice, ovvero centralizzata, ognuna di esse, sia per quanto riguarda gli aspetti di sicurezza che di organizzazione del lavoro.
È stato infatti creato poi un "progetto" che potesse tracciare tutte le _issue_ e le _pull request_ a mo' di kanban board e
permettesse di vedere a colpo d'occhio cosa è necessario fare e dove. Per forzare la separazione dei bounded context così come il
Domain Driven Design impone, è stata creata una _repository_ per ciascun microservizio, più una generale, capace di raccogliere
tutte le altre come "git submodules". È in quest'ultima che è contenuto il codice per effettuare il rilascio del sistema nella
sua interezza, così come la demo e tutta la documentazione scritta prodotta. È infatti contenuto qui il sito di progetto, contenente
tutte le informazioni utili, tramite il servizio "GitHub Pages" in un _branch_ apposito. Questa _repository_ è denominata
"toys-store", come il progetto. Poiché i _repository_ dei microservizi sono molto simili tra di loro, è stato creato un _template_
per poter velocizzare e quanto più possibile automatizzare la loro creazione. Il _template_ è chiamato "toys-store-bc-template".
Anche il template viene mantenuto aggiornato automaticamente con i sistemi descritti in seguito.

## Continuous Integration

Per effettuare "Continuous Integration", ciascuna _repository_ adotta gli stessi strumenti, messi in esecuzione dalla piattaforma
di CI/CD di GitHub "GitHub Actions". Innanzitutto, è stato adottato "scalafmt" come strumento di _linting_ del codice, mentre sono
stati utilizzati sia "scalafix" che "wartremover" come strumenti di verifica della qualità del codice, pensati per individuare le
cattive prassi più comuni e i potenziali _bug_. Come strumento per mantenere sotto controllo la _test coverage_ è stato utilizzato
"scoverage", i cui risultati sono stati caricati sulla piattaforma "Codecov" per semplificare l'analisi dei risultati. Tutte queste
operazioni sono state effettuate dal _workflow_ di test, lanciato ad ogni _push_ su di un branch che non è "main" o "beta", ovvero
i "trunk" principali, che riguarda file che contengono codice. Poiché i risultati della coverage saranno necessari anche in
seguito, sono stati caricati come _artifact_ tramite una "GitHub Action" apposita.

Per quanto riguarda la fase di "code review", questa è stata fatta sfruttando il meccanismo delle "pull request". Non è possibile
infatti fare dei "push" sui branch "master" e "beta", ma solamente richiedere che il codice inserito in un proprio _branch_ che è
stato biforcato da uno dei due venga unificato con lo stesso. La richiesta, detta appunto "pull request", mette in moto lo
strumento di _code review_ "SonarCloud", che dà una valutazione qualitativa sul codice che sta venendo incluso nel _branch_. Visto
che questo strumento necessita di analizzare anche la copertura dei test, nel _workflow_ di _code review_ per prima cosa viene
scaricato lo "artifact" relativo ai risultati ottenuti dal _workflow_ precedente. Inoltre, poiché sono stati caricati i risultati
della _coverage_ in precedenza, Codecov è capace di valutare come questa cambierebbe nel caso in cui la pull request dovesse
concludersi con successo. Per poter far funzionare correttamente SonarCloud, però, è stato necessario dover creare una GitHub
Action nostra, capace di correggere i percorsi contenuti nel file contenente i risultati della _coverage_ di test, in quanto il
_tool_ non li ottiene da scoverage nel formato che si aspetta.

Inoltre, sono stati imposti ulteriori vincoli sui branch "main" e "beta", ovvero:

* tutti i _commit_ devono essere firmati con la chiave GPG dell'autore di cui GitHub è a conoscenza. Questo serve per poter
  bloccare eventuale codice che proviene da utenti non identificati e perciò potenzialmente inserito per effettuare attacchi di
  tipo "supply chain";
* ogni pull request richiede che almeno una persona con sufficienti diritti all'interno del team di sviluppo fornisca la sua
  approvazione al _merge_, persona che sarà necessariamente diversa da chi ha creato la pull request e la cui approvazione sarà
  annullata in caso nuovo codice venga introdotto in un momento successivo all'approvazione. In questo modo, l'approvazione viene
  fatta sempre e solo sul codice più aggiornato, senza il rischio che venga aggiunto all'insaputa di chi effettua la sua revisione;
* il _merge_ può avvenire solamente se sono stati rispettati tutti i vincoli imposti al _branch_ che si vuole riunire, ovverosia
  che il riferimento con cui lo si sta unendo del _branch_ destinazione sia la sua "head", che ci sia stata risoluzione delle
  conversazioni. Questa cosa serve perché un revisore potrebbe aprire una "code review" segnalando i punti problematici nel codice
  e in questo modo il _merge_ non può proseguire finché la review non è terminata e le problematiche sono state risolte. Infine,
  deve essersi completato con successo il _workflow_ di test con il codice contenuto nella pull request e deve essere passato il
  controllo di GitGuardian. Quest'ultimo strumento serve per tracciare la presenza di eventuali _secrets_ nel codice, come password,
  _token_ di accesso, dati sensibili, che gli sviluppatori hanno lasciato a propria insaputa, impedendo che possano verificarsi
  accessi non autorizzati ai servizi utilizzati dall'organizzazione;
* non sono stati utilizzati come vincoli per il controllo del _branch_ i risultati delle analisi di Codecov e SonarCloud. Questo
  perché scoverage, pur essendo funzionante per il linguaggio scala dalla versione 3.2.0, non riesce ad analizzare correttamente
  il codice a causa di problemi con il compilatore e la _coverage_ risulta più bassa del dovuto;
* i _branch_ "sensibili", ovvero "main" e "beta", possono essere creati e cancellati o possono farci _push_ solamente persone con
  privilegi adeguati, ovvero gli amministratori della _repository_, in modo tale da limitare eventuali compromissioni della loro
  integrità.

Per motivi di emergenza, è stata lasciata la possibilità di effettuare _force push_ sui _branch_ protetti, in modo tale da poter
sovrascrivere la storia in caso si rendesse necessario. Per fare tutto questo sono stati inseriti come _secrets_ della _repository_
i _token_ dei diversi servizi utilizzati. È stata inserita anche una chiave privata cifrata, assieme alla sua chiave di cifratura,
e un _token_ speciale per impersonare un amministratore della _repository_, che servono al _workflow_ di rilascio, di cui si
discuterà nella prossima sezione, in modo tale che tutti i _commit_ e le _release_ siano firmate e appaiano come legittime, per
distinguerle da altre eventuali non legittime frutto di attacchi.

## Continuous Deployment

Il rilascio di una nuova versione di un microservizio avviene mediante un _workflow_ apposito, come già indicato in precedenza.
Questo _workflow_ utilizza il _tool_ "semantic release" per automatizzare il rilascio di nuove versioni, come già discusso poco
sopra, il quale utilizza come schema di _versioning_ "semantic versioning". Per poter funzionare correttamente, tutti i messaggi
dei commit seguono il formato "Angular Commit Message", derivante dal progetto AngularJS, che è un'estensione del formato
"Conventional Commits". Tutte le _release_ effettuate su "beta" sono da considerarsi "prerelease" e sono etichettate su GitHub come
tali, mentre quelle effettuate su "main" sono _release_ vere e proprie. Il _tool_ si preoccupa di generare le "release notes",
ovvero le informazioni su che cosa è cambiato nella nuova versione del sistema, assieme al "changelog", che altro non è che la
cumulata delle diverse release notes a partire dalla prima versione ufficiale del software. Inoltre, "semantic release" aggiorna
in tutti i file di progetto che contengono il riferimento alla versione, che viene portata all'ultima rilasciata. L'ultimo file
che viene generato è il JAR eseguibile del progetto, qualora fosse possibile farlo, utilizzando il _tool_ "sbt assembly". Viene
passato al _workflow_ di CD tramite una variabile d'ambiente predefinita se deve essere effettuata una release o una prerelease e
il processo termina facendo _commit_ di tutti i file modificati durante la _release_ e effettuando la pubblicazione su GitHub.
È in questo _workflow_ che vengono utilizzate le chiavi PGP per poter effettuare le operazioni di _commit_ e _release_ discusse in
maniera autenticata e perciò capaci di soddisfare i vincoli di protezione dei _branch_.

È sempre specificato in cima a ciascun file sorgente il "copyright statement" per quel file, indicante i diritti che l'organizzazione
che lo ha realizzato trattiene su di esso. Essendo questo un progetto ad elevata innovazione, pressoché unico e per cui il fatto
di essere rivelato al grande pubblico anzitempo potrebbe essere un rischio per il successo dello stesso in quanto potrebbe favorire
la concorrenza, l'azienda che lo ha commissionato ha richiesto che tutto ciò che lo riguarda sia da considerarsi segreto industriale.
Per questo motivo, non è stata indicata nessuna licenza in quanto i diritti di licenza non possono e non devono essere concessi a
nessuna persona fisica o giuridica ed è stato chiaramente indicato che tutti i diritti sono riservati. Lo stesso, in versione più
estesa, è stato riportato nel file "README" di presentazione di ciascuna _repository_. Si assume che tutto il software aggiuntivo
utilizzato possieda licenze compatibili con il segreto industriale, ovvero licenze non "strong copyleft" che permettono il
_relicensing_ delle opere derivate e, qualora non fosse possibile, che l'ufficio legale dell'azienda sia riuscito a negoziare
questo diritto con i legittimi proprietari del software con licenza incompatibile. Per automatizzare l'inserimento dell'intestazione
con il copyright statement è stato utilizzato il _plugin_ per sbt denominato "sbt header".

Per la gestione dell'aggiornamento delle dipendenze è stato utilizzato il _tool_ "renovate", capace di individuare tutti quei file
che indicano delle _dependency_, identificare la loro versione e generare una _pull request_ per l'aggiornamento in caso non fosse
l'ultima. È stata creata una repository con la configurazione di base per renovate da cui tutte le altre possono attingere. Si è
fatto in modo che anche i _commit_ che crea in automatico seguano il formato "Conventional Commits", ma essendo vincolato il
comportamento dei _branch_, non è stato possibile permettere il _rebase_ degli aggiornamenti in automatico, che devono essere fatti
dall'amministratore delle _repository_. Inoltre, è sempre compito di renovate quello di aggiornare i suoi _branch_ tramite _rebase_
qualora diventassero _stale_ e non fossero più aggiornati con la versione del codice più recente del _branch_ da cui ha biforcato
il proprio. Per questo tipo di progetto, è stato abilitato anche l'aggiornamento dei "git submodule". Infine, sono stati abilitati
anche gli _alert_ di sicurezza di "dependabot" perché renovate è capace di leggerli e generare delle richieste di aggiornamento.

<div>
    <div style="text-align: center"><a href="#">Torna su</a></div>
    <div style="width: 50%; float: left">Vai a <a href="/toys-store/detailed-design">Design di dettaglio</a></div>
    <div style="width: 50%; float: right; text-align: right">Vai a <a href="/toys-store/implementation">Implementazione</a></div>
</div>
<br/>
