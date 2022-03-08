# Análise de resultados obtidos nas simulações realizadas

Os testes foram realizados nas seguintes condições:

- o ambiente de simulação é constituído por: 1 _orchestrator_; 1 _broker_; 3
  _workers_; 3 _olts_.

- 3 _seeds_ no objeto _Random_ do **Java** para conferir variabilidade às
  sequências de mensagens que servem de _input_ ao ambiente de simulação.

- para cada um dos cenários foram realizados testes com 50, 100, 500, 1000 e
  2000 mensagens.

- foram utilizados como algoritmos de processamento de mensagens os quatro
  algoritmos que estão descritos no
  [_Wiki_](wiki.ptin.corppt.com/display/SMZTCONF/Projeto+Testbench).

- as métricas recolhidas foram: tempo médio de cada mensagem no ambiente de
  simulação até que o seu processamento seja concluída quer com sucesso quer
  através do mecanismo de _timeout_; tempo médio de cada mensagem na fila do
  _broker_; tempo médio de cada mensagem na fila do _worker_; tempo médio de cada
  mensagem na fila da _olt_; percentagem de pedidos que terminaram através do
  mecanismo de _timeout_.

## Tempo total no ambiente de simulação

### Seed 42

<div align="center">
    <img src="./output/random_seed_42/average_time_total_chart.png" alt="Tempo total no ambiente de simulação - seed 42">
</div>

#### Seed 7

<div align="center">
    <img src="./output/random_seed_7/average_time_total_chart.png" alt="Tempo total no ambiente de simulação - seed 7">
</div>

#### Seed 34

<div align="center">
    <img src="./output/random_seed_34/average_time_total_chart.png" alt="Tempo total no ambiente de simulação - seed 34">
</div>

## Tempo na fila do _broker_

### Seed 42

<div align="center">
    <img src="./output/random_seed_42/average_time_broker_queue_chart.png" alt="Tempo na fila do broker - seed 42">
</div>

### Seed 7

<div align="center">
    <img src="./output/random_seed_7/average_time_broker_queue_chart.png" alt="Tempo na fila do broker - seed 7">
</div>

### Seed 34

<div align="center">
    <img src="./output/random_seed_34/average_time_broker_queue_chart.png" alt="Tempo na fila do broker - seed 34">
</div>

## Tempo na fila do _worker_

### Seed 42

<div align="center">
    <img src="./output/random_seed_42/average_time_worker_queue_chart.png" alt="Tempo na fila do worker - seed 42">
</div>

### Seed 7

<div align="center">
    <img src="./output/random_seed_7/average_time_worker_queue_chart.png" alt="Tempo na fila do worker - seed 7">
</div>

### Seed 34

<div align="center">
    <img src="./output/random_seed_34/average_time_worker_queue_chart.png" alt="Tempo na fila do worker - seed 34">
</div>

## Tempo na fila da _OLT_

### Seed 42

<div align="center">
    <img src="./output/random_seed_42/average_time_olt_queue_chart.png" alt="Tempo na fila da OLT - seed 42">
</div>

### Seed 7

<div align="center">
    <img src="./output/random_seed_7/average_time_olt_queue_chart.png" alt="Tempo na fila da OLT - seed 7">
</div>

### Seed 34

<div align="center">
    <img src="./output/random_seed_34/average_time_olt_queue_chart.png" alt="Tempo na fila da OLT - seed 34">
</div>

## Percentagem de mensages _timedout_

### Seed 42

<div align="center">
    <img src="./output/random_seed_42/timedout_percentage_chart.png" alt="Percentagem de pedidos timedout - seed 42">
</div>

### Seed 7

<div align="center">
    <img src="./output/random_seed_7/timedout_percentage_chart.png" alt="Percentagem de pedidos timedout - seed 7">
</div>

### Seed 34

<div align="center">
    <img src="./output/random_seed_34/timedout_percentage_chart.png" alt="Percentagem de pedidos timedout - seed 34">
</div>

## Conclusões

O objetivo desta simulação é perceber se o algoritmos 1, 2 e 3 representam algum
tipo de melhoria em relação ao algoritmo 4 que é aquele que representaria a
implementação do sistema sem otimização alguma do ponto de vista arquitetural.

É importante também perceber que as otimizações apresentadas têm por objetivo
proporcionar uma forma de impedir que pedidos para uma mesma OLT sejam tratados
por _workers_ de modo concorrente, uma vez que isto provocaria um aumento de
pedidos na fila da OLT o que faria com que a percentagem de _timeouts_
aumentasse bastante.

Assim, o objetivo da análise destes resultados passa por perceber qual o
algoritmo que, perante uma diversidade de sequências de mensagens de entrada,
proporciona um processamento mais rápido, em média, de cada mensagem e que
mantém a percentagem de pedidos _timedout_ num limite aceitável.

Os primeiros três gráficos relativos ao tempo total médio que cada mensagem
passa no ambiente de simulação permite-nos perceber que o algoritmo 1 é aquele
que fornece um menor tempo de processamento à medida que aumentamos a dimensão
do _input_.

Em termos de percentagens de pedidos _timedout_ apenas faz sentido comparar a
arquitetura 4 com as arquiteturas 1 e 2. A razão pela qual não faz sentido
comparar com o algoritmo 3 é porque ele basicamente mantém a fila das OLT's tão
desocupadas quanto possível, uma vez que um _worker_ apenas pega numa nova
mensagem quando não está a processar nenhuma outra, o que significa que a fila
raramente está povoada. (A fila poderia até ser considerada desncessária).
Conforme conseguimos constatar através dos gráficos tanto o algoritmo 1 como o 2
configuram uma melhoria relativamente ao algoritmo base (o 4). A afirmação
realizada relativamente ao algoritmo 3 é corroborada pelos tempo de espera da
mensagem na fila da OLT que é consistentemente menor do que em todos os outros
algoritmos.
