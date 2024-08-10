部署Java后端程序

0.  前提需要jdk1.8和mysql8.x环境

1. 服务器安全组开启数据库端口（默认3306）和java程序启动的端口（8101）

2. 执行sql文件夹下的init_table.sql文件,完成初始化建表操作

3. 切换到该路径, cd /root/llm_project/ 

3. 将打包好的jar包放在 /root/llm_project/

4. 执行Java后台命令

   ```shell
   nohup java -jar -Xms1024m -Xmx2048m springboot-init-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=prod>> ./register.log 2>&1 &
   ```

5. 查看是否运行成功

   ```shell
   ps -ef | grep java
   lsof -i :8101 
   ```

**Ps:日志文件生成在 /root/llm_project/ ，名为register.log。**