// Auth Store (Zustand)
import { create } from 'zustand';
import { User, AuthResponse } from '@/lib/types';
import { API_ENDPOINTS, getAuthHeader } from '@/lib/api';
import axios from 'axios';

interface AuthState {
  user: User | null;
  token: string | null;
  isLoading: boolean;
  error: string | null;
  register: (email: string, password: string, fullName: string) => Promise<void>;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  initializeAuth: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  token: null,
  isLoading: false,
  error: null,

  register: async (email: string, password: string, fullName: string) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axios.post<AuthResponse>(API_ENDPOINTS.REGISTER, {
        email,
        password,
        fullName,
      });
      set({ isLoading: false });
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Registration failed' });
    }
  },

  login: async (email: string, password: string) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axios.post<AuthResponse>(API_ENDPOINTS.LOGIN, {
        email,
        password,
      });
      const { token, email: userEmail, fullName, userId } = response.data;
      
      localStorage.setItem('token', token);
      set({
        isLoading: false,
        token,
        user: { id: userId, email: userEmail, fullName },
      });
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Login failed' });
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    set({ token: null, user: null });
  },

  initializeAuth: () => {
    const token = localStorage.getItem('token');
    if (token) {
      set({ token });
      // TODO: Validate token on backend
    }
  },
}));
