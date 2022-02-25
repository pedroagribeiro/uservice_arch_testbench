from flask import Flask, make_response, request, json
from marshmallow import Schema, fields, ValidationError
import pika
import os

OrchestrationSchema = Schema.from_dict(
    {
        "olts": fields.Int(),
        "messages": fields.Int(),
        "workers": fields.Int(),
        "algorithm": fields.Int()
    }
) 

def create_app_and_queue_connection():
    app = Flask(
        __name__,
        static_url_path="",
        static_folder="/static"
    )
    app.config["CACHE_TYPE"] = "null"
    while True:
        try:
            print("trying to connect to the orchestration queue")
            connection = pika.BlockingConnection(pika.ConnectionParameters("localhost", 5679, heartbeat=0))
            channel = connection.channel()
            channel.queue_declare(queue="orchestration")
        except Exception:
            pass
        else:
            break
    return app, channel

app, channel = create_app_and_queue_connection()

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

app.run(debug = True, host = '0.0.0.0', port = 8000)
