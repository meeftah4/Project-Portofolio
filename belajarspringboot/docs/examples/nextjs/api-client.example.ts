/*
 * Example Next.js client for this backend auth model.
 * Adjust imports/state management according to your app.
 */

declare const process: {
  env: Record<string, string | undefined>;
};

type UserInfo = {
  id: number;
  username: string;
  email: string;
  fullName: string | null;
  role: "USER" | "ADMIN";
  createdAt: string;
};

type AuthResponse = {
  token: string;
  accessToken: string;
  refreshToken: string;
  tokenType: "Bearer";
  accessTokenExpiresInHours: number;
  user: UserInfo;
};

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

let accessToken: string | null = null;
let refreshToken: string | null = null;
let currentUser: UserInfo | null = null;
let refreshPromise: Promise<string | null> | null = null;

export function setAuthSession(auth: AuthResponse) {
  accessToken = auth.accessToken;
  refreshToken = auth.refreshToken;
  currentUser = auth.user;

  if (typeof window !== "undefined") {
    localStorage.setItem("app_access_token", auth.accessToken);
    localStorage.setItem("app_refresh_token", auth.refreshToken);
    localStorage.setItem("app_user", JSON.stringify(auth.user));
  }
}

export function hydrateAuthSessionFromStorage() {
  if (typeof window === "undefined") {
    return;
  }

  accessToken = localStorage.getItem("app_access_token");
  refreshToken = localStorage.getItem("app_refresh_token");

  const rawUser = localStorage.getItem("app_user");
  if (rawUser) {
    try {
      currentUser = JSON.parse(rawUser) as UserInfo;
    } catch {
      currentUser = null;
    }
  }
}

export function clearAuthSession() {
  accessToken = null;
  refreshToken = null;
  currentUser = null;

  if (typeof window !== "undefined") {
    localStorage.removeItem("app_access_token");
    localStorage.removeItem("app_refresh_token");
    localStorage.removeItem("app_user");
  }
}

async function requestNewAccessToken(): Promise<string | null> {
  if (!refreshToken) {
    return null;
  }

  const res = await fetch(`${API_BASE_URL}/api/auth/refresh`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken }),
  });

  if (!res.ok) {
    return null;
  }

  const data = (await res.json()) as AuthResponse;
  setAuthSession(data);
  return data.accessToken;
}

async function getFreshAccessToken(): Promise<string | null> {
  if (!refreshPromise) {
    refreshPromise = requestNewAccessToken().finally(() => {
      refreshPromise = null;
    });
  }

  return refreshPromise;
}

export async function apiFetch(path: string, init: RequestInit = {}, retry = true): Promise<Response> {
  const headers = new Headers(init.headers ?? {});

  if (!headers.has("Content-Type") && init.body) {
    headers.set("Content-Type", "application/json");
  }

  if (accessToken) {
    headers.set("Authorization", `Bearer ${accessToken}`);
  }

  const res = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers,
  });

  if (res.status === 401 && retry) {
    const newAccessToken = await getFreshAccessToken();
    if (!newAccessToken) {
      clearAuthSession();
      return res;
    }

    return apiFetch(path, init, false);
  }

  return res;
}

export async function apiFetchJson<T = unknown>(path: string, init: RequestInit = {}): Promise<T> {
  const res = await apiFetch(path, init);

  if (!res.ok) {
    const errBody = await safeParseJson(res);
    throw new Error(
      (errBody as { message?: string } | null)?.message ?? `HTTP ${res.status}: ${res.statusText}`,
    );
  }

  if (res.status === 204) {
    return undefined as T;
  }

  return (await res.json()) as T;
}

export async function loginWithPassword(email: string, password: string) {
  const res = await fetch(`${API_BASE_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) {
    const errBody = await safeParseJson(res);
    throw new Error((errBody as { message?: string } | null)?.message ?? "Login gagal");
  }

  const auth = (await res.json()) as AuthResponse;
  setAuthSession(auth);
  return auth;
}

export async function registerWithPassword(payload: {
  username: string;
  email: string;
  password: string;
  fullName?: string;
}) {
  const res = await fetch(`${API_BASE_URL}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const errBody = await safeParseJson(res);
    throw new Error((errBody as { message?: string } | null)?.message ?? "Register gagal");
  }

  const auth = (await res.json()) as AuthResponse;
  setAuthSession(auth);
  return auth;
}

export async function logout() {
  const tokenBeforeLogout = accessToken;
  const refreshBeforeLogout = refreshToken;

  try {
    await fetch(`${API_BASE_URL}/api/auth/logout`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(tokenBeforeLogout ? { Authorization: `Bearer ${tokenBeforeLogout}` } : {}),
      },
      body: JSON.stringify({ refreshToken: refreshBeforeLogout }),
    });
  } finally {
    clearAuthSession();
  }
}

async function safeParseJson(res: Response): Promise<unknown | null> {
  try {
    return await res.json();
  } catch {
    return null;
  }
}
