IMAGE_NAME=scootrent-app:latest

build:
	./mvnw clean package spring-boot:build-image -Dspring-boot.build-image.imageName=$(IMAGE_NAME) -DskipTests


up:
	docker compose up -d


down:
	docker compose down


down volumes:
	docker compose down -v


rebuild: build down up


logs:
	docker compose logs -f app
