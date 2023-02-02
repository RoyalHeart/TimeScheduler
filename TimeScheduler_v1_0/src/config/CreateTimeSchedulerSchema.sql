drop table EVENT_PARTICIPANT;
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
    userFullName    VARCHAR2(64),
    userEmail       VARCHAR2(128) not null,
    userPhoneNumber VARCHAR2(25),
    constraint user_id_username_pk primary key (id)
);

INSERT INTO TISCH_USER VALUES ('0', 'admin', 'D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892', 'admin', 'notification.tisch@gmail.com', '1744519780'); -- insert admin user

CREATE TABLE EVENT(
    -- expected to have up to 1 000 000 000 events(each user can have up to 1 000 events)
    id                  CHAR(9),
    userId              CHAR(6),
    eventTitle          VARCHAR2(60) not null,
    eventDescription    VARCHAR2(512),
    eventDate           DATE not null,
    eventRemind         DATE,
    eventLocation       VARCHAR2(128),
    eventDuration       INT not null,
    eventPriority       INT,
    constraint event_id_userId_pk primary key (id, userId),
    constraint event_userId_fk foreign key (userId) references TISCH_USER(id) ON DELETE CASCADE
);

CREATE TABLE ADMINISTRATOR(
    id CHAR(6) not null,
    constraint admin_id_pk primary key (id)
);

CREATE TABLE EVENT_PARTICIPANT(
    EVENT_ID  CHAR(9) not null,
    USER_ID   CHAR(6) not null,
    PARTICIPANT VARCHAR2(128) not null,
    constraint event_participant_eventId_userId_participant_pk primary key (EVENT_ID, USER_ID, PARTICIPANT),
    constraint event_participant_eventId_fk foreign key (EVENT_ID,USER_ID) references EVENT(id,userID) ON DELETE CASCADE
);
INSERT INTO ADMINISTRATOR VALUES ('0');

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
