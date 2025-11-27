package zuun.studying.firstapp.domain.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zuun.studying.firstapp.Dto.ApiResponse;
import zuun.studying.firstapp.Dto.CommentRequestDto;
import zuun.studying.firstapp.Dto.CommentResponseDto;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.service.CommentService;
import zuun.studying.firstapp.service.PostService;
import zuun.studying.firstapp.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class AJAXPostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    //게시글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePosts(@PathVariable Long id){

        postService.addLike(id);
        return ResponseEntity.accepted().body(new ApiResponse(true,"좋아요 요청 성공",null));

    }

    //댓글 생성
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @AuthenticationPrincipal UserDetails user,
                                        @RequestBody CommentRequestDto commentDto){

        String username = user.getUsername();

        Comment savecomment = commentService.addComment(id,commentDto,username);

        CommentResponseDto Comment = new CommentResponseDto(
                savecomment.getId(),
                username,
                savecomment.getContent(),
                savecomment.getLikes(),
                savecomment.getPostId()// 어느 게시글에 댓글이 들어가는지 파악하기 위함(필수) 좋아요 증가시도 사용
                //게시글 내에 다른거 추가시 id값 필수로 넘길것
        );

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true,"댓글 등록 완료",Comment));

    }

    //댓글 좋아요
    @PostMapping("/{postId}/comments/{commentId}/like")
    public  ResponseEntity<?> likeComment(@PathVariable Long postId,@PathVariable Long commentId){

        commentService.addLike(postId,commentId);
        return ResponseEntity.accepted().body(new ApiResponse(true,"좋아요 요청 성공",null));

    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.accepted().body(new ApiResponse(true,"삭제 요청 성공",null));
    }

    //게시글 삭제
    @DeleteMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<?> deletePost(@PathVariable Long id,RedirectAttributes redirectAttributes){
        postService.deletePost(id);
        return ResponseEntity.ok(new ApiResponse(true,"success",null));
    }

}

//✅ 5️⃣ 디버그 방법(인텔리 제이로도 오류 원인을을 모를때 JS 오류
//
//브라우저 F12 → Network 탭 열고, 좋아요 클릭
//
//요청이 /api/posts/.../comments/.../like 로 나가는지 확인
//
//404면 postId나 commentId 전달 안 된 것
//
//Console 탭에서 댓글 좋아요 클릭 감지 ✅가 출력되는지 확인