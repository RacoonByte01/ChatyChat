<h1 align="center">
    Chatychat
</h1>
<p align="center">
    <a href="#">
        <img src="/assets/logo.png">
    </a>
</p>
<p align="center">
    <a href="https://github.com/RacoonByte01/ChatyChat/releases/tag/v0.2.0"><img src="https://img.shields.io/badge/v0.2.0-green?style=for-the-badge&label=VERSION"></a>
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

## Installation

Clone the repository and launch it with this command:

```sh
git clone https://github.com/RacoonByte01/ChatyChat.git
cd ChatyChat
./mvnw spring-boot:run
```

Or build it as a .jar with:

```sh
./mvnw clean package
```

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

## Groups

Groups are used to organize users and store the roles assigned to them.

| Method   | Endpoint           | Description                               |
| -------- | ------------------ | ----------------------------------------- |
| `GET`    | `/groups`          | Get all groups the user belongs to        |
| `GET`    | `/group/:id`       | Get specific group the user belongs to    |
| `POST`   | `/group`           | Create new group _(user is set as admin)_ |
| `POST`   | `/group/:id`       | Add user to the group                     |
| `PUT`    | `/group/:id`       | Update group                              |
| `PUT`    | `/group/:id/:name` | Update role of specific user              |
| `DELETE` | `/group/:id`       | Delete specific group                     |
| `DELETE` | `/group/exit/:id`  | Delete your user of specific group        |
| `DELETE` | `/group/:id/:name` | Delete specific user                      |

### Roles

Roles are used to restrict or grant access to other users.

This number is converted to binary, and depending on whether a bit is enabled or disabled, a specific action will be allowed or denied.

| Bit | Value | Action       | Description                     |
| --- | ----- | ------------ | ------------------------------- |
| `0` | `1`   | Read         | Allow read the group            |
| `1` | `2`   | Write        | Allow write to the group        |
| `2` | `4`   | Add user     | Allow add users to the group    |
| `3` | `8`   | Update roles | Allow update other users roles  |
| `4` | `16`  | Delete user  | Allow remove users from a group |
| `5` | `32`  | Update group | Allow update the group          |
| `6` | `64`  | Delete group | Allow delete the group          |

> [!NOTE]
>
> A user with role `0` has **no management permissions** and cannot perform any group actions beyond the permissions granted by the group.
>
> A user with role `127` has **full management permissions** and can manage the group with the same level of control as an administrator.
