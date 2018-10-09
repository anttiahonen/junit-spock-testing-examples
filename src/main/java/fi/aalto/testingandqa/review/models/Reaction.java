package fi.aalto.testingandqa.review.models;

import javax.persistence.*;

@Entity
public class Reaction {

    public Reaction() {

    }

    public Reaction(ReactionType type) {
        this.amount = 0;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer amount;

    @Column
    @Enumerated(EnumType.STRING)
    private ReactionType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ReactionType getType() {
        return type;
    }

    public void setType(ReactionType type) {
        this.type = type;
    }

}
