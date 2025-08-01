#!/bin/bash
git pull origin main  # Pull latest changes
npm install --legacy-peer-deps          # Install dependencies
npm run build         # Build React app
pm2 restart serve     # Restart PM2


