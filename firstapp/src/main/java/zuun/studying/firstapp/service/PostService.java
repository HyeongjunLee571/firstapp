package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zuun.studying.firstapp.Dto.PostCreateDto;
import zuun.studying.firstapp.Dto.PostDetailDto;
import zuun.studying.firstapp.Dto.PostDto;
import zuun.studying.firstapp.entity.FileEntity;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.PostNotFoundException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.CommentMapper;
import zuun.studying.firstapp.mapper.FileMapper;
import zuun.studying.firstapp.mapper.PostMapper;
import zuun.studying.firstapp.repository.FileRepository;
import zuun.studying.firstapp.repository.UserRepository;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    //private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final CommentMapper commentMapper;

    @Value("${custom.page-size}")
    private int PageSize;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public void save(PostDto postDto, String author, List<MultipartFile> files) throws IOException {

        User user = userRepository.findByUsername(author).
                orElseThrow(()->new UserNotFoundException("없는 사용자입니다."));

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUserId(user.getId());

        postMapper.insertPost(post);

        Files.createDirectories(Paths.get(uploadDir));

        for(MultipartFile file : files){

            if(file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            String storedName = UUID.randomUUID() + "_" + originalName;
            Path path = Paths.get(uploadDir,storedName);
            file.transferTo(path.toFile());
            System.out.println("파일 경로"+path.toString());


            String contentType = file.getContentType();
            String fileType = (contentType != null && contentType.startsWith("video")) ? "video" : "image";
            //업로드 된 파일 타입을 가져오고 파일이 비디오면 그대로 비디오로 대입 그게 아니면 이미지로 대입

            FileEntity fileEntity = new FileEntity();
            fileEntity.setOriginalName(originalName);
            fileEntity.setStoredName(storedName);
            fileEntity.setFilePath("/uploads/"+storedName);
            System.out.println("웹 접근할 경로"+fileEntity.getFilePath());
            fileEntity.setFileType(fileType);
            fileEntity.setPostId(post.getId());

            fileRepository.save(fileEntity); // 엔티티에 반영해도 꼭 세이브를 통해 저장해야 데이터베이스에 수정정보가 저장 됨
            //세이브 안 할 시 엔티티에만 값 들어가고 데이터베이스는 실제 적용 X

        }
    }

    //게시글 단일 조회
    public PostDetailDto findById(Long id){

        PostDetailDto postDetailDto =  postMapper.findPostWithUserFiles(id);
        if(postDetailDto == null){
            throw new PostNotFoundException("존재하지 않는 파일정보입니다.");
        }

        return postDetailDto;
    }

    //update 또는 delete 쿼리는 반드시 트랜잭션 안에서 실행되어야 함(생성 및 추가가 곧 수정이기이)
    @Transactional
    public void addLike(Long id){

        PostDetailDto postDetailDto =  postMapper.findPostWithUserFiles(id);
        if(postDetailDto == null){
            throw new PostNotFoundException("존재하지 않는 파일정보입니다.");
        }
        postMapper.updatePostLikes(postDetailDto.getId());

    }
    @Transactional
    public void updatePost(Long id,PostCreateDto postCreateDto,String author,List<MultipartFile> files) throws IOException {

        PostDetailDto postDetailDto =  postMapper.findPostWithUserFiles(id);
        if(postDetailDto == null){
            throw new PostNotFoundException("존재하지 않는 파일정보입니다.");
        }

        User user = userRepository.findByUsername(author).
                orElseThrow(()->new UserNotFoundException("없는 사용자입니다."));


        if(!postDetailDto.getUser().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }//로그인 사용자와 작성자가 같지 않을때 예외 발생 > 조건문 앞에 ! 없으면 같으면 예외 발생

        PostDto postDtos = new PostDto();
        postDtos.setId(id);
        postDtos.setTitle(postCreateDto.getTitle());
        postDtos.setContent(postCreateDto.getContent());
        //나머지 좋아요는 엔티티를 별도로 수정하는것이 아니기에 기존 값이 적용
        //> 이유는 상세 페이지가 엔티티와 연동되어있기 때문
        //유저 네임 및 유저도 다시 넘길 필요 X 변동이 없기때문(유저도 엔티티로 연동됨)

        postMapper.updatePost(postDtos);


        List<FileEntity> fileEntities = fileRepository.findByPostId(id);

        if(files == null || files.isEmpty() || files.get(0).isEmpty()){
            return;
        }

        for(FileEntity fileEntity : fileEntities) {
                //기존 파일 물리적으로 삭제 및 DB 삭제
                deleteFile(fileEntity);
                fileMapper.deleteFilesByPostId(id);
        }

        //새 파일 업로드
        String uploadDir = "C:/myapp/uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        for(MultipartFile file : files){

            String originalName = file.getOriginalFilename();
            String storeName = UUID.randomUUID() + "_" + originalName;
            Path path = Paths.get(uploadDir,storeName);
            file.transferTo(path.toFile());

            String contentType = file.getContentType();
            String fileType = (contentType != null && contentType.startsWith("video")) ? "video" : "image";

            FileEntity newfileEntity = new FileEntity();
            newfileEntity.setOriginalName(originalName);
            newfileEntity.setStoredName(storeName);
            newfileEntity.setFilePath("/uploads/" + storeName);
            newfileEntity.setFileType(fileType);
            newfileEntity.setPostId(postDtos.getId());

            fileRepository.save(newfileEntity);

        }
    }

    public Page<PostDto> getPagePosts(int page){
        Pageable pageable = PageRequest.of(page - 1,PageSize);//N페이지 가져와 > 한 페이지당 표시 데이터 N개 적용
        return postMapper.selectAllPosts(pageable);
    }

    @Transactional
    public void deletePost(Long id){

        List<FileEntity> fileEntities = fileRepository.findByPostId(id);
        for(FileEntity fileEntity : fileEntities){
            deleteFile(fileEntity);
        }
        fileMapper.deleteFilesByPostId(id);
        commentMapper.deleteCommentByPostId(id);
        postMapper.deletePost(id);
    }

    public void deleteFile(FileEntity fileEntity){

        Path filePath = Paths.get(fileEntity.getFilePath());

        try {
            if(Files.exists(filePath)) {
                Files.delete(filePath);
                log.warn("파일 삭제 성공 : {}",filePath);
            }else {
                log.warn("파일이 존재 하지 않습니다 : {}",filePath);
            }
        }catch (Exception e){
            log.error("파일 삭제 실패 : {}",filePath,e);
        }

    }
}
