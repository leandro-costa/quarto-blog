# Trabalho 26 – Sistema de Controle de Acesso Biométrico

## Cenário
Uma empresa precisa de um sistema para controlar o acesso a áreas restritas usando tecnologia biométrica (reconhecimento facial ou digital de impressão digital). O systèma será desenvolvido em **Java**, com interface **JavaFX** e arquitetura em **tré camadas**.

## Requisitos Funcionais
| Camada | Funccionalidade |
|--------|----------------|
| **Apresentação (JavaFX)** | Tela de cadastro de usuários (nome, código biométrico), tela de registro de acesso, janela de autenticação biométrica.
| **Negócio** | • Validar dados de entrada (nome, biométrico, horário). <br>
• Aplicar regras de acesso (horários permitidos, biometria válida).
| **Dados** | • Armazenar registros de acesso e usuarios (`HashMap`, `ArrayList`).
| 

## Regras de Negócio (3)
1. **Autenticação Biométrica Válida** – O biometria deve corresponder ao cadastro com 95% de confidëncia mínia. Se houver discrepancia, a autenticação falha.  
2. **Período de Acesso Authorizado** – Acesso só permitido entre 08:00 e 18:00. Fora deste intervalo, registra bloqueio temporário.  
3. **Bloqueio por Falhas Máquinasas** – Após 3 falhas consecutivas, o sistema bloqueia o cadastro 15 minutos.

## Fluxo de Comunicação
1. Usuário inicia autenticação biométrica.
2. Camada de Apresentação envia dados ao Serviço de Acesso.
3. Serviço verifica regras de autenticação e horário.
4. Se valido, atualiza registo de acesso. Se não, incrementa falhas e pode bloquear.
5. Camada de Apresentação mostra status ao usuário.

### Diagrama de Sequência
```plantuml
@startuml
actor Usuario
box "Apresentação\n<Boundary>" #lightblue
    participant TelaAutenticao
end box
box "Negócio\n<Control>" #lightgreen
    participant ServicoAcesso
end box
box "Persistência\n<Entity>" #yellow
    participant RepositorioAcesso
end box

Usuario -> TelaAutenticao : Tenta autenticao
TelaAutenticao -> ServicoAcesso : autenticaBiometria biometria, hora
ServicoAcesso -> ServicoAcesso : validarBiometria()
ServicoAcesso -> ServicoAcesso : verificarHorario()
alt biometria e horario validos
    ServicoAcesso -> RepositorioAcesso : registrarAcesso()
    RepositorioAcesso --> ServicoAcesso : sucesso
    ServicoAcesso --> TelaAutenticao : Acesso concedido
else biometria ou horario invalido
    ServicoAcesso -> RegistroAcesso : atualizarFalhas()
    alt falhas >=3
        ServicoAcesso -> RegistrarAcesso : bloquearCadastro()
        RegistrarAcesso --> ServicoAcesso : bloqueioAtivo
        ServicoAcesso --> TelaAutenticao : Bloqueio temporario
    else
        ServicoAcesso --> TelaAutenticao : Falha na autenticao
end
@enduml```

## Barema de Avaliação (100 pontos)
| Área | Peso | Critérios |
|--------|------|------------|
| Interface (JavaFX) | 20 pts | Tela de autenticao clara, tratamento de tentativas
| Camada de Negócio | 30 pts | Regras de autenticao e horário implementadas
| Camada de Dados | 20 pts | Registro de acesso e falhas
| Separação em Camadas | 20 pts | Arquitetura clara com 3 camadas
| Boas Práticas | 10 pts | Código limpo e organizado

## Entregáveis
1. Projeto Java (Maven/Gradle) com pacotes `presentation`, `business`, `data`.
2. README com instruções de execução.
3. Diagrama de Classes (UML) para `Usuario`, `AcessoBiometrico`.
4. Diagrama de sequência para autenticao.
5. Testes JUnit: testar autenticao valida, bloqueio por falhas, horario incorreto.