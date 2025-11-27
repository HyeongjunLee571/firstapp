package zuun.studying.firstapp.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zuun.studying.firstapp.Dto.UserDto;
import zuun.studying.firstapp.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user",new UserDto());
        return "register"; // → register.html
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult bindingResult,RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            return "register";
        }

        userService.register(userDto);
        redirectAttributes.addFlashAttribute("successMsg", "회원가입 성공하였습니다.");
        return "redirect:/users/login";
        //전체 URL을 입력해야 오류 없이 동작함 /users/login 해야 RequestMapping 타고
        // @GetMapping("/login") 여길 타기에 정상적으로 기능 동작 그냥  @GetMapping("/login")
        // 이것만 하면 오류 발생 이유 없는 URL이라서

    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error",required = false) String error,
                                @ModelAttribute("successMsg") String successMsg) {

        model.addAttribute("successMsg",successMsg);
        if(error!=null) {
            model.addAttribute("errorMsg", "아이디 및 비밀번호가 잘못되었습니다.");
        }

        return "login";
    }

    @GetMapping("/home")
    public String home(Model model,@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails != null) {
            model.addAttribute("userName", userDetails.getUsername());
            //model.addAttribute("message","로그인 성공하였습니다.");//html 네임이랑 매핑하여 값 대입하여서 웹 페이지상 출력
        } else {
            model.addAttribute("userName","게스트");
        }
        return "home";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        model.addAttribute("message","로그아웃 성공하였습니다.");
        return "login";
    }
}
