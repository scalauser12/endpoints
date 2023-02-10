# Http4s Server with OpenApi Endpoint

To run the application type `sbt run` in the root folder of the project.

## Endpoints:

### GET /current-value
Returns the current value of the counter.

### POST /increment
example body: `{"step": 10}`
Increments the current value of the counter by `step`.

### GET /documentation.json
Returns the OpenAPI documentaion.
