package com.xlw.db;

import com.xlw.model.Photo;
import com.xlw.model.PhotoDao;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/5.
 */
public class PhotoDBHelper {
//    Context mContext;
    PhotoDao photoDao;

    public PhotoDBHelper() {
//        this.mContext = mContext;
        photoDao = DBHelper.getInstance().photoDao;
    }

    // 根据id加载对象
    public Photo loadPhoto(long id) {
        return photoDao.load(id);
    }

    // 加载所有对象
    public List<Photo> loadAllPhoto(){
        return photoDao.loadAll();
    }

    /**
     * 使用where子句查询所有符合条件的Trip对象e
     * 例如: start >= ?
     * @param where where子句, 要包括'where'单词
     * @param params 查询参数
     * @return
     */
    public List<Photo> queryPhoto(String where, String... params){
        return photoDao.queryRaw(where, params);
    }

    // 添加
    public long savePhoto(Photo photo){
        return photoDao.insertOrReplace(photo);
    }

    // 删除
    public void removePhoto(long id){
        Photo photo = photoDao.load(id);
        photoDao.delete(photo);
    }

    // 修改
    public void updatePhoto(Photo photo){
        photoDao.insertOrReplace(photo);
    }

    /**
     * 应用事务,插入或更新一批数据
     * @param list 保存的一批数据
     */
    public void savePhotoLists(final List<Photo> list){
        if(list == null || list.isEmpty()){
            return;
        }
        photoDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    Photo photo = list.get(i);
                    photoDao.insertOrReplace(photo);
                }
            }
        });

    }

    /**
     * 删除所有
     */
    public void deleteAllPhoto(){
        photoDao.deleteAll();
    }

    /**
     * 通过id删除指定的对象
     * @param id 要被删除的对象的key
     */
    public void deletePhoto(long id){
        photoDao.deleteByKey(id);
    }

    public void deletePhoto(Photo photo){
        photoDao.delete(photo);
    }
}
