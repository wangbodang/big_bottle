package com.tool.generator;

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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器
 */
@Slf4j
public class AutoGenerator_PostgreSQL {

    @Test
    public void crtCode(){

        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=vefuture";
        String username = "postgres";
        String password = "7241";


        //表名集合
        List<String> tables = new ArrayList<>();
        tables.add("b_vefuture_big_bottle");
        //tables.add("dne_commencement_settlement_ds");
        //tables.add("dne_initial_design_ds");

        // 使用函数里的表名列表
        /*
        List<String> bizList = getTabNameList();
        tables.clear();
        bizList.forEach(biz->tables.add(biz));
        */

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
                            //.addTablePrefix("CUX_KS_")//表名前缀，配置后生成的代码不会有此前缀
                            .serviceBuilder()
                                .enableFileOverride()
                                .formatServiceFileName("%sService")//服务层接口名后缀
                                .formatServiceImplFileName("%sServiceImpl")//服务层实现类名后缀
                            .entityBuilder()
                                .enableFileOverride()
                                .enableLombok()//实体类使用lombok,需要自己引入依赖
                                //.logicDeleteColumnName("status")//逻辑删除字段，使用delete方法删除数据时会将status设置为1。调用update方法时并不会将该字段放入修改字段中，而是在条件字段中
                                .enableTableFieldAnnotation()//加上字段注解@TableField
                                .addIgnoreColumns(strings)//添加要忽略的数据库中列
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
                //设置生成哪些类
                .templateEngine(new VelocityTemplateEngine()).templateConfig(builder -> {
                    /*
                    builder.controller("")
                           .service("")
                           .serviceImpl("")
                           .mapper("")
                           .mapperXml("");
                    */

                }).execute();
    }

    public List<String> getTabNameList(){
        String[] nameArr = new String[]{
                "dne_commencement_settlement_ds",
                "dne_commencement_start_ds",
                "dne_commencement_supervisor_ds",
                "dne_design_change_ds",
                "dne_dev_fund_plan_ds",
                "dne_initial_design_ds",
                "dne_intermediate_acceptanc_ds",
                "dne_login_user_ds",
                "dne_material_acquisition_ds",
                "dne_material_delivery_ds",
                "dne_material_return_ds",
                "dne_monthly_plan_ds",
                "dne_node_plan_ds",
                "dne_node_time_plan_ds",
                "dne_pj_feasibility_material_ds",
                "dne_prj_bidding_ds",
                "dne_prj_complete_accep_ds",
                "dne_prj_contract_ds",
                "dne_prj_defect_ds",
                "dne_prj_feasibility_ds",
                "dne_prj_name_ds",
                "dne_prj_requirement_ds",
                "dne_prj_visa_change_ds",
                "dne_project_basic_info_ds",
                "dne_reconnaissance_at_criminal_scene_ds",
                "dne_single_project_ds",
                "dne_site_disclosure_ds",
                "dne_stop_return_work_ds",
                "dne_supl_score_ds",
                "dne_supplier_ds",
                "dne_task_ticket_ds",
                "dne_three_measures_one_ds"
        };
        List<String> tempList = Arrays.asList(nameArr);
        List<String> result = new ArrayList<>();
        result.addAll(tempList);
        return result;
    }
}
