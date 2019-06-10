package com.qingfeng.shop.action;

import com.qingfeng.shop.bean.ArticleType;
import com.qingfeng.shop.service.ShopService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.WebConnection;
import java.io.IOException;
import java.util.List;

//servlet配一个地址
@WebServlet("/getArticleTypes")
public class ArticleTypeServlet extends HttpServlet {

    //2、定义业务层对象
    //这个却不能注入进来，因为ArticleTypeServlet没有交给spring进行管理，
    // spring不会把其他对象注入给他
    //@Resource
    private ShopService shopService;


    //3、定义一个初始化的方法
    @Override
    public void init() throws ServletException {
        super.init();
        //获取spring容器，然后容器中得到业务层对象
        //首先ServletContext上下文对象
        ServletContext servletContext =this.getServletContext();
        //然后拿到WebApplicationContext对象，通过这个上下文对象，得到spring的上下文对象
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        //拿到spring容器，通过容器得到想要的bean，从而得到业务层对象
        shopService= (ShopService) context.getBean("shopService");

    }

    //重写一个servlet方法，重写我们的请求
    //调用业务层进行功能查询
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
        //1、查询出所有的商品类型信息

        //4、业务层拿到后写一个方法
        List<ArticleType> articleTypes =shopService.getArticleTypes();
        System.out.println(articleTypes);


    }
}
