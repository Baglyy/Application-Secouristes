-- Désactiver temporairement les vérifications de clés étrangères pour faciliter le remplissage
-- (Utile si on exécute plusieurs fois le script ou si l'ordre n'est pas parfait au début)
-- Mais pour un script de remplissage initial, il est bon de les laisser actives pour attraper les erreurs.
-- SET FOREIGN_KEY_CHECKS=0;

-- 1. Remplissage de la table Competence
INSERT INTO Competence (intitule) VALUES
('PSE1'),          -- Premiers Secours en Équipe de niveau 1
('PSE2'),          -- Premiers Secours en Équipe de niveau 2
('Chef de Poste'),
('Conduite VPSP'), -- Conduite de Véhicule de Premiers Secours à Personnes
('Logistique'),
('Communication Radio');

-- 2. Remplissage de la table Secouriste
INSERT INTO Secouriste (id, nom, prenom, dateNaissance, email, tel, adresse) VALUES
(1, 'Dupont', 'Jean', '1990-05-15', 'jean.dupont@email.com', '0601020304', '1 rue de la Paix, Paris'),
(2, 'Martin', 'Sophie', '1995-11-20', 'sophie.martin@email.com', '0611223344', '10 avenue des Fleurs, Lyon'),
(3, 'Bernard', 'Luc', '1988-02-10', 'luc.bernard@email.com', '0655667788', '5 chemin du Moulin, Marseille'),
(4, 'Petit', 'Alice', '1998-07-01', 'alice.petit@email.com', '0612345678', '2 boulevard de la Liberté, Lille'),
(5, 'Durand', 'Paul', '1992-09-25', 'paul.durand@email.com', '0698765432', '7 place du Marché, Toulouse');

-- 3. Remplissage de la table Possede (liaison Secouriste - Competence)
INSERT INTO Possede (idSecouriste, competence) VALUES
(1, 'PSE2'),
(1, 'Chef de Poste'),
(1, 'Conduite VPSP'),
(2, 'PSE1'),
(2, 'Communication Radio'),
(3, 'PSE2'),
(3, 'Logistique'),
(4, 'PSE1'),
(5, 'PSE2'),
(5, 'Conduite VPSP');

-- 4. Remplissage de la table Journee
INSERT INTO Journee (jour, mois, annee) VALUES
(15, 7, 2024),
(16, 7, 2024),
(20, 8, 2024),
(21, 8, 2024),
(5, 9, 2024);

-- 5. Remplissage de la table Disponibilite (liaison Secouriste - Journee)
INSERT INTO Disponibilite (idSecouriste, jour, mois, annee) VALUES
(1, 15, 7, 2024),
(1, 16, 7, 2024),
(2, 15, 7, 2024),
(3, 20, 8, 2024),
(3, 21, 8, 2024),
(4, 15, 7, 2024),
(4, 5, 9, 2024),
(5, 16, 7, 2024),
(5, 20, 8, 2024);

-- 6. Remplissage de la table Site
INSERT INTO Site (code, nom, longitude, latitude) VALUES
('STAD01', 'Stade Municipal', 2.3488, 48.8534),    -- Paris (approximatif)
('PARC01', 'Parc de la Tête dOr', 4.8526, 45.7808), -- Lyon (approximatif)
('PLAG01', 'Plage du Prado', 5.3699, 43.2590);     -- Marseille (approximatif)

-- 7. Remplissage de la table Sport
INSERT INTO Sport (code, nom) VALUES
('FOOT', 'Football'),
('ATHLE', 'Athlétisme'),
('VOLLEY', 'Volley-ball de plage'),
('CONCERT', 'Concert en plein air');

-- 8. Remplissage de la table DPS
INSERT INTO DPS (id, horaire_depart, horaire_fin, leSite, leSport, jour, mois, annee) VALUES
(101, '14:00:00', '18:00:00', 'STAD01', 'FOOT', 15, 7, 2024),
(102, '09:00:00', '17:00:00', 'PARC01', 'ATHLE', 16, 7, 2024),
(103, '10:00:00', '22:00:00', 'PLAG01', 'CONCERT', 20, 8, 2024),
(104, '13:00:00', '19:00:00', 'PLAG01', 'VOLLEY', 21, 8, 2024);

-- 9. Remplissage de la table Besoin (liaison Competence - DPS avec nombre)
INSERT INTO Besoin (intituleComp, idDPS, nombre) VALUES
('PSE1', 101, 2),          -- Pour DPS 101, besoin de 2 PSE1
('Chef de Poste', 101, 1), -- Pour DPS 101, besoin de 1 Chef de Poste
('PSE2', 102, 3),          -- Pour DPS 102, besoin de 3 PSE2
('Conduite VPSP', 102, 1), -- Pour DPS 102, besoin de 1 Conducteur
('Communication Radio', 102, 1),
('PSE1', 103, 4),
('PSE2', 103, 2),
('Chef de Poste', 103, 1),
('Logistique', 103, 1),
('PSE1', 104, 2);

-- 10. Remplissage de la table Affectation (liaison Secouriste - DPS)
-- Il faut s'assurer que les secouristes affectés sont disponibles
-- et qu'ils ont potentiellement les compétences requises (non vérifié par FK ici, mais logiquement important)
INSERT INTO Affectation (idSecouriste, idDps) VALUES
-- Affectations pour DPS 101 (15/07/2024)
-- Secouriste 1 (PSE2, ChefP, Conduite) est dispo
-- Secouriste 2 (PSE1, ComRad) est dispo
-- Secouriste 4 (PSE1) est dispo
(1, 101), -- Jean Dupont (Chef de Poste)
(2, 101), -- Sophie Martin (PSE1)
(4, 101), -- Alice Petit (PSE1)

-- Affectations pour DPS 102 (16/07/2024)
-- Secouriste 1 (PSE2, ChefP, Conduite) est dispo
-- Secouriste 5 (PSE2, Conduite) est dispo
(1, 102), -- Jean Dupont (PSE2, Conducteur)
(5, 102), -- Paul Durand (PSE2)
-- On pourrait vouloir ajouter un autre PSE2 si besoin, mais pas d'autre secouriste dispo avec PSE2 ce jour-là dans cet exemple

-- Affectations pour DPS 103 (20/08/2024)
-- Secouriste 3 (PSE2, Log) est dispo
-- Secouriste 5 (PSE2, Conduite) est dispo
(3, 103), -- Luc Bernard (PSE2, Logistique)
(5, 103); -- Paul Durand (PSE2)
-- Il manquerait des PSE1 et un Chef de Poste pour ce DPS selon les besoins définis

-- Réactiver les vérifications si elles avaient été désactivées
-- SET FOREIGN_KEY_CHECKS=1;
