# Projet Animaux Domestiques

Ce projet est une application Android conçue pour permettre aux utilisateurs de gérer les activités de leurs animaux domestiques.

## Fonctionnalités principales

- **Gestion des animaux** : Ajouter, modifier et supprimer des animaux (espèces prédéfinies : chien, chat, hamster, poisson, etc.).
- **Fiche d'identité** : Affichage des informations détaillées de chaque animal (nom, espèce, photo et activités associées).
- **Gestion des activités** :
  - Ajouter, modifier et supprimer des activités pour chaque animal.
  - Activités par défaut paramétrables selon l'espèce (ex. promenade pour un chien).
  - Notifications pour les activités programmées (quotidiennes, hebdomadaires ou uniques).
- **Paramétrage de l'application** :
  - Personnalisation de la couleur de fond, de la taille des polices, etc.
- **Base de données Room** :
  - Gestion locale des données des animaux et de leurs activités.
- **Recherche** :
  - Possibilité de chercher parmis les animaux, espèces et activités de l'application.

## Technologies utilisées

- **Kotlin** pour le développement de l'application.
- **Jetpack Compose** pour la création des interfaces utilisateur.
- **Room** pour la gestion des données en base locale.
- **ViewModel** pour la gestion des états et des données.
- **Coroutines** pour la gestion asynchrone.
- **DataStore** pour le stockage des paramètres utilisateur.
- **Material 3** pour une interface moderne et adaptée aux différents écrans.

## Configuration du projet

1. Clonez ce dépôt :
   ```bash
   git clone <URL_DU_DEPOT>
   ```
2. Ouvrez le projet dans **Android Studio**.
3. Assurez-vous d'utiliser la version Android Studio **Koala** et un émulateur ou appareil compatible.
4. Compilez et exécutez le projet.

## Critères d'évaluation

- **Fonctionnalité** : Le projet doit être fonctionnel et ergonomique.
- **Technologies** : Utilisation de technologies modernes enseignées en cours (Jetpack Compose, Room, coroutines, etc.).
- **Qualité du code** : Architecture modulaire, clarté et lisibilité du code.
- **Ergonomie** : Interface intuitive et utilisation fluide.
