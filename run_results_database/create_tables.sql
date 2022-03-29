CREATE TABLE results (
    run                     integer PRIMARY KEY NOT NULL UNIQUE,
    algorithm               integer             NOT NULL,
    avg_time_total          double precision            ,
    avg_time_broker_queue   double precision            ,
    avg_time_worker_queue   double precision            ,
    avg_time_olt_queue      double precision            ,
    olts                    integer             NOT NULL,
    workers                 integer             NOT NULL,
    requests                integer             NOT NULL,
    timedout                double precision            ,
    status                  varchar(255)        NOT NULL 
);

CREATE TABLE messages (
    id                      integer PRIMARY KEY NOT NULL UNIQUE,
    olt                     varchar(255)        NOT NULL,
    processing_time         double precision    NOT NULL,
    timeout                 double precision    NOT NULL,
    issued_at               double precision    NOT NULL,
    worker                  integer             NOT NULL,
    enqueued_at_broker      double precision    NOT NULL,
    dequeued_at_broker      double precision    NOT NULL,
    enqueued_at_worker      double precision    NOT NULL,
    dequeued_at_worker      double precision    NOT NULL,
    enqueued_at_olt         double precision    NOT NULL,
    dequeued_at_olt         double precision    NOT NULL,
    completed               double precision    NOT NULL
);

CREATE TABLE responses (
    id                      integer PRIMARY KEY NOT NULL UNIQUE,
    status                  integer             NOT NULL,
    started_handling        double precision    NOT NULL,
    ended_handling          double precision    NOT NULL,
    origin_message_id       integer             NOT NULL,
    timedout                boolean             NOT NULL
);

CREATE SEQUENCE hibernate_sequence START 1;