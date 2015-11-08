package stja.data_access.tags;

import stja.entities.tags.Tag;

import javax.ejb.Stateless;
import java.util.Comparator;
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

    @Override
    public List<Tag> getAll() {
        List<Tag> list = super.getAll();
        list.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                String surnameo1 = o1.getText();
                String surnameo2 = o2.getText();
                return surnameo1.compareTo(surnameo2);
            }
        });
        return list;
    }
}
