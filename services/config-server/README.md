# config-server

Central configuration service for TicketFlow.

By default it reads the repository-local `config-repo/` directory using the
Spring Cloud Config native backend. Run it from the repository root:

    $env:CONFIG_REPO_LOCATION = 'file:/' + ((Resolve-Path .\config-repo).Path -replace '\\', '/')
    .\mvnw.cmd -pl services/config-server spring-boot:run

Then verify a configuration:

    curl http://localhost:8888/customer-service/default

To use a Git backend instead, set `CONFIG_SERVER_BACKEND=git` and
`CONFIG_GIT_URI` before starting the service.
