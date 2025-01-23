package si.afridau.commerce.auth.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import si.afridau.commerce.auth.service.JwtService;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (log.isTraceEnabled()) {
                log.trace("HTTP DATA: {}", getHttpData(request));
            }
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(jwtService.extractJwtUser(jwt), null, jwtService.extractAuthorities(jwt));
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getHttpData(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nREQUEST URL: ");
        sb.append(request.getRequestURL());
        sb.append("\nMethod: ");
        sb.append(request.getMethod());
        sb.append("\nContent type: ");
        sb.append(request.getContentType());
        sb.append("\nHeaders:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            sb.append(key);
            sb.append(" - ");
            sb.append(request.getHeader(key));
            sb.append("\n");
        }
        return sb.toString();
    }
}

