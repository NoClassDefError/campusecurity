FROM java:8
VOLUME /eureka/
ADD ./target/eureka-1.0.jar ./
ENTRYPOINT ["java","-jar","eureka-1.0.jar"]
EXPOSE 8761