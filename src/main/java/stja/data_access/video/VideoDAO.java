package stja.data_access.video;

import stja.data_access.AbstractDAO;
import stja.entities.video.Video;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Delth on 17.10.2015.
 */
@Stateless
public class VideoDAO extends AbstractDAO<Video> {

    @Override
    protected Class<Video> getEntityClass() {
        return Video.class;
    }

    public void testUpdate() {
        em.createNativeQuery("UPDATE Videos set core = 0").executeUpdate();
    }

    @Override
    public List<Video> getAll() {
        return em.createQuery("FROM Video ORDER BY title", Video.class).getResultList();
    }
}
