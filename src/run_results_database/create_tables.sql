CREATE TABLE results (
    run                                     integer PRIMARY KEY NOT NULL UNIQUE,
    algorithm                               integer             NOT NULL,
    theoretical_total_time_limit            integer             NOT NULL,
    verified_total_time                     integer             NOT NULL,
    theoretical_timedout_requests_limit     integer             NOT NULL,
    verified_timedout_requests              integer             NOT NULL,
    start_instant                           double precision    NOT NULL,
    end_instant                             double precision    NOT NULL,
    olts                                    integer             NOT NULL,
    workers                                 integer             NOT NULL,
    requests                                integer             NOT NULL,
    status                                  varchar(255)        NOT NULL
);

CREATE TABLE messages (
    id                              integer PRIMARY KEY NOT NULL UNIQUE,
    olt                             varchar(255)        NOT NULL,
    issued_at                       double precision    NOT NULL,
    worker                          integer             NOT NULL,
    completed                       double precision    NOT NULL,
    successful                      boolean             NOT NULL,
    minimum_theoretical_duration    double precision    NOT NULL,
    has_red_requests                boolean             NOT NULL
);

CREATE TABLE olt_requests (
    id                  integer PRIMARY KEY NOT NULL,
    origin_message_id   integer             NOT NULL,
    issued_at           double precision    NOT NULL,
    duration            double precision    NOT NULL,
    timeout             double precision    NOT NULL,
    enqueued_at_olt     double precision    NOT NULL,
    dequeued_at_olt     double precision    NOT NULL,
    completed           double precision    NOT NULL
);

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
CREATE SEQUENCE message_id_seq START 1;