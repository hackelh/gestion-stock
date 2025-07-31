"# Gestion de Stock Automobile - Application JavaFX

## 1. Description

Cette application est un outil de gestion de stock de pièces détachées automobiles, développé en Java avec l'interface graphique JavaFX. Elle permet d'ajouter, de visualiser et de gérer des pièces de manière intelligente, en fournissant des alertes et des suggestions pour optimiser les niveaux de stock.

L'application se connecte à une base de données MySQL et utilise l'ORM Hibernate pour la persistance des données, ce qui automatise la création et la mise à jour des tables.

## 2. Fonctionnalités

### Gestion des Ventes

L'application inclut un module de vente complet pour simuler une caisse enregistreuse de quincaillerie.

- **Interface de Vente :** Une vue dédiée permet de sélectionner des produits dans une liste filtrable, de spécifier une quantité et de les ajouter à un panier.
- **Panier Dynamique :** Le panier se met à jour en temps réel, affichant les produits, les quantités et le total de la vente.
- **Validation et Mise à Jour du Stock :** Lors de la validation d'une vente, le stock des produits est automatiquement décrémenté. Le système empêche la vente si le stock est insuffisant.
- **Intégration au Dashboard :** Le tableau de bord principal reflète immédiatement les changements de stock après une vente, mettant à jour les alertes de seuil critique et les quantités disponibles.

### Fonctionnalités Clés

- **Formulaire d'Ajout de Pièce** : Interface claire pour ajouter de nouvelles pièces avec des informations détaillées (nom, catégorie, référence, marque, prix, quantité, seuil critique, etc.).
- **Tableau de Bord Intelligent** : Vue centralisée de toutes les pièces en stock.
- **Alertes Visuelles** :
    - Les pièces dont le stock est inférieur ou égal au **seuil critique** sont surlignées en **rouge**.
    - Les pièces **obsolètes** (sans activité depuis plus de 6 mois) sont surlignées en **gris**.
- **Suggestions de Commande** : Pour les pièces en stock bas, l'application calcule et suggère automatiquement une quantité à recommander pour atteindre un stock cible.
- **Persistance Automatique** : Grâce à Hibernate, toutes les données sont sauvegardées dans une base de données MySQL, et la structure de la base est gérée automatiquement.
- **Gestion des Images :**
    - **Ajout d'Images :** Il est désormais possible d'associer une image à chaque pièce via le formulaire d'ajout ou de modification. Un bouton "Choisir une image" ouvre un explorateur de fichiers pour une sélection facile.
    - **Aperçu Instantané :** L'image sélectionnée est immédiatement affichée dans un aperçu, permettant de vérifier le visuel avant de sauvegarder.
    - **Sauvegarde Automatisée :** Les images sont copiées et stockées dans un dossier dédié du projet (`src/main/resources/images/pieces`), garantissant leur persistance avec le reste de l'application.
    - **Affichage dans le Tableau de Bord :** Le tableau de bord principal inclut une nouvelle colonne affichant une miniature de l'image de chaque pièce, pour une identification visuelle rapide.

## 3. Architecture du Projet

L'application suit une architecture inspirée du modèle **MVC (Modèle-Vue-Contrôleur)**, étendue avec une couche de **Service** pour la logique métier.

-   **`com.example.model`** (Modèle) :
    -   `Piece.java`: L'entité JPA qui représente une pièce automobile. Contient les annotations Hibernate pour le mapping avec la base de données.
    -   `PieceDAO.java`: (Data Access Object) Classe responsable de toutes les interactions avec la base de données (CRUD : Create, Read, Update, Delete) pour les pièces.
    -   `JPAUtil.java`: Classe utilitaire pour initialiser et fournir l'EntityManagerFactory d'Hibernate, qui gère la connexion à la base de données.

-   **`com.example.view`** (Vue) :
    -   `AddPieceView.fxml`: Fichier FXML décrivant l'interface du formulaire d'ajout.
    -   `DashboardView.fxml`: Fichier FXML décrivant l'interface du tableau de bord.
    -   `dashboard.css`: Feuille de style CSS pour les couleurs des alertes dans le tableau.

-   **`com.example.controller`** (Contrôleur) :
    -   `AddPieceController.java`: Gère la logique du formulaire d'ajout (validation des entrées, sauvegarde).
    -   `DashboardController.java`: Gère la logique du tableau de bord (chargement des données, application des styles, calcul des statuts).

-   **`com.example.service`** (Service) :
    -   `StockService.java`: Contient la logique métier pure (non liée à l'interface). C'est ici que sont définies les règles pour le stock bas, l'obsolescence et les suggestions de commande.

-   **`com.example.Main.java`** : Point d'entrée de l'application JavaFX.

## 4. Technologies Utilisées

- **Langage** : Java 17
- **Interface Graphique** : JavaFX 17
- **Gestion de projet & Dépendances** : Maven
- **Base de Données** : MySQL
- **ORM (Mapping Objet-Relationnel)** : Hibernate
- **API de Persistance** : JPA (Jakarta Persistence API)

## 5. Comment lancer l'application

1.  Assurez-vous d'avoir une instance MySQL en cours d'exécution.
2.  Configurez les informations de connexion à la base de données dans le fichier `src/main/resources/META-INF/persistence.xml`.
3.  Utilisez Maven pour construire le projet et télécharger les dépendances.
4.  Exécutez la méthode `main` dans la classe `com.example.Main.java`.
" 
