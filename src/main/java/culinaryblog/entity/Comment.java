package culinaryblog.entity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    private Integer id;

    private String content;

    private User author;






    public Comment(String content, User author) {
        this.content = content;
        this.author = author;

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

    @Transient
    public String getSummery(){

        return this.getContent();

    }

}
