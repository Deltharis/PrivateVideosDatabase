package stja.entities.tags;

import stja.data_access.tags.TagRanking;
import stja.entities.video.Video;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Delth on 07.11.2015.
 */
@SqlResultSetMapping(
        name = "TagRanking",
        classes = @ConstructorResult(
                targetClass = TagRanking.class,
                columns = {
                        @ColumnResult(name = "text", type = String.class),
                        @ColumnResult(name = "countz", type = Integer.class)}))
@DiscriminatorValue(value = "1")
@Entity
public class Person extends AbstractTag {

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "people")
    private Set<Video> videos;

    public Set<Video> getVideos() {
        return videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }
}
