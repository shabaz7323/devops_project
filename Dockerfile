FROM node:18-alpine
WORKDIR /app

# Copy only app folder content
COPY app/ .

RUN npm install
EXPOSE 3000

CMD ["node", "server.js"]
