# 🏰 Strategic Game - AOE Style

Um jogo de estratégia inspirado em Age of Empires, desenvolvido em Java, onde geres recursos, trabalhadores e construções numa aldeia em constante evolução através das eras históricas.

## 🎮 Características do Jogo

### Sistema de Eras
Progride através de 9 eras históricas:
- 🗿 **Idade da Pedra** - Recursos básicos e primeiras construções
- 🥉 **Idade do Bronze** - Ferro torna-se disponível  
- ⚔️ **Idade do Ferro** - Prata e estruturas mais avançadas
- 🏰 **Idade Medieval** - Uvas e construções militares
- 🎨 **Renascimento** - Arqueiros e refinamento
- 🏭 **Idade Industrial** - Ouro e tecnologia
- 🌆 **Idade Moderna** - Favor dos deuses
- 💻 **Idade da Informação** - Tecnologia avançada
- 🚀 **Idade Futura** - O limite da civilização

### Gestão de Recursos
**Recursos Básicos:**
- 🪵 Madeira, 💧 Água, 🍞 Comida, 🪨 Pedra
- 👥 População (determina número de trabalhadores)

**Recursos Avançados:**
- ⚙️ Ferro, 🥈 Prata, 🍇 Uvas, 🏅 Ouro, ⭐ Favor aos Deuses

### Sistema de Construções
**Edifícios de Produção:**
- 🏘️ Cidade Central - Gera população
- 🏠 Casa - Produz água
- 🪓 Madeireira - Produz madeira
- 🌾 Moinho - Produz comida
- 🍷 Vinharia - Produz uvas
- ⛪ Templo - Produz favor dos deuses

**Edifícios Militares:**
- 🏕️ Barracas - Treino militar
- 🏹 Campo de Tiro - Archeiros
- 🐎 Estábulos - Cavalaria

**Edifícios Especiais:**
- 🛒 Mercado - Comércio
- ⚒️ Ferreiro - Equipamentos
- 📚 Universidade - Tecnologia
- ⛵ Docas - Recursos marítimos

### Gestão de Trabalhadores
- **Sistema multi-threaded** - Cada trabalhador opera independentemente
- **Missões dinâmicas** - Procura de recursos ou construção
- **Consumo automático** - Trabalhadores consomem recursos para sobreviver
- **Gestão de ocupação** - Trabalhadores livres vs ocupados

### 🎁 Sistema de Recompensas Diárias
**Inspirado nos jogos MMO mobile:**
- **30 dias de recompensas únicas** - Cada dia oferece recursos diferentes
- **Sistema de streaks** - Dias consecutivos aumentam as recompensas
- **Multiplicadores de era** - Recompensas ajustadas conforme a era atual
- **Recompensas especiais** - Dias 7, 14 e 30 oferecem bónus extraordinários
- **Persistência entre sessões** - O progresso é mantido entre jogos
- **Reset automático** - Streak reinicia se faltares um dia

## 🎨 Interface Enhanced

### Nova Experiência Visual
- **🏰 Tema Medieval** - Interface completamente redesenhada com temática medieval
- **📦 Caixas ASCII** - Menus elegantes com bordas duplas estilo pergaminho
- **🌈 Cores ANSI** - Texto colorido para melhor experiência visual
- **⚔️ Ícones Temáticos** - Emojis contextualizados para cada ação
- **🎯 Mensagens Imersivas** - Textos que remetem à época medieval

### Características da Interface
- **Council Chamber** - Menu principal estilizado como câmara real
- **Resource Expedition** - Menu de recursos com tema de exploração
- **Construction Guild** - Interface de construção com estilo arquitetônico
- **Daily Tribute** - Sistema de recompensas como tributos reais
- **Kingdom Status** - Relatórios do reino com pergaminhos reais

## 🚀 Como Jogar

### Requisitos
- Java 21+
- Maven 3.6+

### Instalação e Execução

```bash
# Clonar o repositório
git clone https://github.com/devTASE/strategic-game.git
cd strategic-game

# Compilar o projeto
mvn clean compile

# Executar o jogo
mvn exec:java -Dexec.mainClass="org.hsh.games.aoe.Game"

# Ou compilar e executar JAR
mvn clean package
java -jar target/StrategyGame-1.0-SNAPSHOT.jar
```

### Controlos
1. **Procurar Recursos** - Envia trabalhadores para coletar recursos específicos
2. **Construir/Atualizar** - Constrói novos edifícios ou melhora os existentes
3. **🎁 Recompensas Diárias** - Coleta recompensas diárias e vê o teu streak
4. **Ver Status** - Consulta estado atual da aldeia e progressão

### Dicas de Estratégia
- 🎯 **Equilibra recursos** - Mantém produção balanceada
- 👥 **Gere população** - Mais trabalhadores = mais produtividade
- 🏗️ **Constrói estrategicamente** - Cada edifício tem requisitos específicos
- ⏰ **Gere tempo** - Construções e buscas demoram tempo real
- 📈 **Progride nas eras** - Cumpre requisitos para desbloquear novas tecnologias

## 🛠️ Arquitetura Técnica

### Estrutura do Projeto
```
src/main/java/org/hsh/games/aoe/
├── entities/          # Entidades do jogo (Player, Worker, Building, etc.)
├── threads/           # Threads para operações assíncronas
├── ui/               # Utilitários de interface console enhanced
├── visual/           # Componentes de interface (futuro JavaFX)
├── Game.java         # Ponto de entrada
├── GameOfStrategy.java # Loop principal do jogo
├── ApplicationConstants.java # Constantes e mensagens temáticas
└── ConsoleUtils.java # Utilitários básicos de console
```

### Funcionalidades Técnicas
- **Programação Concorrente** - Múltiplas threads para simulação realista
- **Padrão Strategy** - Diferentes tipos de construção e recursos
- **Validação Robusta** - Verificação de entradas e estados
- **Arquitetura Modular** - Fácil extensão e manutenção

### Tecnologias
- **Java 21** - Linguagem principal
- **Maven** - Gestão de dependências e build
- **JUnit 5** - Testes unitários
- **Mockito** - Mocking para testes
- **JavaFX** - Interface gráfica (preparação futura)

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=PlayerTest
mvn test -Dtest=PlayerServiceTest
```

**Cobertura de Testes:**
- ✅ Validação de nome de jogador
- ✅ Gestão de recursos
- ✅ Sistema de construções
- ✅ Lógica de trabalhadores
- ✅ Sistema de recompensas diárias
- ✅ Multiplicadores de era
- ✅ Gestão de streaks
- ✅ Interface console enhanced (ConsoleDisplayUtils)
- ✅ Mensagens temáticas e cores ANSI
- ✅ Formatação de menus medievais

## 🎯 Funcionalidades Futuras

- [ ] Interface gráfica com JavaFX
- [ ] Sistema de combate
- [ ] Multiplayer
- [ ] Salvamento de progresso
- [ ] Eventos aleatórios
- [ ] Sistema de conquistas
- [ ] Modo campanha

## 🤝 Contribuição

1. Fork o projeto
2. Cria uma branch para a tua feature (`git checkout -b feature/nova-feature`)
3. Commit das mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abre um Pull Request

## 👨‍💻 Desenvolvedor

**devTASE** - [GitHub](https://github.com/devTASE)

## 📄 Licença

Este projeto está sob a licença MIT. Vê o arquivo `LICENSE` para mais detalhes.

---

*"Constrói o teu império, era por era, recurso por recurso."* 🏛️
