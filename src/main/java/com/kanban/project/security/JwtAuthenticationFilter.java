package com.kanban.project.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kanban.project.domain.User;
import com.kanban.project.service.CustomUserDetailsService;
import static com.kanban.project.security.SecurityConstants.HEADER_STRING;
import static com.kanban.project.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private CustomUserDetailsService detailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwt = getJWTFromRequest(httpServletRequest);
			
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				
				Long userId = tokenProvider.getUserIdFromJWT(jwt);
				
				User userDetails = detailsService.loadUserById(userId);
				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception e) {
			logger.error("Could not set user authentication in security context", e);
		}
		
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		
	}
	
	private String getJWTFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HEADER_STRING);
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(7, bearerToken.length());
		}
		
		return null;
	}

}
