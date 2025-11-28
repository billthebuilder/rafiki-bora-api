package rafikibora.security.conf;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import rafikibora.services.CustomUserDetailsService;
import rafikibora.services.JwtProviderI;
import rafikibora.handlers.LogUtil;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProviderI jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // creates an access web token
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtToken(httpServletRequest);
            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                String username = jwtProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            LogUtil.logException(log, "Token authentication filter encountered an exception while processing request", ex);
            // For non-whitelisted endpoints, an invalid token should ultimately result in 401.
            // However, throwing here would also break permitAll endpoints if a header is present.
            // So, just proceed without setting authentication; security will enforce auth where required.
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        // Allow CORS preflight requests to pass through without authentication
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // Whitelist Swagger/OpenAPI endpoints and static assets
        return uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-ui/")
                || "/swagger-ui.html".equals(uri)
                || uri.startsWith("/api/auth/")
                || uri.startsWith("/actuator")
                || uri.startsWith("/error");
    }

    // Gets the token from the incoming request
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            if (accessToken == null) return null;

            return accessToken;
        }
        return null;
    }



    private String getJwtToken(HttpServletRequest request) {
        return getJwtFromRequest(request);
    }
}
