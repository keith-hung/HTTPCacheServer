# Tiny HTTP CACHE SERVER

use http method `POST` to set data and use `GET` to get data

	EX: 
	1. key = GOGOGO, value = POWERRANGER
	for set data, send request to http://localhost:8080/GOGOGO, use method `POST` and body `POWERRANGER`
	for get data, send request to http://localhost:8080/GOGOGO, use method `GET` and it'll return `POWERRANGER` on body
