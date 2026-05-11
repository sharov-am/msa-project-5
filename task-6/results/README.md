1) Добавлен контроллер JobRestController
2) Добавлен uri в паттерн logback-spring.xml
3) Добавлен MdcUriFilter.java для автоматической простановки uri в контектс запроса
4) Тестировал с помощью curl -X POST http://localhost:8080/api/v1/jobs/import-products
5) Логи смотрел в кибана с отдельными полями для traceid, spanid, uri