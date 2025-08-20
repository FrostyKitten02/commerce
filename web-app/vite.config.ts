import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    rollupOptions: {
      onwarn(warning, warn) {
        // Ignore TypeScript errors during build
        if (warning.code === 'TYPESCRIPT_ERROR') return
        warn(warning)
      }
    }
  },
  esbuild: {
    // Ignore TypeScript errors
    logOverride: { 'this-is-undefined-in-esm': 'silent' }
  }
})
