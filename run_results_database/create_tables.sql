CREATE TABLE results (
    run                     integer PRIMARY KEY NOT NULL,
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
