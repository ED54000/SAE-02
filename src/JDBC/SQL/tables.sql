CREATE TABLE "ADRESSE" (
    "ID" NUMBER GENERATED BY DEFAULT AS IDENTITY,
    "ADRESSE" VARCHAR2(255) NOT NULL,
    PRIMARY KEY ("ID")
);

CREATE TABLE "RESTAU" (
    "ID" NUMBER GENERATED BY DEFAULT AS IDENTITY,
    "NOM" VARCHAR2(255) NOT NULL,
    "NUMERO" NUMBER NOT NULL,
    "ADRESSE" NUMBER NOT NULL,
    "LATITUDE" FLOAT,
    "LONGITUDE" FLOAT,
    "NBPLACES" INT NOT NULL,
    PRIMARY KEY ("ID")
);

ALTER TABLE "RESTAU"
    ADD CONSTRAINT "FK_ADRESSE"
    FOREIGN KEY ("ADRESSE") REFERENCES ADRESSE("ID");

CREATE TABLE "RESERV" (
    "ID" NUMBER GENERATED BY DEFAULT AS IDENTITY,
    "IDRESTAURANT" NUMBER NOT NULL,
    "NOM" VARCHAR2(100) NOT NULL,
    "PRENOM" VARCHAR2(100) NOT NULL,
    "NBCONVIVES" INT NOT NULL,
    "COORD_TEL" VARCHAR2(10) NOT NULL,
    PRIMARY KEY ("ID")
);

ALTER TABLE "RESERV" 
    ADD CONSTRAINT "FK_IDRESTAURANT"
    FOREIGN KEY ("IDRESTAURANT") REFERENCES RESTAU("ID");
