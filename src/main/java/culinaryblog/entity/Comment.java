package culinaryblog.entity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    private Integer id;

    private String content;

    private User author;

    private Recipe recipe;




    public Comment(String content, User author, Recipe recipe) {
        this.content = content;
        this.author = author;
        this.recipe = recipe;

    }

    public Comment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    @ManyToOne()
    @JoinColumn(name = "recipeId")
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


    @Transient
    public String getSummery(){

        return this.getContent();

    }

}
