package com.example.shopappbackend.filters;

import com.example.shopappbackend.components.JwtTokenUtils;
import com.example.shopappbackend.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter { // Dùng để kiểm tra request có token không và token có hợp lệ không

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        try{
            if(isByPassToken(request)){
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader("Authorization");
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "authHeader null or not stated with bearer token"
                );
                return;
            }
            String token = authHeader.substring(7);
            String email = jwtTokenUtils.getEmailFromToken(token);
            if( email != null // Kiểm tra email tồn tại không
                    && SecurityContextHolder.getContext().getAuthentication() == null
            ){
                User userDetails = (User) userDetailsService.loadUserByUsername(email); // Kiểm tra email có trong repo không

                // Kiểm tra token đã quá hạn chưa
                if(jwtTokenUtils.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                filterChain.doFilter(request, response); // enable bypass
            }
        }catch(AccessDeniedException e){
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );
            response.getWriter().write(e.getMessage());
        } catch (Exception e){
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED
            );
            response.getWriter().write(e.getMessage());
        }

    }

    private boolean isByPassToken(@NotNull HttpServletRequest request) {
        List<Pair<String, String>> byPassToken = Arrays.asList(
                Pair.of(String.format("%s/categories**", apiPrefix),"GET"),
                Pair.of(String.format("%s/materials**", apiPrefix),"GET"),
                Pair.of(String.format("%s/comments**", apiPrefix),"GET"),

                Pair.of(String.format("%s/get-order-by-keywords", apiPrefix),"GET"),
                Pair.of(String.format("%s/users/register", apiPrefix),"POST"),
                Pair.of(String.format("%s/users/login", apiPrefix),"GET")

                ,Pair.of(String.format("%s/users**", apiPrefix),"POST"),
                Pair.of(String.format("%s/products/image**", apiPrefix),"GET")
        );

        for (Pair<String, String>  pair : byPassToken) {
            String left = pair.getLeft();
            String right = pair.getRight();

            if(request.getServletPath().matches(left.replace("**", ".*"))
                    && request.getMethod().equalsIgnoreCase(right)){
                return true;
            }
        }
        return false;
    }


}
