package zuun.studying.firstapp.mapper;

import org.apache.ibatis.annotations.Mapper;
import zuun.studying.firstapp.Dto.FileDto;
import zuun.studying.firstapp.entity.FileEntity;

import java.io.File;
import java.util.List;

@Mapper
public interface FileMapper {

    void deleteFilesByPostId(Long postId);
    void insertFile(FileEntity file);
    List<FileDto> findFilesByPostId(Long id);

}
