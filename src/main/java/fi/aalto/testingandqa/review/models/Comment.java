package fi.aalto.testingandqa.review.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String author;

    @Column( nullable = false)
    private String body;

    @OneToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
    private Set<Reaction> reactions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }

    public void addReaction(ReactionType type) {
        Optional<Reaction> reactionWithType = this.reactions.stream()
                .filter(reaction -> reaction.getType() == type )
                .findFirst();

        Reaction reaction = reactionWithType.orElse(new Reaction(type));
        reaction.setAmount( reaction.getAmount() + 1 );

        if (reactionWithType.isPresent())
            reactions.remove(reaction);

        reactions.add(reaction);
    }

}
