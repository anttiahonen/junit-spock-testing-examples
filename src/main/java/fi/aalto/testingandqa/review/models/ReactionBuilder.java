package fi.aalto.testingandqa.review.models;

public final class ReactionBuilder {
    private Long id;
    private Integer amount;
    private ReactionType type;

    private ReactionBuilder() {
    }

    public static ReactionBuilder aReaction() {
        return new ReactionBuilder();
    }

    public ReactionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ReactionBuilder withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public ReactionBuilder withType(ReactionType type) {
        this.type = type;
        return this;
    }

    public Reaction build() {
        Reaction reaction = new Reaction(type);
        reaction.setId(id);
        reaction.setAmount(amount);
        return reaction;
    }
}
