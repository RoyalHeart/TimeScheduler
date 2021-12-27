drop table TISCH_USER;
drop table EVENT;
drop table ADMINISTRATOR;
drop sequence USER_SEQUENCE;
drop TRIGGER user_bir;

CREATE TABLE TISCH_USER(
    -- expected to have up to 1 000 000 users
    id              CHAR(6), 
    username        VARCHAR2(30) UNIQUE not NULL,
    password        RAW(64) not null,
    uName           VARCHAR2(64) not null,
    PhoneNumber     VARCHAR2(20),
    Email           VARCHAR2(128) not null,
    constraint user_id_username_pk primary key (id)
);

CREATE TABLE EVENT(
    -- expected to have up to 1 000 000 000 events(each user can have up to 1 000 events)
    id              CHAR(9), 
    eventName       VARCHAR2(60) not null,
    eventContent    VARCHAR2(512),
    eventDate       DATE not null,
    constraint event_id_pk primary key (id)
);

CREATE TABLE ADMINISTRATOR(
    ID CHAR(9) not null,
    constraint admin_id_pk primary key (id)
);

CREATE SEQUENCE USER_SEQUENCE START WITH 1;

CREATE OR REPLACE TRIGGER user_bir 
BEFORE INSERT ON TISCH_USER 
FOR EACH ROW
BEGIN
  SELECT USER_SEQUENCE.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;