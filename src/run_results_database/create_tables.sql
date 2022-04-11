CREATE TABLE results (
    run                     integer PRIMARY KEY NOT NULL UNIQUE,
    algorithm               integer             NOT NULL,
    avg_time_total          double precision            ,
    avg_time_broker_queue   double precision            ,
    avg_time_worker_queue   double precision            ,
    avg_time_olt_queue      double precision            ,
    start_instant           double precision            ,
    end_instant             double precision            ,
    avg_time_total_2        double precision            ,
    olts                    integer             NOT NULL,
    workers                 integer             NOT NULL,
    requests                integer             NOT NULL,
    timedout                double precision            ,
    status                  varchar(255)        NOT NULL 
);

CREATE TABLE messages (
    id          integer PRIMARY KEY NOT NULL,
    olt         varchar(255)        NOT NULL,
    issued_at   double precision    NOT NULL,
    worker      integer             NOT NULL,
    completed   double precision    NOT NULL
)

CREATE TABLE messages (
    id                      integer PRIMARY KEY NOT NULL UNIQUE,
    olt                     varchar(255)        NOT NULL,
    issued_at               double precision    NOT NULL,
    worker                  integer             NOT NULL,
    completed               double precision    NOT NULL
);

CREATE TABLE olt_requests (
    id                  integer PRIMARY KEY NOT NULL,
    origin_message_id   integer             NOT NULL,
    issued_at           double precision    NOT NULL,
    duration            double precision    NOT NULL,
    timedout            double precision    NOT NULL,
    enqueued_at_olt     double precision    NOT NULL,
    dequeued_at_olt     double precision    NOT NULL,
    completed           double precision    NOT NULL
)

CREATE TABLE responses (
    id                      integer PRIMARY KEY NOT NULL UNIQUE,
    status                  integer             NOT NULL,
    started_handling        double precision    NOT NULL,
    ended_handling          double precision    NOT NULL,
    origin_request_id       integer             NOT NULL,
    timedout                boolean             NOT NULL
);

CREATE SEQUENCE result_id_seq START 1;
CREATE SEQUENCE response_id_seq START 1;
CREATE SEQUENCE olt_request_id_seq START 1;