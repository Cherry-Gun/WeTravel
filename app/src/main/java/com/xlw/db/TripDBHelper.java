package com.xlw.db;

import android.content.Context;

import com.xlw.model.Trip;
import com.xlw.model.TripDao;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/4.
 *
 * 对Trip表操作的辅助类
 */
public class TripDBHelper {

    Context mContext;
    TripDao tripDao;

    public TripDBHelper(Context mContext) {
        this.mContext = mContext;
        tripDao = DBHelper.getInstance(mContext).tripDao;
    }

    public TripDBHelper() {
        tripDao = DBHelper.getInstance().tripDao;
    }

    // 根据id加载Trip对象
    public Trip loadTrip(long id) {
        return tripDao.load(id);
    }

    // 加载所有的Trip对象
    public List<Trip> loadAllTrip(){
        return tripDao.loadAll();
    }

    /**
     * 使用where子句查询所有符合条件的Trip对象e
     * 例如: start >= ?
     * @param where where子句, 要包括'where'单词
     * @param params 查询参数
     * @return
     */
    public List<Trip> queryTrip(String where, String... params){
        return tripDao.queryRaw(where, params);
    }

    // 添加
    public long saveTrip(Trip trip){
        return tripDao.insertOrReplace(trip);
    }

    // 删除
    public void removeTrip(long id){
        Trip trip = tripDao.load(id);
        tripDao.delete(trip);
    }

    // 修改
    public void updateTrip(Trip trip){
        tripDao.insertOrReplace(trip);
    }

    /**
     * 应用事务,插入或更新一批Trip
     * @param list 保存的一批trip
     */
    public void saveTripLists(final List<Trip> list){
        if(list == null || list.isEmpty()){
            return;
        }
        tripDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    Trip trip = list.get(i);
                    tripDao.insertOrReplace(trip);
                }
            }
        });

    }

    /**
     * 删除所有的trip
     */
    public void deleteAllTrip(){
        tripDao.deleteAll();
    }

    /**
     * 通过id删除指定的trip
     * @param id 要被删除的trip的key
     */
    public void deleteTrip(long id){
        tripDao.deleteByKey(id);
    }

    public void deleteTrip(Trip trip){
        tripDao.delete(trip);
    }

//    public List<Trip> getAll(String category){
////        QueryBuilder qb = tripDao.queryBuilder();
////        qb.where(Properties.FirstName.eq("Joe"),
////                qb.or(Properties.YearOfBirth.gt(1970),
////                        qb.and(Properties.YearOfBirth.eq(1970),
////                                Properties.MonthOfBirth.ge(10))));
////        List youngJoes = qb.list();
//        re
//    }
}
