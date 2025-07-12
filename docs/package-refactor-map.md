# Package Refactor Map

This document provides a mapping from the current package structure to the target hexagonal architecture.

## Mapping

| Current Package/Class                                      | Target Package/Class                                              | Category      |
|------------------------------------------------------------|-------------------------------------------------------------------|---------------|
| `org.hsh.games.aoe.Game`                                   | `org.hsh.games.aoe.Game`                                          | infrastructure|
| `org.hsh.games.aoe.application.dto.PlayerDTO`               | `org.hsh.games.aoe.application.dto.PlayerDTO`                     | application   |
| `org.hsh.games.aoe.application.dto.ResourceDTO`             | `org.hsh.games.aoe.application.dto.ResourceDTO`                   | application   |
| `org.hsh.games.aoe.domain.entities.Player`                 | `org.hsh.games.aoe.domain.entities.Player`                        | domain        |
| `org.hsh.games.aoe.domain.services.PlayerService`                 | `org.hsh.games.aoe.application.services.PlayerService`            | application   |
| `org.hsh.games.aoe.infrastructure.adapters.inbound.console.ConsoleGameController` | `org.hsh.games.aoe.infrastructure.console.ConsoleGameController` | infrastructure|
| `org.hsh.games.aoe.shared.utils.ConsoleUtils`                     | `org.hsh.games.aoe.shared.utils.ConsoleUtils`                     | shared        |

## Pending Review
- Classes under `org.hsh.games.aoe.entities`: Need clarity on their domain alignment.
- Any uncovered dependencies or relations mentioned in the audit report.
