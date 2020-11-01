![CI (Build and Test)](https://github.com/flamestro/AgileIsTheNewOrange/workflows/CI%20(Build%20and%20Test)/badge.svg)

This application is an API that can be used for a KanBan/Scrum board UI.

Application runs on: `http://localhost:8080`

Metrics are on: `http://localhost:8081/actuator/prometheus`

Swagger is on: `http://localhost:8080/swagger-ui.html`


Needs to be dockerized: 

`docker run -d -p 27017-27019:27017-27019 --name mongodb mongo:4.0.4`

In Mongo Compass:

`mongodb://localhost:27017`