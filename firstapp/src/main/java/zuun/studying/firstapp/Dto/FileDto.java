package zuun.studying.firstapp.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private Long id;
    //private String originalName;//(실제 파일명)
    private String storedName;//(UUID로 변환된 파일명)
    private String filePath;
    private String fileType;
    private Long postId;
}
