# Scaling em ambiente Kubernetes

## Auto-escalamento horizonal - Pods

Neste contexto escalamento horizontal significa que a resposta ao aumento na
carga sobre o sistema é o _deploy_ de mais _pods_.

O _HorizontalPodAutoscaler_ controla a escala do _Deployment_ e o _ReplicaSet_.
O _loop_ de controlo deste processo corre intermitentemente a cada 15 segundos,
sendo que este intervalo de tempo pode ser alterado.

## Tutorial

https://www.youtube.com/watch?v=iodq-4srXA8&ab_channel=AntonPutra

## Processo

1. Pensar na métrica que queremos utilizar para controlar o scaling
2. Temos que configurar o Prometheus
3. Comunicar a comunicação da métrica ao Prometheus
4. Configurar a comunicação entre o Prometheus e o Metris Controller do Kubernetes
5. Configurar o HorizontalPodAutoscaler para utilizar essa métrica como controlo do escalamento

## A nossa métrica

1. Tempo médio que cada pedido demora a ser atendido.
2. % de timeouts no último minuto.

A segunda métrica parece-me que é a que faz mais sentido uma vez que no
Whiteboard temos que o objetivo primordial é garantir um _rate_ tal que a
probabilidade de ocorrência de _timeouts_ é mínima.
