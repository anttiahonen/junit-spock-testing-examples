package fi.aalto.testingandqa.review.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String title;

    @Column( nullable = false)
    private String body;

    @OneToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
    private List<Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
