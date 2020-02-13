package donald.apiwithspringboot.resolver.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import donald.apiwithspringboot.config.JwtToken;
import donald.apiwithspringboot.exceptions.ValidationException;
import donald.apiwithspringboot.model.JwtRequest;
import donald.apiwithspringboot.model.JwtResponse;
import donald.apiwithspringboot.model.UserInfo;
import donald.apiwithspringboot.model.mutation.AuthenticateUserInfo;
import donald.apiwithspringboot.model.mutation.CreateUserInfo;
import donald.apiwithspringboot.repository.UserInfoRepository;
import donald.apiwithspringboot.service.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@AllArgsConstructor
public class UserInfoMutation implements GraphQLMutationResolver {

    private UserInfoRepository userInfoRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Transactional
    public Boolean createUserInfo(CreateUserInfo input){
        String username = input.getUsername();
        if (userInfoRepository.existsByUsername(username)){

            throw new ValidationException("Username already existed");

        }
        String password = input.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
//        String hashedPassword = hashData.get_SHA_512_SecurePassword(password);
        String fullname = input.getFullname();
        userInfoRepository.save(new UserInfo(username, encodedPassword, fullname));
        return true;
    }

    @Transactional
    public JwtResponse authenticateUserInfo(AuthenticateUserInfo authenticationRequest) throws Exception{
        authenticate(authenticationRequest.getUsername(),authenticationRequest.getPassword());

        final UserDetails userDetails = jwtUserDetailsService

                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtToken.generateToken(userDetails);

        return new JwtResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {

            throw new Exception("USER_DISABLED", e);

        } catch (BadCredentialsException e) {

            throw new Exception("INVALID_CREDENTIALS", e);

        }

    }



}
