# muteki
*springboot和vue搭建的前后端分离的后台管理模板,简便resultful接口开发*
*闪耀吧! 宛如流星一般*

## <a href="http://39.96.52.201/muteki/vuepress" target="_blank">说明文档</a>
   * 文档更新中。。。

## <a href="https://gitee.com/WeiziPlus/springboot2-vue3-old" target="_blank">原项目地址</a>

## 上手指南
以下指南将帮助你在本地机器上安装和运行该项目，进行开发和测试。关于如何将该项目部署到在线环境，请参考部署小节。
### 安装要求、步骤
   * 安装配置Java环境， *JDK1.8*
   * 安装 *mysql 8* ，创建数据库( *utf8mb4* , *utf8mb4_general_ci* )导入sql(doc目录下)
   * 安装 *redis* ,下载安装即可
   * 开发工具需要安装 *lombok* 插件(开发工具推荐IDEA)
   * ---后端运行(如果出问题一般是yml配置文件中数据源之类的配置出错)
   * 安装配置 *node* 环境
   * 安装 *vue-cli* ,进入vue目录执行`npm install`
   * ---前端运行`npm run serve`( *WebStorm* 或者 *IDEA* 可以直接点击 *package.json* 文件中第6行左边绿三角)

### 演示地址
   * 在线演示地址
       [ant-design-vue版,vue3.0](http://39.96.52.201/muteki/vue-ant)
       [element版,vue2.5](http://39.96.52.201/muteki/vue)  
        
        |  账号  |  密码  |
        |  :---: | :---: |
        |  superadmin | 111111 |
        |  admin | 111111 |
        |  admin1 | 111111 |
        * *tip:多个用户同时登陆可能会被顶掉,superadmin超级管理员才可以看到所有页面*
        * *tip:演示环境已经禁止部分增删改操作*
        * *tip:如果有其他异常，请强制刷新页面(有可能是缓存问题),如果还不行请提issues*
   * [接口文档](http://39.96.52.201/muteki/doc.html)
        * *swagger-bootstrap*
        * *tips:请在请求前面手动添加/muteki;请求会提示时间戳错误，请求请携带__t=当前时间戳*

## 部署
   * springboot目录下运行`mvn clean package`命令打包,打包后生成文件在/target/build目录下
        * config目录为存放的配置文件
        * lib目录为maven依赖的jar包
        * static目录存放静态文件
        * jar文件为生成的jar包(以后pom依赖不变的话可以只替换该jar包)
   * vue目录下运行`npm run build`命令打包，打包后生成文件在/dist目录下
        * 打包配置在 *.env* 文件和 *vue.config.js* 文件中
   * 部署服务器上面需要配置 *JDK1.8* 、 *mysql 8* 、 *redis* 环境
   * jar包运行`nohup java -jar springboot.jar &`可以在后台运行并且将日志输出到当前目录下 *nohup.out* 文件
   * 部署服务器建议配置 *nginx* ,vue打包后放在nginx下,不配置可以放在 *jar* 包同一目录 *static* 下面
   
## 常见错误
   * `java.lang.RuntimeException: Cannot resolve classpath entry: E:\maven-repository\mysql\mysql-connector-java\8.0.15\mysql-connector-java-8.0.15.jar`
       * 出错: 根据数据库生成实体类
       * 解决: *resources/config/generator-config.xml* 第6行jar包路径改成自己的jar包路径
   
### 后端:
#### springboot、mybatis、redis
1. 简介
    * 基于 *springboot* 模板创建的项目
2. 基本配置
    * *.yml* 文件可以配置相关信息
    * *config* 目录下为常用模块配置
    * *filter* 配置了 *跨域* 、  *参数过滤*等
        * *参数过滤* 请求 *自动去除前后空格* ,使用了 *Jsoup* 过滤 *html标签* (可以自定义配置过滤级别)
3. 权限管理
    * 带有`@AuthToken`注解的接口，请求头必须有 *token* 才能访问
    * 配合vue前端页面动态渲染路由，以及隐藏显示按钮:按钮保存在`this.$globalFun.getSessionStorage('buttonMap')`中(前端)
    * 精确到接口级别权限，必须完善 *功能管理* 中菜单或者按钮对应的 *对应api* ,否则存在 *token* 后默认放行
4. 封装厂用CURD,继承 *BaseService* 即可
    * `baseInsert()`和`baseUpdate()`会自动过滤值为null的字段
5. 根据数据库自动生成实体类 
    * 运行 *org.mybatis.generator.plugin.MyBatisTest.main()* 方法
    * 具体配置 *resources/config/generator-config.xml* 
6. 日志按天存放，具体配置在 *resources/config/logback-spring.xml* 
7. 根据数据库自动生成数据库文档
    * 运行 *org.screw.ScrewTest.testScrew()* 方法

### 前端:
#### vue:
1. 简介
    * 基于*vue cli*创建的项目
    * 界面UI:*element-ui*
    * 网络请求:*axios*
        * 全局调用方式 `this.$axios({
                          url: '',
                          data: {},
                          success(data) {}
                     });`
            * url:只需要域名之后的地址
            * success:回调只需要处理 *code为200* 的情况
    * 全局变量和方法在 /src/utils目录下
    * 覆盖element-ui样式在 /src/assets/sass/element-variables.scss 文件
    * 项目中大部分都有注释

2. 基本配置
    * 配合后端实现动态路由:功能管理表单填写path路径，默认根路径为 _/src/views/main/**/*/Index.vue/_
    * *.env.production/development* 文件和 *vue.config.js* 为配置文件
                     
3. 封装的常用组件
    * dialog:弹出框  
        * detail: 标题+内容的方式展示数据
        * form: 表单提交,`@submit`只需要处理表单验证之后的情况
        * index: 普通弹出框
    * table:表格
        * 表格接受的返回示例: `{"list":[],"pageNum":1,"pageSize":10}`
        * 表格数据请求 `tableDataRequest: {
                                      url: '',
                                      data: {}
                        }`
            * url:请求地址
            * data:额外参数，配合顶部搜索使用
        * 表格展示 `tableColumns: [
                         {prop: 'username', label: '用户名',formatter(){
                               return '';
                         }}
                    ]` 接受一个数组---参考layui表格
            * formatter: 复杂展示，可以返回一个dom

## 鸣谢
   首先感谢 *springboot* 、 *vue* 、*element-ui* 等优秀的开源项目  
   其次该项目参考了很多网上的示例，如果看到相似的代码，**那么，答案只有一个了**