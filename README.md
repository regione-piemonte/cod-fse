# Prodotto

COD - Contatto Digitale

## Versione

1.0.0

## Descrizione del prodotto

CONTATTO DIGITALE realizza un servizio che consente al paziente di inviare comunicazioni veloci ed eventuali referti al proprio medico curante attraverso un sistema di messaggistica dedicato. 

## Componenti:

Il prodotto Contatto Digitale è costituito dalle seguenti componenti software:

* [APICODOPSAN](apicodopsan) API per la gestione delle comunicazioni da parte del medico verso il proprio paziente
* [APIOPSAN](https://github.com/regione-piemonte/webappmed-fse/blob/main/apicodopsan) API trasversali per l'operatore sanitario per la consultazione dei documenti nel FSE
* [APIOPSANAURA](apiopsanaura) API per l'integrazione con l'anagrafica regionale degli assistiti
* [CODCIT](codcit) API per la gestione delle comunicazioni da parte del cittadino verso il proprio medico
* [DMACODBATCH](dmacodbatch) API per la gestione degli assistiti di un medico e delle relative conversazioni
* [PWACODMED](pwacodmed) Web application dell'operatore sanitario

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
Il prodotto dipende da Web Services del FSE regionale piemontese per la verifica del consenso alla consultazione da parte degli operatori sanitari.

### Sistema di autenticazione
Il sistema di autenticazione con cui è protetta la web application PWACODMED è esterno al presente prodotto ed è basato sul sistema di Single Sign-On SHIBBOLETH.

## Versioning

Per il versionamento del software si utilizza la modalità Semantic Versioning (http://semver.org).

## Authors
CSI Piemonte

## Copyrights

“© Copyright Regione Piemonte – 2023”

## License

SPDX-License-Identifier: EUPL-1.2
Vedere il file LICENSE per i dettagli.
