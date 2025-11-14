package zuun.studying.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zuun.studying.firstapp.entity.FileEntity;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

List<FileEntity> findByPostId(Long id);


}

