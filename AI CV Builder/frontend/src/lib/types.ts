// Frontend TypeScript Types
export interface User {
  id: number;
  email: string;
  fullName: string;
  profilePicture?: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  fullName: string;
  userId: number;
  message: string;
}

export interface CV {
  id: number;
  title: string;
  description: string;
  content: string; // JSON format
  atsScore?: number;
  aiReview?: string;
  keywordSuggestions?: string;
  grammarAnalysis?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CVContent {
  personalInfo: {
    fullName: string;
    email: string;
    phone: string;
    location: string;
    title?: string;
  };
  summary?: string;
  experience: Array<{
    company: string;
    position: string;
    startDate: string;
    endDate: string;
    description: string;
  }>;
  education: Array<{
    school: string;
    degree: string;
    field: string;
    graduationDate: string;
  }>;
  skills: string[];
  certifications?: Array<{
    name: string;
    issuer: string;
    date: string;
  }>;
}
