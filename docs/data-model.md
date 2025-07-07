---
title: Data Model
description: "UML class diagram, entity-relationship diagram, and DDL."
order: 20
---

{% include ddc-abbreviations.md %}

## Page contents
{:.no_toc:}

- ToC
{:toc}

## UML class diagram

[//]: # (TODO Use Markdown or Liquid include to show UML class diagram in SVG format, linking to PDF format. )

## Entity-relationship diagram (ERD)

[![Entity-relationship diagram](img/erd.svg)](pdf/erd.pdf)

## Data Definition Language code

{% include linked-file.md file="sql/ddl-server.sql" type="sql" %}

## Implementation

### Entity classes

- [`User`](https://github.com/ddc-java-21/chat/blob/main/server/src/main/java/edu/cnm/deepdive/chat/model/entity/User.java)
- [`Channel`](https://github.com/ddc-java-21/chat/blob/main/server/src/main/java/edu/cnm/deepdive/chat/model/entity/Channel.java)
- [`Message`](https://github.com/ddc-java-21/chat/blob/main/server/src/main/java/edu/cnm/deepdive/chat/model/entity/Message.java)
