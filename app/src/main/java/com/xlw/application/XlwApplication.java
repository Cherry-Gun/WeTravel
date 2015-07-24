package com.xlw.application;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.tandong.sa.vl.RequestQueue;
import com.tandong.sa.vl.toolbox.Volley;
import com.tandong.sa.zUImageLoader.cache.disc.impl.UnlimitedDiscCache;
import com.tandong.sa.zUImageLoader.cache.disc.naming.Md5FileNameGenerator;
import com.tandong.sa.zUImageLoader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoaderConfiguration;
import com.tandong.sa.zUImageLoader.core.assist.QueueProcessingType;
import com.tandong.sa.zUImageLoader.core.download.BaseImageDownloader;
import com.xlw.model.DaoMaster;
import com.xlw.model.DaoSession;
import java.io.File;
import java.util.Random;

/**
 * Created by xinliwei on 2015/7/4.
 *
 * 全局类
 */
public class XlwApplication extends Application {

    // 使用单例模式
    private static XlwApplication instance;
    public static XlwApplication getInstance(){
        return instance;
    }

    /* 构建一个全局的RequestQueue请求队列
       用于使用Volley请求网络数据
     */
    public static RequestQueue requestQueue;

    /* 本就用需要缓存的地方主要是以下情形:
        1. 当查看旅行记忆时,ListView最好是先从缓存中查找照片,所以这里要应用缓存策略
        2. 查看照片画廊或照片干墙时,因为图片比较多,所以需要缓存照片,并使用缩略图显示
     */
    LruCache<String,Bitmap> lruCache;

    private com.tandong.sa.zUImageLoader.core.ImageLoader imageLoader;

    /* 数据库DAO访问相关 */
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    /**
     *主题theme设置
     */
    // 动态切换theme,必须放在super.onCreate()方法之前
    public static int currentTheme;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 获得图像加载器的单例
        imageLoader = com.tandong.sa.zUImageLoader.core.ImageLoader.getInstance();

        init();
        initTheme();    // 初始化主题
    }

    protected void init(){
        initVolleryRequestQueue();
        initImageLoaderConfig();
    }

    protected void initTheme(){
        Random random = new Random();
        currentTheme = random.nextInt(3);
    }

    protected void initVolleryRequestQueue(){
        requestQueue = Volley.newRequestQueue(this);
    }

    // 初始化ImageLoader
    protected void initImageLoaderConfig(){
        // 首先需要配置全局的图片加载处理策略，当然你也可以单独为每一个用到的地方写一个配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)      // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())     //将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(new File(AppConfig.CACHE_DIR)))   //自定义缓存路径,定义在AppConfig.java中
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()   // Remove for release app
                .build();           //开始构建

        imageLoader.init(config);//全局初始化此配置
    }

    /**
     * 获得DaoMaster
     *
     * @param context
     * @return
     */
    public DaoMaster getDaoMaster(Context context){
        if(daoMaster == null){
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,AppConfig.DATABASE_NAME,null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }

        return daoMaster;
    }

    /**
     * 获得DaoSession
     *
     * @param context
     * @return
     */
    public DaoSession getDaoSession(Context context){
        if(daoMaster == null){
            daoMaster = getDaoMaster(context);
        }

        daoSession = daoMaster.newSession();

        return daoSession;
    }

    public DaoMaster getDaoMaster(){
        if(daoMaster == null){
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(XlwApplication.getInstance(),AppConfig.DATABASE_NAME,null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }

        return daoMaster;
    }

    /**
     * 获得DaoSession
     *
     * @param
     * @return
     */
    public DaoSession getDaoSession(){
        if(daoMaster == null){
            daoMaster = getDaoMaster();
        }

        daoSession = daoMaster.newSession();

        return daoSession;
    }

    /**
     * 获得当前"com.xlw.onroad"类型的账号.如果没有,则跳转到MenuActivity.class
     * @return 当前账号.如果没有,则返回null
     */
//    public Account getCurrentAccount() {
//        AccountManager accountManager = AccountManager.get(this);
//        // String PARAM_ACCOUNT_TYPE = "com.xlw.onroad"
//        Account[] accounts = accountManager.getAccountsByType(AuthenticatorActivity.PARAM_ACCOUNT_TYPE);
//
//        if (accounts.length > 0) {
//            return accounts[0];
//        } else {
//            Intent intent = new Intent(this, MenuActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            return null;
//        }
//    }

    public com.tandong.sa.zUImageLoader.core.ImageLoader getImageLoader(){
        // 单例模式
        return imageLoader;
    }

}
