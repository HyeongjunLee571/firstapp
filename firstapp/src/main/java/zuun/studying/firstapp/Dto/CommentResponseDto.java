package zuun.studying.firstapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String author;
    private String content;
    private int likes;
    private Long postId; // 댓글 생성시 필수(어느 게시글에 댓글이 붙는지와 좋아요 클릭시도 사용됨

}
