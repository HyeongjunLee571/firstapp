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
    private String originalName;
    private String filePath;
    private LocalDateTime createdAt;
}
