package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.exception.CommentException;
import zuun.studying.firstapp.exception.PostNotException;
import zuun.studying.firstapp.repository.CommentRepository;
import zuun.studying.firstapp.repository.PostRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addComment(Long postId, String content, String username) {

        Post post = postRepository.viewPost(postId).orElseThrow(()->
                new PostNotException("존재 하지 않는 게시글입니다."));; //(PostExcption::new) > new PostExcption()); 같음

        commentRepository.createComment(content,username,post.getId(),0);
    }

    public List<Comment> getComments(Long postId) {

        return commentRepository.findByPostId(postId);

    }

    @Transactional
    public void addLike(Long commentId) {

        Comment comment = commentRepository.viewComment(commentId).orElseThrow(CommentException::new);

        int commentLike = comment.getLikes()+1;
        commentRepository.updateCommentLikes(comment.getId(),commentLike);
    }
}
