# Trabalho 35 – Sistema de Gestão de Mudanças de Pequenos Negócios

## Cenário
Pequenos negócios precisam controlar mudanças de preços, fornecedores e estoque. O sistema será desenvolvido em **Java**, com **JavaFX** e arquitetura em **três camadas**.

## Requisitos Funcionais
| Camada | Funcionalidade |
|--------|----------------|
| **Apresentação (JavaFX)** | Tela de cadastro de mudanças, listagem de histórico, filtro por tipo de mudança.
| **Negócio** | • Validar impacto da mudança (custo, estoque). <br>• Aplicar regras de aprovação (acima de 10 % precisa de aprovação do sócio). <br>• Atualizar preços e estoque após aprovação.
| **Dados** | • Armazenar mudanças em `ArrayList`, histórico em `HashMap`. <br>• CRUD completo.

## Regras de Negócio (3)
1. **Aprovação para Aumento > 10 %** – Se o percentual de aumento for maior que 10 %, o sistema solicita confirmação manual do sócio.
2. **Estoque Mínimo** – Ao reduzir preço, verificar se o estoque está abaixo do mínimo permitido; se sim, alertar.
3. **Validade da Mudança** – Data de vigência não pode ser anterior à data atual.

## Fluxo de Comunicação
1. Usuário cadastra uma mudança.
2. Camada de Apresentação envia ao **Serviço de Mudanças**.
3. Serviço valida as regras (aprovação, estoque, data).
4. Se válido, persiste via **Repositório**; caso contrário, devolve erro.
5. Camada de Apresentação exibe resultado.

### Diagrama de Sequência
```plantuml
@startuml
actor Usuario
box "Apresentação" #lightblue
    participant TelaMudanca
end box
box "Negócio" #lightgreen
    participant ServicoMudanca
end box
box "Persistência" #yellow
    participant RepositorioMudanca
end box
Usuario -> TelaMudanca : cadastra mudança
TelaMudanca -> ServicoMudanca : registrarMudanca(dados)
ServicoMudanca -> ServicoMudanca : validarAprovissao()
ServicoMudanca -> ServicoMudanca : validarEstoque()
ServicoMudanca -> ServicoMudanca : validarData()
alt regras ok
    ServicoMudanca -> RepositorioMudanca : salvarMudanca()
    RepositorioMudanca --> ServicoMudanca : sucesso
    ServicoMudanca --> TelaMudanca : sucesso
    TelaMudanca --> Usuario : mensagem de sucesso
else erro
    ServicoMudanca --> TelaMudanca : mensagem de erro
    TelaMudanca --> Usuario : exibir alerta
end
@enduml```

## Barema de Avaliação (100 pontos)
| Área | Peso | Critérios |
|------|------|-----------|
| Interface (JavaFX) | 20 pts | Tela de cadastro e histórico |
| Negócio | 30 pts | Implementação das 3 regras |
| Dados | 20 pts | Armazenamento e CRUD |
| Separação em Camadas | 20 pts | Arquitetura em 3 camadas |
| Boas Práticas | 10 pts | Código organizado e legível |

## Entregáveis
1. Projeto Java (Maven/Gradle) com pacotes `presentation`, `business`, `data`.
2. README com instruções.
3. Diagrama de classes (`Produto`, `MudancaPreco`).
4. Diagrama de sequência (registrar mudança).
5. Testes JUnit: aprovação automática para aumento ≤10 %, bloqueio para aumento >10 % sem aprovação, data inválida rejeitada.