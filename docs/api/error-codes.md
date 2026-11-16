# Error responses

Errors use RFC 9457 Problem Details with `type`, `title`, `status`, and `detail`. Expected status mapping: invalid body/query 400; product/category/order not found 404; duplicate SKU/category, optimistic conflict, insufficient stock, invalid state or idempotency race 409; missing or invalid HTTP Basic credentials 401. Treat `detail` as user-readable, not a machine key; clients should branch on status and any future stable `code` extension.


