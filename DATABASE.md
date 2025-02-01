```mermaid
---
title: Database Diagram
---
classDiagram

class AuthObjects{
    AuthObjectsId Integer PK
    DiscordUserId TEXT
    AuthorizationToken TEXT
    RefreshToken TEXT 
    DateCreated TEXT
}
```