package stja.entities.video;


import org.hibernate.annotations.Where;
import stja.entities.tags.Person;
import stja.entities.tags.Tag;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by Delth on 17.10.2015.
 */
@Entity
@Table(name = "Videos")
public class Video implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "url", length = 200, nullable = false)
    private String url = "";
    @Column(name = "title", length = 50, nullable = false)
    private String title = "";
    @Column(name = "description", length = 500)
    private String description = "";

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "VideoTags", joinColumns = {
            @JoinColumn(name = "VideoId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "TagId",
                    nullable = false, updatable = false)})
    @Where(clause = "person = 0")
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "VideoTags", joinColumns = {
            @JoinColumn(name = "VideoId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "TagId",
                    nullable = false, updatable = false)})
    @Where(clause = "person = 1")
    private Set<Person> people;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "video_date")
    private Date video_date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getVideo_date() {
        return video_date;
    }

    public void setVideo_date(Date video_date) {
        this.video_date = video_date;
    }

    @PrePersist
    @PreUpdate
    private void timestamping() {
        timestamp = new Date();
    }


}
