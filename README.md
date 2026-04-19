# Contact Manager

A role-based console application for managing contacts, built with Java and MySQL.

## Overview

Contact Manager is a command-line application that allows teams to store, search, and manage a contact directory. Access to features is controlled by user roles, ensuring that each user can only perform actions appropriate to their level of responsibility.

## Features

- **User Authentication** — Secure login with hashed passwords
- **Role-Based Access Control** — Four distinct roles with different permission levels
- **Contact Management** — Add, view, search, edit, and delete contacts
- **Soft Delete** — Deleted contacts are flagged rather than permanently removed
- **Undo Support** — The last operation can be reversed using the Command pattern
- **Contact History** — Every create, update, delete, and restore action is recorded
- **Activity Logging** — All user actions (login, add, update, delete) are logged
- **Statistics** — Managers can view contact statistics including email domain distribution
- **Sorting** — Contacts can be sorted by name, surname, or email with proper Turkish locale collation

## Roles & Permissions

| Feature              | Tester | Junior Developer | Senior Developer | Manager |
|----------------------|:------:|:----------------:|:----------------:|:-------:|
| List contacts        | ✅     | ✅               | ✅               | ✅      |
| Search contacts      | ✅     | ✅               | ✅               | ✅      |
| Add contact          | ❌     | ✅               | ✅               | ✅      |
| Edit contact (names) | ❌     | ✅               | ✅               | ✅      |
| Edit contact (full)  | ❌     | ❌               | ✅               | ✅      |
| Delete contact       | ❌     | ❌               | ✅               | ✅      |
| View statistics      | ❌     | ❌               | ❌               | ✅      |
| Undo last operation  | ❌     | ❌               | ❌               | ✅      |

## Tech Stack

- **Language:** Java
- **Database:** MySQL
- **JDBC Driver:** MySQL Connector/J 8.2.0
- **Architecture:** Layered (UI → Service → DAO → Database)
- **Design Patterns:** Singleton (DB connection), Command (undo support)

## Project Structure

```
src/
├── Main.java              # Application entry point
├── command/               # Command pattern (add, update, delete, undo)
├── dao/                   # Data Access Objects (contacts, users, activity logs)
├── db/                    # Singleton database connection
├── intro/ & outro/        # Console intro/outro animations
├── model/                 # Domain models (Contact, User, Role, and role subclasses)
├── service/               # Business logic and permission enforcement
├── ui/                    # Console UI and interaction helpers
└── util/                  # Utility classes
lib/
└── mysql-connector-j-8.2.0.jar   # MySQL JDBC driver
database.sql               # Database schema and seed data
```

## Setup

### Prerequisites

- Java 11 or later
- MySQL 8.0 or later

### Database

1. Run the provided SQL script to create the database, tables, user account, and sample data:

   ```bash
   mysql -u root -p < database.sql
   ```

   This will:
   - Create the `cmpe343_project` database
   - Create a MySQL user `myuser` with password `1234`
   - Create the `users`, `contacts`, `contact_history`, and `activity_logs` tables
   - Insert default application users and 50 sample contacts

### Compile

From the project root, compile all sources with the JDBC driver on the classpath:

```bash
javac -cp lib/mysql-connector-j-8.2.0.jar -d bin src/**/*.java src/Main.java
```

### Run

```bash
java -cp bin:lib/mysql-connector-j-8.2.0.jar Main
```

> On Windows, replace `:` with `;` in the classpath separator.
## Default Users

| Username | Role             | Password  |
|----------|------------------|-----------|
| `tt`     | Tester           | `1234`    |
| `jd`     | Junior Developer | `1234jd`  |
| `sd`     | Senior Developer | `1234sd`  |
| `man`    | Manager          | `1234man` |

> Passwords above are examples — the database stores SHA-256 hashes. Use the values already inserted by `database.sql`.
## Database Schema

| Table             | Description                                              |
|-------------------|----------------------------------------------------------|
| `users`           | Application users with roles and hashed passwords        |
| `contacts`        | Contact records with soft-delete support                 |
| `contact_history` | Full snapshot history for every contact change           |
| `activity_logs`   | Audit trail of all user actions                          |
