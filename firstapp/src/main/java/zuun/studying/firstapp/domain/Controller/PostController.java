package zuun.studying.firstapp.domain.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.service.CommentService;
import zuun.studying.firstapp.service.PostService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    //게시글 작성 폼
    @GetMapping("/new")
    public String newPostForm(Model model,@AuthenticationPrincipal UserDetails user){

        Post post = new Post();//post 객체 생성
        post.setAuthor(user.getUsername());// 객체 내 값 변경
        model.addAttribute("post",post);//변경된 값을 가지고 있는 객체 post 이름으로 모델에 넘김

        return "post-create";//post-create html로 이동

    }

    //게시글 생성
    @PostMapping
    public String createPost(@ModelAttribute Post post,Model model){

        postService.save(post);
        return "redirect:/posts";//get 요청

    }

    //게시글 리스트 조회
    @GetMapping
    public String listPost(Model model){

        model.addAttribute("posts",postService.findAll());
        return "post-list";

    }

    //게시글 상세 조회+댓글 리스트
    @GetMapping("/{id}")
    public String viewpost(@PathVariable Long id, Model model){

        model.addAttribute("post",postService.findById(id));
        model.addAttribute("comments",commentService.getComments(id));
        return "post-view";

    }

    //게시글 좋아요
    @PostMapping("/{id}/like")
    public String viewpost(@PathVariable Long id){

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
