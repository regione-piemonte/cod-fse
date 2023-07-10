# Prodotto

COD - Contatto Digitale

## Versione

1.0.0

## Descrizione del prodotto

CONTATTO DIGITALE realizza un servizio che consente al paziente di inviare comunicazioni veloci ed eventuali referti al proprio medico curante attraverso un sistema di messaggistica dedicato. 

## Componenti:

Il prodotto Contatto Digitale è costituito dalle seguenti componenti software:

| Componente |Descrizione  |Versione |
|--|--|--|
| [APICODOPSAN](apicodopsan) | API per la gestione delle comunicazioni da parte del medico verso il proprio paziente | 1.0.0 |
| [APIOPSAN](https://github.com/regione-piemonte/webappmed-fse/tree/main/apiopsan) | API trasversali per l'operatore sanitario per la consultazione dei documenti nel FSE | 1.0.0 |
| [APIOPSANAURA](apiopsanaura) | API per l'integrazione con l'anagrafica regionale degli assistiti | 1.0.0 |
| [CODCIT](codcit) | API per la gestione delle comunicazioni da parte del cittadino verso il proprio medico | 1.0.0 |
| [DMACODBATCH](dmacodbatch) | API per la gestione degli assistiti di un medico e delle relative conversazioni | 1.0.0 |
| [PWACODMED](pwacodmed) | Web application dell'operatore sanitario | 1.0.0 |

Le interfacce utente per il cittadino, implementate come Progressive Web App, sono disponibili sul prodotto [SANSOL](https://github.com/regione-piemonte/sansol). La pwa richiama servizi JSON/REST disponibili sul prodotto [APISAN](https://github.com/regione-piemonte/apisan). La web application dell'operatore sanitario è resa disponibile mediante abilitazione effettuata sul prodotto [LCCE](https://github.com/regione-piemonte/lcce).

## Prerequisiti di sistema

Server Web:
Apache 2.4.x

Application Server:
Wildfly 23

JDK:
Java Adoptium OpenJDK 11

Tipo di database:
PostgreSQL 12.2

## Dipendenze da sistemi esterni

### Sistema FSE della Regione Piemonte
Il servizio consente a medico e cittadino di effettuare il download del pacchetto delle immagini appoggiandosi alle componenti software esterne [DMASS](https://github.com/regione-piemonte/imr-fse/tree/main/DMASS) e [DMASSIMR](https://github.com/regione-piemonte/imr-fse/tree/main/DMASSIMR).
Il prodotto dipende da Web Services del FSE regionale piemontese per la verifica del consenso alla consultazione da parte degli operatori sanitari.

### Sistema di autenticazione
Il sistema di autenticazione con cui è protetta la web application PWACODMED è esterno al presente prodotto ed è basato sul sistema di Single Sign-On SHIBBOLETH.

## Versioning

Per il versionamento del software si utilizza la modalità Semantic Versioning (http://semver.org).

## Authors
La lista delle persone che hanno partecipato alla realizzazione del software sono:
- Andrea Borrelli
- Andrea Elmi
- Antonino Lofaro
- Gaia Bourlot
- Giuliano Iunco
- Liliana Guerra
- Manuela Bontempi
- Michele Mastrorilli
- Nicola Gaudenzi
- Pasquale Daloiso
- Tommaso Ruggiero
- Yvonne Carpegna

## Copyrights

“© Copyright Regione Piemonte – 2023”

## License

SPDX-License-Identifier: EUPL-1.2
Vedere il file LICENSE per i dettagli.
