package com.epam.training.web.interceptor;

import com.epam.training.persistence.pojo.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleg_Burshinov Interceptor for separate authentication authorization
 */
@Component
public class UserAccessInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(UserAccessInterceptor.class);

    @Autowired
    private UserContext userContext;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
     * (javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI().substring(
                request.getContextPath().length());
//        logger.info("Intercepting: " + request.getRequestURI() + " "
//                + " User in session "
//                + request.getSession().getAttribute("userrole"));
//        if (path.equals("/showitems.htm") || path.equals("/advancedsearch.htm")
//                || path.equals("/showmyitems.htm")) {
//            Integer page = Integer.valueOf(1);
//            String pageNumber = (String) request.getParameter("pageNumber");
//            if (pageNumber != null) {
//                try {
//                    page = Integer.parseInt(pageNumber);
//                } catch (NumberFormatException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//            request.setAttribute("pageNumber", page);
//        }
        if (path.equals("/edititem.htm") || path.equals("/showmyitems.htm")) {
            if (userContext == null) {
                redirect(request, response, "/login.htm");
                return false;
            } else {
                if (!userContext.isAuthorized()) {
                    redirect(request, response, "/login.htm");
                }
                return userContext.isAuthorized();
            }

        }

        return true;

    }

    private void redirect(HttpServletRequest request,
                          HttpServletResponse response, String path) throws ServletException {
        try {
            response.sendRedirect(request.getContextPath() + path);
        } catch (java.io.IOException e) {
            throw new ServletException(e);
        }
    }

}
