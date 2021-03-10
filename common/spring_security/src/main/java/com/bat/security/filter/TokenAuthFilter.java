package com.bat.security.filter;

import com.bat.security.security.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilterInternal(request, response, chain);
        //获取当前认证成功的用户权限信息
        UsernamePasswordAuthenticationToken authInfo = getAuthentication(request);
        //如果有权限信息，放到权限上下文中
        if(authInfo != null){
            SecurityContextHolder.getContext().setAuthentication(authInfo);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        //从header中获取token
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            return null;
        }
        //从token中获取用户名
        String username = tokenManager.getUserInfoFromToken(token);
        //从Redis中获取对应的权限列表
        List<String> permissionList = (List<String>)redisTemplate.opsForValue().get(username);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String permission : permissionList){
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }
}
