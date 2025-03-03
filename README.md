# NanoWeb

NanoWeb is a lightweight web framework built on top of Javalin, designed to simplify the creation of RESTful web services using annotations.

## Features

- Annotation-based request mapping
- Support for request parameters, path parameters, request bodies, and more
- Authentication handler support
- Easy integration with Gson for JSON serialization/deserialization

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>net.rivalgames</groupId>
    <artifactId>nanoweb</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


### Building the Project

To build the project, run the following command in the root directory:

```sh
mvn clean install
```

## Quick Start

```java
@RestController("api")
public class MyController {
    @MethodMapping("hello")
    public Response sayHello() {
        return new Response("Hello World", HttpStatus.OK);
    }
}

public class Main {
    public static void main(String[] args) {
        NanoWeb web = new NanoWeb(8080);
        web.registerControllers(new MyController());
        web.register();
    }
}
```

## Authentication

Implement custom authentication by creating a handler:

```java
public class CustomAuthHandler implements AuthenticationHandler {
    @Override
    public Boolean handle(Context context) {
        // Your authentication logic here
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
```

## Available Annotations

| Annotation          | What It Does                                                                                               |
|---------------------|------------------------------------------------------------------------------------------------------------|
| `@RestController`   | Marks a class as a controller. It sets a base path for all the routes in that class.                         |
| `@MethodMapping`    | Connects a controller method to a URL path. You can also set the HTTP method (default is GET).               |
| `@RequestBody`      | Puts the request body (often JSON) into a method parameter.                                                |
| `@PathParam`        | Takes a part of the URL path and passes it to the method.                                                  |
| `@RequestParam`     | Retrieves query parameters from the URL. You can provide a default value if needed.                        |
| `@RequestFormParam` | Handles form data. It also works with file uploads if the parameter type is `UploadedFile`.                  |
| `@RequestContext`   | Passes the whole request context (with request and response details) to the method.                           |
| `@RequestAttribute` | Gets a previously set attribute from the request context.                                                 |