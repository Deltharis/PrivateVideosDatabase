package stja.data_access.tags;

import stja.entities.tags.Person;

import javax.ejb.Stateless;
import java.util.Comparator;
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

    @Override
    public List<Person> getAll() {
        List<Person> list = super.getAll();
        list.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                String surnameo1 = o1.getText().split(" ")[1];
                String surnameo2 = o2.getText().split(" ")[1];
                return surnameo1.compareTo(surnameo2);
            }
        });
        return list;
    }
}
