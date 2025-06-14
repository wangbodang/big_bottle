package com.tool.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.baomidou.mybatisplus.generator.config.TemplateType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器
 */
@Slf4j
public class AutoGenerator_Pg_Rbac {

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
        dsConfig.typeConvert(new ITypeConvert() {
                                 @Override
                                 public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                                     String t = fieldType.toLowerCase();
                                     //这里是把数据库中datetime的类型变成Java的data类型的数据
                                     if(t.contains("date")){
                                         return DbColumnType.DATE;
                                     }
                                     /*

                                     //这里是把数据库中number的类型变成Java的long类型的数据
                                     if(t.contains("bigint")){
                                         return DbColumnType.LONG;
                                     }
                                     if(t.contains("integer")){
                                         return DbColumnType.INTEGER;
                                     }
                                     if(t.contains("numberic")){
                                         return DbColumnType.BIG_DECIMAL;
                                     }
                                     */
                                     //指定数据库类型
                                     //其它字段采用默认转换（非mysql数据库可以使用其它默认的数据库转换器）
                                     return new PostgreSqlTypeConvert().processTypeConvert(globalConfig,fieldType);
                                 }
                             }
        );

        FastAutoGenerator.create(dsConfig)
                //全局配置
                .globalConfig(builder -> {
                    //builder.fileOverride()   //	开启覆盖之前生成的文件
                    builder.disableOpenDir()  //禁止打开输出目录
                            .outputDir(System.getProperty("user.dir") + "\\src\\main\\java")   //指定输出目录
                            .author("wangchao")   //作者名
                            //.enableKotlin()      //开启 kotlin 模式
                            //.enableSwagger()     //开启 swagger 模式
                            .dateType(DateType.TIME_PACK)   //时间策略
                            .commentDate("yyyy-MM-dd");   //注释日期
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent("com.vefuture.big_bottle.web")     //父包名
                            .moduleName("vefuture") // 设置父包模块名，这里一般不设置
                            .entity("entity")                 //Entity 包名
                            .service("service")             //	Service 包名
                            .serviceImpl("service.impl")    //Service Impl 包名
                            .mapper("mapper")               //Mapper 包名
                            .xml("mapper.xml")              //	Mapper XML 包名
                            .controller("controller")       //Controller 包名
                            //.other("config")                //自定义文件包名	输出自定义文件时所用到的包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "\\src\\main\\resources\\mapper"));//指定xml位置
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix("b_vef_")  // ✅ 这句会自动去掉 sys_，实体类名仍是 UserEntity 等
                            //.addTablePrefix("CUX_KS_")//表名前缀，配置后生成的代码不会有此前缀
                            .serviceBuilder()
                                .enableFileOverride()
                                .formatServiceFileName("%sService")//服务层接口名后缀
                                .formatServiceImplFileName("%sServiceImpl")//服务层实现类名后缀
                            .entityBuilder()
                                .idType(IdType.ASSIGN_ID)  // ✅ 添加这一行，使用雪花 ID
                                //.logicDeleteColumnName("is_delete") // ✅ 开启逻辑删除支持
                                .enableFileOverride()
                                .enableLombok()//实体类使用lombok,需要自己引入依赖
                                //.logicDeleteColumnName("status")//逻辑删除字段，使用delete方法删除数据时会将status设置为1。调用update方法时并不会将该字段放入修改字段中，而是在条件字段中
                                .enableTableFieldAnnotation()//加上字段注解@TableField
                                .addIgnoreColumns(strings)//添加要忽略的数据库中列
                                .addTableFills(
                                        new Column("create_time", FieldFill.INSERT),
                                        new Column("update_time", FieldFill.INSERT_UPDATE)
                                )
                            .controllerBuilder()
                                .enableFileOverride()
                                .formatFileName("%sController")//控制类名称后缀
                                .enableRestStyle()
                            .mapperBuilder()
                                .enableFileOverride()
                                .superClass(BaseMapper.class)
                                .formatMapperFileName("%sMapper")
                                .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                            //.entityBuilder()

                })
                // … globalConfig、packageConfig、strategyConfig 略
                .templateConfig(builder -> builder.disable(
                        TemplateType.CONTROLLER,   // 不生成 Controller
                        TemplateType.SERVICE,      // 不生成 Service 接口
                        TemplateType.SERVICE_IMPL   // 不生成 ServiceImpl
                ))
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }

    public List<String> getTabNameList(){
        String[] nameArr = new String[]{
                /*"sys_users",
                "sys_roles",
                "sys_user_roles",
                "sys_resources",
                "sys_role_resources"
                "b_vef_process_log"
                "sys_oper_log",
                "sys_login_info"*/
                "b_vef_sent_token"
        };
        List<String> tempList = Arrays.asList(nameArr);
        List<String> result = new ArrayList<>();
        result.addAll(tempList);
        return result;
    }
}
