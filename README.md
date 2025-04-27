# ScootRent App

##  Запуск проекта
Для запуска используется makefile со следующими командами:

`make build` - собирает приложение и создаёт docker-образ

`make up` - запускает контейнеры через docker-compose

`make down` - останавливает контейнеры и удаляет их

`make down volumes` - останавливает контейнеры и удаляет связанные с ними тома

`make rebuild` - пересобирает образ, останавливает текущие контейнеры и перезапускает их

`make logs` - показывает логи приложения в реальном времени

---

## Swagger UI
Также доступен Swagger UI:

- **URL Swagger UI:**  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
