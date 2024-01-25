

```markdown
# Java Redis Clone

A simplistic Redis clone implemented in Java, providing a minimalistic in-memory key-value store with support for basic commands like PING, ECHO, SET, and GET.
Following the John Crickett coding challenges https://codingchallenges.substack.com/p/coding-challenge-37-redis-cli-tool


## Features

- In-memory key-value storage
- Support for basic Redis commands:
  - `PING`: Checks the connection to the server.
  - `ECHO`: Echoes the given string.
  - `SET`: Sets the string value of a key.
  - `GET`: Gets the string value of a key.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software:

- Java JDK 11 or later
- Maven 3.6 or later

### Installation

1. Clone the repository:
```
git clone https://github.com/balangovindarajpersonal/building_redis_server
cd redis-clone

```

2. Build the project:

mvn clean install
```

## Usage

### Starting the server

To start the server, run the following command:

```bash
java --enable-preview -jar target/redis-clone-1.0-SNAPSHOT.jar

```

### Running the commands

Once the server is running, you can connect to it using redis-cli

```bash
redis-cli
```

- PING

```bash
PING
```

- ECHO

```bash
ECHO Hello, World!
```

- SET

```bash
SET mykey myvalue
```

- GET

```bash
GET mykey
```

