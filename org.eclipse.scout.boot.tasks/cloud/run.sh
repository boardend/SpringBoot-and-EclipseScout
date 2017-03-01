{ \
java -jar cloud-eureka-server/target/cloud-eureka-server-0.1.0-SNAPSHOT.jar & \
java -jar cloud-tasks-service/target/cloud-tasks-service-0.1.0-SNAPSHOT.jar & \
java -jar cloud-tasks-api/target/cloud-tasks-api-0.1.0-SNAPSHOT.jar & \
java -jar cloud-tasks-gateway/target/cloud-tasks-gateway-0.1.0-SNAPSHOT.jar & \
java -jar cloud-hystrix-dashboard/target/cloud-hystrix-dashboard-0.1.0-SNAPSHOT.jar; \
} | cat
