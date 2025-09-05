package zuun.studying.firstapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.exception.PostException;
import zuun.studying.firstapp.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void save(Post post){

        postRepository.save(post);
    }

    public List<Post> findAll(){

        return postRepository.findAll();
    }

    public Post findById(Long id){

        return postRepository.findById(id).orElseThrow(PostException::new);

    }

    public void addLike(Long id){

        Post post = findById(id);
        post.setLikes(post.getLikes()+1);
        postRepository.save(post);

    }
}
