IMAGE_NAME=scootrent-app:latest

# TODO не забыть про -DskipTests
build:
	./mvnw clean package spring-boot:build-image -Dspring-boot.build-image.imageName=$(IMAGE_NAME) -DskipTests


up:
	docker compose up -d


down:
ifeq ($(VOLUMES),true)
	docker compose down -v
else
	docker compose down
endif

rebuild: build down up


logs:
	docker compose logs -f app
