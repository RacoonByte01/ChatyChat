<h1 align="center">
    Chatychat
</h1>
<p align="center">
    <a href="#">
        <img src="/assets/logo.png">
    </a>
</p>
<p align="center">
    <a href="/releases/tag/v0.1.0"><img src="https://img.shields.io/badge/v0.1.0-green?style=for-the-badge&label=VERSION"></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/GPL%203.0-yellow?style=for-the-badge&label=LICENSE"></a>
    <a href="CHANGELOG.md"><img src="https://img.shields.io/badge/keep%20a%20changelog-red?style=for-the-badge&label=changelog"></a>
</p>
<p align="center">
    <a href="https://spring.io/"><img src="https://img.shields.io/badge/v4.1.1-%236DB33F?style=flat&logo=spring&logoColor=%23fff&label=Spring"></a>
    <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Springboot-gray?style=flat&logo=springboot&logoColor=%23fff"></a>
    <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/jdk%2017-blue?style=flat&logo=openjdk&logoColor=%23fff&label=Java"></a>
    <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/3.9.16-%23C71A36?style=flat&logo=apachemaven&logoColor=%23fff&label=Apache%20Maven"></a>
</p>
<p align="center">
    A lightweight, self-hosted API for sending and receiving PGP-encrypted messages, with built-in user, role, and group management. Keep your communications private and maintain full control over your data by running everything on your own server.
</p>

## Features

- User management
- Group management
- Role management
- Send and receive messages

## Requirements

- Java 17
- Apache Maven 3.9.16

## Usage

### Users

| Method   | Endpoint      | Description       |
| -------- | ------------- | ----------------- |
| `GET`    | `/users`      | Get all users     |
| `GET`    | `/user/:name` | Get especifi user |
| `POST`   | `/user`       | Create new user   |
| `PUT`    | `/user`       | Update user       |
| `DELETE` | `/user`       | Delete user       |

#### Tokens

Tokens are a fundamental part of the authentication process. They are generated via login and serve as credentials for authenticating the user.

It uses the `Authorization` header to pass the token.

| Method   | Endpoint  | Description                                 |
| -------- | --------- | ------------------------------------------- |
| `GET`    | `/tokens` | Get all tokens                              |
| `POST`   | `/login`  | Create token                                |
| `DELETE` | `/token`  | Delete specific token                       |
| `DELETE` | `/logout` | Delete token used in `Authorization` header |
