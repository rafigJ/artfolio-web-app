# В случае, если на хосте отсутствует node
#FROM node:20.9.0-alpine3.18 as build
#WORKDIR /app
#COPY . .
#RUN npm install
#RUN npm run build

FROM nginx:1.23.4-alpine3.17-slim
WORKDIR /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
#COPY --from=build /app/build /usr/share/nginx/html
COPY ./build /usr/share/nginx/html
COPY ./.nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]