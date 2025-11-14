package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.CommentRequestDto;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.FileEntity;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.CommentNotFoundException;
import zuun.studying.firstapp.exception.PostNotFoundException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.repository.CommentRepository;
import zuun.studying.firstapp.repository.PostRepository;
import zuun.studying.firstapp.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment addComment(Long postId, CommentRequestDto commentDto, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UserNotFoundException("존재하지 않는 유저입니다."));

        Post post = postRepository.findPostWithUserFiles(postId).orElseThrow(()->
                new PostNotFoundException("존재하지 않는 게시글입니다."));; //(PostExcption::new) > new PostExcption()); 같음

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setLikes(0);

        Comment saveComment = commentRepository.save(comment);

        return saveComment;


    }

    public List<Comment> getComments(Long postId) {

        return commentRepository.findByPostId(postId);

    }

    @Transactional
    public void addLike(Long postId,Long commentId) {

        Comment comment = commentRepository.viewComment(commentId).orElseThrow(()->
                new CommentNotFoundException("존재하지 않는 댓글입니다.", postId));

        int commentLike = comment.getLikes()+1;
        commentRepository.updateCommentLikes(comment.getId(),commentLike);
    }


    @Transactional
    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }

}
