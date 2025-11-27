package zuun.studying.firstapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {

    private Long id;
    private String title;
    private String content;
    private int likes;
    private UserDto user;
    private List<FileDto> files;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

}
