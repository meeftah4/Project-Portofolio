import React from 'react';

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">🚀 AI CV Builder</h1>
          <div className="space-x-4">
            <a href="/login" className="text-gray-600 hover:text-blue-600">Login</a>
            <a href="/register" className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700">Register</a>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 py-20">
        <div className="text-center">
          <h2 className="text-5xl font-bold text-gray-900 mb-4">
            Build Your AI-Powered Resume
          </h2>
          <p className="text-xl text-gray-600 mb-8">
            Create, analyze, and optimize your CV with cutting-edge AI technology
          </p>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-12">
            <div className="bg-white p-8 rounded-lg shadow-md hover:shadow-lg transition">
              <div className="text-4xl mb-4">✏️</div>
              <h3 className="text-xl font-semibold mb-2">Create & Edit</h3>
              <p className="text-gray-600">Build beautiful, professional CVs with an intuitive editor</p>
            </div>
            
            <div className="bg-white p-8 rounded-lg shadow-md hover:shadow-lg transition">
              <div className="text-4xl mb-4">🤖</div>
              <h3 className="text-xl font-semibold mb-2">AI Analysis</h3>
              <p className="text-gray-600">Get AI-powered reviews and improve your CV instantly</p>
            </div>
            
            <div className="bg-white p-8 rounded-lg shadow-md hover:shadow-lg transition">
              <div className="text-4xl mb-4">📊</div>
              <h3 className="text-xl font-semibold mb-2">ATS Score</h3>
              <p className="text-gray-600">Check your ATS compatibility and boost your chances</p>
            </div>
          </div>

          <a 
            href="/register" 
            className="mt-12 inline-block bg-blue-600 text-white px-8 py-4 rounded-lg text-lg font-semibold hover:bg-blue-700 transition"
          >
            Get Started Now 🎯
          </a>
        </div>
      </main>
    </div>
  );
}
