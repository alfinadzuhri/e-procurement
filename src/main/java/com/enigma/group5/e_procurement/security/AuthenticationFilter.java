package com.enigma.group5.e_procurement.security;

import com.enigma.group5.e_procurement.dto.response.JwtClaims;
import com.enigma.group5.e_procurement.entity.UserAccount;
import com.enigma.group5.e_procurement.service.JwtService;
import com.enigma.group5.e_procurement.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    final String AUTH_HEADER = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String bearerToken = request.getHeader(AUTH_HEADER);

            // verify token
            if(bearerToken != null && jwtService.verifyJwtToken(bearerToken)){
                // claims token/ decode token
                JwtClaims decodeJwt = jwtService.getClaimsByToken(bearerToken);

                // find UserAccount by id form sub in token
                UserAccount userAccountBySub = userService.getByUserId(decodeJwt.getUserAccountId());
                // verify Authentication use UserPassAuthToken
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAccountBySub.getUsername(),
                        null,
                        userAccountBySub.getAuthorities()
                );

                // kita masukkan detail detail lain seperti ip addres, siapa yg ngehit
                authentication.setDetails(new WebAuthenticationDetails(request));

                // Set ini Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e){
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // ibarat finally
        // Lempar ke controller
        filterChain.doFilter(request,response);
    }
}

