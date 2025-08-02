FROM nginx:alpine
COPY build/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
ENV REACT_APP_API_URL=http://31.97.110.118:8081
EXPOSE 80