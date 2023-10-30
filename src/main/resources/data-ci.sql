-- Insitialisation de données pour les environnements de développement

insert into utilisateur (mail,nom,prenom) values ('jpetit@palo-it.com','Petit','Jean')
insert into utilisateur (mail,nom,prenom) values ('mlaval@palo-it.com','Laval','Marc')
insert into utilisateur (mail,nom,prenom) values ('pdurand@palo-it.com','Durand','Paul')

insert into formation (intitule) values ('TDD')

insert into session (formation_id, formateur_id, client, duree, etat)
    values (
        (select id from formation where intitule = 'TDD'),
        (select id from utilisateur where nom = 'Petit'),
        'SG', 2, 1
    );

insert into session_participant (session_id, participant_id)
    values (
        (select session_id from session),
        (select id from utilisateur where nom = 'Laval')
    );
insert into session_participant (session_id, participant_id)
    values (
        (select session_id from session),
        (select id from utilisateur where nom = 'Durand')
    );
