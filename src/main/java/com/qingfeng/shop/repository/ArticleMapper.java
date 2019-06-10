package com.qingfeng.shop.repository;


import com.qingfeng.shop.bean.Article;
import com.qingfeng.shop.utils.Pager;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ArticleMapper 数据访问类
 */
public interface ArticleMapper {

    //List<Article> searchArticles();
    //传参数
    List<Article> searchArticles(@Param("typeCode") String typeCode,
                                 @Param("secondType") String secondType,
                                 @Param("title") String title,
                                 @Param("pager") Pager pager);


    //统计当前条件下的总数
    int count(@Param("typeCode") String typeCode,
              @Param("secondType") String secondType, @Param("title") String title);

   //数据删除
    //为什么是xx，因为只有一个参数，所有变量随便写
    @Delete("delete from ec_article where id = #{xxxx}")
    void deleteById(String id);

    //预览商品界面
    @Select("select * from ec_article where id = #{dsafa}")
    //用到结果集映射，从articlemapper.xml
    @ResultMap("articleResultMap")
    Article getArticleById(String id);


    void update(Article article);

    void save(Article article);

}