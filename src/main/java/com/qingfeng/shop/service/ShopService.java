package com.qingfeng.shop.service;

import com.qingfeng.shop.bean.Article;
import com.qingfeng.shop.bean.ArticleType;
import com.qingfeng.shop.utils.Pager;

import java.util.List;
import java.util.Map;

/**
 * 业务层接口
 */
public interface ShopService {
    //查所有商品类型
    List<ArticleType> getArticleTypes();

    Map<String, Object> login(String loginName, String passWord);

    List<ArticleType> loadFirstArticleTypes();

    List<Article> searchArticles(String typeCode, String secondType, String title, Pager pager);

    List<ArticleType> loadSecondArticleTypes(String typeCode);

    void deleteById(String id);

    Article getArticleById(String id);

    void updateArticle(Article article);

    void saveArticle(Article article);
}
