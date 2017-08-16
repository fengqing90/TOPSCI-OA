package com.topsci.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.topsci.bean.User;
import com.topsci.utils.MySessionContext;
import com.topsci.utils.StringTools;

/**
 * @dateTime 2013-7-8 下午5:35:22
 * @author FengQing
 * @description session中的用户是否超时
 */
public class SessionTimeoutInterceptor extends HandlerInterceptorAdapter
        implements InitializingBean {

    /***
     * 这个方法是在preHandle执行之后反回一个true之后执行 在我这里并没有用到所以我就不过多介绍了在网上有很多这 个方法的介绍
     */
    public void afterPropertiesSet() {
    }

    /*** 这个方法是在preHandle执行之后返回一个true之后执行 在我这里并没有用到所以我就不过多介绍了在网上有很 多这 *个方法的介绍 */
    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 这个方法是在要执行的过滤器和servlet之前执行的
     * true表示正确执行
     * false表示执行错误或是异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        Object sessionUser = request.getSession().getAttribute("user");
        String uri = request.getRequestURI();
//		System.out.println(uri);
//		System.out.println("session id:"+request.getSession().getId());
        ///////////////////////////////////////由于js里面用location.herf跳转它认为是一个新的会话会导致创建一个新的session，jboss中未发现该现象。
        if (sessionUser == null) {
            String jsessionid = StringTools
                .replaceStringtoNull(request.getParameter("jsessionid"));
//			System.out.println("拦截器 ID:"+jsessionid);
            HttpSession session = MySessionContext.getInstance()
                .getSession(jsessionid);
            if (session != null) {
//				System.out.println(session.getAttribute("user"));
                sessionUser = session.getAttribute("user");
                request.getSession(false).setAttribute("user", sessionUser);
                request.getSession(false).setAttribute("jsessionid",
                    jsessionid);
            }
            if (jsessionid != null && jsessionid.equals("MyJsp")
                && session == null) {
                sessionUser = new User();
            }
        }
        ///////////////////////////////////////MyJsp.jsp
        if (sessionUser == null && uri.indexOf("user/userLogin.do") < 0) {
            RequestDispatcher dispatcher = request
                .getRequestDispatcher("../Error.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("Error.jsp");
            }
            return false;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
}
