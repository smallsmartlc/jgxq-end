package com.jgxq;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author smallsmart
 * @date 2020/12/6 13:49
 * @description
 */
public class MybatisPlusGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }



    /**
     * 会生成在 temp 临时包下面
     *
     * @param args
     */
    public static void main(String[] args) {

        /**
         * 需要修改的动态参数
         * 1、是否覆盖已存在的文件
         * 2、需要生成的表名
         * 3、不需要某一层  设置为 null
         */
        //  bug = "执行前请注意参数配置，别把重要类覆盖了。";
        boolean fileOverride = false;
        // String tableNameStr ="";
        String tableNameStr = "talk";
        boolean notNeedController = false;
        boolean notNeedService = false;
        boolean notNeedMapper = false; // 注意mapper中是否有自定义sql
        boolean notNeedEntity = false;
        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName("temp");
        pc.setParent("com.jgxq.front");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");


        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setPackageInfo(pc);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        projectPath = projectPath + "//src//main";
        // String projectPath ="D://111coding//01working//000000v1.1.7//teamwork-server//teamwork-tabulation//src//main";
        gc.setOutputDir(projectPath + "//java");
        gc.setAuthor("smallsmart");
        gc.setOpen(false);//是否打开资源管理器
        gc.setServiceName("%sService");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setIdType(IdType.AUTO);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        gc.setBaseResultMap(false);// XML ResultMap
        // 是否覆盖已存在的文件
        gc.setFileOverride(fileOverride);


        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/forum?useUnicode=true&characterEncoding=UTF8&useSSL=false");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("password");
        dsc.setDbType(DbType.MYSQL);
        // 自定义类型转换
        dsc.setTypeConvert(new MySqlTypeConvert(){
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("json")){
                    System.out.println("转换类型：" + fieldType);
                    return DbColumnType.STRING;
                } else if (t.contains("tinyint")) {
                    return DbColumnType.BYTE;
                } else if (t.contains("timestamp") || t.contains("date") || t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        mpg.setDataSource(dsc);



        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity 这个模板有点问题
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        String finalProjectPath = projectPath;
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return finalProjectPath + "//resources/mapper"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        // 不需要某一层就设置为null
        if (notNeedController){
            templateConfig.setController(null);
        }
        if (notNeedService){
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
        }
        if (notNeedMapper){
            templateConfig.setMapper(null);
        }
        if (notNeedEntity){
            templateConfig.setEntity(null);
        }

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        // 控制台输入表名
        //strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        // 固定写死，需要生成的 表名
        strategy.setInclude(tableNameStr.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
