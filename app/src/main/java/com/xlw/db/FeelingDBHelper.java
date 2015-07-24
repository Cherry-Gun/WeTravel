package com.xlw.db;

import com.xlw.model.Feeling;
import com.xlw.model.FeelingDao;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/5.
 */
public class FeelingDBHelper {
//    Context mContext;
    FeelingDao feelingDao;

    public FeelingDBHelper() {
//        this.mContext = mContext;
        feelingDao = DBHelper.getInstance().feelingDao;
    }

    // 根据id加载对象
    public Feeling loadPhoto(long id) {
        return feelingDao.load(id);
    }

    // 加载所有对象
    public List<Feeling> loadAllFeeling(){
        return feelingDao.loadAll();
    }

    /**
     * 使用where子句查询所有符合条件的Trip对象e
     * 例如: start >= ?
     * @param where where子句, 要包括'where'单词
     * @param params 查询参数
     * @return
     */
    public List<Feeling> queryFeeling(String where, String... params){
        return feelingDao.queryRaw(where, params);
    }

    // 添加
    public void saveFeeling(Feeling feeling){
        feelingDao.insertOrReplace(feeling);
    }

    // 删除
    public void removeFeeling(long id){
        Feeling feeling = feelingDao.load(id);
        feelingDao.delete(feeling);
    }

    // 修改
    public void updateFeeling(Feeling feeling){
        feelingDao.insertOrReplace(feeling);
    }

    /**
     * 应用事务,插入或更新一批数据
     * @param list 保存的一批数据
     */
    public void saveFeelingLists(final List<Feeling> list){
        if(list == null || list.isEmpty()){
            return;
        }
        feelingDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    Feeling feeling = list.get(i);
                    feelingDao.insertOrReplace(feeling);
                }
            }
        });

    }

    /**
     * 删除所有
     */
    public void deleteAllFeeling(){
        feelingDao.deleteAll();
    }

    /**
     * 通过id删除指定的对象
     * @param id 要被删除的对象的key
     */
    public void deleteFeeling(long id){
        feelingDao.deleteByKey(id);
    }

    public void deleteFeeling(Feeling feeling){
        feelingDao.delete(feeling);
    }

}
