package zuun.studying.firstapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_likes",uniqueConstraints = @UniqueConstraint(columnNames = {"post_id","user_id"}))
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Post post;
    @ManyToOne
    private User user;

}
