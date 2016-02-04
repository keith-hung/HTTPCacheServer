# Tiny HTTP CACHE SERVER

## Build and Start Service
	
	gradle clean build;
	java -jar build/output/HTTPCacheService-1.0.jar 8080

use http method `POST` to set data and use `GET` to get data

	EX: 
	1. key = GOGOGO, value = POWERRANGER
	for set data, send request to http://localhost:8080/GOGOGO, use method `POST` and body `POWERRANGER`
	for get data, send request to http://localhost:8080/GOGOGO, use method `GET` and it'll return `POWERRANGER` on body
