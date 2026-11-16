# Pagination and search

`GET /api/v1/products?page=0&size=20&search=key&categoryId=3` returns Spring's `Page` JSON with content, page metadata and total counts. Pages start at zero. Size must be 1-100; sort is currently server-controlled by descending ID so clients cannot request arbitrary entity properties.

Large text search is a contains query and may need a SQL Server full-text/indexing strategy as volume grows. Measure before changing the query. Do not remove pagination from list endpoints.


