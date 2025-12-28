# PlayMeet Website

**The Web Companion to PlayMeet.** A Spring Boot application serving as both a landing page and a web-based management
portal for the PlayMeet ecosystem.

## 📖 The Story Behind the Web Portal

After building the PlayMeet Android app, I realized I needed to expand my horizons beyond mobile development. I decided
to create a web version of PlayMeet to challenge myself and dive deep into Spring Boot.

Initially, the goal was simple: create a landing page to showcase the app. However, as I got comfortable with the
framework, the project evolved. I started adding functionality to manage posts from the web, effectively mirroring parts
of the Android app's logic.

To be honest, at first, Spring Boot annotations felt like "black magic." The learning curve was steep, and I was
overwhelmed by the ecosystem. But in the long run, it was a fantastic choice. Thanks to documentation and a lot of trial
and error, I grew to admire Spring's simplicity and speed.

## ✨ Features & Architecture

This project is a monolithic web application that integrates deeply with the existing Firebase infrastructure used by
the mobile app.

* **Landing Page:** A promotional front-end for the mobile application.
* **Post Management:** Allows users to view and manage game posts via a web interface.
* **Data Synchronization:** Mirrors functionality from the Android app, ensuring data consistency across platforms using
  Firebase Realtime Database and Firestore.
* **Secure Authentication:** Integrates Spring Security with Firebase Authentication.

## 🛠 Tech Stack

While the Android app focused on native mobile SDKs, this project was my playground for backend web technologies and
testing.

### Backend

* **Framework:** Spring Boot 3.2.2
* **Language:** Java 17
* **Template Engine:** Thymeleaf (with Spring Security extras)
* **Security:** Spring Security

### Data & Infrastructure

* **Firebase Admin SDK:** Used for backend access to Firestore and Realtime Database
* **Deployment:** Configured for Google App Engine
* **Build Tool:** Gradle (Kotlin DSL)

### Frontend Tools

* **Styling:** HTML, CSS, and `purgecss` for removing unused styles
* **Bundling:** Webpack

### Testing (The "Firsts")

Aside from learning a new framework, I had a specific goal for this project: **mastering testing**. I knew that to be a
better developer, I needed to understand how to verify my code automatically.

* This project contains my **first unit tests ever**.
* I learned to use **JUnit** for test structuring and **Mockito** to mock external dependencies (like the database).

## License and Use Restrictions

This project is created for educational and experimental purposes. All rights reserved.