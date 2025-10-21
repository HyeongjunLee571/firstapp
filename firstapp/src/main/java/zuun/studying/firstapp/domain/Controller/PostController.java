package zuun.studying.firstapp.domain.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zuun.studying.firstapp.Dto.PostDto;
import zuun.studying.firstapp.Dto.UserDto;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.service.CommentService;
import zuun.studying.firstapp.service.PostService;
import zuun.studying.firstapp.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


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

        model.addAttribute("post",new PostDto());//변경된 값을 가지고 있는 객체 post 이름으로 모델에 넘김
        return "post-create";//post-create html로 이동

    }

    //게시글 생성
    @PostMapping
    public String createPost(@Valid @ModelAttribute("user") PostDto postDto,
                             @AuthenticationPrincipal UserDetails userDetails){

        postService.save(postDto.getTitle(),postDto.getContent(),userDetails.getUsername());
        return "redirect:/posts";//get 요청

    }

    @GetMapping("/{id}/edit")
    public String editPostFrom(@PathVariable Long id,Model model){
        Post post = postService.findById(id);
        model.addAttribute("post",post);
        return "post-edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails){
        postService.updatePost(id,title,content,userDetails.getUsername());
        return "redirect:/posts/"+id;
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id){
        postService.deletePost(id);

        return "redirect:/posts";
    }

    //게시글 리스트 조회
    @GetMapping
    public String listPost(@RequestParam(defaultValue = "1") int page, Model model,
                           @AuthenticationPrincipal UserDetails userDetails){

        int size = 5;
        List<Post> posts = postService.getPagePosts(page,size);
        int totalPages = postService.getTotalPages(size);

        User user = userService.getUser(userDetails.getUsername());

        model.addAttribute("posts",posts);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("page",page);

        return "post-list";

    }

    //게시글 상세 조회+댓글 리스트
    @GetMapping("/{id}")
    public String viewpost(@PathVariable Long id, Model model){

        Post post = postService.findById(id);
        List<Comment> comments = commentService.getComments(id);

        model.addAttribute("post",post);
        model.addAttribute("comments",comments);
        return "post-view";

    }

    //게시글 좋아요
    @PostMapping("/{id}/like")
    public String viewposts(@PathVariable Long id){

        postService.addLike(id);
        return "redirect:/posts/"+id;

    }

    //댓글 생성
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @AuthenticationPrincipal UserDetails user,
                             @RequestParam String content){

        String username = user.getUsername();

        commentService.addComment(id,content,username);
        return "redirect:/posts/"+id;

    }

//댓글 좋아요
    @PostMapping("/{postId}/comments/{commentId}/like")
    public String likeComment(@PathVariable Long postId,@PathVariable Long commentId){

        commentService.addLike(commentId);
        return "redirect:/posts/"+postId;

    }

}
