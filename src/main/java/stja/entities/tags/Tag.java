package stja.entities.tags;

import stja.data_access.tags.TagRanking;
import stja.entities.video.Video;

import javax.persistence.*;
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
@DiscriminatorValue(value = "0")
public class Tag extends AbstractTag {

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "tags")
    private Set<Video> videos;

    public Set<Video> getVideos() {
        return videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

}
