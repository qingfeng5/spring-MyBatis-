package com.qingfeng.shop.action;

import com.qingfeng.shop.bean.User;
import com.qingfeng.shop.service.ShopService;
import com.qingfeng.shop.utils.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    //1、取到request对象，response对象
    private HttpServletRequest request ;
    private HttpServletResponse response ;
    // 定义业务层对象
    private ShopService shopService;

    @Override
    public void init() throws ServletException {
        super.init();
        // 4、获取sping的容器。然后从容器中得到业务层对象
        ServletContext servletContext = this.getServletContext() ;
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        shopService = (ShopService) context.getBean("shopService");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //2、把上面两个对象给它
        this.request = req ;
        this.response = resp ;
        //login逻辑接受区分
        //获取参数
        String method = req.getParameter("method");
        switch (method){
            case "getJsp":
                // 跳转到登录界面
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req,resp);
                break;
            case "login":
                //登陆功能
                login();
                break;

        }
    }

    private void login() {
        try{
            //3、接受参数
            String loginName = request.getParameter("loginName");
            String passWord = request.getParameter("passWord");

            //5、登陆逻辑的认证
            //把登陆名和密码传回后台

            Map<String,Object> results = shopService.login(loginName , passWord);
            //从shopserviceimpl中来，登陆是否成功
            if((int)results.get("code") == 0){
                // 登陆成功的
                // 把登陆成功的用户注入到session会话中去
                // 跳转到主界面
                User user = (User) results.get("msg");
                request.setAttribute(Constants.USER_SESSION , user);
                // 请求跳转到主界面的servlet
                //response.sendRedirect(request.getContextPath()+"/list");
                response.sendRedirect(request.getContextPath()+"/list?method=getAll");
                //request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request,response);

            }else{
                //登陆失败，不等于0
                String msg = results.get("msg")+"";
                //把错误信息带回去
                request.setAttribute("msg",msg);
                //重新回来登陆界面
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
