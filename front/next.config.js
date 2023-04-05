/** @type {import('next').NextConfig} */
const nextConfig = {
  poweredByHeader: false,
  env: {
    KEYCLOAK_URL: process.env.KEYCLOAK_URL,
    APP_URL: process.env.APP_URL,
  },
  experimental: {
    appDir: true,
  },
}

module.exports = nextConfig
