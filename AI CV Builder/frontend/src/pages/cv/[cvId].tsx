import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuthStore } from '@/store/authStore';
import { useCVStore } from '@/store/cvStore';
import { CVContent } from '@/lib/types';

const defaultCV: CVContent = {
  personalInfo: {
    fullName: '',
    email: '',
    phone: '',
    location: '',
    title: '',
  },
  summary: '',
  experience: [],
  education: [],
  skills: [],
  certifications: [],
};

export default function CVEditorPage() {
  const router = useRouter();
  const { cvId } = router.query;
  const { user } = useAuthStore();
  const { currentCV, fetchCV, createCV, updateCV } = useCVStore();
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [cvData, setCVData] = useState<CVContent>(defaultCV);
  const [aiAnalysis, setAiAnalysis] = useState({
    review: '',
    atsScore: 0,
    keywords: '',
  });

  useEffect(() => {
    if (!user) {
      router.push('/login');
      return;
    }

    if (cvId && cvId !== 'new') {
      fetchCV(Number(cvId));
    }
  }, [cvId, user, router, fetchCV]);

  useEffect(() => {
    if (currentCV) {
      setTitle(currentCV.title);
      setDescription(currentCV.description);
      try {
        setCVData(JSON.parse(currentCV.content));
      } catch {
        setCVData(defaultCV);
      }
    }
  }, [currentCV]);

  const handlePersonalInfoChange = (field: string, value: string) => {
    setCVData((prev) => ({
      ...prev,
      personalInfo: { ...prev.personalInfo, [field]: value },
    }));
  };

  const handleSave = async () => {
    if (!title.trim()) {
      alert('Please enter a CV title');
      return;
    }

    const contentJSON = JSON.stringify(cvData);

    try {
      if (cvId === 'new') {
        const newCV = await createCV(title, description, contentJSON);
        router.push(`/cv/${newCV.id}`);
      } else {
        await updateCV(Number(cvId), title, description, contentJSON);
      }
      alert('CV saved successfully!');
    } catch (err) {
      alert('Error saving CV');
    }
  };

  if (!user) return null;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navbar */}
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">🚀 CV Editor</h1>
          <div className="flex gap-2">
            <button
              onClick={() => router.push('/dashboard')}
              className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700"
            >
              Back
            </button>
            <button
              onClick={handleSave}
              className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 font-semibold"
            >
              Save CV
            </button>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Editor Panel */}
          <div className="lg:col-span-2 space-y-6">
            {/* Title & Description */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-xl font-semibold mb-4">CV Details</h2>
              <div className="space-y-4">
                <div>
                  <label className="block text-gray-700 font-semibold mb-2">CV Title</label>
                  <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg"
                    placeholder="e.g., Senior Developer CV"
                  />
                </div>
                <div>
                  <label className="block text-gray-700 font-semibold mb-2">Description</label>
                  <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg h-20"
                    placeholder="Optional description"
                  />
                </div>
              </div>
            </div>

            {/* Personal Information */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-xl font-semibold mb-4">Personal Information</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                  type="text"
                  placeholder="Full Name"
                  value={cvData.personalInfo.fullName}
                  onChange={(e) => handlePersonalInfoChange('fullName', e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
                <input
                  type="email"
                  placeholder="Email"
                  value={cvData.personalInfo.email}
                  onChange={(e) => handlePersonalInfoChange('email', e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
                <input
                  type="tel"
                  placeholder="Phone"
                  value={cvData.personalInfo.phone}
                  onChange={(e) => handlePersonalInfoChange('phone', e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
                <input
                  type="text"
                  placeholder="Location"
                  value={cvData.personalInfo.location}
                  onChange={(e) => handlePersonalInfoChange('location', e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
            </div>

            {/* Summary */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-xl font-semibold mb-4">Professional Summary</h2>
              <textarea
                value={cvData.summary || ''}
                onChange={(e) => setCVData((prev) => ({ ...prev, summary: e.target.value }))}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg h-24"
                placeholder="Write a brief summary of your professional background..."
              />
            </div>
          </div>

          {/* Preview & AI Analysis Panel */}
          <div className="space-y-6">
            {/* AI Analysis Tools */}
            <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg shadow-md p-6">
              <h2 className="text-xl font-semibold mb-4">🤖 AI Analysis</h2>
              <div className="space-y-3">
                <button className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 text-sm font-semibold">
                  📋 Review CV
                </button>
                <button className="w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 text-sm font-semibold">
                  📊 Check ATS Score
                </button>
                <button className="w-full bg-yellow-600 text-white py-2 rounded-lg hover:bg-yellow-700 text-sm font-semibold">
                  🔑 Keyword Suggestions
                </button>
                <button className="w-full bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700 text-sm font-semibold">
                  ✏️ Grammar Check
                </button>
              </div>
            </div>

            {/* Download */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-lg font-semibold mb-4">📥 Export</h2>
              <button className="w-full bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 font-semibold">
                Download PDF
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
