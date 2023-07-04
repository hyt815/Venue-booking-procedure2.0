package com.example.demo.Interceptors;

import com.example.demo.Utils.JWTUtil;
import com.example.demo.annotation.Token;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //从请求头内获取token

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(Token.class)) {//获取这个方法是否有这个注释
            String token = request.getHeader("authorization");
            if (token == null) {
                response.sendError(1014, "访问未携带token");
                return false;
            }
            boolean flag= false;
            //验证令牌，如果令牌不正确会出现异常会被全局异常处理
            try {
                flag=JWTUtil.VerifyJWTToken(token);
            }catch (Exception e){
                System.out.println(e.getMessage());
//                response.setStatus(412);
                response.sendError(1011, e.getMessage());
//                response.setHeader("token",e.getMessage());
            }
            return flag;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {}
}
