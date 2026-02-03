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
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}