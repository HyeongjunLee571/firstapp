package zuun.studying.firstapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "files")
@NoArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private String originalName; //원본 파일명
    private String storedName; //저장된 파일명(UUID)
    private String filePath; //실제 경로
    private String fileType; //파일 속성

}
