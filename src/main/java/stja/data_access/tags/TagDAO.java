package stja.data_access.tags;

import stja.entities.tags.Tag;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Delth on 17.10.2015.
 */
@Stateless
public class TagDAO extends AbstractTagDAO<Tag> {

    @Override
    protected Class<Tag> getEntityClass() {
        return Tag.class;
    }

    @Override
    public List<TagRanking> getPopularityCount() {
        return em.createNativeQuery("select tag.text text, count(*) countz from Tags tag inner join VideoTags videotag on tag.id = videotag.tagid where tag.person = 0 group by text order by countz desc, text asc", "TagRanking").getResultList();
    }

    public void removeAll() {
        em.createQuery("DELETE FROM Tag").executeUpdate();
    }
}
