# Considerações sobre o método de _rule matching_

## Parâmetros das regras identificados até agora

- `token` - identificador da regra na base de dados.

- `fwVersion` - permite especificar que versão de _firmware_ os equipamentos
  devem ter para dar _match_ nesta regra.

  1. A _string_ que é a armazenada deve ser uma expressão regular.
  2. O facto de suportarmos uma expressão regular permite especificar todos os
     tipos de ocorrências possíveis.

  - `1.1.1` faria com que apenas equipamentos com a versão de _firmware_ '1.1.1'
    aplicassem a regra.
  - `^1.\d{1,2}.\d{1,2}$` faria com que todos os equipamentos com subservções da
    versão 1 aplicassem a regra.
  - Outras aplicações ficaram explícitas na secção em que analisaremos casos
    possíveis/prováveis de utilização.

- `ipAddress` - tal como o parâmetro anterior a utilização de expressões
  regulares permitirá a maior flexibilidade no estabelecimento do parâmetro.

- `status` - permite manter a informação relativa ao estado atual da regra e,
  portanto, do provisionamento/configuração do equipamento. É importante referir
  que este parâmetro apenas faz sentido quando acoplado ao identificador de um
  **ONU** uma vez que a relação entre `Regra <-> ONU` é `1 -> N`.

- `olt` - permite direcionar a regra a **ONU** que se encontram conectados a um
  **OLT**.

- `card` - permite direcionar a regra a **ONU** que se encontram conectados num
  determinado `card`. Quando conjugado com um parâmetro `olt` definido permite
  direcionar a uma `olt` e `card` específicos.

- `card_interface` - mesma coisa de cima.

- `equipment_id` - permite direcionar a um equipamento específico, embora não
  seja espectável que este seja um cenário de utilização muito frequente.

- `password` -

- `vendor` -

- `template` - aponta para o _template_ Ansible que deve ser corrigido quando a
  regra é _matched_.

- `parameters` - permite adicionar à regra parâmetros e variáveis relevantes que
  podem ser consultadas em _runtime_ do _playbook_ Ansible.

## Determinar a prioridade das regras

A prioridade do _match_ das regras é um parte essencial do maneira como o
_matcher_ de regras tem de ser pensado.

Se fizermos com que o _match_ ocorra com todas as regras que cumprem os
requisitos mínimos temos de ter um grande controlo para separar cada ação num
_playbook_, caso contrário vamos cair em casos em que aquando de um alarme
**NEW_ONT** vão ser corridos vários _playbooks_ Ansible que acabam por ter ações
rápidas. Este método pode resultar mas tem de se garantir que cada _playbook_
tem o mínimo de ações necessárias para cumprir o seu propósito, isto é, cada
_playbook_ deve ser atómico em termos de ações a realizar no **ONU**.

Uma outra opção é dar _match_ em apenas uma das regras que estão guardadas na
base de dados. No entanto, podem existir regras definida de tal maneira que o
_match_ pode ocorrer em mais do que uma. Desta forma, se queremos que o _match_
ocorra em apenas uma temos de definir uma mecanismo que permita comparar as
diversas regras em que deu _match_. O mecanismo que proponho para este caso é
contar o número de parâmetro da regra que foram cumpridos correr o _playbook_
corresponde à regra em que o número de _matches_ de parâmetros foi maior.

## Análise de _performance_

SQL suporta nativamente REGEXP:

```sql
SELECT * FROM rules WHERE fwVersion RLIKE '^1.\d{1,2}.\d{1,2}$'
```

O desempenho deste módulo essencialmente prende-se com o desempenho da própria
base de dados e, por isso, a sua dimensão e maneira como as _queries_ são
realizadas. O principal processo que pode compromenter o desempenho é o processo
de contagem do número de filtros que deram _match_ em cada uma das regras e a
partir escolher qual aquela que vai ser aplicada.

## Cenários de uso

- Regra para ONU's com versão de _firmware_ superior a '1.0.0':

```json
{
  "token": "1234",
  "fwVersion": "^[1-9].[1-9]d*.d+$",
  "ipAddress": null,
  "olt": null,
  "card": null,
  "interface": null,
  "equipment_id": null,
  "password": null,
  "vendor": null,
  "template": null,
  "parameters": null
}
```

- Regra para ONU's com versão de _firmware_ superior a '1.0.0' e conectadas na 'OLT47':

```json
{
  "token": "1235",
  "fwVersion": "^[1-9].[1-9]d*.d+$",
  "ipAddress": null,
  "olt": null,
  "card": "OLT47",
  "interface": null,
  "equipment_id": null,
  "password": null,
  "vendor": null,
  "template": null,
  "parameters": null
}
```

Por exemplo, esta regra e aquela que foi mostrado anteriormente colidiriam, ou
seja, ambas seriam utilizáveis por uma ONU com versão de _firmware_ '1.1.1'
conectada na "OLT47". Neste caso teriam de ser calculados o número de filtros
que dão _match_, no primeiro caso seria um: o `fwVersion` e no segundo caso
dois: `fwVersion` e `card`. Como o número de campos que dão _match_ é maior no
segundo caso então a regra utilizada seria a segunda.
