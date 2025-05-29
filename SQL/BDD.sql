/* Schéma relationnel : 
Secouriste(id (1) (NN), nom (NN), prenom (NN), dateNaissance (NN), email (NN), tel (NN), adresse (NN))
Competence(intitule (1))
Possede([idSecouriste = @Secouriste.id, intituleComp = @Competence.intitule] (1))
Journee([jour, mois, annee] (1))
Site(code (1), nom (NN), longitude (NN), latitude (NN))
Sport(code (1), nom (NN))
DPS(id (1), horaire_depart (NN), horaire_fin (NN), codeSite = @Site.code (NN), codeSport = @Sport.code (NN), [jour = @Journee.jour, mois = @Journee.mois, annee = @Journee.annee] (NN))
Besoin([intituleComp = @Competence.intitule, idDPS = @DPS.id] (1), nombre (NN))
EstAffecteA([idSecouriste = @Secouriste.id, intituleComp = @Competence.intitule, idDPS = @DPS.id] (1))
*/

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

CREATE TABLE Possede (
    idSecouriste BIGINT,
    intituleComp VARCHAR(50),
    PRIMARY KEY (idSecouriste, intituleComp),
    FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id),
    FOREIGN KEY (intituleComp) REFERENCES Competence(intitule)
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
    horaire_depart INT[2] NOT NULL,
    horaire_fin INT[2] NOT NULL,
    codeSite VARCHAR(50) NOT NULL,
    codeSport VARCHAR(50) NOT NULL,
    jour INT NOT NULL,
    mois INT NOT NULL,
    annee INT NOT NULL,
    FOREIGN KEY (codeSite) REFERENCES Site(code),
    FOREIGN KEY (codeSport) REFERENCES Sport(code),
    FOREIGN KEY (jour) REFERENCES Journee(jour),
    FOREIGN KEY (mois) REFERENCES Journee(mois),
    FOREIGN KEY (annee) REFERENCES Journee(annee)
);

CREATE TABLE Besoin (
    intituleComp VARCHAR(50),
    idDPS BIGINT,
    nombre INT NOT NULL,
    PRIMARY KEY (intituleComp, idDPS)
);

CREATE TABLE EstAffecteA (
    idSecouriste BIGINT,
    intituleComp VARCHAR(50),
    idDPS BIGINT,
    PRIMARY KEY (idSecouriste, intituleComp, idDPS)
);