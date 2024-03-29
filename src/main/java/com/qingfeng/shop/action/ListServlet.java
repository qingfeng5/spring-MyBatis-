package com.qingfeng.shop.action;

import com.qingfeng.shop.bean.Article;
import com.qingfeng.shop.bean.ArticleType;
import com.qingfeng.shop.service.ShopService;
import com.qingfeng.shop.utils.Pager;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
@MultipartConfig //申明这个servlet是要接受大文件的对象的
//servlet配一个地址
@WebServlet("/list")
public class ListServlet extends HttpServlet {

    //2、定义业务层对象
    //这个却不能注入进来，因为ArticleTypeServlet没有交给spring进行管理，
    // spring不会把其他对象注入给他
    //@Resource
    private ShopService shopService;

    //要使用请求对象，定义请求对象
    private HttpServletRequest request;
    private HttpServletResponse response;


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
        try {
            this.request=req;
            this.response=resp;
            //去参数之前编码
            request.setCharacterEncoding("UTF-8");
            /**
             * 实现点击一级类型展示
             * 并且查询对应类型下的商品
             * 下面创建根据条件查
             */
            String method =request.getParameter("method");
            switch (method){
                case "getAll":
                    //检索所有商品
                    //通过alt+enter创建方法到2
                    getAll();
                    break;
                case "deleteById":
                    deleteById();
                    break;
                case "preArticle":
                    preArticle();
                    break;
                case "showUpdate":
                    showUpdate();
                    break;
                case "updateArticle":
                    updateArticle();
                    break;
                case "addArticle":
                    addArticle();
                    break;


                /**
                 * 下面是以前只可以查一级商品列表
                  */
//            //1、查询出所有的一级商品类型信息
//            List<ArticleType> firstArticleTypes = shopService.loadFirstArticleTypes();
//
//            //2、查询所有的商品信息
//            List<Article> articles = shopService.searchArticles();
//
//            //写完了应该带回界面
//            req.setAttribute("firstArticleTypes",firstArticleTypes);
//            req.setAttribute("articles",articles);
//
//            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req,resp);
//
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //修改数据
    private void addArticle() throws ServletException, IOException, ParseException {
        // 接收界面提交的参数
        // 获取请求参数 ----普通表单元素
        String code = request.getParameter("code");
        String title = request.getParameter("titleStr");
        String supplier = request.getParameter("supplier");
        String locality = request.getParameter("locality");
        String price = request.getParameter("price");
        String putawayDate = request.getParameter("putawayDate");
        String storage = request.getParameter("storage");
        String description = request.getParameter("description");
        // 定义一个商品对象封装界面提交的参数
        Article article = new Article();
        // 发布时间 ： 2018-04-25 21:34:40
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        article.setPutawayDate(sdf.parse(putawayDate));

        // 接收用户可能上传的封面
        String imageUrl = receiveImage();
        article.setImage(imageUrl);
        ArticleType type = new ArticleType();
        type.setCode(code);
        article.setArticleType(type);
        article.setTitle(title);
        article.setSupplier(supplier);
        article.setLocality(locality);
        article.setPrice(Double.parseDouble(price));
        article.setStorage(Integer.parseInt(storage));
        article.setDescription(description);
        shopService.saveArticle(article);
        request.setAttribute("tip","添加商品成功");
        getAll();
    }
    private String receiveImage() {
        try{
            // 如果用户上传了这里代码是不会出现异常 了
            // 如果没有上传这里出现异常
            Part part = request.getPart("image");
            // 保存到项目的路径中去
            String sysPath = request.getSession().getServletContext().getRealPath("/resources/images/article");
            // 定义一个新的图片名称
            String fileName = UUID.randomUUID().toString() ;
            //  提取图片的类型
            // 上传文件的内容性质
            String contentDispostion = part.getHeader("content-disposition");
            // 获取上传文件的后缀名
            String suffix = contentDispostion.substring(contentDispostion.lastIndexOf("."), contentDispostion.length() - 1);
            fileName+=suffix ;
            // 把图片保存到路径中去
            part.write(sysPath+"/"+fileName);
            return fileName ;
        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    private void updateArticle() {
        // 接收界面提交的参数
        // 获取请求参数 ----普通表单元素
        String code = request.getParameter("code");
        String title = request.getParameter("titleStr");
        String supplier = request.getParameter("supplier");
        String locality = request.getParameter("locality");
        String price = request.getParameter("price");
        String storage = request.getParameter("storage");
        String description = request.getParameter("description");
        String id = request.getParameter("id"); // 物品编号
        String picUrl = request.getParameter("picUrl"); // 物品旧封面
        // 定义一个商品对象封装界面提交的参数
        Article article = new Article();

        // 接收用户可能上传的封面
        String newUrl = receiveImage();
        picUrl = newUrl!=null ?newUrl:picUrl ;

        article.setId(Integer.valueOf(id));
        article.setImage(picUrl);

        ArticleType type = new ArticleType();
        type.setCode(code);
        article.setArticleType(type);
        article.setTitle(title);
        article.setSupplier(supplier);
        article.setLocality(locality);
        article.setPrice(Double.parseDouble(price));
        article.setStorage(Integer.parseInt(storage));
        article.setDescription(description);
        shopService.updateArticle(article);

        request.setAttribute("tip","修改商品成功");
        showUpdate();


    }

    //展示修改命令showUpdate的方法
    private void showUpdate() {
        try {
            //接受这个商品传过来的id
            String id =request.getParameter("id");
            //查询出所有的商品类型
            List<ArticleType> types=shopService.getArticleTypes();

            //通过id把商品查出来
            //下一把到业务层里面把这个商品id查出来
            Article article =shopService.getArticleById(id);
            //查出来把商品扔到商品界面,重定向
            request.setAttribute("article",article);
            request.setAttribute("types",types);
            request.getRequestDispatcher("/WEB-INF/jsp/updateArticle.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    //预览商品
    private void preArticle() {

        try {
            //接受这个商品传过来的id
            String id =request.getParameter("id");
            //通过id把商品查出来
            //下一把到业务层里面把这个商品id查出来
            Article article =shopService.getArticleById(id);
            //查出来把商品扔到商品界面,重定向
            request.setAttribute("article",article);
            request.getRequestDispatcher("/WEB-INF/jsp/preArticle.jsp").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteById() throws ServletException, IOException {
        try {
            String id =request.getParameter("id");
            shopService.deleteById(id);
            request.setAttribute("tip","删除成功");
        }catch (Exception e){
            request.setAttribute("tip","删除失败");
            e.printStackTrace();
        }
       //删除完了，应该会查这个商品
        request.getRequestDispatcher("/list?method=getAll").forward(request,response);

    }

    /**
     * 2、查询所有商品
     */
    private void getAll() throws ServletException, IOException {

        //考虑一个对象来封装
        Pager pager=new Pager();
        /**
         * 考虑分页查询
         * 看是否传入了分页参数的页码
         */
        String pageIndex = request.getParameter("pageIndex");
        if(!StringUtils.isEmpty(pageIndex)){
            //把这个pageIndex转成整数
            int pSize =Integer.valueOf(pageIndex);
            pager.setPageIndex(pSize);
        }


        //接受二级类型编号查询
        String secondType =request.getParameter("secondType");
        String title =request.getParameter("title");
        request.setAttribute("secondType",secondType);
        //接受一级类型编号查询
        String typeCode =request.getParameter("typeCode");

        //根据一级类型查询对应的二级类型
        //查了一级类型才可以查二级类型
        if (!StringUtils.isEmpty(typeCode)){
            List<ArticleType> secondTypes =shopService.loadSecondArticleTypes(typeCode);
            request.setAttribute("typeCode",typeCode);
            request.setAttribute("secondTypes",secondTypes);
        }

             // 1、查询出所有的一级商品类型信息
            List<ArticleType> firstArticleTypes = shopService.loadFirstArticleTypes();

            //2、查询所有的商品信息
            //多一个查询出来二级类型
            List<Article> articles = shopService.searchArticles(typeCode,secondType,title,pager);

            //写完了应该带回界面
        request.setAttribute("articleTypes",shopService.getArticleTypes());
        request.setAttribute("firstArticleTypes",firstArticleTypes);
        request.setAttribute("pager",pager);
        request.setAttribute("articles",articles);

        request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request,response);

    }


}
