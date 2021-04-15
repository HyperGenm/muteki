#使用spring-boot-starter-actuator关闭
curl -X POST 127.0.0.1:8085/weiziplus/shutdown
echo 项目关闭,端口号:8080
#后台运行springboot
nohup java -jar springboot-1.0.jar --server.port=8080 >/dev/null 2>&1 &
echo 项目启动中,端口号:8080
tail -f nohup.out