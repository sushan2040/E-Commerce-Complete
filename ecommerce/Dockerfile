FROM nginx:alpine
COPY build/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
ENV REACT_APP_API_URL=${REACT_APP_API_URL}
EXPOSE 80