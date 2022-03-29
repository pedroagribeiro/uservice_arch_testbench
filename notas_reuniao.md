# Notas reunião

- os resultados obtidos para a seed 34 seguem a tendência que se esperava.
- à medida que vamos acrescentando workers o benefício em termos de tempo de
  processamento é cada vez menor.

- falei com o orientador:
- questionou a necessidade do redis que guarda a informacao do que esta a
  processar cada worker numa instancia de redis. acha que esta informacao pode
  estar no proprio broker.
- questionou a semelhanca com a realidade do nosso input (nao existem bursts de
  mensagens para OLTS? ou a distribuicao uniforme faz sentido?)
