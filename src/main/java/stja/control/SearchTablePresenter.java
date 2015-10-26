package stja.control;

import stja.data_access.TagDAO;
import stja.data_access.TagRanking;
import stja.data_access.VideoDAO;
import stja.entities.video.Tag;
import stja.entities.video.Video;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Michal on 2015-10-17.
 */
@Stateless
public class SearchTablePresenter {

    @Inject
    private VideoDAO videoDAO;

    @Inject
    private TagDAO tagDAO;

    public List<Video> getAllVideos(){
        return videoDAO.getAll();
    }

    public List<Tag> getAllTags() {
        return tagDAO.getAll();
    }
    public void delete(Video video){
        videoDAO.remove(video);
    }

    public void saveVideo(Video video){
        videoDAO.merge(video);
    }
    public void saveTag(Tag tag){
        tagDAO.merge(tag);
    }
    public List<TagRanking> getRanking(){
        return tagDAO.getPopularityCount();
    }


}
