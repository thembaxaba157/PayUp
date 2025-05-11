# 💸 PayUp – Group Expense & Payment Tracker

**PayUp** is a web-based Server-side Java application designed to simplify group expense management. It enables users to log expenses, send or receive payment requests, and track outstanding payments. The project is built with **Javalin** as the web framework and **Thymeleaf** for server-side HTML templating.

---

## 📦 Tech Stack

- **Language:** Java 17+
- **Framework:** [Javalin](https://javalin.io/)
- **Template Engine:** [Thymeleaf](https://www.thymeleaf.org/)
- **Build Tool:** Maven
- **Currency Handling:** [JavaMoney (Moneta)](https://javamoney.github.io/)
- **Session Handling:** Jetty
- **Validation:** Apache Commons & Guava
- **Persistence:** In-memory DAO pattern

---

## 🚀 Features

- 👥 User login via email (mock authentication)
- 📋 Record and view personal expenses
- 🧾 Request payments from others
- ✅ Accept and process payment requests
- 💰 See outstanding amounts owed to/from users
- 📊 Visual HTML interface using Thymeleaf
- 🧪 In-memory data seed for demo/testing

---

## 🗂️ Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── payup/
│   │       ├── controller/         # Web controllers (Javalin Handlers)
│   │       ├── model/              # Expense, PaymentRequest, Person, MoneyHelpers
│   │       ├── persistence/        # DAO interfaces and in-memory implementations
│   │       └── server/             # App server, routes, and service registry
│   └── resources/
│       ├── templates/              # Thymeleaf HTML templates
│       └── html/                   # Static resources (index.html, CSS)
├── test/                           # Unit/integration tests (if added)
```

---

## ▶️ Running the Application

### 💻 Via Maven

1. Clone the repository:
   ```bash
   git clone https://github.com/thembaxaba157/payup.git
   cd payup
   ```

2. Build and run:
   ```bash
   mvn clean package
   java -cp target/payup-1.0-SNAPSHOT.jar payup.server.PayUpServer
   ```

3. Open in your browser:
   ```
   http://localhost:5050/
   ```

---

## 👤 Login Info

Use **any valid email** (e.g. `alice@example.com`) to simulate a login. No password required. Demo data will auto-load.

---

## 🌐 Available Routes

| Route                         | Purpose                                 |
|------------------------------|-----------------------------------------|
| `/`                          | Login page                              |
| `/login.action`              | Login handler                           |
| `/expenses`                  | View expenses                           |
| `/newexpense`                | Add new expense                         |
| `/expense.action`            | Submit new expense                      |
| `/paymentrequest`            | View payment requests for an expense    |
| `/paymentrequest.action`     | Create new payment request              |
| `/paymentrequests_sent`      | See who owes you                        |
| `/paymentrequests_received`  | See who you owe                         |
| `/payment.action`            | Submit payment                          |
| `/logout`                    | Logout                                  |

---

## 📷 UI Overview

- **Login Page:** Email entry with session tracking
- **Expenses:** Table showing expenses, payments requested/received
- **Payment Requests:** List per expense or person
- **Styles:** Basic responsive layout with Google Fonts and Normalize.css
- **Templates:** Modular Thymeleaf layout for shared HTML sections

---

## 🧪 Demo Seed Data (Auto-Loaded)

Upon server start, the app seeds:

- 3 users (`student1@mockemail.com`, `student2@mockemail.com`, `student3@mockemail.com`)
- Sample expenses
- Linked payment requests

This allows you to immediately explore the app.

---

## ⚠️ Limitations & Notes

- No real authentication (demo only)
- No persistent storage — memory resets on restart
- No JavaScript — pure HTML/CSS/Thymeleaf

---

## 📜 License

MIT License – free to use, modify, and distribute.

---

## 👨‍💻 Author

Built by [Your Name]. Contributions welcome!
