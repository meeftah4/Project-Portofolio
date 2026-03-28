// Frontend API Configuration
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

export const API_ENDPOINTS = {
  // Auth
  REGISTER: `${API_BASE_URL}/auth/register`,
  LOGIN: `${API_BASE_URL}/auth/login`,

  // CV Management
  GET_CVS: `${API_BASE_URL}/cv`,
  CREATE_CV: `${API_BASE_URL}/cv`,
  GET_CV: (id: number) => `${API_BASE_URL}/cv/${id}`,
  UPDATE_CV: (id: number) => `${API_BASE_URL}/cv/${id}`,
  DELETE_CV: (id: number) => `${API_BASE_URL}/cv/${id}`,

  // AI Analysis
  REVIEW_CV: (id: number) => `${API_BASE_URL}/cv/${id}/ai/review`,
  ATS_SCORE: (id: number) => `${API_BASE_URL}/cv/${id}/ai/ats-score`,
  KEYWORDS: (id: number) => `${API_BASE_URL}/cv/${id}/ai/keywords`,
  GRAMMAR_CHECK: (id: number) => `${API_BASE_URL}/cv/${id}/ai/grammar`,
};

export const getAuthHeader = (token?: string) => {
  const finalToken = token || localStorage.getItem('token');
  return {
    'Authorization': `Bearer ${finalToken}`,
    'Content-Type': 'application/json',
  };
};
