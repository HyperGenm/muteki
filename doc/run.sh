 #清空原来的日志文件，并后台运行springboot
nohup java -jar springboot-1.0.jar --server.port=8080 >/dev/null 2>&1 &
