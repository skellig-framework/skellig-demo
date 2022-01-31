# Skellig Framework Demo
This demo project shows how to use various features of the Skellig Framework

[![skellig-core](https://img.shields.io/maven-central/v/org.skelligframework/skellig-junit-runner)](https://repo1.maven.org/maven2/org/skelligframework/)

# How to run

### pre-requsites: 
* Docker
* Java 17 (8+ should work too but not tested)
* Skellig Framework plugin for IntelliJ - _Optional_ (TBA)

### run the app and tests
1) In order to start up the app for testing, you need to run few docker containers which are used by the app. You can find `docker-compose.yml`
in resources folder of `skellig-demo-app`.

2) In `skellig-demo-app` run the spring-boot app `Application.java`. The `application.yaml` contains all its configurations.

3) In `skellig-demo-test` run `SkelligDemoTestRunner.java` with args: `-Dtest.profile=local`. This JUnit runner start all the tests in the `resources/tests`.

4) In `skellig-demo-test` run `BookingPerformanceRunner.java` with args: `-Dtest.profile=local` to be able to run performance tests in `resources/performance.sts`.
You can access to performance tests UI page in `localhost:7080`

   
Log level can be controlled from `log4j.properties` in `skellig-demo-test`

The args`-Dtest.profile=local` is just a demonstration that you can have many Skellig Configs and load them from the args.
