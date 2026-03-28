// CV Store (Zustand)
import { create } from 'zustand';
import { CV } from '@/lib/types';
import { API_ENDPOINTS, getAuthHeader } from '@/lib/api';
import axios from 'axios';

interface CVState {
  cvs: CV[];
  currentCV: CV | null;
  isLoading: boolean;
  error: string | null;
  fetchCVs: () => Promise<void>;
  fetchCV: (id: number) => Promise<void>;
  createCV: (title: string, description: string, content: string) => Promise<CV>;
  updateCV: (id: number, title: string, description: string, content: string) => Promise<void>;
  deleteCV: (id: number) => Promise<void>;
  analyzeCVWithAI: (id: number, type: 'review' | 'ats' | 'keywords' | 'grammar') => Promise<string | number>;
}

export const useCVStore = create<CVState>((set, get) => ({
  cvs: [],
  currentCV: null,
  isLoading: false,
  error: null,

  fetchCVs: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axios.get<CV[]>(API_ENDPOINTS.GET_CVS, {
        headers: getAuthHeader(),
      });
      set({ cvs: response.data, isLoading: false });
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Failed to fetch CVs' });
    }
  },

  fetchCV: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axios.get<CV>(API_ENDPOINTS.GET_CV(id), {
        headers: getAuthHeader(),
      });
      set({ currentCV: response.data, isLoading: false });
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Failed to fetch CV' });
    }
  },

  createCV: async (title: string, description: string, content: string) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axios.post<CV>(API_ENDPOINTS.CREATE_CV, {
        title,
        description,
        content,
      }, {
        headers: getAuthHeader(),
      });
      set((state) => ({
        cvs: [...state.cvs, response.data],
        isLoading: false,
      }));
      return response.data;
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Failed to create CV' });
      throw error;
    }
  },

  updateCV: async (id: number, title: string, description: string, content: string) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axios.put<CV>(API_ENDPOINTS.UPDATE_CV(id), {
        title,
        description,
        content,
      }, {
        headers: getAuthHeader(),
      });
      set((state) => ({
        cvs: state.cvs.map((cv) => (cv.id === id ? response.data : cv)),
        currentCV: response.data,
        isLoading: false,
      }));
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Failed to update CV' });
    }
  },

  deleteCV: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await axios.delete(API_ENDPOINTS.DELETE_CV(id), {
        headers: getAuthHeader(),
      });
      set((state) => ({
        cvs: state.cvs.filter((cv) => cv.id !== id),
        isLoading: false,
      }));
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Failed to delete CV' });
    }
  },

  analyzeCVWithAI: async (id: number, type: 'review' | 'ats' | 'keywords' | 'grammar') => {
    set({ isLoading: true, error: null });
    try {
      let url = '';
      if (type === 'review') url = API_ENDPOINTS.REVIEW_CV(id);
      else if (type === 'ats') url = API_ENDPOINTS.ATS_SCORE(id);
      else if (type === 'keywords') url = API_ENDPOINTS.KEYWORDS(id);
      else if (type === 'grammar') url = API_ENDPOINTS.GRAMMAR_CHECK(id);

      const response = await axios.post(url, {}, {
        headers: getAuthHeader(),
      });
      set({ isLoading: false });
      return response.data;
    } catch (error: any) {
      set({ isLoading: false, error: error.response?.data?.message || 'Analysis failed' });
      throw error;
    }
  },
}));
