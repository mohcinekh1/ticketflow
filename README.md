# TicketFlow

Projet pédagogique de réservation de billets pour apprendre les microservices
avec Java 21, Spring Boot 4 et Spring Cloud 2025.1.

Dépôt : [mohcinekh1/ticketflow](https://github.com/mohcinekh1/ticketflow).

## Branches et travaux pratiques

Les branches sont cumulatives : chaque TP conserve les fonctionnalités du précédent.
`main` contient la dernière version intégrée.

| Branche | TP | Contenu |
|---|---|---|
| `codex/tp1-customer-service` | TP1 — Premier microservice | CRUD clients, JPA/H2, validation, DTO, erreurs et tests |
| `codex/tp2-eureka-gateway` | TP2 — Découverte et routage | Eureka, API Gateway et répartition entre instances |
| `codex/tp3-config-server` | TP3 — Configuration centralisée | Config Server, clients Config, backend local/Git et refresh |
| `codex/tp4-openfeign-event-booking` | TP4 — Communication entre services | Catalogue Event, Booking et appel OpenFeign via Eureka |

Pour ouvrir un TP, après avoir sauvegardé tes modifications locales :

```powershell
git fetch mohcine
git switch codex/tp3-config-server
```

Après un nouveau clone, utilise `git fetch origin` pour récupérer les branches.

## Fonctionnalités et limites actuelles

| Module | Port | Fonctionnalité |
|---|---|---|
| `config-server` | 8888 | Distribue les fichiers de `config-repo/` ; backend Git configurable |
| `discovery-service` | 8761 | Registre Eureka |
| `api-gateway` | 8080 | Routes dynamiques `lb://` vers Customer, Event et Booking |
| `customer-service` | 8081 | CRUD clients avec base H2 en mémoire et message actualisable |
| `event-service` | 8082 | Catalogue de trois événements en mémoire ; lecture et erreur 404 |
| `booking-service` | 8083 | Démonstration d'appel à Event Service avec OpenFeign |

Le TP4 démontre la communication entre services. Il ne crée pas encore de
réservations et ne diminue pas le nombre de places. Le catalogue Event est fixe.
Les données Customer disparaissent au redémarrage et chaque instance possède
sa propre base H2. PostgreSQL, migrations, réservations persistantes, résilience,
Kafka, Saga/Outbox, sécurité et observabilité restent des évolutions futures.

## Structure

```text
ticketflow/
├── .github/workflows/ci.yml
├── config-repo/
│   ├── application.yml
│   ├── customer-service.yml
│   ├── api-gateway.yml
│   ├── event-service.yml
│   └── booking-service.yml
├── infra/docker-compose.yml
├── pom.xml
└── services/
    ├── config-server/
    ├── discovery-service/
    ├── customer-service/
    ├── api-gateway/
    ├── event-service/
    └── booking-service/
```

## Installation et tests

Prérequis : JDK 21 ; Docker Desktop pour le lancement avec conteneurs.
Le wrapper Maven est inclus.

```powershell
git clone https://github.com/mohcinekh1/ticketflow.git
cd ticketflow
.\mvnw.cmd -B -ntp clean verify
```

Les tests utilisent des configurations locales et ne nécessitent pas de lancer
Config Server ni Eureka. La CI exécute `verify` sur les pull requests et les
pushs sur `main`.

## Lancement local sous Windows

Exécute chaque commande dans un terminal distinct, depuis la racine du projet.
Attends que Config Server soit prêt avant de lancer ses clients.

Terminal 1 — Config Server. Le chemin absolu évite de dépendre du dossier de
travail choisi par Maven :

```powershell
$env:CONFIG_REPO_LOCATION = 'file:/' + ((Resolve-Path .\config-repo).Path -replace '\\', '/')
.\mvnw.cmd -pl services/config-server spring-boot:run
```

Terminal 2 — Eureka :

```powershell
.\mvnw.cmd -pl services/discovery-service spring-boot:run
```

Terminal 3 — Customer Service :

```powershell
.\mvnw.cmd -pl services/customer-service spring-boot:run
```

Terminal 4 — Event Service :

```powershell
.\mvnw.cmd -pl services/event-service spring-boot:run
```

Terminal 5 — Booking Service :

```powershell
.\mvnw.cmd -pl services/booking-service spring-boot:run
```

Terminal 6 — API Gateway :

```powershell
.\mvnw.cmd -pl services/api-gateway spring-boot:run
```

Vérifie les instances dans [Eureka](http://localhost:8761).
L'enregistrement et la mise à jour des caches prennent un délai : une gateway
qui démarre peut temporairement répondre 503.

Pour lancer un deuxième Customer Service, utilise `8091` car Event utilise `8082` :

```powershell
.\mvnw.cmd -pl services/customer-service spring-boot:run "-Dspring-boot.run.arguments=--server.port=8091"
```

## Tests dans Postman

Choisis la méthode dans la liste Postman et colle uniquement l'URL dans le champ.

| Méthode | URL | Résultat attendu |
|---|---|---|
| GET | `http://localhost:8888/customer-service/default` | Propriétés centralisées du Customer Service |
| GET | `http://localhost:8080/api/customers` | Liste des clients |
| GET | `http://localhost:8080/api/events` | Catalogue des événements |
| GET | `http://localhost:8080/api/events/1` | Événement TicketFlow Live |
| GET | `http://localhost:8080/api/events/999` | 404 |
| GET | `http://localhost:8080/api/bookings/test-event/1` | Événement obtenu par Booking via OpenFeign |
| GET | `http://localhost:8080/actuator/gateway/routes` | Routes de la gateway |

Pour créer un client, envoie `POST http://localhost:8080/api/customers` avec
**Body → raw → JSON** :

```json
{
  "firstName": "Sara",
  "lastName": "Idrissi",
  "email": "sara@example.com",
  "phone": "+212600000001"
}
```

Réponse attendue : `201 Created` ; un email déjà présent retourne `409 Conflict`.

Le parcours de l'appel TP4 est :

```text
Postman → Gateway :8080 → Booking :8083 → OpenFeign → Event :8082
```

Eureka fournit les adresses des instances ; Config Server fournit leurs propriétés.

## Actualiser la configuration — TP3

1. Lis le message avec `GET http://localhost:8081/api/config/message`.
2. Modifie `ticketflow.message` dans `config-repo/customer-service.yml`.
3. Envoie `POST http://localhost:8081/actuator/refresh`, sans body.
4. Relis `GET http://localhost:8081/api/config/message`.

Le message utilise `@RefreshScope`. Chaque instance doit être actualisée
séparément. Le changement de port exige un redémarrage. Le endpoint de
démonstration est accessible directement sur Customer Service.

Le backend `native` est activé par défaut. Pour utiliser ton propre dépôt Git :

```powershell
$env:CONFIG_SERVER_BACKEND = 'git'
$env:CONFIG_GIT_URI = 'https://github.com/TON-COMPTE/ticketflow-config.git'
$env:CONFIG_GIT_LABEL = 'main'
.\mvnw.cmd -pl services/config-server spring-boot:run
```

Crée d'abord ce dépôt avec les fichiers YAML de `config-repo/`. Ne publie pas de
mots de passe ni de clés dans ces fichiers ; fournis-les par l'environnement.

## Docker Compose

```powershell
docker compose -f infra/docker-compose.yml up --build
docker compose -f infra/docker-compose.yml down
```

Compose démarre les six services avec leurs ports habituels. Les clients utilisent
`http://config-server:8888` et `http://discovery-service:8761/eureka/` sur le réseau
Docker. Ils redémarrent en cas d'échec de démarrage. Les fichiers de configuration
sont copiés dans l'image Config Server : reconstruis l'image après modification.

Cette configuration est destinée aux TP locaux : Actuator n'est pas authentifié,
le scaling Compose et la persistance partagée ne sont pas encore configurés.

## Origine du projet

Base pédagogique créée par **AHAIDOUS Khadija**
([GitHub](https://github.com/kahaidous)). Les TP de cette version sont maintenus
dans [mohcinekh1/ticketflow](https://github.com/mohcinekh1/ticketflow).
Licence à définir.
