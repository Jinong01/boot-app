package kor.it.academy.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kor.it.academy.common.vo.SecureUser;
import kor.it.academy.login.vo.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 사용자가 로그인 하기 전에 요청했던 페이지를
 * 기억했다가, 로그인 후 해당 페이지로 이동
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    //http 기억 객체
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        //기본 경로 설정
        setDefaultTargetUrl("/board/list");
        //세션 시간 설정 - 30분
        request.getSession().setMaxInactiveInterval(1800);
        //쓰기 편하게 세션에 로그인 사용자 정보 등록
        SecureUser user = (SecureUser) authentication.getPrincipal();
        request.getSession().setAttribute("userInfo", user);

        //저장된 이전 경로 객체 가져오기
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            //뭔가 에러가 났을 경우는 기본 경로로 이동
            if(targetUrl.contains("error") || targetUrl.contains(".well-known")) {
                targetUrl = getDefaultTargetUrl();
            }
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
