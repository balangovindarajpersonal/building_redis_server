

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

```bash
git clone [https://github.com/your-username/java-redis-clone.git](https://github.com/balangovindarajpersonal/building_redis_server)
cd redis-clone
```

2. Build the project:

```bash
mvn clean install
```

## Usage

### Starting the server

To start the server, run the following command:

```bash
java -jar target/original-redis-clone-1.0-SNAPSHOT.jar
```

### Running the commands

Once the server is running, you can connect to it using `telnet` or `nc` and run the supported commands.

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

## Development

To contribute to this project:

1. Fork it on GitHub.
2. Create your feature branch (`git checkout -b my-new-feature`).
3. Commit your changes (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin my-new-feature`).
5. Create a new Pull Request.

