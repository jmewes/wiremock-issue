# Problem with Multipart Stub Test

This repo reproduces an issue which occurs due to incompatibilities between the Jetty version required by Wiremock
and the Jetty version delivered by Spring Boot 2.

See https://github.com/tomakehurst/wiremock/issues/1007 for details.

## Reproduce the problem

In order to reproduce the problem, run the [MultipartStubTest](https://github.com/jmewes/wiremock-issue-1007/blob/master/src/test/java/com/example/wiremockissue/MultipartStubTest.java).

```
git clone https://github.com/jmewes/wiremock-issue.git
cd wiremock-issue
./gradlew test
```
