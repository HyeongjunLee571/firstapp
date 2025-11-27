package zuun.studying.firstapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zuun.studying.firstapp.Dto.PostDto;
import zuun.studying.firstapp.Dto.UserDto;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice //뷰 전용
public class GlobalExcptionHandler{

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUsersAlreadyExists(UserAlreadyExistsException ex, Model model)
    {
        model.addAttribute("errorMessage",ex.getMessage());
        model.addAttribute("user",new UserDto());//<- 모델 추가 없이 예외처리 시 로그인 페이지로 이동 됨 이유는 예외가 발생 시 모델 객체가 사라져서 로그인 필터가 인증 안 된 유저로 인식하기 때문
        return "register";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model){
        model.addAttribute("errorMessage",ex.getMessage());
        model.addAttribute("user",new UserDto());
        return "register";
    }

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFound(PostNotFoundException e, Model model)
    {
        model.addAttribute("error",e.getMessage());
        model.addAttribute("post",new PostDto());
        return "post-create";
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFound(CommentNotFoundException e, RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());

        if(e.getPostId() ==  -1){
            return "redirect:/posts";
        }

        return "redirect:/posts" + e.getPostId();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException e,
                                     HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) { //Ajax만 X-Requested-With값 존재 일반 요청은 가져와도 값이 null임
            Map<String,String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        throw e;
    }
}
