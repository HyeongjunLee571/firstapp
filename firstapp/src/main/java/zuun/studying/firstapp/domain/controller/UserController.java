package zuun.studying.firstapp.domain.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.UserException;
import zuun.studying.firstapp.service.UserService;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // 빈 객체 전달
        return "register"; // → register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,Model model){
        try {
            userService.register(user);
            model.addAttribute("message","회원가입 성공입니다.");
            return "login";
        }catch (UserException e){
            model.addAttribute("error",e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,@RequestParam String error){
        model.addAttribute("error",error);
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model,@AuthenticationPrincipal UserDetails userDetails){
        model.addAttribute("userName",userDetails.getUsername());
        model.addAttribute("message","로그인 성공하였습니다.");//html 네임이랑 매핑하여 값 대입하여서 웹 페이지상 출력
        return "home";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        model.addAttribute("message","로그아웃 성공하였습니다.");
        return "login";
    }

//    @PostMapping("/signup")
//    public ResponseEntity<String> signUp(@Valid @RequestBody UserRequestDto userRequestDto){
//        userService.userSignUp(userRequestDto.getUserName(),userRequestDto.getEmail(),userRequestDto.getPassword());
//        return new ResponseEntity<>("회원가입성공",HttpStatus.CREATED);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestParam String email ,String password,Model model,
//    HttpServletResponse httpServletResponse) {
//        //리턴 타입이 LoginResponseDto 이기에
//        //LoginResponseDto 객체를 만들고 값을 담아 리턴한다.
//        //서비스에서 리턴되는 값이 String 이여서 받는 변수도 String로 선언 후 변수 생성하고 담기
//        //LoginResponseDto loginResponseDto  = userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
//        try {
//            String token = userService.login(email,password,httpServletResponse);
//            //String 타입으로 DTO에서 받기 때문에 위  생성한 변수 대입 후 리턴 진행 > 토큰 생성 및 출력
//            return new ResponseEntity<>((Map.of("token",token)),HttpStatus.OK);
//        } catch (Exception e){
//            model.addAttribute("error",e.getMessage());
//            return "login";
//        }
//
//    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<UserResponseDto> findUser
//            (@PathVariable Long userId){
//        //리턴 타입이 UserViewingResponserDto 이기에
//        //UserViewingResponserDto타입 변수에 서비스에 받아온 값 담고 리턴진행
//        UserResponseDto UserResponseDto
//                = userService.viewingUser(userId);
//        //서비스 메소드에 아이디 값만 넘겨서 활용하기 때문에 매개변수로 아이디 활용
//        //유저 한태도 아이디값만 입력 받는다.
//        //서비스에서 리턴되는 값도 UserViewingResponserDto 타입이다.
//        return new ResponseEntity<>(UserResponseDto,HttpStatus.OK);
//    }
//
//    @PutMapping("/{userId}")
//    public ResponseEntity<String>
//    userChangePassword(@PathVariable Long userId,
//                       @RequestBody userChangePasswordRequestDto userChangePassword,
//                       @RequestHeader("Authorization") String authorizationHeader){
//        //서비스에 값 넘기고 데이터베이스에 저장
//        userService.changePassword(userId,userChangePassword.getChangePassword(),userChangePassword.getExistingPassword(),authorizationHeader);
//
//        return new ResponseEntity<>("비밀번호 변경 성공",HttpStatus.OK);
//    }
}
