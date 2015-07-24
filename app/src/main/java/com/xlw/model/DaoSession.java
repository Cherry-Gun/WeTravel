package com.xlw.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.xlw.model.Trip;
import com.xlw.model.Location;
import com.xlw.model.Photo;
import com.xlw.model.Feeling;

import com.xlw.model.TripDao;
import com.xlw.model.LocationDao;
import com.xlw.model.PhotoDao;
import com.xlw.model.FeelingDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig tripDaoConfig;
    private final DaoConfig locationDaoConfig;
    private final DaoConfig photoDaoConfig;
    private final DaoConfig feelingDaoConfig;

    private final TripDao tripDao;
    private final LocationDao locationDao;
    private final PhotoDao photoDao;
    private final FeelingDao feelingDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        tripDaoConfig = daoConfigMap.get(TripDao.class).clone();
        tripDaoConfig.initIdentityScope(type);

        locationDaoConfig = daoConfigMap.get(LocationDao.class).clone();
        locationDaoConfig.initIdentityScope(type);

        photoDaoConfig = daoConfigMap.get(PhotoDao.class).clone();
        photoDaoConfig.initIdentityScope(type);

        feelingDaoConfig = daoConfigMap.get(FeelingDao.class).clone();
        feelingDaoConfig.initIdentityScope(type);

        tripDao = new TripDao(tripDaoConfig, this);
        locationDao = new LocationDao(locationDaoConfig, this);
        photoDao = new PhotoDao(photoDaoConfig, this);
        feelingDao = new FeelingDao(feelingDaoConfig, this);

        registerDao(Trip.class, tripDao);
        registerDao(Location.class, locationDao);
        registerDao(Photo.class, photoDao);
        registerDao(Feeling.class, feelingDao);
    }
    
    public void clear() {
        tripDaoConfig.getIdentityScope().clear();
        locationDaoConfig.getIdentityScope().clear();
        photoDaoConfig.getIdentityScope().clear();
        feelingDaoConfig.getIdentityScope().clear();
    }

    public TripDao getTripDao() {
        return tripDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public PhotoDao getPhotoDao() {
        return photoDao;
    }

    public FeelingDao getFeelingDao() {
        return feelingDao;
    }

}