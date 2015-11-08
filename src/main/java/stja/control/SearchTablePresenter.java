package stja.control;

import stja.data_access.tags.PersonDAO;
import stja.data_access.tags.TagDAO;
import stja.data_access.tags.TagRanking;
import stja.data_access.video.VideoDAO;
import stja.entities.tags.AbstractTag;
import stja.entities.tags.Person;
import stja.entities.tags.Tag;
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

    @Inject
    private PersonDAO personDAO;

    public List<Video> getAllVideos(){
        return videoDAO.getAll();
    }

    public List<Tag> getAllTags() {
        return tagDAO.getAll();
    }

    public List<Person> getAllPeople() {
        return personDAO.getAll();
    }
    public void delete(Video video){
        videoDAO.remove(video);
    }

    public void saveVideo(Video video){
        videoDAO.merge(video);
    }

    public void saveTag(AbstractTag tag) {
        if (tag instanceof Person) {
            savePerson((Person) tag);
        } else {
            saveTag((Tag) tag);
        }
    }

    private void savePerson(Person person) {
        personDAO.merge(person);
    }

    private void saveTag(Tag tag) {
        tagDAO.merge(tag);
    }

    public List<TagRanking> getTagRanking() {
        return tagDAO.getPopularityCount();
    }

    public List<TagRanking> getPersonRanking() {
        return personDAO.getPopularityCount();
    }

    public void deleteTags() {
        tagDAO.removeAll();
        personDAO.removeAll();
    }

    public void testUpdateValues() {
        videoDAO.testUpdate();
    }


}
