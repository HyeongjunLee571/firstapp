package zuun.studying.firstapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
//@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private int likes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

 }
