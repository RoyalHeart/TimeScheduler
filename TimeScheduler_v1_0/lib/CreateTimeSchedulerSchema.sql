drop table EVENT;
drop table TISCH_USER;
drop table ADMINISTRATOR;
drop sequence USER_SEQUENCE;
drop sequence EVENT_SEQUENCE;
drop TRIGGER user_bir;
drop TRIGGER event_bir;

CREATE TABLE TISCH_USER(
    -- expected to have up to 1 000 000 users
    id              CHAR(6), 
    username        VARCHAR2(30) UNIQUE not NULL,
    password        RAW(64) not null,
    userFullName    VARCHAR2(64) not null,
    userEmail       VARCHAR2(128) not null,
    userPhoneNumber VARCHAR2(25),
    constraint user_id_username_pk primary key (id)
);

INSERT INTO TISCH_USER VALUES ('0', 'admin', 'd82494f05d6917ba02f7aaa29689ccb444bb73f20380876cb05d1f37537b7892', 'admin', 'notification.tisch@gmail.com', '1744519780'); -- insert admin user

CREATE TABLE EVENT(
    -- expected to have up to 1 000 000 000 events(each user can have up to 1 000 events)
    id                  CHAR(9), 
    userId              CHAR(6),
    eventTitle          VARCHAR2(60) not null,
    eventDescription    VARCHAR2(512),
    eventDate           DATE not null,
    eventStartTime      DATE not null,
    eventLocation       VARCHAR2(128),
    eventDuration       INT not null,
    eventPriority       INT,
    eventRemind       INT,
    constraint event_id_userId_pk primary key (id, userId),
    constraint event_userId_fk foreign key (userId) references TISCH_USER(id)
);

CREATE TABLE ADMINISTRATOR(
    id CHAR(6) not null,
    constraint admin_id_pk primary key (id)
);

CREATE SEQUENCE USER_SEQUENCE MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER user_bir 
BEFORE INSERT ON TISCH_USER 
FOR EACH ROW
BEGIN
  SELECT USER_SEQUENCE.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE EVENT_SEQUENCE MINVALUE 1 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER event_bir 
BEFORE INSERT ON EVENT 
FOR EACH ROW
BEGIN
  SELECT EVENT_SEQUENCE.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/