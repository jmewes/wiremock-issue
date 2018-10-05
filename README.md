# Problem with Multipart Stub Test

This repo reproduced an issue which occurs due to incompatibilities between the Jetty version required by Wiremock
and the Jetty version delivered by Spring Boot 2.

See XXX for details.

## Reproduce the problem

In order to reproduce the problem, run the [MultipartStubTest](#).

```
git clone https://github.com/jmewes/wiremock-issue.git
cd wiremock-issue
./gradlew test
```
