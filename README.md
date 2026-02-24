# Document Management Service

Сервис для управления жизненным циклом документов с фоновой пакетной обработкой и реестром утверждений.

## Стек технологий

* **Java 21 / Spring Boot 3**
* **PostgreSQL (Docker Compose)**
* **JPA/Hibernate**
* **Liquibase**
* **Gradle**

---

## Быстрый запуск

### 1. Поднятие инфраструктуры

Для запуска базы данных PostgreSQL выполните:

```bash
docker-compose up -d
```

### Запуск основного сервиса

```./gradlew bootRun```

### Запуск основного сервиса с утилитой генерации документов

Утилита считывает количество документов N из конфигурации и создает их через API

```./gradlew bootRun --args='generate'```

### Конфигурация воркеров

В файле application.yaml можно настроить поведение фоновых процессов:

Для проверки API вручную без вмешательства фоновых процессов, установите enabled: false

```yaml
workers:
  submit:
    enabled: true # Включение/выключение воркера
    delay: 1000 # Пауза между итерациями (мс)
    fetch-batch-size: 400  # Размер пачки для обработки
  approve:
    enabled: true
    delay: 5000
    fetch-batch-size: 100
```

### Мониторинг и логи

Логи выводятся напрямую в консоль приложения. Для удобства отладки в многопоточной среде реализована поддержка **traceId
** или логи между сервисам.

* **traceId**: Уникальный идентификатор, который привязывается к каждой операции. Позволяет отследить всю цепочку логов
  одной задачи, даже если они перемешаны в консоли логами других потоков.

**Пример формата лога:**
`2026-02-24 22:49:38.383 [app-task-33] INFO  c.e.d.s.d.DocumentBatchServiceImpl [ID:2142b2df-29fd-45b9-b1c9-49564c8ab257] - Прогресс обработки: 50/400 (12.5%)`

`2026-02-24 22:54:02.062 [scheduling-1] INFO  c.e.d.u.a.l.PerformanceLoggingAspect [ID:15d6a487-51b5-428b-854e-4f196e5c45fd] - [START] Запуск фонового процесса SUBMIT-worker`

`2026-02-24 23:09:35.581 [http-nio-8080-exec-32] INFO  c.e.d.s.d.DocumentBatchServiceImpl [ID:5612c0c1-ddfa-4865-af3b-03a3c5a3dbcd] - Полученно 400 документов на batch утверждение`

### Прочее:

1) Для фильтрации сущностей используется две сущности:
    - [Filter Builder](src/main/java/com/example/docflow_service/entity/filter/builder/BaseFilterBuilder.java) (
      Агрегатор фильтров для сущности, использует дженерики,
      пример [DocumentFilterBuilder](src/main/java/com/example/docflow_service/entity/filter/builder/document/DocumentFilterBuilder.java)).
      Компонент, который:
        1) принимает входные параметры поиска
        2) перебирает применимые фильтры
        3) формирует итоговый Specification
        4) возвращает его для выполнения запроса.
    - [Filter Items](src/main/java/com/example/docflow_service/entity/filter/filter_item/BaseFilter.java) (Отдельные
      фильтры,
      пример [DocumentStatusFilter](src/main/java/com/example/docflow_service/entity/filter/filter_item/document/DocumentStatusFilter.java)).
      Каждый фильтр инкапсулирует логику добавления условия в Specification.
      Например:
        1) фильтр по статусу
        2) фильтр по автору
        3) фильтр по периоду дат
2) Data Model: Поля author_id и initiator_id хранят только идентификаторы. Предполагается наличие внешнего
   user-service. В рамках масштабирования системы возможна реализация локальной таблицы пользователей,
   синхронизируемой через Kafka (чтобы не делать запрос в user-service).
3) В проекте подключён Checkstyle для контроля соблюдения правил кодирования. Запускается автоматически при билде или
   вручную ```./gradlew checkstyleMain```  ```./gradlew checkstyleTest```
4) [ApiExceptionHandler.java](src/main/java/com/example/docflow_service/exception/ApiExceptionHandler.java) - глобальный
   обработчик исключений, который:
    - Централизованно перехватывает исключения приложения
    - Преобразует их в единый формат ответа API
    - Устанавливает корректные HTTP-статусы
    - Логирует ошибки на соответствующем уровне
5) [DocumentGenerator.java](src/main/java/com/example/docflow_service/console/DocumentGenerator.java) - консольная
   команда для генерации файлов