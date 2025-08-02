FROM nginx:alpine
COPY build/ /usr/share/nginx/html
ENV REACT_APP_API_URL=${REACT_APP_API_URL}
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80