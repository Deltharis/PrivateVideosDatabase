package stja.data_access;

import stja.entities.video.Tag;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Michal on 2015-10-17.
 */
@Stateless
public class TagDAO extends AbstractDAO<Tag> {

    @Override
    protected Class<Tag> getEntityClass() {
        return Tag.class;
    }

    public List<TagRanking> getPopularityCount(){
        return em.createNativeQuery("select tag.text text, count(*) countz from Tags tag inner join VideoTags videotag on tag.id = videotag.tagid group by text order by countz desc, text asc", "TagRanking").getResultList();
    }
}
