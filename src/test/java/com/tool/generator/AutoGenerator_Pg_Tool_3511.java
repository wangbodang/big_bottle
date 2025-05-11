package com.tool.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器 使用3.5.11版本的生成器
 */
@Slf4j
public class AutoGenerator_Pg_Tool_3511 {

    @Test
    public void crtCode(){

        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=vefuture";
        String username = "postgres";
        String password = "7241";


        //表名集合
        List<String> tables = new ArrayList<>();
        //tables.add("b_vefuture_big_bottle");
        //tables.add("dne_commencement_settlement_ds");
        //tables.add("dne_initial_design_ds");

        // 使用函数里的表名列表
        List<String> bizList = getTabNameList();
        tables.clear();
        bizList.forEach(biz->tables.add(biz));

        //添加要忽略表中的字段
        String[] strings = new String[]{
                "FOOKTHIS"
        };



        //数据源配置
        DataSourceConfig.Builder dsConfig = new DataSourceConfig.Builder(url, username, password);
        //dsConfig.schema("power_ds");

        //创建数据库中类型到Java实体类中的数据类型的对应关系
        // Note: This custom type converter seems overly complex for just handling 'date'.
        // The default PostgreSqlTypeConvert usually handles standard types well.
        // Consider simplifying or removing this if default conversion works.
        dsConfig.typeConvert(new ITypeConvert() {
                                 @Override
                                 public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                                     String t = fieldType.toLowerCase();
                                     // Ensure this matches your actual PostgreSQL date/timestamp types accurately
                                     if(t.contains("timestamp") || t.contains("date")){ // More robust check
                                         // Make sure DateType.TIME_PACK is compatible with this choice
                                         return DbColumnType.DATE;
                                     }
                                     // Use the default converter for everything else
                                     return new PostgreSqlTypeConvert().processTypeConvert(globalConfig,fieldType);
                                 }
                             }
        );

        FastAutoGenerator.create(dsConfig)
                //全局配置
                .globalConfig(builder -> {
                    //builder.fileOverride()   //   开启覆盖之前生成的文件 (Uncomment if needed)
                    builder.disableOpenDir()  //禁止打开输出目录
                            .outputDir(System.getProperty("user.dir") + "\\src\\main\\java")   //指定输出目录
                            .author("wangchao")   //作者名
                            //.enableKotlin()      //开启 kotlin 模式
                            //.enableSwagger()     //开启 swagger 模式
                            .dateType(DateType.TIME_PACK)   //时间策略 java.time 包下的类
                            .commentDate("yyyy-MM-dd");   //注释日期
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent("com.vefuture.big_bottle.web")     //父包名
                            .moduleName("vefuture") // Generally not needed if parent covers it
                            .entity("entity")                 //Entity 包名
                            //.service("service")             //  Service 包名 (Still needed for pathInfo below if XML location depends on it, but won't generate Java files if disabled)
                            //.serviceImpl("service.impl")    //Service Impl 包名 (Same as above)
                            .mapper("mapper")               //Mapper 包名
                            .xml("mapper.xml")              //  Mapper XML 包名 (Logical name within resources)
                            //.controller("controller")       //Controller 包名 (Same as above)
                            //.other("config")                //自定义文件包名 输出自定义文件时所用到的包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "\\src\\main\\resources\\mapper"));//指定xml物理位置
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix("sys_", "b_vef_") // ✅ 可以添加多个前缀
                            //.addTablePrefix("CUX_KS_")//表名前缀，配置后生成的代码不会有此前缀

                            // ----- REMOVE OR COMMENT OUT controllerBuilder and serviceBuilder -----
                            /*
                            .serviceBuilder() // REMOVE THIS BLOCK
                                .enableFileOverride()
                                .formatServiceFileName("%sService")//服务层接口名后缀
                                .formatServiceImplFileName("%sServiceImpl")//服务层实现类名后缀
                            */
                            .entityBuilder()
                            .enableFileOverride() // Enable override for entity if needed
                            .idType(IdType.ASSIGN_ID)  // ✅ 使用雪花 ID
                            .logicDeleteColumnName("is_delete") // ✅ 开启逻辑删除支持 (Ensure column exists)
                            .enableLombok()//实体类使用lombok
                            .enableTableFieldAnnotation()//加上字段注解@TableField
                            .addIgnoreColumns(strings)//添加要忽略的数据库中列
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT), // Ensure these columns exist
                                    new Column("update_time", FieldFill.INSERT_UPDATE)
                            )
                            /*
                            .controllerBuilder() // REMOVE THIS BLOCK
                                .enableFileOverride()
                                .formatFileName("%sController")//控制类名称后缀
                                .enableRestStyle() // Use @RestController
                            */
                            .mapperBuilder()
                            .enableFileOverride() // Enable override for mapper/xml if needed
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation() //@Mapper annotation
                            .formatXmlFileName("%sMapper");
                })
                //模板配置: 禁用不需要的模板
                /*.templateConfig(builder -> builder.disable(
                        TemplateType.CONTROLLER,   // 禁用 Controller 模板
                        TemplateType.SERVICE,      // 禁用 Service 接口模板
                        TemplateType.SERVICE_IMPL  // 禁用 Service 实现类模板
                        // You could potentially disable ENTITY, MAPPER, XML here too if needed
                ))*/
                //模板引擎配置
                //设置生成哪些类
                .strategyConfig(builder -> builder
                        .entityBuilder()
                        .enableLombok()
                        .mapperBuilder()
                        // --- 下面是 Mapper 接口 和 XML 的相关配置 ---
                        .superClass(BaseMapper.class)     // 设置 Mapper 接口的父类 (可选)
                        .enableMapperAnnotation()       // 给 Mapper 接口添加 @Mapper 注解 (可选)
                        .formatMapperFileName("%sMapper") // 设置 Mapper 接口 文件名格式 (推荐)

                        // --- 主要影响 XML 的配置 ---
                        .formatXmlFileName("%sMapper")    // 设置 XML 文件名格式 (推荐)
                        .enableBaseResultMap()          // 生成基础 ResultMap (可选)
                        .enableBaseColumnList()         // 生成基础 ColumnList (可选)
                        // .enableFileOverride()        // 如果需要覆盖已存在的 Mapper 和 XML 文件 (可选)

                        // ================= Service 层配置 =================
                        /*
                        .serviceBuilder()
                        // 设置 Service 接口 文件名格式， %s 会被替换为实体类名
                        .formatServiceFileName("%sService") // 生成 IUserService (如果实体是User)
                        // 设置 Service 实现类 文件名格式
                        .formatServiceImplFileName("%sServiceImpl") // 生成 UserServiceImpl
                        //.superServiceClass(YourBaseService.class) // 设置自定义 Service 接口的父接口 (可选)
                        //.superServiceImplClass(YourBaseServiceImpl.class) // 设置自定义 Service 实现类的父类 (可选)
                        .enableFileOverride() // 允许覆盖已存在的 Service 接口和实现类文件 (可选)
                        */

                        // ================= Controller 层配置 =================
                        /*
                        .controllerBuilder()
                        // 设置 Controller 类 文件名格式
                        .formatFileName("%sController") // 生成 UserController
                        //.superClass(YourBaseController.class) // 设置自定义 Controller 的父类 (可选)
                        .enableRestStyle() // 启用 @RestController 注解 (前后端分离常用)
                        //.enableHyphenStyle() // 开启驼峰转连字符(hyphen)命名规则, 如 'userList' 映射为 'user-list' (现在较少用, Restful风格更常见)
                        .enableFileOverride() // 允许覆盖已存在的 Controller 文件 (可选)
                        */

                )
                .templateEngine(new VelocityTemplateEngine())

                .execute();
    }

    public List<String> getTabNameList(){
        String[] nameArr = new String[]{
                /*"sys_users",
                "sys_roles",
                "sys_user_roles",
                "sys_resources",
                "sys_role_resources"*/
                "b_vef_black_list"
        };
        // Use List.of for immutable list or new ArrayList<>(Arrays.asList(...)) for mutable
        return new ArrayList<>(Arrays.asList(nameArr));
    }
}