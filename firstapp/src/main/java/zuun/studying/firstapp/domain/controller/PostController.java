package zuun.studying.firstapp.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zuun.studying.firstapp.Dto.*;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.AccessDeniedException;
import zuun.studying.firstapp.service.CommentService;
import zuun.studying.firstapp.service.PostService;
import zuun.studying.firstapp.service.UserService;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;


    //게시글 작성 폼
    @GetMapping("/new")
    public String newPostForm(Model model){

        model.addAttribute("post",new PostDto());//뷰와 실시간으로 소통하는 객체
        return "post-create";//post-create html로 이동

    }

    //게시글 생성
    @PostMapping
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult bindingResult,Model model,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("files") List<MultipartFile> files) throws IOException {

        if(bindingResult.hasErrors()){
            model.addAttribute("customError","제목 및 내용 입력값이 올바르지 않습니다. 바르게 입력해주세요.");
            return "post-create";
        }

        redirectAttributes.addFlashAttribute("successMessage", "게시글이 정상적으로 등록되었습니다!");
        postService.save(postDto,userDetails.getUsername(),files);
        return "redirect:/posts";//get 요청

    }

    //게시글 수정
    @GetMapping("/{id}/edit")
    public String editPostFrom(@PathVariable Long id,Model model){//매개변수 아이디는 URL에서 받은 아이디
        PostDetailDto post = postService.findById(id);

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());//조회된 게시글에서 받은 아이디
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());

        model.addAttribute("post",postDto);
        return "post-edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id,
                             @Valid @ModelAttribute("post") PostCreateDto postDto,
                             BindingResult bindingResult,Model model,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("files") List<MultipartFile> files) throws IOException {

        if(bindingResult.hasErrors()){
            model.addAttribute("customError","제목 및 내용 입력값이 올바르지 않습니다. 바르게 입력해주세요.");
            return "post-edit";
        }

        try {
            postService.updatePost(id,postDto,userDetails.getUsername(),files);
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 정상적으로 수정되었습니다!");
        } catch (AccessDeniedException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/posts";
        }

        return "redirect:/posts/"+id;
    }

    //게시글 리스트 조회
    @GetMapping
    public String listPost(@RequestParam(defaultValue = "1") int page, Model model,
                           @AuthenticationPrincipal UserDetails userDetails,
                           @ModelAttribute("errorMessage")String errorMessage){

        Page<PostDto> postsPage = postService.getPagePosts(page);

        UserResponseDto user = userService.getUser(userDetails.getUsername());

        model.addAttribute("posts",postsPage);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("totalPages",postsPage.getTotalPages());
        model.addAttribute("page",page);

        return "post-list";

    }

    //게시글 상세 조회+댓글 리스트
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model){

        PostDetailDto post = postService.findById(id);
        List<CommentResponseDto> comments = commentService.getComments(id);

        model.addAttribute("post",post);
        model.addAttribute("comments",comments);
        return "post-view";

    }

}
