from flask import Flask, make_response, request, json
from marshmallow import Schema, fields, ValidationError
import pika
import sqlalchemy as db
from sqlalchemy import text
import argparse
import time

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
    app.config["CACHE_TYPE"] = "null"
    orchestration_queue_host = "orch-queue" if (containerized_environment == True) else "localhost" 
    orchestration_queue_port = 5672 if (containerized_environment == True) else 5679
    run_results_relational_db_host = "run-results-db" if (containerized_environment == True) else "localhost"
    run_results_relational_db_port = 5432 if (containerized_environment == True) else 5432
    while True:
        try:
            print("trying to connect to the orchestration queue at " + orchestration_queue_host + ":" + str(orchestration_queue_port))
            time.sleep(4)
            connection = pika.BlockingConnection(pika.ConnectionParameters(orchestration_queue_host, orchestration_queue_port, heartbeat=0))
            channel = connection.channel()
            channel.queue_declare(queue="orchestration")
        except Exception:
            pass
        else:
            break
    engine = db.create_engine("postgresql://postgres:postgres@" + run_results_relational_db_host + ":" + str(run_results_relational_db_port) + "/results")
    return app, channel, engine

containerized_environment = define_environment()
app, channel, engine = create_app_and_queue_connection(containerized_environment)

@app.route('/ping', methods = ['GET'])
def ping():
    return make_response("I'm alive!", 200)

@app.route("/orchestration", methods = ['POST'])
def new_orchestration():
    data = request.get_json()
    try:
        OrchestrationSchema().load(data)
    except ValidationError as err:
        return make_response(err.messages, 400)
    channel.basic_publish(exchange="", routing_key="orchestration", body=json.dumps(data))
    return make_response("Your orchestration request was published to the job queue", 201)

@app.route("/results", methods = ['GET'])
def get_run_results():
    results = engine.execute(text("select * from results"))
    rows = [dict(row) for row in results.fetchall()]
    return make_response(str(rows), 200)

app.run(debug = True, host = '0.0.0.0', port = 5000)
