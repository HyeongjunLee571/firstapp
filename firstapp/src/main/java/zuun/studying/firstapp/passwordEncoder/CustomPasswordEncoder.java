package zuun.studying.firstapp.passwordEncoder;


import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component//불필요 메서드
public class CustomPasswordEncoder implements PasswordEncoder {


    @Override
    //비밀번호 암호화하여 생성하는 메서드
    public String encode(CharSequence rawPassword) {
        return BCrypt.withDefaults().hashToString(10, rawPassword.toString().toCharArray());
    }

    @Override
    //비밀번호가 현재 입력받은 것과 저장된 비밀번호가 일치하는지 검토
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toString().toCharArray(), encodedPassword);
        return result.verified;
    }

}

