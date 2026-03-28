import type { AppProps } from 'next/app';
import '../styles/globals.css';
import { useAuthStore } from '@/store/authStore';
import { useEffect } from 'react';

export default function App({ Component, pageProps }: AppProps) {
  const { initializeAuth } = useAuthStore();

  useEffect(() => {
    initializeAuth();
  }, [initializeAuth]);

  return <Component {...pageProps} />;
}
