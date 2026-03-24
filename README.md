#  MiniBankOS

A **Java-based Operating System + DBMS hybrid simulation** that models a banking system with:

* Concurrent transaction execution
* Process scheduling
* Synchronization & locking
* Crash recovery (WAL logging)
* Authentication & authorization (RBAC)

#  Overview

MiniBankOS simulates how an operating system and database system cooperate to:

* Execute multiple transactions concurrently
* Maintain data consistency
* Prevent race conditions
* Recover from system crashes

##  Operating System Concepts

* Process scheduling (Priority + Round Robin)
* Critical section handling
* Process synchronization
* Time slicing
* Resource sharing

##  Database Concepts

* Transactions (Atomic execution)
* Write-Ahead Logging (WAL)
* Crash recovery
* Consistency guarantees

##  Security Concepts

* Authentication (login/register)
* Session management
* Role-Based Access Control (RBAC)
* Permission control (grant/revoke)


#  Features

##  Concurrency & Scheduling

* Multi-threaded transaction execution
* Hybrid scheduler:

  * Priority Scheduling
  * Round Robin Queue
* Transaction abstraction using `TransactionProcess`

---

##  Synchronization

* LockManager (read/write locking)
* Prevents race conditions
* Safe concurrent access to accounts

---

##  Transaction System

* BEGIN / COMMIT logging
* Atomic operations
* Scheduled execution via kernel scheduler

---

##  Crash Recovery

* Persistent logging (`transaction.log`)
* RecoveryManager restores system state
* Ensures no data inconsistency after crash

---

##  Authentication System

* User registration & login
* Password validation
* Session tracking

---

##  Authorization (RBAC)

### User:

* View own balance
* Transfer from own account (if permitted)

### Admin:

* Create accounts
* View any account
* Grant / revoke transfer permissions
* Delete users (with root protection)

## Terminal Interface

* Command-line interaction
* Dynamic system behavior
* Acts like an OS shell

#  Project Structure

```
BankingOS/
│
├── src/
│   │
│   ├── kernel/                # Core OS mechanisms
│   │   ├── TransactionManager.java
│   │   ├── LockManager.java
│   │   ├── RecoveryManager.java
│   │   └── scheduler/
│   │        ├── Scheduler.java
│   │        ├── PriorityScheduler.java
│   │        ├── RoundRobinQueue.java
│   │        ├── TransactionProcess.java
│   │        └── TimeSlice.java
│   │
│   ├── system/                # Shared resources
│   │   ├── Account.java
│   │   └── BankDatabase.java
│   │
│   ├── shell/                 # CLI interface
│   │   ├── Terminal.java
│   │   └── CommandParser.java
│   │
│   ├── logging/              # Logging system
│   │   └── Logger.java
│   │
│   ├── Auth/                 # Authentication system
│   │   ├── AuthManager.java
│   │   ├── Session.java
│   │   └── User.java
│   │
│   └── Main.java
│
├── data/
│   └── transaction.log       # Persistent WAL logs
│
└── README.md
```

#  Supported Commands

##  Authentication

```
register <username> <password>
login <username> <password>
logout
```


##  Banking

```
create <name> <balance>       # Admin only
balance <name>
transfer <from> <to> <amount>
```


## Admin Controls

```
grant <username>
revoke <username>
delete <username>
```


#  How to Run

###  Compile All Files (IMPORTANT)

```bash
javac -d . (Get-ChildItem -Recurse -Filter *.java).FullName
```

---

###  Run the System

```bash
java Main
```

---

#  Troubleshooting

If changes are not reflected:

* You may be compiling only `Main.java`
* Always recompile the entire project:

```bash
javac -d . (Get-ChildItem -Recurse -Filter *.java).FullName
```

---

# Future Enhancements

*  Persistent user storage (`users.db`)
*  Multi-terminal support (multiple sessions)
*  Deadlock detection (wait-for graph)
*  Distributed BankingOS (network-based)
*  Monitoring dashboard (process + logs)
*  Performance optimization

---

#  Final Goal

* Concurrency
* Security
* Recovery
* Persistence
* Multi-user environment

---

#  Author

MiniBankOS is designed as a **systems-level project** to demonstrate deep understanding of:

* Operating Systems
* Database Systems
* Concurrency & Synchronization

---

 *This is not just a project — it's a simulation of how real systems work internally.*
