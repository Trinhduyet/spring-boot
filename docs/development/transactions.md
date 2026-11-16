# Transactions

`OrderService.create` owns one transaction that writes order/line rows, reserves inventory, and records audit rows. If any reservation fails, the order and all earlier reservations roll back. `InventoryService` locks the stock row with `PESSIMISTIC_WRITE`; database checks preserve `0 <= reserved <= on_hand`.

Spring `@Transactional` is proxy-based. A method calling another transactional method on `this` bypasses advice; call through a separate injected bean or restructure the use case. Avoid holding a DB transaction during remote HTTP calls. If asynchronous delivery is added, persist an outbox row in the same transaction and make consumers idempotent.


