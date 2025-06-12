CREATE TABLE Secouriste (
    id BIGINT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    dateNaissance VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    tel VARCHAR(50) NOT NULL,
    adresse VARCHAR(50) NOT NULL
);

CREATE TABLE Competence (
    intitule VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Journee (
    jour INT,
    mois INT,
    annee INT,
    PRIMARY KEY (jour, mois, annee)
);

CREATE TABLE Site (
    code VARCHAR(50) PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    longitude FLOAT NOT NULL,
    latitude FLOAT NOT NULL
);

CREATE TABLE Sport (
    code VARCHAR(50) PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);

CREATE TABLE DPS (
    id BIGINT PRIMARY KEY,
    horaire_depart TIME NOT NULL,
    horaire_fin TIME NOT NULL,
    leSite VARCHAR(50) NOT NULL,
    leSport VARCHAR(50) NOT NULL,
    jour INT NOT NULL,
    mois INT NOT NULL,
    annee INT NOT NULL,
    FOREIGN KEY (leSite) REFERENCES Site(code),
    FOREIGN KEY (leSport) REFERENCES Sport(code),
    FOREIGN KEY (jour, mois, annee) REFERENCES Journee(jour, mois, annee)
);

CREATE TABLE Besoin (
    intituleComp VARCHAR(50),
    idDPS BIGINT,
    nombre INT NOT NULL,
    PRIMARY KEY (intituleComp, idDPS),
    FOREIGN KEY (intituleComp) REFERENCES Competence(intitule),
    FOREIGN KEY (idDPS) REFERENCES DPS(id)
);

CREATE TABLE Possede (
    idSecouriste BIGINT NOT NULL,
    competence VARCHAR(255) NOT NULL,
    PRIMARY KEY (idSecouriste, competence), -- Clé primaire composite
    FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id),
    FOREIGN KEY (competence) REFERENCES Competence(intitule)
);

CREATE TABLE Disponibilite (
    idSecouriste BIGINT NOT NULL,
    jour INT NOT NULL,
    mois INT NOT NULL,
    annee INT NOT NULL,
    PRIMARY KEY (idSecouriste, jour, mois, annee), -- Clé primaire composite
    FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id),
    FOREIGN KEY (jour, mois, annee) REFERENCES Journee(jour, mois, annee)
);

CREATE TABLE Affectation (
    idSecouriste BIGINT NOT NULL,
    idDps BIGINT NOT NULL,
    PRIMARY KEY (idSecouriste, idDps), -- Clé primaire composite
    FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id),
    FOREIGN KEY (idDps) REFERENCES DPS(id)
);
