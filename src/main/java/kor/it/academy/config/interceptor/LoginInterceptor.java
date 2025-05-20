package kor.it.academy.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isLogin = true;
        HttpSession session = request.getSession();

        if (session.getAttribute("userInfo") == null) {
            JSONObject obj = new JSONObject();
            obj.put("resultCode", 401);
            obj.put("resultMsg", "로그인이 필요합니다.");

            //ajax 처리
            if (isAjaxCall(request)) {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(obj.toString());
            } else {
                response.sendRedirect("/login/form");
            }
            isLogin = false;
        }

        return isLogin;
    }

    public boolean isAjaxCall(HttpServletRequest request) {
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }
}
