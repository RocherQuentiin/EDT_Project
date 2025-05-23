CREATE DATABASE IF NOT EXISTS edt_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE edt_db;

CREATE USER IF NOT EXISTS 'edt_user'@'localhost' IDENTIFIED BY 'edt_password';
GRANT ALL PRIVILEGES ON edt_db.* TO 'edt_user'@'localhost';

CREATE TABLE Utilisateur (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP);

CREATE TABLE TypeUtilisateur (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(20) UNIQUE NOT NULL -- 'Administrateur', 'Enseignant', 'Etudiant'
);

ALTER TABLE Utilisateur
    ADD COLUMN type_utilisateur_id INT,
    ADD FOREIGN KEY (type_utilisateur_id) REFERENCES TypeUtilisateur(id) ON DELETE CASCADE;


CREATE TABLE Etudiant (
    id INT PRIMARY KEY,
    emploiDuTemps_id INT,
    FOREIGN KEY (id) REFERENCES Utilisateur(id) ON DELETE CASCADE
    -- emploiDuTemps_id référencé plus bas
);

CREATE TABLE Salle (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_salle VARCHAR(20) NOT NULL,
    capacite INT NOT NULL,
    localisation VARCHAR(100)
);

CREATE TABLE Equipement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    numero_equipement VARCHAR(50) NOT NULL,
    type VARCHAR(50),
    stock INT NOT NULL
);

CREATE TABLE Salle_Equipement (
    salle_id INT,
    equipement_id INT,
    PRIMARY KEY (salle_id, equipement_id),
    FOREIGN KEY (salle_id) REFERENCES Salle(id) ON DELETE CASCADE,
    FOREIGN KEY (equipement_id) REFERENCES Equipement(id) ON DELETE CASCADE
);

CREATE TABLE Horaire (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    heureDebut TIME NOT NULL,
    heureFin TIME NOT NULL
);

CREATE TABLE Cours (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    enseignant_id INT,
    salle_id INT,
    horaire_id INT,
    FOREIGN KEY (enseignant_id) REFERENCES Utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (salle_id) REFERENCES Salle(id) ON DELETE CASCADE,
    FOREIGN KEY (horaire_id) REFERENCES Horaire(id) ON DELETE CASCADE
);

CREATE TABLE Cours_Etudiant (
    cours_id INT,
    etudiant_id INT,
    PRIMARY KEY (cours_id, etudiant_id),
    FOREIGN KEY (cours_id) REFERENCES Cours(id) ON DELETE CASCADE,
    FOREIGN KEY (etudiant_id) REFERENCES Etudiant(id) ON DELETE CASCADE
);

CREATE TABLE EmploiDuTemps (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utilisateur_id INT,
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

ALTER TABLE Etudiant
    ADD CONSTRAINT fk_emploiDuTemps
    FOREIGN KEY (emploiDuTemps_id) REFERENCES EmploiDuTemps(id) ON DELETE CASCADE;

CREATE TABLE EmploiDuTemps_Cours (
    emploiDuTemps_id INT,
    cours_id INT,
    PRIMARY KEY (emploiDuTemps_id, cours_id),
    FOREIGN KEY (emploiDuTemps_id) REFERENCES EmploiDuTemps(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES Cours(id) ON DELETE CASCADE
);

CREATE TABLE Notification (
    id INT PRIMARY KEY AUTO_INCREMENT,
    message TEXT NOT NULL,
    destinataire_id INT,
    dateEnvoi DATETIME,
    FOREIGN KEY (destinataire_id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

INSERT INTO TypeUtilisateur (nom) VALUES ('Administrateur'), ('Enseignant'), ('Etudiant');

ALTER TABLE Notification
    ADD COLUMN expediteur_id INT,
    ADD FOREIGN KEY (expediteur_id) REFERENCES Utilisateur(id) ON DELETE CASCADE;
