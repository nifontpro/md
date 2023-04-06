/** @type {import('next').NextConfig} */
const nextConfig = {
  poweredByHeader: false,
  env: {
    KEYCLOAK_URL: process.env.KEYCLOAK_URL,
    APP_URL: process.env.APP_URL,
    API_SERVER_URL: process.env.API_SERVER_URL,
  },
  experimental: {
    appDir: true,
  },
}

module.exports = nextConfig
