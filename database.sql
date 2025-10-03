CREATE DATABASE IF NOT EXISTS bank;
USE bank;

CREATE TABLE Client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20) NOT NULL
);

CREATE TABLE Carte (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(16) UNIQUE NOT NULL,
    dateExpiration DATE NOT NULL,
    statut ENUM('ACTIVE', 'SUSPENDUE', 'BLOQUEE') NOT NULL DEFAULT 'ACTIVE',
    typeCarte ENUM('DEBIT', 'CREDIT', 'PREPAYEE') NOT NULL,
    plafondJournalier DECIMAL(10, 2),
    plafondMensuel DECIMAL(10, 2),
    tauxInteret DECIMAL(5, 2),
    soldeDisponible DECIMAL(10, 2),
    idClient INT NOT NULL,
    FOREIGN KEY (idClient) REFERENCES Client(id) ON DELETE CASCADE
);

CREATE TABLE OperationCarte (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dateOperation DATETIME NOT NULL,
    montant DECIMAL(10, 2) NOT NULL,
    type ENUM('ACHAT', 'RETRAIT', 'PAIEMENTENLIGNE') NOT NULL,
    lieu VARCHAR(200) NOT NULL,
    idCarte INT NOT NULL,
    FOREIGN KEY (idCarte) REFERENCES Carte(id) ON DELETE CASCADE
);

CREATE TABLE AlerteFraude (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    niveau ENUM('INFO', 'AVERTISSEMENT', 'CRITIQUE') NOT NULL,
    dateAlerte DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idCarte INT NOT NULL,
    FOREIGN KEY (idCarte) REFERENCES Carte(id) ON DELETE CASCADE
);

INSERT INTO Client (nom, email, telephone) VALUES
('Ahmed Alami', 'ahmed.alami@email.com', '0612345678'),
('Fatima Zohra', 'fatima.zohra@email.com', '0623456789'),
('Youssef Bennani', 'youssef.bennani@email.com', '0634567890');

INSERT INTO Carte (numero, dateExpiration, statut, typeCarte, plafondJournalier, idClient) VALUES
('1234567890123456', '2026-12-31', 'ACTIVE', 'DEBIT', 5000.00, 1);

INSERT INTO Carte (numero, dateExpiration, statut, typeCarte, plafondMensuel, tauxInteret, idClient) VALUES
('2345678901234567', '2027-06-30', 'ACTIVE', 'CREDIT', 20000.00, 1.5, 2);

INSERT INTO Carte (numero, dateExpiration, statut, typeCarte, soldeDisponible, idClient) VALUES
('3456789012345678', '2025-12-31', 'ACTIVE', 'PREPAYEE', 1500.00, 3);
