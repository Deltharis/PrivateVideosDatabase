package stja.entities.video;

import stja.data_access.TagRanking;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Michal on 2015-10-17.
 */
@SqlResultSetMapping(
        name = "TagRanking",
        classes = @ConstructorResult(
                targetClass = TagRanking.class,
                columns = {
                        @ColumnResult(name = "text", type = String.class),
                        @ColumnResult(name = "countz", type = Integer.class)}))
@Entity
@Table(name = "Tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "person")
    private boolean person = false;
    @Column(name = "text", length = 25)
    private String text = "";

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "tags")
    private Set<Video> videos;

    public Set<Video> getVideos() {
        return videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPerson() {
        return person;
    }

    public void setPerson(boolean person) {
        this.person = person;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString(){
        return getText();
    }
}
