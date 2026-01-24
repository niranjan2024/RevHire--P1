# RevHire – Console-Based Job Portal Application

RevHire is a Java-based console application that connects **Job Seekers** and **Employers** on a single platform.  
It allows job seekers to create profiles, upload resumes, search and apply for jobs, while employers can post jobs, view applicants, and manage application statuses.

This project follows **layered architecture**, **secure authentication**, and **database-driven design** using JDBC and MySQL.

---

## 🚀 Features

### 👤 Job Seeker
- Register & Login
- Complete and Update Profile
- Upload & Update Resume
    - Education (stored in Education table)
    - Experience (stored in Experience table)
    - Skills (stored in Skills table)
- Search Jobs (by title, location, experience)
- Apply for Jobs
- View Application Status
- Withdraw Application
- View Notifications (Shortlisted / Rejected)
- Change Password
- Forgot Password (Security Question)

---

### 🏢 Employer
- Register & Login
- Create Company Profile
- Post Job Listings
- View Applicants
- Shortlist or Reject Applications
- Notify Job Seekers automatically

---

### 🔐 Common Features
- Secure Login System
- Password Hashing (SHA-256)
- Input Validation
- Notifications System
- Prepared Statements (SQL Injection Safe)

---

## 🛠 Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Language     | Java (JDK 21)                       |
| Database     | MySQL                               |
| Connectivity | JDBC                                |
| Testing      | JUnit 5                             |
| Logging      | Console-based (Extensible to Log4j) |
| Architecture | DAO + Service + UI                  |
| Tools        | IntelliJ IDEA                       |

---

## 🗂 Project Structure

com.revhire
│
├── dao → Database access logic (JDBC)
├── service → Business logic
├── util → Utilities (Password hashing, Validation)
├── model → Entity classes
├── Test → JUnit test classes
├── config → Database configuration
└── ui → Console menus


---

## 🗄 Database Design

- users
- job_seeker_profiles
- employer_profiles
- resumes
- education
- experience
- skills
- job_listings
- applications
- notifications

📌 ER Diagram included in project documentation.

---

## 🔐 Security & Authentication

- **Passwords are hashed** using SHA-256 before storing in database
- **Prepared Statements** used everywhere (SQL Injection safe)
- Input validation for:
    - Email format
    - Password strength
- Forgot Password:
    - Security Question + Answer verification
- Change Password:
    - Old password validation required
- Role-based access (JOB_SEEKER / EMPLOYER)

---

## 🧪 Testing

JUnit 5 is used for unit testing critical DAO and service classes.

### Test Coverage Includes:
- UserDAO (Register, Login)
- JobDAO (Search Jobs)
- ApplicationDAO (Apply, Status Update)
- NotificationDAO (View Notifications)

✔ Tests ensure:
- Methods do not crash
- Core flows work correctly
- Database connectivity is validated

---

## ▶ How to Run the Project

1. Clone or extract the project
2. Create MySQL database:
   CREATE DATABASE revhire_db;
3.Import provided SQL tables
4.Update database credentials in:
    com.revhire.config.DBConnection
5.Add MySQL Connector JAR to classpath
6.Run:
    com.revhire.Main
