# Visão Geral da Arquitetura Hexagonal

## Domínio no Centro

O domínio é o núcleo da aplicação e contém as entidades e serviços principais. Ele é responsável por manter a lógica de negócio central e deve ser independente das interfaces externas:

```java
public class Player {
    private String id;
    private String name;
    // getters and setters
}
```

## Portas e Adaptadores de Entrada (Driving Side)

As portas e adaptadores de entrada permitem que a aplicação interaja com o mundo exterior. Um exemplo comum é a interface de console:

```java
public interface Command {
    void execute();
}
```

## Portas e Adaptadores de Saída (Driven Side)

Essas portas e adaptadores são usadas pelo domínio para realizar operações externas, como acessar repositórios ou enviar notificações:

```java
public interface NotificationService {
    void sendNotification(String message);
}
```

## Nova Porta CyberOperative

A nova porta CyberOperative atua como uma porta de entrada, conectando o PlayerService com a interface de usuário (UI):

```java
public interface CyberOperative {
    void updateUI(Player player);
}
```

Esta porta permite a comunicação entre a lógica de negócio central e a camada de interface com o usuário, mantendo a arquitetura modular e flexível.
