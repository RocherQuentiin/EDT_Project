-- Ajouter des utilisateurs (inclut enseignants supplémentaires)
INSERT INTO Utilisateur (nom, email, mot_de_passe, type_utilisateur_id) VALUES
                                                                            ('Admin', 'admin@isep.fr', 'admin123', 1), -- Administrateur
                                                                            ('Marie Dupont', 'mdupont@isep.fr', 'enseignant123', 2), -- Enseignant
                                                                            ('Emilie Garnier', 'egarnier@isep.fr', 'enseignant123', 2), -- Enseignant ajouté
                                                                            ('Thomas Lefevre', 'tlefevre@isep.fr', 'enseignant123', 2), -- Enseignant ajouté
                                                                            ('Claire Durand', 'cdurand@isep.fr', 'enseignant789', 2), -- Enseignant ajouté
                                                                            ('Nicolas Moreau', 'nmoreau@isep.fr', 'enseignant456', 2), -- Enseignant ajouté
                                                                            ('Anne Robert', 'arobert@isep.fr', 'enseignant321', 2), -- Enseignant ajouté
                                                                            ('Paul Rousseau', 'prousseau@isep.fr', 'etudiant123', 3), -- Étudiant
                                                                            ('Julie Martin', 'jmartin@isep.fr', 'etudiant456', 3); -- Étudiant

-- Ajouter des étudiants dans la table Etudiant
INSERT INTO Etudiant (id, emploiDuTemps_id) VALUES
                                                (3, NULL), -- Paul Rousseau
                                                (4, NULL); -- Julie Martin

-- Ajouter des salles
INSERT INTO Salle (numero_salle, capacite, localisation) VALUES
                                                             ('A101', 30, 'Bâtiment A'),
                                                             ('B205', 40, 'Bâtiment B'),
                                                             ('C303', 50, 'Bâtiment C');

-- Ajouter des équipements
INSERT INTO Equipement (nom, numero_equipement, type, stock) VALUES
                                                                 ('Vidéo Projecteur', 'VP101', 'Audiovisuel', 5),
                                                                 ('Tableau Blanc', 'TB202', 'Mobilier', 10),
                                                                 ('Ordinateur Portable', 'PC303', 'Informatique', 15);

-- Associer des équipements aux salles
INSERT INTO Salle_Equipement (salle_id, equipement_id) VALUES
                                                           (1, 1), -- Vidéo Projecteur dans la salle A101
                                                           (2, 2), -- Tableau Blanc dans la salle B205
                                                           (3, 3); -- Ordinateur Portable dans la salle C303

-- Ajouter des horaires
INSERT INTO Horaire (date, heureDebut, heureFin) VALUES
                                                     ('2023-11-20', '08:00:00', '10:00:00'),
                                                     ('2023-11-20', '10:15:00', '12:15:00'),
                                                     ('2023-11-21', '14:00:00', '16:00:00');

-- Ajouter des cours (tous associés à des enseignants avec type_utilisateur_id = 2)
INSERT INTO Cours (nom, enseignant_id, salle_id, horaire_id) VALUES
                                                                 ('Mathématiques', 2, 1, 1), -- Salle A101, 1er horaire, Marie Dupont
                                                                 ('Informatique', 3, 2, 2), -- Salle B205, 2e horaire, Emilie Garnier
                                                                 ('Physique', 4, 3, 3), -- Salle C303, 3e horaire, Thomas Lefevre
                                                                 ('Chimie', 5, 1, 1), -- Salle A101, 1er horaire, Claire Durand
                                                                 ('Biologie', 6, 2, 2), -- Salle B205, 2e horaire, Nicolas Moreau
                                                                 ('Programmation Java', 7, 3, 3), -- Salle C303, 3e horaire, Anne Robert
                                                                 ('Analyse de Données', 3, 1, 2); -- Salle A101, 2e horaire, Emilie Garnier

-- Associer étudiants et cours
INSERT INTO Cours_Etudiant (cours_id, etudiant_id) VALUES
                                                       (1, 3), -- Paul Rousseau participe au cours de Mathématiques
                                                       (2, 3), -- Paul Rousseau participe au cours d'Informatique
                                                       (3, 4), -- Julie Martin participe au cours de Physique
                                                       (4, 4), -- Julie Martin participe au cours de Chimie
                                                       (5, 3); -- Paul Rousseau participe au cours de Biologie

-- Ajouter des emplois du temps pour les étudiants
INSERT INTO EmploiDuTemps (utilisateur_id) VALUES
                                               (3), -- Paul Rousseau
                                               (4); -- Julie Martin

-- Associer des cours aux emplois du temps
INSERT INTO EmploiDuTemps_Cours (emploiDuTemps_id, cours_id) VALUES
                                                                 (1, 1), -- Emploi du temps de Paul Rousseau pour Mathématiques
                                                                 (1, 2), -- Emploi du temps de Paul Rousseau pour Informatique
                                                                 (2, 3), -- Emploi du temps de Julie Martin pour Physique
                                                                 (2, 4), -- Emploi du temps de Julie Martin pour Chimie
                                                                 (1, 5); -- Emploi du temps de Paul Rousseau pour Biologie
INSERT INTO Notification (message, destinataire_id, dateEnvoi, expediteur_id) VALUES
                                                                                  ('Votre cours de Mathématiques a été déplacé.', 3, '2023-11-19 10:00:00', 1), -- Envoyée par Admin à Paul Rousseau
                                                                                  ("Votre cours d\'Informatique est confirmé.", 3, '2023-11-19 11:00:00', 1), -- Envoyée par Admin à Paul Rousseau
                                                                                ('Votre cours de Physique est annulé.', 4, '2023-11-20 08:00:00', 1); -- Envoyée par Admin à Julie Martin

-- Ajouter plus de salles
INSERT INTO Salle (numero_salle, capacite, localisation) VALUES
                                                             ('A102', 25, 'Bâtiment A'),
                                                             ('A103', 35, 'Bâtiment A'),
                                                             ('B206', 50, 'Bâtiment B'),
                                                             ('B207', 40, 'Bâtiment B'),
                                                             ('C304', 60, 'Bâtiment C'),
                                                             ('D101', 45, 'Bâtiment D'),
                                                             ('D102', 30, 'Bâtiment D'),
                                                             ('E201', 70, 'Bâtiment E'),
                                                             ('E202', 60, 'Bâtiment E');

-- Ajouter plus d'horaires
INSERT INTO Horaire (date, heureDebut, heureFin) VALUES
                                                     ('2023-11-22', '08:00:00', '10:00:00'), -- Plage horaire 1
                                                     ('2023-11-22', '10:20:00', '12:20:00'), -- Plage horaire 2
                                                     ('2023-11-22', '13:30:00', '15:30:00'), -- Plage horaire 3
                                                     ('2023-11-23', '09:00:00', '11:00:00'), -- Plage horaire 4
                                                     ('2023-11-23', '11:15:00', '13:15:00'), -- Plage horaire 5
                                                     ('2023-11-24', '14:00:00', '16:00:00'), -- Plage horaire 6
                                                     ('2023-11-24', '16:15:00', '18:15:00'); -- Plage horaire 7

-- Ajouter plus de cours
INSERT INTO Cours (nom, enseignant_id, salle_id, horaire_id) VALUES
                                                                 ('Chimie', 2, 4, 4),       -- Salle B206 à un horaire précis
                                                                 ('Biologie', 2, 5, 5),     -- Salle B207
                                                                 ('Histoire', 2, 6, 6),     -- Salle C304
                                                                 ('Anglais', 2, 7, 7),      -- Salle D101
                                                                 ('Philosophie', 2, 8, 1),  -- Salle D102
                                                                 ('Programmation Java', 2, 9, 2), -- Salle E201
                                                                 ('Analyse des Données', 2, 10, 3), -- Salle E202
                                                                 ('Gestion de Projets', 2, 3, 4),   -- Salle C303 (déjà existante)
                                                                 ('Communication', 2, 2, 6),        -- Salle B205
                                                                 ('Intelligence Artificielle', 2, 1, 7); -- Salle A101

-- Ajouter des étudiants supplémentaires aux cours
INSERT INTO Utilisateur (nom, email, mot_de_passe, type_utilisateur_id) VALUES
                                                                            ('Laure Bernard', 'lbernard@isep.fr', 'etudiant789', 3),
                                                                            ('Olivier Thomas', 'othomas@isep.fr', 'etudiant999', 3),
                                                                            ('Manon Leclerc', 'mleclerc@isep.fr', 'etudiant1234', 3);

-- Ajouter les nouveaux étudiants à la table Etudiant
INSERT INTO Etudiant (id, emploiDuTemps_id) VALUES
                                                (5, NULL), -- Laure Bernard
                                                (6, NULL), -- Olivier Thomas
                                                (7, NULL); -- Manon Leclerc

-- Assigner les nouveaux étudiants aux cours
INSERT INTO Cours_Etudiant (cours_id, etudiant_id) VALUES
                                                       (4, 5), -- Laure Bernard inscrite à Chimie
                                                       (5, 5), -- Laure Bernard inscrite à Biologie
                                                       (6, 6), -- Olivier Thomas inscrit à Histoire
                                                       (7, 7), -- Manon Leclerc inscrite à Anglais
                                                       (8, 5), -- Laure Bernard inscrite à Philosophie
                                                       (9, 6), -- Olivier Thomas inscrit à Programmation Java
                                                       (10, 7), -- Manon Leclerc inscrite à Analyse des Données
                                                       (3, 5), -- Laure Bernard inscrite à Gestion de Projets
                                                       (2, 6), -- Olivier Thomas inscrit à Communication
                                                       (1, 7); -- Manon Leclerc inscrite à Intelligence Artificielle

-- Ajouter des étudiants aux emplois du temps
INSERT INTO EmploiDuTemps (utilisateur_id) VALUES
                                               (5), -- Laure Bernard
                                               (6), -- Olivier Thomas
                                               (7); -- Manon Leclerc

-- Associer des cours aux nouveaux emplois du temps
INSERT INTO EmploiDuTemps_Cours (emploiDuTemps_id, cours_id) VALUES
                                                                 (3, 4), -- Chimie pour Laure Bernard
                                                                 (3, 5), -- Biologie pour Laure Bernard
                                                                 (4, 6), -- Histoire pour Olivier Thomas
                                                                 (5, 7), -- Anglais pour Manon Leclerc
                                                                 (3, 8), -- Philosophie pour Laure Bernard
                                                                 (4, 9), -- Programmation Java pour Olivier Thomas
                                                                 (5, 10), -- Analyse des Données pour Manon Leclerc
                                                                 (3, 3), -- Gestion de Projets pour Laure Bernard
                                                                 (4, 2), -- Communication pour Olivier Thomas
                                                                 (5, 1); -- Intelligence Artificielle pour Manon Leclerc

-- Ajouter des enseignants dans la table Utilisateur
INSERT INTO Utilisateur (nom, email, mot_de_passe, type_utilisateur_id) VALUES
                                                                            ('Emilie Garnier', 'egarnier@isep.fr', 'enseignant123', 2), -- Enseignante
                                                                            ('Thomas Lefevre', 'tlefevre@isep.fr', 'enseignant123', 2), -- Enseignant
                                                                            ('Claire Durand', 'cdurand@isep.fr', 'enseignant789', 2), -- Enseignante
                                                                            ('Nicolas Moreau', 'nmoreau@isep.fr', 'enseignant456', 2), -- Enseignant
                                                                            ('Anne Robert', 'arobert@isep.fr', 'enseignant321', 2); -- Enseignante

-- Ajouter des enseignants dans la table Utilisateur
INSERT INTO Utilisateur (nom, email, mot_de_passe, type_utilisateur_id) VALUES
                                                                            ('Emilie Garnier', 'egarnier@isep.fr', 'enseignant123', 2), -- Enseignante
                                                                            ('Thomas Lefevre', 'tlefevre@isep.fr', 'enseignant123', 2), -- Enseignant
                                                                            ('Claire Durand', 'cdurand@isep.fr', 'enseignant789', 2), -- Enseignante
                                                                            ('Nicolas Moreau', 'nmoreau@isep.fr', 'enseignant456', 2), -- Enseignant
                                                                            ('Anne Robert', 'arobert@isep.fr', 'enseignant321', 2); -- Enseignante

-- Associer chaque enseignant à quelques cours
UPDATE Cours
SET enseignant_id = 5
WHERE nom = 'Mathématiques'; -- Emilie Garnier enseigne Mathématiques

UPDATE Cours
SET enseignant_id = 6
WHERE nom = 'Informatique'; -- Thomas Lefevre enseigne Informatique

UPDATE Cours
SET enseignant_id = 7
WHERE nom = 'Physique'; -- Claire Durand enseigne Physique

UPDATE Cours
SET enseignant_id = 8
WHERE nom = 'Chimie'; -- Nicolas Moreau enseigne Chimie

UPDATE Cours
SET enseignant_id = 9
WHERE nom = 'Biologie'; -- Anne Robert enseigne Biologie

-- Ajouter plus de cours attribués à ces enseignants
INSERT INTO Cours (nom, enseignant_id, salle_id, horaire_id) VALUES
                                                                 ('Algorithmique', 5, 2, 4), -- Emilie Garnier enseigne Algorithmique
                                                                 ('Réseaux', 6, 3, 5), -- Thomas Lefevre enseigne Réseaux
                                                                 ('Thermodynamique', 7, 4, 6), -- Claire Durand enseigne Thermodynamique
                                                                 ('Chimie Organique', 8, 5, 7), -- Nicolas Moreau enseigne Chimie Organique
                                                                 ('Médecine Biologique', 9, 6, 1); -- Anne Robert enseigne Médecine Biologique