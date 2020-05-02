#DockerFile - PostGreSQL 

#docker run  -d -p 5432:5432 jsonb/postgres

FROM postgres
ENV DB_HOST localhost
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgrespw 
ENV POSTGRES_DB logging 
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 5432