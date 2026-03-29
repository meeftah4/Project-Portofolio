# Next.js Integration Guide

This guide shows how to connect a Next.js frontend to this backend with access-token + refresh-token flow.

## 1. Prepare frontend env

In your Next.js app, create `.env.local`:

```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

## 2. Add API client helper

Use the example in [docs/examples/nextjs/api-client.example.ts](examples/nextjs/api-client.example.ts) and adapt to your folder structure.

What it handles:

1. Attach `Authorization: Bearer <accessToken>` automatically.
2. Retry once on `401` by calling `/api/auth/refresh`.
3. Rotate both access and refresh token from refresh response.
4. Force logout when refresh fails.

## 3. Recommended auth flow in UI

1. Register/Login page calls `/api/auth/register` or `/api/auth/login`.
2. Save `accessToken`, `refreshToken`, and `user` to client auth store.
3. Protected pages use API client helper for all requests.
4. Logout button calls `/api/auth/logout` with refresh token body and access token header.

## 4. Example usage

```ts
import { apiFetchJson, loginWithPassword, logout } from "@/lib/api-client";

// login
const auth = await loginWithPassword(email, password);

// create CV
const newCv = await apiFetchJson("/api/cvs", {
  method: "POST",
  body: JSON.stringify({ title: "CV ATS - Andi" }),
});

// list CVs for current account
const cvs = await apiFetchJson("/api/cvs");

// logout
await logout();
```

## 5. Important notes

1. Current backend expects refresh token in request body, not cookie.
2. For higher security in production, consider moving refresh token to HttpOnly cookie with backend changes.
3. If role changes (for example user promoted to ADMIN), user should login again to get updated role claim.
