package zuun.studying.firstapp.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.enums.UserRoleEnum;
import zuun.studying.firstapp.exception.UserException;
import zuun.studying.firstapp.passwordEncoder.CustomPasswordEncoder;
import zuun.studying.firstapp.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder passwordEncoder;



    public void register(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException();
        }

        user.setUserRole(UserRoleEnum.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
