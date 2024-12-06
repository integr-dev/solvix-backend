package net.integr.solvix.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.integr.solvix.db.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.beans.Customizer


@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Autowired var userService: UserService? = null

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
        val delegate = XorCsrfTokenRequestAttributeHandler()
        delegate.setCsrfRequestAttributeName("_csrf")
        val requestHandler = CsrfTokenRequestHandler(delegate::handle)

        http
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/register","/api/account/register", "/login", "/api/account/login").not().authenticated()
                    .requestMatchers("/**").authenticated()
            }
            .csrf { csrf -> csrf
                /*.csrfTokenRepository(tokenRepository)
                .csrfTokenRequestHandler(requestHandler)
                .ignoringRequestMatchers("/api/logout")*/
                .disable()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/api/account/login")
                    .successHandler { _, response, _ ->
                        response.status = 200
                        response.writer.write("Logged in successfully")
                    }
                    .failureHandler { _, response, _ ->
                        response.status = 401
                        response.writer.write("Invalid username or password")
                    }
                    .permitAll()
            }
            .rememberMe { customizer ->
                customizer
                    .key("remember-me")
            }
            .logout { logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(AntPathRequestMatcher("/api/account/logout"))
                .logoutSuccessUrl("/login")
                .permitAll()
            }
            //.addFilterAfter(CsrfCookieFilter(), BasicAuthenticationFilter::class.java)

        return http.build()
    }

    /*
    class CsrfCookieFilter : OncePerRequestFilter() {
        override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
            val csrfToken = request.getAttribute(CsrfToken::class.java.name) as CsrfToken
            csrfToken.token // Load token to cookie only, throw away result

            filterChain.doFilter(request, response)
        }
    }
     */

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder, passwordEncoder: PasswordEncoder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder)
    }
}