# TicketFlow — TP3 : configuration centralisée

Projet pédagogique Java 21, Spring Boot 4 et Spring Cloud 2025.1.
Dépôt : [mohcinekh1/ticketflow](https://github.com/mohcinekh1/ticketflow).

## Branches cumulatives

| Branche | Contenu |
|---|---|
| `codex/tp1-customer-service` | TP1 — CRUD clients, JPA/H2, DTO, validation et tests |
| `codex/tp2-eureka-gateway` | TP2 — Eureka, gateway et load balancing |
| `codex/tp3-config-server` | TP3 — Config Server, configuration centralisée et refresh |
| `codex/tp4-openfeign-event-booking` | TP4 — Event et Booking avec OpenFeign, étape suivante |

Cette branche correspond au TP3. Event Service et Booking Service se trouvent
dans la branche TP4. `main` contient la dernière version intégrée.

## Modules

| Module | Port | Rôle |
|---|---|---|
| `config-server` | 8888 | Distribue les fichiers de `config-repo/` |
| `discovery-service` | 8761 | Registre Eureka |
| `customer-service` | 8081 | CRUD clients et message de démonstration actualisable |
| `api-gateway` | 8080 | Routage dynamique vers Customer Service |

## Vérifier et démarrer

Depuis la racine du dépôt, avec JDK 21 :

```powershell
.\mvnw.cmd -B -ntp clean verify
```

Dans un premier terminal PowerShell, configure le chemin absolu avant de lancer
Config Server pour éviter toute dépendance au dossier de travail Maven :

```powershell
$env:CONFIG_REPO_LOCATION = 'file:/' + ((Resolve-Path .\config-repo).Path -replace '\\', '/')
.\mvnw.cmd -pl services/config-server spring-boot:run
```

Puis lance chaque commande dans un terminal distinct :

```powershell
.\mvnw.cmd -pl services/discovery-service spring-boot:run
.\mvnw.cmd -pl services/customer-service spring-boot:run
.\mvnw.cmd -pl services/api-gateway spring-boot:run
```

Les tests utilisent des configurations locales et ne nécessitent pas de serveur
Config ni Eureka actif.

## Tester le TP3 dans Postman

1. `GET http://localhost:8888/customer-service/default` : propriétés centralisées.
2. `GET http://localhost:8081/api/config/message` : message actuel.
3. Modifie `ticketflow.message` dans `config-repo/customer-service.yml`.
4. `POST http://localhost:8081/actuator/refresh`, sans body.
5. Relis `GET http://localhost:8081/api/config/message` : nouveau message.

Le message utilise `@RefreshScope`. Actualise chaque instance séparément.
Les routes clients sont accessibles via `http://localhost:8080/api/customers`.
Le message de démonstration est accessible directement sur Customer Service.

## Backend Git et Docker

Le backend local `native` fonctionne par défaut. Pour utiliser ton propre dépôt
Git contenant les mêmes fichiers YAML :

```powershell
$env:CONFIG_SERVER_BACKEND = 'git'
$env:CONFIG_GIT_URI = 'https://github.com/TON-COMPTE/ticketflow-config.git'
$env:CONFIG_GIT_LABEL = 'main'
.\mvnw.cmd -pl services/config-server spring-boot:run
```

Pour lancer les quatre services avec Docker Desktop :

```powershell
docker compose -f infra/docker-compose.yml up --build
docker compose -f infra/docker-compose.yml down
```

Les fichiers Config sont copiés dans l'image : reconstruis-la après modification.
Cette version est destinée aux TP locaux. H2 est volatile et distinct par instance ;
Actuator n'est pas authentifié. Ne publie pas de secrets dans les fichiers YAML.

## Origine

Base pédagogique créée par **AHAIDOUS Khadija**
([GitHub](https://github.com/kahaidous)). Adaptations dans le dépôt de Mohcine.
Licence à définir.
