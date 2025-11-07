"# 📦 SmartLogi - Système de Gestion de Livraison

SmartLogi est une application de gestion de colis et de livraison développée avec Spring Boot. Elle permet de gérer les expéditions, les livreurs, les destinataires et le suivi des colis avec un système de notification par email.

## 📊 Diagramme de Classe

![SmartDelivery Diagramme.png](../SmartDelivery%20Diagramme.png)

> **Note** : Pour afficher le diagramme, placez votre image PNG du diagramme de classe à la racine du projet avec le nom `diagram-class.png`

### Relations principales :
- Un **ClientExpediteur** peut expédier plusieurs **Colis** (relation 1..*)
- Un **Destinataire** peut recevoir plusieurs **Colis** (relation 1..*)
- Un **Livreur** livre plusieurs **Colis** dans sa **Zone** assignée (relation 1..*)
- Un **Colis** peut contenir plusieurs **Produits** via la table de liaison **Colis_Produit** (relation *..*)
- Chaque **Colis** possède un **HistoriqueLivraison** pour tracer son parcours (relation 1..*)

## 🚀 Fonctionnalités

- **Gestion des Colis** : Création, suivi et mise à jour du statut des colis
- **Gestion des Livreurs** : Attribution des colis aux livreurs par zone
- **Gestion des Clients** : Expéditeurs et destinataires
- **Gestion des Produits** : Association de produits aux colis
- **Système de Zones** : Organisation géographique des livraisons
- **Historique de Livraison** : Traçabilité complète des opérations
- **Notifications Email** : Envoi automatique d'emails lors de :
  - Création d'un colis
  - Attribution à un livreur
  - Changement de statut

## 🛠️ Technologies Utilisées

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** - Gestion de la persistance
- **PostgreSQL** - Base de données
- **Liquibase** - Gestion des migrations de base de données
- **MapStruct** - Mapping objet-objet
- **Thymeleaf** - Templates HTML pour les emails
- **Spring Mail** - Envoi d'emails
- **Swagger/OpenAPI** - Documentation de l'API
- **Maven** - Gestion des dépendances

## 📋 Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- PostgreSQL 12+
- Un compte Gmail avec App Password (pour l'envoi d'emails)

## ⚙️ Configuration

### 1. Base de données

Créez une base de données PostgreSQL :

```sql
CREATE DATABASE smartlogi_db;
```

### 2. Variables d'environnement

Configurez les variables d'environnement suivantes pour l'envoi d'emails :

```bash
set SMTP_USERNAME=votre-email@gmail.com
set SMTP_PASSWORD=votre-app-password
```

### 3. Configuration de l'application

Le fichier `application.yaml` contient la configuration de l'application. Modifiez les paramètres de la base de données si nécessaire :

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smartlogi_db
    username: postgres
    password: root
```

## 🚀 Installation et Démarrage

### 1. Cloner le projet

```bash
git clone <url-du-repository>
cd SmartLogi
```

### 2. Compiler le projet

```bash
mvnw clean install
```

### 3. Lancer l'application

```bash
mvnw spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## 📚 Documentation de l'API

Une fois l'application lancée, accédez à la documentation Swagger :

```
http://localhost:8080/swagger-ui.html
```

## 🔑 Endpoints Principaux

### Colis
- `POST /api/colis` - Créer un colis
- `GET /api/colis` - Lister tous les colis
- `GET /api/colis/{id}` - Obtenir un colis par ID
- `PUT /api/colis/{id}` - Mettre à jour un colis
- `DELETE /api/colis/{id}` - Supprimer un colis

### Livreurs
- `POST /api/livreurs` - Créer un livreur
- `GET /api/livreurs` - Lister tous les livreurs
- `GET /api/livreurs/{id}` - Obtenir un livreur par ID

### Clients Expéditeurs
- `POST /api/clients-expediteurs` - Créer un client expéditeur
- `GET /api/clients-expediteurs` - Lister tous les clients

### Zones
- `POST /api/zones` - Créer une zone
- `GET /api/zones` - Lister toutes les zones

### Produits
- `POST /api/produits` - Créer un produit
- `GET /api/produits` - Lister tous les produits

## 📧 Système de Notification Email

Le système envoie automatiquement des emails HTML personnalisés lors de :

1. **Création d'un colis** : Un email de confirmation est envoyé à l'expéditeur
2. **Attribution à un livreur** : Notification avec les détails du livreur
3. **Mise à jour du statut** : Notification de changement de statut

Les templates d'emails sont situés dans `src/main/resources/templates/email/`

## 📂 Structure du Projet

```
SmartLogi/
├── src/main/java/com/houssam/SmartLogi/
│   ├── controller/      # Contrôleurs REST
│   ├── service/         # Logique métier
│   ├── repository/      # Accès aux données
│   ├── model/           # Entités JPA
│   ├── dto/             # Data Transfer Objects
│   ├── mapper/          # Mappers MapStruct
│   ├── email/           # Services d'envoi d'emails
│   ├── exception/       # Gestion des exceptions
│   └── enums/           # Énumérations
├── src/main/resources/
│   ├── application.yaml # Configuration
│   ├── db/changelog/    # Migrations Liquibase
│   └── templates/email/ # Templates d'emails
└── pom.xml
```

## 🗃️ Schéma de Base de Données

Le projet utilise Liquibase pour gérer les migrations. Les principales tables :

- `client_expediteur` - Informations des expéditeurs
- `destinataire` - Informations des destinataires
- `livreur` - Informations des livreurs
- `zone` - Zones géographiques
- `colis` - Informations des colis
- `produit` - Catalogue de produits
- `colis_produit` - Association colis-produits
- `historique_livraison` - Historique des opérations

## 🧪 Tests

Pour exécuter les tests :

```bash
mvnw test
```

## 👥 Auteurs

- **Houssam** - Développeur principal

## 📄 Licence

Ce projet est développé à des fins éducatives.

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou une pull request.

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une issue sur le repository GitHub."

