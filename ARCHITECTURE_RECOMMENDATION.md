# Recomendação Arquitetural - Strategic Game

## Análise da Arquitetura Atual

### Situação Atual
O projeto apresenta uma estrutura híbrida que está em processo de evolução:

1. **Estrutura Existente**: Mistura elementos de arquitetura em camadas com alguns padrões de service
2. **Problemas Identificados**:
   - Acoplamento forte entre a lógica de negócio e a interface (console)
   - Falta de separação clara entre camadas
   - Injeção de dependências manual e inconsistente
   - Falta de repositórios/persistência estruturada
   - Mistura de responsabilidades no `GameOfStrategy`

## Recomendação: Hexagonal Architecture (Ports & Adapters)

### Por que Hexagonal Architecture?

1. **Flexibilidade de Interface**: O jogo tem tanto interface console quanto JavaFX - perfeito para adapters
2. **Independência de Framework**: Permite trocar facilmente entre diferentes UIs
3. **Testabilidade**: Facilita testes unitários isolados
4. **Evolução Futura**: Permite adicionar persistência, APIs REST, etc.
5. **Domínio Rico**: O jogo tem regras de negócio complexas que se beneficiam do isolamento

### Estrutura Proposta

```
📁 src/main/java/org/hsh/games/aoe/
├── 📁 domain/                    # CORE - Domínio Isolado
│   ├── 📁 entities/              # Entidades de domínio
│   │   ├── Player.java
│   │   ├── Guild.java
│   │   ├── Resource.java
│   │   └── ...
│   ├── 📁 valueobjects/          # Value Objects
│   │   ├── ResourceAmount.java
│   │   └── PlayerId.java
│   ├── 📁 services/              # Serviços de domínio
│   │   ├── PlayerDomainService.java
│   │   └── GuildDomainService.java
│   └── 📁 exceptions/            # Exceções de domínio
│       └── InvalidPlayerException.java
│
├── 📁 application/               # Application Layer
│   ├── 📁 usecases/              # Use Cases (Application Services)
│   │   ├── CreatePlayerUseCase.java
│   │   ├── ManageResourcesUseCase.java
│   │   └── GuildManagementUseCase.java
│   ├── 📁 ports/                 # Interfaces (Ports)
│   │   ├── 📁 inbound/           # Driving Ports
│   │   │   ├── PlayerManagementPort.java
│   │   │   └── GameControllerPort.java
│   │   └── 📁 outbound/          # Driven Ports
│   │       ├── PlayerRepositoryPort.java
│   │       ├── NotificationPort.java
│   │       └── TimeServicePort.java
│   └── 📁 dto/                   # Data Transfer Objects
│       ├── PlayerDTO.java
│       └── ResourceDTO.java
│
├── 📁 infrastructure/            # ADAPTERS - Infraestrutura
│   ├── 📁 adapters/
│   │   ├── 📁 inbound/           # Driving Adapters
│   │   │   ├── 📁 console/       # Console Interface
│   │   │   │   ├── ConsoleGameController.java
│   │   │   │   └── ConsoleDisplayUtils.java
│   │   │   └── 📁 javafx/        # JavaFX Interface
│   │   │       ├── JavaFXGameController.java
│   │   │       └── GameOfStrategyUI.java
│   │   └── 📁 outbound/          # Driven Adapters
│   │       ├── 📁 persistence/   # Persistência
│   │       │   ├── InMemoryPlayerRepository.java
│   │       │   └── FileBasedPlayerRepository.java
│   │       ├── 📁 notification/  # Notificações
│   │       │   └── ConsoleNotificationAdapter.java
│   │       └── 📁 threading/     # Threads
│   │           └── ThreadPoolAdapter.java
│   └── 📁 configuration/         # Configuração e DI
│       └── ApplicationConfig.java
│
└── 📁 shared/                    # Utilities compartilhadas
    ├── 📁 utils/
    └── 📁 constants/
```

### Benefícios desta Arquitetura

1. **Domínio Protegido**: Regras de negócio isoladas das implementações técnicas
2. **Flexibilidade**: Fácil troca entre Console e JavaFX
3. **Testabilidade**: Testes unitários puros no domínio
4. **Extensibilidade**: Fácil adição de novas funcionalidades
5. **Manutenibilidade**: Responsabilidades bem definidas

### Implementação Sugerida

1. **Primeiro Passo**: Extrair o domínio atual (entidades + regras)
2. **Segundo Passo**: Criar ports para abstrair dependências
3. **Terceiro Passo**: Implementar adapters para console e JavaFX
4. **Quarto Passo**: Adicionar injeção de dependências adequada

### Padrões Complementares

- **Repository Pattern**: Para persistência
- **Command Pattern**: Para operações de jogo
- **Observer Pattern**: Para notificações
- **Strategy Pattern**: Para diferentes tipos de recursos/buildings
- **Factory Pattern**: Para criação de entidades

---

**Autor**: devTASE  
**Data**: 2025-07-08  
**Versão**: 1.0
