package zuun.studying.firstapp.exception;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import zuun.studying.firstapp.Dto.PostDto;
import zuun.studying.firstapp.Dto.UserDto;
import zuun.studying.firstapp.entity.Post;

@ControllerAdvice //뷰 전용
public class GlobalExcptionHandler{

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String UsersAlreadyExistsException(UserAlreadyExistsException ex, Model model)
    {
        model.addAttribute("errorMessage",ex.getMessage());
        model.addAttribute("user",new UserDto());//<- 모델 추가 없이 예외처리 시 로그인 페이지로 이동 됨 이유는 예외가 발생 시 모델 객체가 사라져서 로그인 필터가 인증 안 된 유저로 인식하기 때문
        return "register";
    }

    @ExceptionHandler(PostNotException.class)
    public String PostNotException(PostNotException e, Model model)
    {
        model.addAttribute("error",e.getMessage());
        model.addAttribute("post",new PostDto());
        return "post-list";
    }
}
