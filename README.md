# SYSC 4806 Lab 5 - Address Book

[![build](../../actions/workflows/maven.yml/badge.svg)](../../actions/workflows/maven.yml)

Student: Samy Kaddour

This is a Spring Boot address book app with a REST API, a Thymeleaf page,
automated unit and integration tests, GitHub Actions CI, and Azure App Service
deployment support.

## Run locally

The project uses Java 21.

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

Run all tests with:

```bash
mvn verify
```

## Main API routes

| Method | Route | Purpose |
| --- | --- | --- |
| `POST` | `/api/address-books` | Create an address book |
| `POST` | `/api/buddies` | Create a buddy with name, address, and phone |
| `GET` | `/api/address-books/{id}` | Get an address book as JSON |
| `PUT` | `/api/address-books/{bookId}/buddies/{buddyId}` | Add a buddy to a book |
| `GET` | `/address-books/{id}` | View an address book page |

## Continuous integration

The Maven CI workflow uses JDK 21 and runs `mvn verify` for every push and pull
request. This runs the unit, mock web, and real HTTP integration tests before
building the application JAR.

## Azure deployment

Azure deployment: add the live `https://<app-name>.azurewebsites.net` link here
after Azure assigns the unique app name.

The deployment workflow is ready for Azure App Service. After creating a Java
21 Web App, add these GitHub Actions settings:

- Repository variable `AZURE_WEBAPP_NAME`: the Azure Web App name.
- Repository secret `AZURE_WEBAPP_PUBLISH_PROFILE`: the complete downloaded
  publish profile.

Push to `main` after both values are configured. GitHub Actions will test,
package, and deploy the JAR automatically.
