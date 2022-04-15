CREATE TABLE messages (
    id                              integer PRIMARY KEY NOT NULL UNIQUE,
    olt                             varchar(255)        NOT NULL,
    issued_at                       double precision    NOT NULL,
    worker                          integer                     ,
    completed                       double precision            ,
    successful                      boolean                     ,
    minimum_theoretical_duration    double precision            ,
    has_red_requests                boolean            
);

CREATE TABLE olt_requests (
    id                  varchar(255) PRIMARY KEY NOT NULL UNIQUE,
    issued_at           double precision         NOT NULL,
    duration            double precision         NOT NULL,
    timeout             double precision         NOT NULL,
    enqueued_at_olt     double precision                 ,
    dequeued_at_olt     double precision                 , 
    completed           double precision                 ,
    origin_message_id  integer                  NOT NULL,
    CONSTRAINT fk_origin_message FOREIGN KEY(origin_message_id) REFERENCES messages(id) 
);

CREATE TABLE responses (
    id                      varchar(255) PRIMARY KEY NOT NULL UNIQUE,
    status                  integer                          , 
    started_handling        double precision                 ,
    ended_handling          double precision                 ,
    timedout                boolean                          , 
    origin_request_id       varchar(255)             NOT NULL,
    CONSTRAINT fk_origin_request FOREIGN KEY(origin_request_id) REFERENCES olt_requests(id)
);

CREATE TABLE results (
    id                                      integer PRIMARY KEY NOT NULL UNIQUE,
    algorithm                               integer             NOT NULL,
    theoretical_total_time_limit            double precision            ,
    verified_total_time                     double precision            ,
    theoretical_timedout_requests_limit     integer                     ,
    verified_timedout_requests              integer                     , 
    start_instant                           double precision    NOT NULL,
    end_instant                             double precision            ,
    olts                                    integer             NOT NULL,
    workers                                 integer             NOT NULL,
    requests                                integer             NOT NULL,
    status                                  varchar(255)        NOT NULL
);

CREATE SEQUENCE message_id_seq START 1;
CREATE SEQUENCE result_id_seq START 1;