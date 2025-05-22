-- Utilisateurs
INSERT INTO Utilisateur (nom, email, mot_de_passe, type_utilisateur_id) VALUES
  ('Admin', 'admin@isep.fr', 'admin123', 1),
  ('Marie Dupont', 'mdupont@isep.fr', 'enseignant123', 2),
  ('Emilie Garnier', 'egarnier@isep.fr', 'enseignant123', 2),
  ('Thomas Lefevre', 'tlefevre@isep.fr', 'enseignant123', 2),
  ('Claire Durand', 'cdurand@isep.fr', 'enseignant789', 2),
  ('Nicolas Moreau', 'nmoreau@isep.fr', 'enseignant456', 2),
  ('Anne Robert', 'arobert@isep.fr', 'enseignant321', 2),
  ('Paul Rousseau', 'prousseau@isep.fr', 'etudiant123', 3),
  ('Julie Martin', 'jmartin@isep.fr', 'etudiant456', 3),
  ('Laure Bernard', 'lbernard@isep.fr', 'etudiant789', 3),
  ('Olivier Thomas', 'othomas@isep.fr', 'etudiant999', 3),
  ('Manon Leclerc', 'mleclerc@isep.fr', 'etudiant1234', 3),
  ('Lucas Petit', 'lpetit@isep.fr', 'etudiant567', 3),
  ('Sophie Girard', 'sgirard@isep.fr', 'etudiant890', 3),
  ('quentin', 'q@isep.fr', 'q', 3);

-- Etudiants
INSERT INTO Etudiant (id, emploiDuTemps_id) VALUES
  (8, NULL), (9, NULL), (10, NULL), (11, NULL), (12, NULL), (13, NULL), (14, NULL);

-- Salles
INSERT INTO Salle (numero_salle, capacite, localisation) VALUES
  ('A101', 30, 'Bâtiment A'),
  ('B205', 40, 'Bâtiment B'),
  ('C303', 50, 'Bâtiment C'),
  ('A102', 25, 'Bâtiment A'),
  ('A103', 35, 'Bâtiment A'),
  ('B206', 50, 'Bâtiment B'),
  ('B207', 40, 'Bâtiment B'),
  ('C304', 60, 'Bâtiment C'),
  ('D101', 45, 'Bâtiment D'),
  ('D102', 30, 'Bâtiment D'),
  ('E201', 70, 'Bâtiment E'),
  ('E202', 60, 'Bâtiment E');

-- Equipements
INSERT INTO Equipement (nom, numero_equipement, type, stock) VALUES
  ('Vidéo Projecteur', 'VP101', 'Audiovisuel', 5),
  ('Tableau Blanc', 'TB202', 'Mobilier', 10),
  ('Ordinateur Portable', 'PC303', 'Informatique', 15),
  ('Micro', 'MIC404', 'Audiovisuel', 8);

-- Salle_Equipement
INSERT INTO Salle_Equipement (salle_id, equipement_id) VALUES
  (1, 1), (1, 2), (2, 2), (3, 3), (4, 4), (5, 1), (6, 3);

-- Horaires
INSERT INTO Horaire (date, heureDebut, heureFin) VALUES
  ('2025-11-20', '08:00:00', '10:00:00'),
  ('2025-11-20', '10:15:00', '12:15:00'),
  ('2025-11-21', '14:00:00', '16:00:00'),
  ('2025-11-22', '08:00:00', '10:00:00'),
  ('2025-11-22', '10:20:00', '12:20:00'),
  ('2025-11-22', '13:30:00', '15:30:00'),
  ('2025-11-23', '09:00:00', '11:00:00'),
  ('2025-11-23', '11:15:00', '13:15:00'),
  ('2025-11-24', '14:00:00', '16:00:00'),
  ('2025-11-24', '16:15:00', '18:15:00'),
  ('2025-11-25', '09:00:00', '11:00:00'),
  ('2025-11-25', '11:15:00', '13:15:00');

-- Cours
INSERT INTO Cours (nom, enseignant_id, salle_id, horaire_id) VALUES
  ('Mathématiques', 2, 1, 1),
  ('Informatique', 3, 2, 2),
  ('Physique', 4, 3, 3),
  ('Chimie', 5, 4, 4),
  ('Biologie', 6, 5, 5),
  ('Programmation Java', 7, 6, 6),
  ('Analyse de Données', 3, 7, 7),
  ('Gestion de Projets', 2, 3, 8),
  ('Communication', 2, 2, 9),
  ('Intelligence Artificielle', 2, 1, 10),
  ('Electronique', 4, 8, 11),
  ('Anglais', 5, 9, 12);

-- Cours_Etudiant
INSERT INTO Cours_Etudiant (cours_id, etudiant_id) VALUES (1, 8), (2, 8), (3, 9), (4, 9), (5, 8), (4, 10), (5, 10), (6, 11), (7, 12), (8, 10), (9, 11), (10, 12), (11, 13), (12, 14), (1, 13), (2, 14), (3, 13), (4, 14), (5, 13), (6, 14), (7, 13), (8, 14), (9, 13), (10, 14), (11, 12), (12, 11), (1, 12), (2, 11), (3, 12), (4, 11), (5, 12), (8, 11), (9, 12), (10, 11), (11, 14), (12, 13), (1, 14), (2, 13), (3, 14), (4, 13), (5, 14), (6, 13), (7, 14), (8, 13), (9, 14), (10, 13);


-- EmploiDuTemps
INSERT INTO EmploiDuTemps (utilisateur_id) VALUES
  (8), (9), (10), (11), (12), (13), (14);

-- EmploiDuTemps_Cours
INSERT INTO EmploiDuTemps_Cours (emploiDuTemps_id, cours_id) VALUES
  (1, 1), (1, 2), (2, 3), (2, 4), (1, 5),
  (3, 4), (3, 5), (4, 6), (5, 7), (3, 8),
  (4, 9), (5, 10), (6, 11), (7, 12);

-- Notifications
INSERT INTO Notification (message, destinataire_id, dateEnvoi, expediteur_id) VALUES
  ('Votre cours de Mathématiques a été déplacé.', 8, '2025-11-19 10:00:00', 1),
  ("Votre cours d\'Informatique est confirmé.", 8, '2025-11-19 11:00:00', 1),
  ('Votre cours de Physique est annulé.', 9, '2025-11-20 08:00:00', 1),
  ("Nouveau cours d\'Electronique ajouté.", 13, '2025-11-21 09:00:00', 1),
  ('Votre emploi du temps a été mis à jour.', 14, '2025-11-21 10:00:00', 1);
