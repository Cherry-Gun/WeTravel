package com.xlw.db;

import com.xlw.model.Location;
import com.xlw.model.LocationDao;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/5.
 */
public class LocationDBHelper {
//    Context mContext;
    LocationDao locationDao;

    public LocationDBHelper() {
//        this.mContext = mContext;
        locationDao = DBHelper.getInstance().locationDao;
    }

    // 根据id加载Location对象
    public Location loadLocation(long id) {
        return locationDao.load(id);
    }

    // 加载所有的Trip对象
    public List<Location> loadAllLocation(){
        return locationDao.loadAll();
    }

    /**
     * 使用where子句查询所有符合条件的Trip对象e
     * 例如: start >= ?
     * @param where where子句, 要包括'where'单词
     * @param params 查询参数
     * @return
     */
    public List<Location> queryLocation(String where, String... params){
        return locationDao.queryRaw(where, params);
    }

    // 添加
    public void saveLocation(Location location){
        locationDao.insertOrReplace(location);
    }

    // 删除
    public void removeLocation(long id){
        Location location = locationDao.load(id);
        locationDao.delete(location);
    }

    // 修改
    public void updateLocation(Location location){
        locationDao.insertOrReplace(location);
    }

    /**
     * 应用事务,插入或更新一批Location
     * @param list 保存的一批Location
     */
    public void saveLocationLists(final List<Location> list){
        if(list == null || list.isEmpty()){
            return;
        }
        locationDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    Location location = list.get(i);
                    locationDao.insertOrReplace(location);
                }
            }
        });

    }

    /**
     * 删除所有的trip
     */
    public void deleteAllLocation(){
        locationDao.deleteAll();
    }

    /**
     * 通过id删除指定的trip
     * @param id 要被删除的trip的key
     */
    public void deleteLocation(long id){
        locationDao.deleteByKey(id);
    }

    public void deleteLocation(Location location){
        locationDao.delete(location);
    }
}

