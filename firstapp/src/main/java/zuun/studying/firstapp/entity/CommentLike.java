package zuun.studying.firstapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comment_likes",uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id","user_id"}))
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Comment comment;
    @ManyToOne
    private User user;

}
