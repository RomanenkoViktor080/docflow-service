# Анализ производительности и использования индексов

**Сценарий 1:** Фоновая обработка пачками (SUBMIT/APPROVE-worker) и ендпоин поиска

**Запрос:** Выборка документов в определенном статусе для пакетной обработки.

```sql
EXPLAIN
(ANALYZE, BUFFERS)
SELECT *
FROM documents
WHERE status = 'DRAFT'
ORDER BY created_at DESC LIMIT 100;
```

```
Limit  (cost=0.28..6.05 rows=1 width=61) (actual time=0.020..0.037 rows=20 loops=1)
  Buffers: shared hit=22
  ->  Index Scan using idx_documents_status_created_at on documents  (cost=0.28..6.05 rows=1 width=61) (actual time=0.019..0.034 rows=20 loops=1)
        Index Cond: ((status)::text = 'DRAFT'::text)
        Buffers: shared hit=22
Planning Time: 0.081 ms
Execution Time: 0.063 ms
```

**Пояснение:**
Был выбран индекс idx_documents_status_created_at. База мгновенно переходит к блоку данных со статусом DRAFT.
Отсутствие сортировки: Благодаря тому, что created_at DESC является второй частью индекса, данные уже отсортированы.

**Сценарий 2**: Поиск по автору и периоду

```sql
EXPLAIN
(ANALYZE, BUFFERS)
SELECT *
FROM documents
WHERE author_id = 500
  AND created_at >= '2026-01-01'
ORDER BY created_at DESC;
```

```
Index Scan using idx_documents_author_created_at on documents  (cost=0.28..6.05 rows=1 width=61) (actual time=0.012..0.012 rows=0 loops=1)
Index Cond: ((author_id = 500) AND (created_at >= '2024-01-01 00:00:00+03'::timestamp with time zone))
Buffers: shared hit=2
Planning Time: 0.070 ms
Execution Time: 0.024 ms
```

**Пояснение:**
Использован индекс idx_documents_author_created_at.
Поле author_id быстро сужает поиск до документов одного пользователя.
Диапазон: Так как created_at идет вторым в покрывающем индексе, PostgreSQL эффективно выполняет поиск по диапазону дат
внутри записей этого автора.

**Сценарий 3**: Поиск по периоду

```sql
EXPLAIN
(ANALYZE, BUFFERS)
SELECT *
FROM documents
WHERE created_at >= '2026-01-01';
```

**Пояснение:**
Использован индекс idx_documents_created_at.
Эффективность при фильтрации: индекс используется, когда в запросе отсутствует author_id или status. Он позволяет базе
данных быстро найти начало диапазона в B-Tree и прочитать только нужные записи.
Условие использования: PostgreSQL выберет этот индекс при условии, что диапазон дат достаточно узкий (высокая
селективность).

**Прочее:**

```
1) idx_document_history_initiator_created_at - для поиска записей изменений по инициатору и дате создания
2) idx_document_history_document_created_at Для получения документа с историей изменений (FETCH JOIN) и для поиска
   записей изменений по документу и дате создания
3) documents_number_key для гарантированости уникальноссти номера документа
4) document_approvals_document_id_key - поиск документа в регистре утверждений
```

