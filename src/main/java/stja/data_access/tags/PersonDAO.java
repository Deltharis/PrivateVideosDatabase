package stja.data_access.tags;

import stja.entities.tags.Person;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Delth on 07.11.2015.
 */
@Stateless
public class PersonDAO extends AbstractTagDAO<Person> {
    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }


    @Override
    public List<TagRanking> getPopularityCount() {
        return em.createNativeQuery("select tag.text text, count(*) countz from Tags tag inner join VideoTags videotag on tag.id = videotag.tagid where tag.person = 1 group by text order by countz desc, text asc", "PersonRanking").getResultList();
    }

    public void removeAll() {
        em.createQuery("DELETE FROM Person").executeUpdate();
    }
}
