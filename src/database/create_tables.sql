CREATE TABLE messages (
    id                              integer PRIMARY KEY NOT NULL UNIQUE,
    olt                             varchar(255)        NOT NULL,
    issued_at                       double precision    NOT NULL,
    worker                          integer                     ,
    started_processing              double precision            ,
    completed_processing            double precision            ,
    successful                      boolean                     ,
    minimum_theoretical_duration    double precision            ,
    yellow_requests                 integer                     ,
    red_requests                    integer                     
);

CREATE TABLE responses (
    id                      varchar(255) PRIMARY KEY NOT NULL UNIQUE,
    status                  integer                          , 
    request_enqueued_at_olt double precision                 ,
    request_dequeued_at_olt double precision                 ,
    started_handling        double precision                 ,
    ended_handling          double precision                 ,
    timedout                boolean                           
);

CREATE TABLE olt_requests (
    id                              varchar(255) PRIMARY KEY NOT NULL UNIQUE,
    issued_at                       double precision         NOT NULL,
    duration                        double precision         NOT NULL,
    timeout                         double precision         NOT NULL,
    left_worker                     double precision                 ,
    started_being_processed_at_olt  double precision                 ,
    ended_being_processed_at_olt    double precision                 ,
    returned_worker                 double precision                 ,
    completed                       double precision                 ,
    not_processed                   boolean                          ,
    origin_message_id               integer                  NOT NULL,
    response_id                     varchar(255)                     ,
    CONSTRAINT fk_origin_message FOREIGN KEY(origin_message_id) REFERENCES messages(id), 
    CONSTRAINT fk_response FOREIGN KEY(response_id) REFERENCES responses(id)
);

CREATE TABLE results (
    id                                      integer PRIMARY KEY NOT NULL UNIQUE,
    algorithm                               integer             NOT NULL,
    sequence                                integer             NOT NULL,
    theoretical_total_time_limit            double precision            ,
    verified_total_time                     double precision            ,
    theoretical_timedout_requests_limit     integer                     ,
    verified_timedout_requests              integer                     , 
    start_instant                           double precision            ,
    end_instant                             double precision            ,
    olts                                    integer             NOT NULL,
    workers                                 integer             NOT NULL,
    requests                                integer             NOT NULL,
    status                                  varchar(255)        NOT NULL
);

CREATE TABLE per_olt_processing_times (
    id                          integer PRIMARY KEY NOT NULL UNIQUE,
    run_id                      integer             NOT NULL       ,
    olt                         varchar(255)        NOT NULL       ,
    minimum_processing_time     double precision    NOT NULL       ,
    maximum_processing_time     double precision    NOT NULL       ,
    average_processing_time      double precision    NOT NULL       ,
    CONSTRAINT fk_run FOREIGN KEY(run_id) REFERENCES results(id) 
);

CREATE SEQUENCE message_id_seq START 1;
CREATE SEQUENCE result_id_seq START 1;
CREATE SEQUENCE per_olt_processing_time_id_seq START 1;