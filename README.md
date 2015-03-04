social-graph-api
================

[![Build Status](https://travis-ci.org/gvolpe/social-graph-api.svg)](http://travis-ci.org/gvolpe/social-graph-api)
This is an open source Social Graph API licensed under the Apache 2 license, quoted below.

## About the project

Social Graph API (SGA) was built on top of [Play! Framework](https://www.playframework.com/).

You can see a [live demo](https://social-graph-api.herokuapp.com) deployed at Heroku.

## Authentication

SGA uses [Silhouette](http://silhouette.mohiva.com/) with the JWTAuthenticator (Json Web Token) to secure the API.
To save the data of the authentication layer uses [Redis](http://redis.io).

## Social Graph Store

SGA uses [Neo4j](http://neo4j.com) as a graph store.
As a Scala client library uses [AnormCypher](http://anormcypher.org/).

## Authentication API usage

### Sign Up
Request:
```
curl -X POST -H "Content-Type: application/json" -d '{ "identifier": "gvolpe@github.com", "password":"123456" }' https://<HOST>/auth/signup
```

Responses:
* 200: Ok       --> Sign up ok
* 209: Conflict --> User already exists

### Sign In
```
curl -X POST -H "Content-Type: application/json" -d '{ "identifier": "gvolpe@github.com", "password":"123456" }' https://<HOST>/auth/signup
```
Responses:
* 200: Ok           --> Sign in ok
* 401: Unauthorized --> Wrong user and/or password

When Sign Up and/or Sign In http response code is 200 the response body is like this:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0MjU1NDExMTEsInN1YiI6ImU5MmIyNDA1NTQ5YzUzZWM3NWExZWMyYWFjYTllZTViMGQ0MWFkNDNmZmJkOTk0YzQzMmEzY2E4ZjhmODhlM2FkN2RjMjhiZmUwOWE5NzEyNWU3Y2I0YzcxNGY3NzAzOGQxNGQxMzlkOWY4NmEyZGQxMDRkNjkwMmU3MTQ4YzdhIiwiaXNzIjoicGxheS1zaWxob3VldHRlIiwianRpIjoiYmE1ZDE5NDMxODY2MzY1NzJhMDgwNzhlNmYwYTZlOGJiOWFjOGJlNzFlMmI0N2Y1NmM1MjQzZjc1MTcyOTQyZmVkMzdlZTRjZWZiNTM0M2ZmYzlkZThlN2JkNjRlMzdkNDE5Mjk3NmM4ZjI0ZjJhNDA5ZTBiOTZhNTMyOTQ3NmM5YzU0NjRiODgxNjUzZWJmY2Q1MzNkN2QyMDIxMzVmODEzZjE1YTBmZTk3OTYzZjczZDYxMmM3ZjcwMTljODIzYzA0OTUzYTJkYjIzZWY0MmY2NzUzZDExN2EzN2QyZjJhMmE5YzAxYzgwZjkzNTUwZjBkZmY3ZGRmYmE0N2FlMiIsImlhdCI6MTQyNTQ5NzkxMX0.ySOKjDPhX2Ghw5ZvWq9A_1BlW2oLl12ncEjzMCY5-ow",
  "expiresAt": "2015-03-05T04:38:31.795-03:00"
}
```

## Users API usage

### Retrieve all the users

#### GET /api/v1/users
Request:
```
curl -X GET -H 'X-Auth-Token:hash3d-t0k3n' https://<HOST>/api/v1/users
```
Response:
```json
[
  {
    "id": 5,
    "username": "foobar",
    "email": "foobar@github.com"
  },
  {
    "id": 6,
    "username": "gvolpe",
    "email": "gvolpe@github.com"
  },
  ...
]
```

### Retrieve user by Id

#### GET /api/v1/users/{id}
Request:
```
curl -X GET -H 'X-Auth-Token:hash3d-t0k3n' https://<HOST>/api/v1/users/6
```
Response:
```json
{
    "id": 6,
    "username": "gvolpe",
    "email": "gvolpe@github.com"
}
```

### Create an user

#### POST /api/v1/users
```
curl -X POST -H 'X-Auth-Token:hash3d-t0k3n' -H "Content-Type: application/json" -d '{ "username": "gvolpe", "email": "gvolpe@github.com" }' https://<HOST>/api/v1/users
```

### Delete an user and his relationships

#### DELETE /api/v1/users/{id}
```
curl -X DELETE -H 'X-Auth-Token:hash3d-t0k3n' https://<HOST>/api/v1/users/3
```

## Social Graph API usage

### Retrieve followers by Id

#### GET /api/v1/followers/{id}
Request:
```
curl -X GET -H 'X-Auth-Token:hash3d-t0k3n' https://<HOST>/api/v1/followers/4
```
Response:
```json
[
  {
    "id": 5,
    "username": "foobar",
    "email": "foobar@github.com"
  }
]
```

### Retrieve friends (AKA following) by Id

#### GET /api/v1/friends/{id}
Request:
```
curl -X GET -H 'X-Auth-Token:hash3d-t0k3n' https://<HOST>/api/v1/friends/2
```
Response:
```json
[
  {
    "id": 3,
    "username": "gvolpe",
    "email": "gvolpe@github.com"
  }
]
```

### Create a friendship

It creates a FRIEND relationship from A to B and a FOLLOWER relationship from B to A.

#### POST /api/v1/friendship
```
curl -X POST -H 'X-Auth-Token:hash3d-t0k3n' -H "Content-Type: application/json" -d '{ "me": 7, "friend": 3 }' https://<HOST>/api/v1/friendship
```

Delete FRIEND and FOLLOWER relationships between two users.

#### DELETE /api/v1/friendship
```
curl -X DELETE -H 'X-Auth-Token:hash3d-t0k3n' -H "Content-Type: application/json" -d '{ "me": 7, "friend": 3 }' https://<HOST>/api/v1/friendship
```

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with
the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.