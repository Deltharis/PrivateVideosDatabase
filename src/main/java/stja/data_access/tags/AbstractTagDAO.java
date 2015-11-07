package stja.data_access.tags;

import stja.data_access.AbstractDAO;

import java.util.List;

/**
 * Created by Delth on 07.11.2015.
 */
public abstract class AbstractTagDAO<T> extends AbstractDAO<T> {

    public abstract List<TagRanking> getPopularityCount();
}
