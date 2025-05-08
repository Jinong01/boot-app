package kor.it.academy.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

/**
 * 쿠키사용 유틸
 */
public class CookieUtils {

    /**
     * 특정 name 의 cookie 객체 얻기
     * @param request
     * @param name
     * @return
     */
    private static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        Optional<Cookie> cookie =
                Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(name)).findAny();
        return cookie;
    }

    /**
     * 쿠키 등록
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
    }

    /**
     * 쿠키 갱신 및 등록
     * @param request
     * @param response
     * @param id
     */
    public static void updateCookie(HttpServletRequest request, HttpServletResponse response, String id) {
        Optional<Cookie> cookieOpt = getCookie(request, "boardIds");
        if (cookieOpt.isEmpty() || !cookieOpt.get().getValue().contains(id)) {
            String ids = cookieOpt.map(Cookie-> Cookie.getValue() + "#" + id).orElse(id); // orElse(id) => container 가 비어있으면 id를 반환해라
            addCookie(response, "boardIds", ids);
        }
    }

    /**
     * 쿠키에 원하는 id 가 있는지 확인
     * @param request
     * @param id
     * @return
     */
    public static boolean checkCookie(HttpServletRequest request, String id) {
        return getCookie(request, "boardIds")
                .map(cookie -> cookie.getValue().contains(id))
                .orElse(false);
    }
}
