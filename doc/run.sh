 #后台运行springboot
nohup java -jar springboot-1.0.jar --server.port=8080 >/dev/null 2>&1 &
echo 项目启动中,端口号:8080
tail -f nohup.out