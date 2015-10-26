package stja.data_access;

import stja.entities.video.Video;

import javax.ejb.Stateless;

/**
 * Created by Michal on 2015-10-17.
 */
@Stateless
public class VideoDAO extends AbstractDAO<Video> {

    @Override
    protected Class<Video> getEntityClass() {
        return Video.class;
    }
}
