FROM mysql:8.0.28

ADD init.sql /docker-entrypoint-initdb.d

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=bytebills
ENV MYSQL_USER=bytebills
ENV MYSQL_PASSWORD=bytebills

EXPOSE 3306

CMD ["mysqld"]