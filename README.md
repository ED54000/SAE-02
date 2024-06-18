# SAE-02
Étudiants du groupe : 
- Maxime Bechard 
- BORTOLOTTI Florian
- JEAN-BAPTISTE Evan
- DUCHÊNE Éloi

Une partie de ce programme à été faite en utilisant la fonctionnalité CodeWithMe d'IntelliJ 

## Introduction

  - Objectif du Projet : Développer une application web interactive pour afficher divers points d'intérêt sur une carte, intégrée à des fonctionnalités météorologiques et de légende.
  - Technologies Utilisées : HTML, CSS, JavaScript (avec modules), Leaflet.js, APIs externes (pour la météo), Serveur HTTP avec Java (HTTPServer), RMI pour la communication entre le serveur et la base de données.

## Architecture
### Frontend 
  - HTML/CSS/JS : Utilisés pour la structure, le style et l'interaction de la page web.
  - Leaflet.js : Bibliothèque JavaScript pour l'affichage de cartes interactives OpenStreetMap.
  - APIs Externes : Utilisées pour obtenir les données météorologiques en temps réel.

### Backend :

  - Java HTTPServer : Serveur HTTP pour la gestion des requêtes et des réponses côté serveur.
  - Java RMI (Remote Method Invocation) : Utilisé pour la communication avec la base de données via des services distants.
  - Base de Données : Non spécifiée dans le code fourni, mais impliquée via les appels RMI à des services de requêtes SQL.

Fonctionnalités Implémentées

   - Carte Interactive : Affiche des points d'intérêt (vélibs, restaurants, établissements d'études supérieurs, incidents) avec des icônes spécifiques.
   - Légende Dynamique : Permet à l'utilisateur de filtrer les catégories de points d'intérêt à afficher sur la carte.
   - Affichage Météorologique : Donne des informations météorologiques actuelles avec des icônes et des descriptions pour différents moments de la journée.
   - Formulaire de Réservation :Permet aux utilisateurs de réserver des restaurants en utilisant un formulaire interactif sur la carte.
   - Ajout de Restaurant : Permet aux utilisateurs d'ajouter de nouveaux restaurants à la carte en cliquant sur un emplacement spécifique.

Informations d'Utilisation

   - Prérequis : Il faut tout d'abord lancer RMIJavaSQL avec les paramètres de la base de données, RMIJavaHttp sans paramètres puis ProxyServeur sans paramètres.
   - Utilisation de l'Application : Ouvrez index.html dans un navigateur web moderne pour voir la carte interactive. Utilisez les cases à cocher de la légende pour filtrer les catégories de points d'intérêt. Consultez les informations météorologiques dans la section dédiée.
  - Formulaire de Réservation : Cliquez sur un marqueur de restaurant sur la carte, remplissez et soumettez le formulaire pour effectuer une réservation. Assurez-vous que le serveur Java avec RMI est en cours d'exécution pour le traitement côté serveur.

Attention : Il faut utiliser xampp ou tout outil similaire pour pouvoir lancer sans erreur.
