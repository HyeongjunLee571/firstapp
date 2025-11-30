package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.CommentRequestDto;
import zuun.studying.firstapp.Dto.CommentResponseDto;
import zuun.studying.firstapp.Dto.PostDetailDto;
import zuun.studying.firstapp.Dto.UserResponseDto;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.exception.CommentNotFoundException;
import zuun.studying.firstapp.exception.PostNotFoundException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.CommentMapper;
import zuun.studying.firstapp.mapper.PostMapper;
import zuun.studying.firstapp.mapper.UserMapper;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponseDto addComment(Long postId, CommentRequestDto commentDto, String username) {

        UserResponseDto user = userMapper.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException("존재하지 않는 유저정보입니다.");
        }

        PostDetailDto postDetailDto =  postMapper.findPostWithUserFiles(postId);
        if(postDetailDto == null){
            throw new PostNotFoundException("존재하지 않는 파일정보입니다.");
        }


        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUserId(user.getId());
        comment.setPostId(postDetailDto.getId());
        comment.setLikes(0);

        commentMapper.insertComment(comment);
        return commentMapper.viewComment(comment.getId());//댓글 단일 조회(댓글 아이디 매개변수로 넘김 이유 > 작성된 댓글 하나만 조회되어 넘기면 되기 때문)
    } // 댓글 등록 오류 발생 원인 > 댓글 id로 미조회 게시글 id로 조회하였으니 전체댓글이라 Dto에 매핑 X 결국 null 발생 > 비동기쪽 data null 댓글 실패로 판정

    public List<CommentResponseDto> getComments(Long postId) {

        return commentMapper.findByPostId(postId);//댓글 전체 조회(게시글 아이디 매개변수로 넘김 이유 > 게시글에 해당하는 모든 댓글을 가져와야하기 때문)

    }

    @Transactional
    public void addLike(Long postId,Long commentId) {

        CommentResponseDto comment = commentMapper.viewComment(commentId);

        if(comment == null){
            throw new CommentNotFoundException("존재하지 않는 댓글입니다.",postId);
        }

        commentMapper.updateCommentLikes(comment.getId());
    }


    @Transactional
    public void deleteComment(Long id){
        commentMapper.deleteCommentById(id);
    }

}
