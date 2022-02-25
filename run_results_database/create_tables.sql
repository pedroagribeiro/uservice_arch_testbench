CREATE TABLE results (
    run                     integer PRIMARY KEY NOT NULL,
    avg_time_total          double precision    NOT NULL,
    avg_time_broker_queue   double precision    NOT NULL,
    avg_time_worker_queue   double precision    NOT NULL,
    avg_time_olt_queue      double precision    NOT NULL,
    olts                    integer             NOT NULL,
    workers                 integer             NOT NULL,
    requests                integer             NOT NULL,
    timedout                double precision    NOT NULL 
);
