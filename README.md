# 🔗 URL Shortener Service

A scalable, production-style URL Shortener built using Spring Boot, Redis, and PostgreSQL.
Designed with real-world backend principles such as caching, rate limiting, and eventual consistency.

---

## 🚀 Live Application

Swagger UI:

```
http://3.109.124.207:8080/swagger-ui.html
```

> The application is deployed on AWS EC2 and publicly accessible.

---

## ✨ Features

* 🔗 Shorten long URLs
* 🆔 Support for **custom alias (user-defined short code)**
* 🎲 Automatic **random short code generation**
* ⚡ Fast redirection using Redis caching
* 🛡 Rate limiting per IP using Redis
* 📊 Real-time click tracking (Redis + DB sync)
* 🔄 Eventual consistency for scalability
* 🧾 Global exception handling
* 📘 Swagger UI for API exploration

---

## 🏗️ Architecture Overview

```
                ┌──────────────┐
                │    Client    │
                └──────┬───────┘
                       │
                       ▼
              ┌─────────────────┐
              │   Controller    │
              └──────┬──────────┘
                     │
                     ▼
              ┌─────────────────┐
              │    Service      │
              └──────┬──────────┘
                     │
         ┌───────────┴───────────┐
         ▼                       ▼
 ┌───────────────┐       ┌───────────────┐
 │ PostgreSQL DB │       │     Redis     │
 │ (Persistent)  │       │ (Cache + CTR) │
 └───────────────┘       └───────────────┘
```

---

## 🔁 Request Flow

### 🔹 URL Shortening

```
Client → POST /shorten
       → Rate limit check (Redis)
       → Validate input
       → Generate short code (custom or random)
       → Save in DB
       → Return response
```

---

### 🔹 URL Redirection

```
Client → GET /{code}
       → Check Redis
           ├─ Cache Hit → return instantly ⚡
           └─ Cache Miss → fetch from DB → store in Redis
       → Increment click count (Redis)
       → Redirect (302)
```

---

### 🔹 Click Tracking

```
User Click
   ↓
Redis INCR (click:<code>)
   ↓
Scheduled Sync Job
   ↓
Batch update to DB
   ↓
Redis cleared (fresh cycle)
```

---

## 🔤 Custom Alias vs Auto Generated Code

### 🟢 Custom Alias

User can provide their own short code:

```json
{
  "longUrl": "https://google.com",
  "customAlias": "my-code"
}
```

👉 Result:

```
http://3.109.124.207:8080/my-code
```

* Checked for uniqueness
* Returns error if already exists

---

### 🎲 Auto Generated Code

If no alias is provided:

```json
{
  "longUrl": "https://google.com"
}
```

👉 System generates:

```
http://3.109.124.207:8080/aB12xY (System generated code)
```

* Random/Base32 generation
* Ensures uniqueness

---

## 📌 API Usage

---

### 🟢 Create Short URL

**POST** `/url_service/shorten`

```json
{
  "longUrl": "https://google.com",
  "customAlias": "optional-code"
}
```

---

### 🔁 Redirect

Open in browser:

```
http://3.109.124.207:8080/<shortCode>
```

👉 Automatically redirects to original URL

---

### 🔍 Get URL Info

```
GET /url_service/info/{code}
```

Returns original URL without redirect

---

### 📊 Get Analytics

```
GET /url_service/getAnalytics/{code}
```

```json
{
  "shortCode": "abc123",
  "clickCount": 25,
  "createdAt": "...",
  "lastAccessed": "..."
}
```

---

## ⚡ Performance Design

### 🔹 Redis Caching

* O(1) lookup time
* Reduces database load
* Handles high traffic efficiently

---

### 🔹 Click Tracking (Optimized)

* Uses Redis atomic `INCR`
* Avoids DB writes per request
* Batch sync ensures scalability

---

### 🔹 Rate Limiting

* Implemented per IP
* Prevents abuse and excessive requests

---

## 🧠 System Design Concepts

* Caching Layer (Redis)
* Eventual Consistency
* High Throughput Design
* Scalable Read Optimization
* Separation of Concerns

---

## ⚖️ Trade-offs

| Design Choice    | Benefit          | Trade-off                     |
| ---------------- | ---------------- | ----------------------------- |
| Redis caching    | Fast response    | Cache invalidation complexity |
| Batch DB updates | High scalability | Slight delay in analytics     |
| Rate limiting    | Protection       | Possible throttling           |

---

## 💼 Project Summary

A production-ready backend system demonstrating:

* Efficient caching using Redis
* Scalable click tracking design
* Real-world rate limiting
* Clean REST API design
* Cloud deployment on AWS

---

## 👨‍💻 Author

Rahul Rautela

---

