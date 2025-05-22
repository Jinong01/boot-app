package kor.it.academy.config;

import kor.it.academy.handler.AfterLogoutHandler;
import kor.it.academy.handler.LoginFailureHandler;
import kor.it.academy.handler.LoginSuccessHandler;
import kor.it.academy.login.service.LoginDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginDetailService loginDetailService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()  //시큐리티 관리 안받을 애들은 ignoring 안에
                .requestMatchers("/webjars/**", "/dist/**", "/plugins/**")
                .requestMatchers("/css/**", "/js/**", "/images/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); //모든 요청 무시
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() //오픈API에 OPTIONS 방식인것들이 있어서 허용
                                        .requestMatchers("/login/**").permitAll()
                                        .requestMatchers("/board/list").permitAll()
                                        .requestMatchers("/board/search").permitAll()
                                        .requestMatchers("/weather/**").permitAll()
                                        .requestMatchers("/user/join").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/user/").permitAll()
                                        .requestMatchers("/user/**").hasRole("ADMIN")
                                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                                        .anyRequest().authenticated() //그 외 경로는 모두 로그인해야 접근가능
                ).formLogin(form -> form.loginPage("/login/form")
                        .loginProcessingUrl("/login/proc")
                        .successHandler(new LoginSuccessHandler())
                        .failureHandler(new LoginFailureHandler())
                        .permitAll()
                ).logout(form ->form.invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(new AfterLogoutHandler())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginDetailService);

        return provider;
    }
}
