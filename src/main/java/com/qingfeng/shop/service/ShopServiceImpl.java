package com.qingfeng.shop.service;

import com.qingfeng.shop.bean.Article;
import com.qingfeng.shop.bean.ArticleType;
import com.qingfeng.shop.bean.User;
import com.qingfeng.shop.repository.ArticleMapper;
import com.qingfeng.shop.repository.ArticleTypeMapper;
import com.qingfeng.shop.repository.UserMapper;
import com.qingfeng.shop.utils.Pager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 实现类
 * 实现类实现接口
 * 业务层需要交给spring容器来管理的
 * 为业务层申请一个bean对象，用注解方式来做
 */
@Service("shopService")
public class ShopServiceImpl implements ShopService {

    //得到数据访问层对象
    @Resource
    private ArticleTypeMapper articleTypeMapper;

    @Resource
    private ArticleMapper articleMapper;

    //注入用户数据去查用户表中的数据
    @Resource
    private UserMapper userMapper;

    @Override
    //查询所有的商品类型
    public List<ArticleType> getArticleTypes() {
        return articleTypeMapper.getArticleTypes();
    }

    @Override
    public Map<String, Object> login(String loginName, String passWord) {
        //最好是用map封装结果的
        Map<String,Object> results = new HashMap<>();
        // 1.判断参数是否为空的
        if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passWord)){
            // 参数为空了
            results.put("code",1);
            results.put("msg","参数为空了");
        }else{
            // 根据登陆名称去查询用户对象

            //到userMapper数据访问层持久层对象中创建要登录的对象
            User user =userMapper.login(loginName);
            //传回来做一个判断
            if(user !=null){
                // 判断密码
                if(user.getPassword().equals(passWord)){
                    // 登陆成功了
                    // 应该将登陆成功的用户存入到Session会话中
                    results.put("code",0);
                    //吧登陆成功的用户扔回去
                    results.put("msg",user);
                }else{
                    // 密码错误了
                    results.put("code",2);
                    results.put("msg","密码错误了");
                }
            }else{
                // 登陆名不存在
                results.put("code",3);
                results.put("msg","登陆名不存在");
            }

        }
        return results;
    }

    //检索这个一级商品类型
    @Override
    public List<ArticleType> loadFirstArticleTypes() {
        List<ArticleType> articleTypes = articleTypeMapper.getFirstArticleTypes();
        return articleTypes;
    }

    @Override
    public List<ArticleType> loadSecondArticleTypes(String typeCode) {
        List<ArticleType> articleTypes = articleTypeMapper.loadSecondTypes(typeCode +"%",typeCode.length()+4);
        //怎么查询二级类型
        return articleTypes;
    }

    @Override
    public void deleteById(String id) {
        articleMapper.deleteById(id);
    }

    @Override
    public Article getArticleById(String id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public void updateArticle(Article article) {
        articleMapper.update(article);
    }

    @Override
    public void saveArticle(Article article) {
        article.setCreateDate(new Date());
        articleMapper.save(article);
    }

    @Override
    public List<Article> searchArticles(String typeCode, String secondType, String title
                        , Pager pager) {
        //界面需要当前总共有多少条数据
        //查询当前条件喜喜啊总共有多少条数据
        int count =articleMapper.count(typeCode,secondType,title);
        pager.setTotalCount(count);
        return articleMapper.searchArticles(typeCode,secondType,title,pager);
    }

}
