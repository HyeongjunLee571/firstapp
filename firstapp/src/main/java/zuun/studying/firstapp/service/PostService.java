package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.PostNotException;
import zuun.studying.firstapp.exception.UserAlreadyExistsException;
import zuun.studying.firstapp.repository.PostRepository;
import zuun.studying.firstapp.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(String title,String content,String username){

        User user = userRepository.findByUsername(username).
                orElseThrow(()->new UserAlreadyExistsException("없는 사용자입니다."));

        postRepository.insertPost(title,content,username,
                user.getUserId(),0);
    }

    public Post findById(Long id){

        return postRepository.viewPost(id).orElseThrow(()->
                 new PostNotException("존재 하지 않는 게시글입니다."));

    }

    //update 또는 delete 쿼리는 반드시 트랜잭션 안에서 실행되어야 함(생성 및 추가가 곧 수정이기이)
    @Transactional
    public void addLike(Long id){

        Post post = postRepository.viewPost(id).orElseThrow(()->
                new PostNotException("존재 하지 않는 게시글입니다."));

        int postLikes = post.getLikes()+1;
        postRepository.updatePostLikes(post.getId(),postLikes);

    }
    @Transactional
    public void updatePost(Long id,String title,String content,String username){
        postRepository.updatePost(id,title,content,username);
    }

    @Transactional
    public void deletePost(Long id){
        postRepository.commentsdeleteById(id);
        postRepository.deletePostById(id);
    }

    public List<Post> getPagePosts(int page,int size){
        int offset = (page - 1)*size;
        return postRepository.findAllPagePosts(size,offset);
    }

    public int getTotalPages(int size){
        int total = postRepository.countAllPost();
        return (int) Math.ceil((double) total / size);
    }
}
