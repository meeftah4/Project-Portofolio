import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuthStore } from '@/store/authStore';
import { useCVStore } from '@/store/cvStore';
import Link from 'next/link';

export default function DashboardPage() {
  const router = useRouter();
  const { user, logout } = useAuthStore();
  const { cvs, fetchCVs, isLoading, deleteCV } = useCVStore();

  useEffect(() => {
    if (!user) {
      router.push('/login');
      return;
    }
    fetchCVs();
  }, [user, router, fetchCVs]);

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this CV?')) {
      await deleteCV(id);
    }
  };

  if (!user) return null;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navbar */}
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">🚀 AI CV Builder</h1>
          <div className="flex items-center space-x-4">
            <span className="text-gray-600">Hello, {user.fullName}</span>
            <button
              onClick={handleLogout}
              className="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-3xl font-bold text-gray-900">My CVs</h2>
          <Link
            href="/cv/new"
            className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 font-semibold"
          >
            + Create New CV
          </Link>
        </div>

        {isLoading ? (
          <div className="text-center py-12">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          </div>
        ) : cvs.length === 0 ? (
          <div className="bg-white rounded-lg shadow-md p-12 text-center">
            <h3 className="text-xl font-semibold text-gray-900 mb-2">No CVs yet</h3>
            <p className="text-gray-600 mb-4">Create your first CV to get started</p>
            <Link
              href="/cv/new"
              className="inline-block bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
            >
              Create CV
            </Link>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {cvs.map((cv) => (
              <div
                key={cv.id}
                className="bg-white rounded-lg shadow-md hover:shadow-lg transition p-6"
              >
                <h3 className="text-xl font-semibold text-gray-900 mb-2">{cv.title}</h3>
                <p className="text-gray-600 text-sm mb-4">{cv.description}</p>
                
                {cv.atsScore && (
                  <div className="mb-4 p-3 bg-blue-50 rounded">
                    <p className="text-sm text-gray-600">ATS Score: <span className="font-bold text-blue-600">{cv.atsScore.toFixed(1)}%</span></p>
                  </div>
                )}

                <div className="flex gap-2">
                  <Link
                    href={`/cv/${cv.id}`}
                    className="flex-1 bg-blue-600 text-white text-center px-4 py-2 rounded-lg hover:bg-blue-700 text-sm"
                  >
                    Edit
                  </Link>
                  <button
                    onClick={() => handleDelete(cv.id)}
                    className="flex-1 bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 text-sm"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}
