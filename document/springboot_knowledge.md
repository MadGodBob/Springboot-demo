springboot 4.0

## 加入mybatis-plus

```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot4-starter</artifactId>
    <version>3.5.15</version>
</dependency>
```

新建application.yml，写入

```
server:
    port: 8090
spring:
    datasource:
        url: jdbc:mysql://mysql6.sqlpub.com:3311
        username: madgod
        password: JAl5GKI9UnkPBYJk
        driver-class-name: com.mysql.cj.jdbc.Driver
```

## 测试代码

在Application.java同根文件夹下创建包：entity	mapper	service	service/impl

创建User类	UserMapper接口	UserService接口	UserServiceImpl类

1. userMapper.selectList方法是继承于**MyBatis Plus**的库，自带数据库查询函数

```
@Data
public class User {
    private Integer id;
    private String no;
    private String name;
    private String password;
    private Integer sex;
    private Integer roleId;
    private String phone;
    @TableField("isValid")
    private String isValid;
}
```

```
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

```
public interface UserService extends IService<User> {
    List<User> list();
}
```

```
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> list() {
        return userMapper.selectList(null);
    }
}
```

##### 在controller中进行测试

```
@GetMapping("/list")
public List<User> list(){
    return userService.list();
}
```

## 使用代码编辑器生成代码

三种依赖分别是为了代码生成器、生成器模板、swagger调试

```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.5.15</version>
</dependency>
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.32</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

新建common包，添加CodeGenerator.java

```
package com.wms.demo.common;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;

import java.nio.file.Paths;
import java.util.Collections;

public class CodeGenerator {
    /*
        参数配置 表名 作者 工作目录 父类包名称 数据库url 数据库账号 数据库密码
    */
    public static String tableName = "test";
    public static String author = "pengbo";
    public static String workspace = "/demo";
    public static String workspaceParent = "com.wms.demo";
    public static String MySQL_url = "jdbc:mysql://mysql6.sqlpub.com:3311/madgod?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    public static String usename = "madgod";
    public static String password = "JAl5GKI9UnkPBYJk";

    public static void main(String[] args) {
        FastAutoGenerator.create(MySQL_url, usename, password)
                .globalConfig(builder ->
                        builder.author(author)
                                .disableOpenDir()
                                .enableSwagger()
                                .outputDir(Paths.get(System.getProperty("user.dir")) + workspace + "/src/main/java")
                )
                .packageConfig(builder ->
                        builder.pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir")+workspace+"/src/main/resources/mapper"))
                                .parent(workspaceParent)
                                .entity("entity")
                                .mapper("mapper")
                                .service("service")
                                .serviceImpl("service.impl")
                )
                .strategyConfig(builder ->
                        builder.addInclude(tableName)
                                .enableSkipView()
                                .entityBuilder().enableLombok(new ClassAnnotationAttributes("@Data","lombok.Data"))
                                .mapperBuilder().mapperAnnotation(Mapper.class)
                                .controllerBuilder()
                                .disable()
                                .mapperBuilder()
                                .mapperXmlTemplate("/templates/simple-mapper.xml")
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
```

在templates文件夹下新建xml的模板文件simple-mapper.xml.ftl

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <!-- 基础结果映射 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
        <#list table.fields as field>
            <#if field.keyFlag>
                <id column="${field.name}" property="${field.propertyName}" />
            <#else>
                <result column="${field.name}" property="${field.propertyName}" />
            </#if>
        </#list>
    </resultMap>

    <!-- 基础字段列表 -->
    <sql id="Base_Column_List">
        <#list table.fields as field>
            ${field.name}<#if field_has_next>, </#if>
        </#list>
    </sql>

</mapper>
```

## swagger调试

```
http://localhost:8090/swagger-ui/index.html
```

## 模糊匹配

```
@PostMapping("/listFuzzyByName")
public List<User> listFuzzyByName(@RequestBody User user){
    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
    lambdaQueryWrapper.like(User::getName, user.getName());
    return userService.list(lambdaQueryWrapper);
}
```

## 分页

#### 使用方法：Mybatis的分页拦截器。此外还有1.编写分页mapper方法 2.自定义xml文件中的SQL语言

在Mybatis-plus 3.5.9后分页功能需要单独导入依赖

```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-jsqlparser</artifactId>
    <version>3.5.15</version>
</dependency>
```

添加MybatisPlusConfig类

```
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

定义分页类QueryPageData。PAGE_SIZE和PAGE_NUM是默认参数

```
@Data
public class QueryPageData {
    private static int PAGE_SIZE = 20;
    private static int PAGE_NUM = 1;

    private int pageSize = PAGE_SIZE;
    private int pageNum = PAGE_NUM;

    // data
    private HashMap data;
}
```

实现分页查询的简单测试。首先获取到请求中的pageSize pageNum，模糊搜索name：getData()得到HashMap，使用get("name")获取值但类型为泛型V因此需要强转，最后用IPage分页类进行承接

```
public List<User> listPage(@RequestBody QueryPageData query){
    Page<User> page = new Page();
    page.setCurrent(query.getPageNum());
    page.setSize(query.getPageSize());
    String name = (String)query.getData().get("name");

    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
    lambdaQueryWrapper.like(User::getName, name);

    IPage result = userService.page(page, lambdaQueryWrapper);
    System.out.println(result.getTotal());

    return result.getRecords();
}
```

## 返回前端数据的封装

创建Result类，返回的数据包含HTTP状态码 提示信息 返回数据长度 返回数据

```
@Data
public class Result {
    private static Integer SUCCESS_CODE = 200;
    private static Integer ERROR_CODE = 400;

    private Integer code;
    private String msg;
    private Long total;
    private Object data;

    // 成功
    public static Result success(){
        return setResult(SUCCESS_CODE, "success", 0L, null);
    }
    // 成功(带data)
    public static Result success(Object data){
        return setResult(SUCCESS_CODE, "success", 0L, data);
    }
    // 成功(带data和total)
    public static Result success(Object data, Long total){
        return setResult(SUCCESS_CODE, "success", total, data);
    }

    // 失败
    public static Result error(Object data){
        return setResult(ERROR_CODE, "error", 0L, data);
    }

    // 设置方法
    public static Result setResult(Integer code, String msg, Long total, Object data){
        Result res = new Result();
        res.setCode(code);
        res.setMsg(msg);
        res.setTotal(total);
        res.setData(data);
        return res;
    }
}

```

示例

```
Result.success(result.getRecords(), result.getTotal());
```

