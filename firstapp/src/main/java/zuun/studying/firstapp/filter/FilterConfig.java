package zuun.studying.firstapp.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import zuun.studying.firstapp.passwordEncoder.CustomPasswordEncoder;
import zuun.studying.firstapp.repository.UserRepository;

//스프링이 컨테니어가 관리하는 빈으로
@Configuration
//final 붙은 속성 생성자 자동생성
@RequiredArgsConstructor
//스프링 시큐리티 보안 활성화
@EnableWebSecurity
//메서드에서 보안설정 가능하도록 설정
@EnableMethodSecurity(securedEnabled = true)
public class FilterConfig {

    private final UserRepository userRepository;

    //스프링 시큐리티의 필터 체인 설정을 구성 > HttpSecurity을 기반으로 보안 규칙 지정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                //CSRF 비활성화 > 이유 세션이 없기 때문
//                .csrf(AbstractHttpConfigurer::disable)
//                //팝업 로그인창 비활성화
//                .httpBasic(AbstractHttpConfigurer::disable)
//                스프링에서 제공하는 기본 로그인 페이지 비활성화
//                .formLogin(AbstractHttpConfigurer::disable)
//                //JWT 커스텀 필터 적용(보안 필터 체인에 추가)
//                //SecurityContextHolderAwareRequestFilter > 해당 필터전에 실행되도록 설정
//                //요청 진행 > jwtFilter 필터 실행 후 > jwt가 있는지 없는지 확인 > 있으면 > SecurityContextHolderAwareRequestFilter 실행 >
//                //SecurityContext에 사용자 정보 저장
//                .addFilterBefore(jwtFilter, SecurityContextHolderAwareRequestFilter.class)
                //경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        //누구나 접근 가능(JWT 활용 X/권한 활용 X)
                        //조건: 아래 코드에 /users,/users/**하고 밑에서 또 /users/**하고 유저 권한 시 접근하도록 설정시 첫 코드만 적용
                        .requestMatchers("/users/register", "/users/login",
                                "/users/home","/uploads/**").permitAll()
                        //(JWT 토큰 활용 o/권한 활용 o)
                        // URL 예시 : api/user/getuser 접속시 > USER 권한 있는 사람만 접근 가능
                        .requestMatchers("/users/**").hasRole("USER")
                        //게시글 GET으로 들어오는 요청이고 posts 및 posts뒤에 다른 URL이 와도 인증없이 허용
                        .requestMatchers(HttpMethod.GET,"posts","posts/**").permitAll()
                        //게시글 POST으로 들어오는 요청이고 posts 및 posts뒤에 다른 URL이 와도 인증 필요 없이는 접근X
                        .requestMatchers(HttpMethod.POST,"posts","posts/**").hasRole("USER")
                        //그외 모든 URL 요청 인증된것만 접근 가능하도록 설정(JWT 토큰 활용 O/권한 제한 X/권한 활용 O)
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/users/login")//접속 시 나오는 주소
                        //유저네임으로 인증 진행(아이디)
                        .usernameParameter("userName")
                        .loginProcessingUrl("/users/login")//포스트 요청시 엔드포인트 주소
                        .defaultSuccessUrl("/users/home",true)//200OK일때 주소처리
                        .failureUrl("/users/login?error=true")
                        .permitAll()
                ).logout(logout-> logout
                        .logoutSuccessUrl("/users/login")//성공시 이동 주소
                        .logoutUrl("/users/logout")//로그아웃 요청 페이지
                        .permitAll()
                )
                //모든 설정 적용 후 securityFilterChain 객체 생성완료
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new CustomPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception{

        AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
