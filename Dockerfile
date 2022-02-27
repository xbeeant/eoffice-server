# 基础镜像使用java
FROM openjdk:8-jdk-alpine

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp

# 将jar包添加到容器中并更名为app.jar
ADD build/libs/*-SNAPSHOT.jar /home/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms512m","-Xmx1024m", "-Dfile.encoding=UTF-8", "-jar","/home/app.jar"]