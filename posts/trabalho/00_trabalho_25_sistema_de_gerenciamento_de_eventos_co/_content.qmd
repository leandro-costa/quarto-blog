# Trabalho 25 – Sistema de Gerenciamento de Eventos Corporativos – Agenda e Convites

## Cenário
Uma agência de eventos precisa de um sistema desktop para gerenciar a agenda de eventos corporativos, permitindo o cadastro de eventos, organização de convites para participantes e controle de datas. O sistema será desenvolvido em **Java**, usando **JavaFX** para a interface e seguindo a arquitetura em **três camadas** (Apresentação, Negócio e Dados).

## Requisitos Funcionais
| Camada | Funccionalidade |
|--------|----------------|
| **Apresentação (JavaFX)** | Tela de cadastro de eventos, tela de gerencia de convites (enviar/cancelar), tela de agenda (visão cronológica) e tela de listagem de participantes. Cada tela deve exibir mensagens de erro claras. |
| **Negócio** | • Validar dados de entrada (nome do evento, data, capacidade máxima, e-mail do convitado). <br>• Aplicar as regras de negócio descritas abaixo. |
| **Dados** | • Armazenar eventos e convites em memoria (`ArrayList`, `HashMap`). <br>• Implementar operações CRUD para eventos e convites. |

## Regras de Negócio (2)
1. **Conflito de Agenda** – Não é permitido cadastrar ou alterar um evento para uma data e horário que já possua outro evento agendado. Se houver sobreposição, a operação deve ser abortada e o usuário deve receber uma mensagem de aviso de conflito de horários.
2. **Capacidade Máxima de Convidados** – Cada evento possui um limite de participantes. Ao tentar enviar um convite, o sistema deve verificar se o número de convites aceitos já atingiu a capacidade máxima. Se o limite for alcançado, o convite não deve ser enviado e o usuário deve ser avisado sobre a lotação do evento.

## Fluxo de Comunicação Entre as Camadas
1. O usuário interage com a interface JavaFX (ex.: cadastrando um novo evento).
2. A **Camada de Apresentação** envia os dados ao **Serviço de Eventos** (camada de negócio).
3. O serviço verifica as regras de negócio (confliato de data e capacidade). Se válidas, delega ao **Repositório de Eventos** (camada de dados). Se violadas, retorna um erro.
4. A **Camada de Apresentação** recebe o retorno e exibe um `Alert` ao usuario (sucesso ou erro).

### Diagrama de Sequêção
```plantuml{kroki=true}
@startuml
actor Usuario
box "Apresentação\n<Boundary>" #lightblue
    participant TelaEvento
end box
box "Negócio\n<Control>" #lightgreen
    participant ServicoEvento
end box
box "Persistência\n<Entity>" #yellow
    participant RepositorioEvento
end box

Usuario -> TelaEvento : Preenche dados do evento
TelaEvento -> ServicoEvento : cadastrarEvento(dados)
ServicoEvento -> ServicoEvento : verificarConflitoAgenda()
ServicoEvento -> ServicoEvento : verificarCapacidade()
alt regras atendidas
    ServicoEvento -> RepositorioEvento : salvarEvento()
    RepositorioEvento --> ServicoEvento : sucesso
    ServicoEvento --> TelaEvento : sucesso
    TelaEvento --> Usuario : Mensagem de sucesso
else regra violada
    ServicoEvento --> TelaEvento : mensagem de erro
    TelaEvento --> Usuario : Exibir alerta de erro
end
@enduml```

## Barema de Avaliação (100 pontos)
| Área | Peso | Critèrios |
|------|------|-----------|
| **Interface Gráfica (JavaFX)** | 20 pts | Funccionalidade completa, usabilidade e tratamento de mensagens de erro. |
| **Camada de Negócio** | 30 pts | Implementação correta das regras de conflito e capacidade,tratamento de exceçóes. |
| **Camada de Dados** | 20 pts | Uso adequado de colecoes, operações CRUD funccionais. |
| **Separação em Camadas** | 20 pts | Arquitetura em 3 camadas bem definida e communicação correta. |
| **Boas Práticas** | 10 pts | Código limpo,organização de pacotes e nomes coerentes. |

## Entregáveis
1. Projeto Java completo (Maven ou Gradle) com pacotes `presentation`, `business`, `data` e `model`.
2. **README** com instruções de compilação e execução.
3. Diagrama de Classes (UML) mostrando as entidades (`Evento`, `Convidado`).
4. Diagrama de sequência (como o acima) para o caso de uso **Cadastrar Evento**.
5. **Testes unitários** (JUnit) que comprovem:
   - Cadastro de evento sem conflitos de horário.
   - Bloqueio de cadastro de evento em horário já ocupado.
   - Bloqueio de envio de convite para evento com lotação esgotada.