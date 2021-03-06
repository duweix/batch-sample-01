CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_SEQ;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (ID BIGINT NOT NULL);
INSERT INTO BATCH_STEP_EXECUTION_SEQ values(0);
CREATE TABLE BATCH_JOB_EXECUTION_SEQ (ID BIGINT NOT NULL);
INSERT INTO BATCH_JOB_EXECUTION_SEQ values(0);
CREATE TABLE BATCH_JOB_SEQ (ID BIGINT NOT NULL);
INSERT INTO BATCH_JOB_SEQ values(0);

CREATE TABLE BATCH_JOB_INSTANCE  (
  JOB_INSTANCE_ID BIGINT  PRIMARY KEY ,
  VERSION BIGINT,
  JOB_NAME VARCHAR(100) NOT NULL ,
  JOB_KEY VARCHAR(2500)
);

CREATE TABLE BATCH_JOB_EXECUTION  (
  JOB_EXECUTION_ID BIGINT  PRIMARY KEY ,
  VERSION BIGINT,
  JOB_INSTANCE_ID BIGINT NOT NULL,
  CREATE_TIME TIMESTAMP NOT NULL,
  START_TIME TIMESTAMP DEFAULT NULL,
  END_TIME TIMESTAMP DEFAULT NULL,
  STATUS VARCHAR(10),
  EXIT_CODE VARCHAR(20),
  EXIT_MESSAGE VARCHAR(2500),
  LAST_UPDATED TIMESTAMP,
  JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
  constraint JOB_INSTANCE_EXECUTION_FK foreign key (JOB_INSTANCE_ID)
  references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

CREATE TABLE BATCH_STEP_EXECUTION  (
  STEP_EXECUTION_ID BIGINT  PRIMARY KEY ,
  VERSION BIGINT NOT NULL,
  STEP_NAME VARCHAR(100) NOT NULL,
  JOB_EXECUTION_ID BIGINT NOT NULL,
  START_TIME TIMESTAMP NOT NULL ,
  END_TIME TIMESTAMP DEFAULT NULL,
  STATUS VARCHAR(10),
  COMMIT_COUNT BIGINT ,
  READ_COUNT BIGINT ,
  FILTER_COUNT BIGINT ,
  WRITE_COUNT BIGINT ,
  READ_SKIP_COUNT BIGINT ,
  WRITE_SKIP_COUNT BIGINT ,
  PROCESS_SKIP_COUNT BIGINT ,
  ROLLBACK_COUNT BIGINT ,
  EXIT_CODE VARCHAR(20) ,
  EXIT_MESSAGE VARCHAR(2500) ,
  LAST_UPDATED TIMESTAMP,
  constraint JOB_EXECUTION_STEP_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
  JOB_EXECUTION_ID BIGINT PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT CLOB,
  constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
        JOB_EXECUTION_ID BIGINT NOT NULL ,
        TYPE_CD VARCHAR(6) NOT NULL ,
        KEY_NAME VARCHAR(100) NOT NULL ,
        STRING_VAL VARCHAR(250) ,
        DATE_VAL DATETIME DEFAULT NULL ,
        LONG_VAL BIGINT ,
        DOUBLE_VAL DOUBLE PRECISION ,
        IDENTIFYING CHAR(1) NOT NULL ,
        constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
  STEP_EXECUTION_ID BIGINT PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT CLOB,
  constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
  references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

create table users (
    id integer not null,
    primary key(id)
);

insert into users(id) values(1);
insert into users(id) values(2);
insert into users(id) values(3);
insert into users(id) values(4);
insert into users(id) values(5);
insert into users(id) values(6);
insert into users(id) values(7);
insert into users(id) values(8);
insert into users(id) values(9);
insert into users(id) values(10);

CREATE TABLE MULTI_EXEC_MGR (
    REQ_ID LONG NOT NULL,
    MULTI_PROC_NO LONG NOT NULL,
    PROC_TARGET VARCHAR(100) NOT NULL,
    PROC VARCHAR(100) NOT NULL,
    STATES VARCHAR(100) NOT NULL,
    ERR_INFO VARCHAR(500)
);

INSERT INTO MULTI_EXEC_MGR(REQ_ID, MULTI_PROC_NO, PROC_TARGET, PROC, STATES, ERR_INFO) VALUES(1, 1, '大阪', '一般方式金額確定', '受付', NULL);
INSERT INTO MULTI_EXEC_MGR(REQ_ID, MULTI_PROC_NO, PROC_TARGET, PROC, STATES, ERR_INFO) VALUES(1, 1, '〇〇', '△△', '受付', NULL);
INSERT INTO MULTI_EXEC_MGR(REQ_ID, MULTI_PROC_NO, PROC_TARGET, PROC, STATES, ERR_INFO) VALUES(1, 2, '〇〇', '□□', '受付', NULL);
INSERT INTO MULTI_EXEC_MGR(REQ_ID, MULTI_PROC_NO, PROC_TARGET, PROC, STATES, ERR_INFO) VALUES(1, 2, '●●', '◇◇', '受付', NULL);

