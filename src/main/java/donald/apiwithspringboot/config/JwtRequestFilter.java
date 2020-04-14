package donald.apiwithspringboot.config;


import donald.apiwithspringboot.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    private final JwtToken jwtTokenUtil;

    public JwtRequestFilter(JwtToken jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)

            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;

        String jwtToken = null;


        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);

            try {

                username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            } catch (IllegalArgumentException e) {

                System.out.println("Unable to get JWT Token");

            } catch (ExpiredJwtException e) {

                System.out.println("JWT Token has expired");

            }

        }
         else if (requestTokenHeader == null){

                logger.info("Does not provide Authorization Header");

            }
         else if (!requestTokenHeader.startsWith("Bearer ")){
             logger.warn("JWT Token does not begin with Bearer");
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);


            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(

                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken

                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
