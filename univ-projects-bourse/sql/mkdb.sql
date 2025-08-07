drop table if exists Offres cascade;
drop table if exists Bons cascade;
drop table if exists Marches cascade;
drop table if exists Roles cascade;
drop table if exists Utilisateurs cascade;

create table Utilisateurs (
	login varchar(20),
	mdp varchar(36) not null,
	nom varchar(30),
	prenom varchar(30),
	date_naissance date,
	solde integer,
	total_marches integer,
	marches_gagnant integer,
	gain integer,
	constraint PK_Utilisateurs primary key (login)
);

create table Roles (
	role char,
	login varchar(20),
	constraint PK_Roles primary key (role,login),
	foreign key (login) references Utilisateurs(login)
);

create table Marches (
	alias char(3),
	libelle_positif text,
	libelle_negatif text,
	date_fin date,
	createur varchar(20),
	constraint PK_Marches primary key (alias),
	foreign key (createur) references Utilisateurs(login)
);

create table Bons (
	idB serial,
	proprietaire varchar(20),
	quantite integer,
	marche char(3),
	positif boolean,
	constraint PK_Bons primary key (idB),
	foreign key (proprietaire) references Utilisateurs(login),
	foreign key (marche) references Marches(alias)
);

create table Offres (
	idO serial,
	passeur varchar(20),
	prix integer,
	achat boolean,
	symetrique boolean,
	quantite integer,
	marche char(3),
	positif boolean,
	constraint PK_Offres primary key (idO),
	foreign key (passeur) references Utilisateurs(login),
	foreign key (marche) references Marches(alias)
);
