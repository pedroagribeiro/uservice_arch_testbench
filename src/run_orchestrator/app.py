from flask import Flask, make_response, request, json
from marshmallow import Schema, fields, ValidationError
import pika
import sqlalchemy as db
from sqlalchemy import text
import argparse
import time
import logging
from flasgger import Swagger

OrchestrationSchema = Schema.from_dict(
    {
        "olts": fields.Int(),
        "messages": fields.Int(),
        "workers": fields.Int(),
        "algorithm": fields.Int()
    }
) 

def define_environment():
    parser = argparse.ArgumentParser(description="Run orchestrator")
    parser.add_argument('--containerized', dest='containerized', action='store_true')
    parser.add_argument('--no-containerized', dest='containerized', action='store_false')
    parser.set_defaults(containerized=True)
    args = parser.parse_args()
    return args.containerized

def create_app_and_queue_connection(containerized_environment):
    app = Flask(
        __name__,
        static_url_path="",
        static_folder="/static"
    )
    app.config['SWAGGER'] = {
        'title': 'Orchestrator API Documentation'
    }
    app.config["CACHE_TYPE"] = "null"
    orchestration_queue_host = "orch-queue" if (containerized_environment == True) else "localhost" 
    orchestration_queue_port = 5672 if (containerized_environment == True) else 5679
    run_results_relational_db_host = "run-results-db" if (containerized_environment == True) else "localhost"
    run_results_relational_db_port = 5432 if (containerized_environment == True) else 5432
    while True:
        try:
            # print("trying to connect to the orchestration queue at " + orchestration_queue_host + ":" + str(orchestration_queue_port))
            logging.info("trying to connect to the orchestration queue at " + orchestration_queue_host + ":" + str(orchestration_queue_port))
            time.sleep(3)
            connection = pika.BlockingConnection(pika.ConnectionParameters(orchestration_queue_host, orchestration_queue_port, heartbeat=0))
            channel = connection.channel()
            channel.queue_declare(queue="orchestration")
        except Exception:
            pass
        else:
            break
    engine = db.create_engine("postgresql://postgres:postgres@" + run_results_relational_db_host + ":" + str(run_results_relational_db_port) + "/results")
    logging.info("all connections are completed")
    swagger = Swagger(app)
    return app, swagger, channel, engine

run_identifier = 0
containerized_environment = define_environment()
app, swagger, channel, engine = create_app_and_queue_connection(containerized_environment)

@app.route('/ping', methods = ['GET'])
def ping():
    """Serve para perceber se o servi??o est?? a correr e dispon??vel.
    ---
    responses:
        200:
            description: O servi??o est?? a correr.
    """
    return make_response("I'm alive!", 200)

@app.route("/orchestration", methods = ['POST'])
def new_orchestration():
    """Serve para introduzir no ambiente uma nova ordem de simula????o.
    ---
    parameters:
        - in: body
          name: orchestration
          description: Cont??m a informa????o necess??ria para uma nova simula????o.
          schema:
            $ref: '#/definitions/Orchestration'
    definitions:
        Orchestration:
            type: object
            properties:
                olts:
                    type: number
                messages:
                    type: number
                workers:
                    type: number
                algorithm:
                    type: number
    responses:
        200:
            description: A nova ordem de simula????o foi incorporada no sistema com sucesso.
        400:
            description: O objeto da ordem de simula????o n??o tinha o formato esperado. 
    """
    global run_identifier
    data = request.get_json()
    try:
        OrchestrationSchema().load(data)
    except ValidationError as err:
        return make_response(err.messages, 400)
    data['id'] = run_identifier 
    run_identifier += 1
    channel.basic_publish(exchange="", routing_key="orchestration", body=json.dumps(data))
    return make_response("Your orchestration request was published to the job queue. The run id is: " + str(run_identifier - 1), 201)

@app.route("/results", methods = ['GET'])
def get_run_results():
    """Serve para obter o resultado de todas as simula????es realizadas.
    ---
    definitions:
        RunResult:
            type: object
            properties:
                run:
                    type: number
                algorithm:
                    type: number
                avg_time_total:
                    type: number
                avg_time_broker_queue:
                    type: number
                avg_time_worker_queue:
                    type: number
                avg_time_olt_queue:
                    type: number
                olts:
                    type: number
                workers:
                    type: number
                requests:
                    type: number
                timedout:
                    type: number
                status:
                    type: string
    responses:
        200:
            description: Os resultados foram obtidos com sucesso.
            schema:
                type: array
                items: 
                    $ref: '#/definitions/RunResult'
    """
    results = engine.execute(text("select * from results"))
    rows = [dict(row) for row in results.fetchall()]
    return make_response(str(rows), 200)

@app.route("/results/<int:run_id>", methods = ['GET'])
def get_run_result(run_id):
    """
    Permite obter o resultado de uma simula????o da qual se tem o identificador.
    ---
    definitions:
        RunResult:
            type: object
            properties:
                run:
                    type: number
                algorithm:
                    type: number
                avg_time_total:
                    type: number
                avg_time_broker_queue:
                    type: number
                avg_time_worker_queue:
                    type: number
                avg_time_olt_queue:
                    type: number
                olts:
                    type: number
                workers:
                    type: number
                requests:
                    type: number
                timedout:
                    type: number
                status:
                    type: string
    parameters:
        - name: run_id
          in: path
          type: number
          required: true
    responses:
        200:
            description: O resultado da simula????o com o identificador fornecido foi encontrado.
            schema:
                $ref: '#/definitions/RunResult'
        400:
            description: O identificador n??o foi fornecido ou n??o corresponde a uma simula????o que foi iniciada.
    """
    results = engine.execute(text("select * from results where run = " + str(run_id)))
    rows = [dict(row) for row in results.fetchall()]
    result = rows[0]
    if result['status'] == 'waiting_to_start':
        return make_response("The run is still waiting to start so the results are not ready", 200)
    if result['status'] == 'on_going':
        return make_response("The run is still being performed so the results are not ready", 200)
    if result['status'] == 'completed':
        return make_response(str(result), 200)
    return make_response(str(rows[0]), 200)

app.run(debug = True, host = '0.0.0.0', port = 8000)
