package com.mrluo.cloud.config;


import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.security.NewsWebSessionService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectProvider<SecurityConfigure> securityConfigureProvider;

    @Bean
    @ConditionalOnMissingBean
    public NewsWebSessionService sessionService() {
        return new NewsWebSessionService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        SecurityConfigure securityConfigure = securityConfigureProvider.getIfAvailable();

        if (securityConfigure != null && securityConfigure.configure(web)) {
            return;
        }

        web.ignoring().antMatchers("/webjars/**");

        web.ignoring().antMatchers("/swagger-ui/**",
                "/v2/api-docs", "/v3/api-docs",
                "/swagger-resources",
                "/swagger-resources/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(sessionService(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers(NewsDefs.FACADE_PREFIX_URI + "/**").permitAll();

        SecurityConfigure securityConfigure = securityConfigureProvider.getIfAvailable();

        if (securityConfigure != null && securityConfigure.configure(http)) {
            return;
        }

        http.authorizeRequests()
                .antMatchers(NewsDefs.API_PREFIX_URI + "/**").authenticated()
                .anyRequest().authenticated();
    }

}
