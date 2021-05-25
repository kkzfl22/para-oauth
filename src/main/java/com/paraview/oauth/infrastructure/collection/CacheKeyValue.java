package com.paraview.oauth.infrastructure.collection;


import com.paraview.oauth.infrastructure.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 进行永久缓存器的实现,此缓存只会加不会减
 * <p>
 * 本类利用key的HashCode和分流器大小简单求余来分流,使得不依赖一个对象同步锁,提高性能
 *
 * @author liujun
 * @version 0.0.1
 * @since 2017年6月13日 下午1:31:28
 */
@Slf4j
public class CacheKeyValue<K, V> {


    /**
     * map的初始化大小
     */
    private static final int INIT_MAP_SIZE = 32768;


    /**
     * 系统的默认路径
     */
    private static final String DEFAULT_PATH = "./";

    /**
     * 默认后缀名
     */
    private static final String DEFAULT_NAME = ".dataStore.buffMap";

    /**
     * 后缀名
     */
    private String dataStore;

    /**
     * 默认缓存分流大小
     */
    public static final int DEFAULT_CORE_POOL_SIZE = 16;

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 当前缓存器的分流大小
     */
    private int corePoolSize;

    /**
     * 缓存实体对象
     */
    private ConcurrentHashMap<K, V>[] mapCache;

    /**
     * 构造一个默认大小分流方式的缓存器
     */
    public CacheKeyValue() {
        this(DEFAULT_CORE_POOL_SIZE, DEFAULT_NAME);
    }

    /**
     * 构造一个默认大小分流方式的缓存器
     */
    public CacheKeyValue(String dataStore) {
        this(DEFAULT_CORE_POOL_SIZE, dataStore);
    }

    /**
     * 构造一个指定大小分流方式的缓存器
     *
     * @param corePoolSize 初始化分流器值
     */
    public CacheKeyValue(int corePoolSize, String dataStore) {
        this.corePoolSize = tableSizeFor(corePoolSize);
        this.dataStore = dataStore;
        mapCache = new ConcurrentHashMap[this.corePoolSize];
        for (int i = 0; i < this.corePoolSize; i++) {
            mapCache[i] = new ConcurrentHashMap<>(INIT_MAP_SIZE);
        }

        // 当数容器启动完成，需要加载数据
        this.defaultLoader();
    }


    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 简单的求余分流算法
     *
     * @param h      当前的唯一HashCode
     * @param length 分流器大小
     * @return 获取分流索引
     * @see [类、类#方法、类#成员]
     */
    private static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * 存放一个对象键值对
     *
     * @param key   键
     * @param value 值
     * @return 原始值
     * @see [类、类#方法、类#成员]
     */
    public void put(K key, V value) {
        indexForMap(key).put(key, value);
    }

    /**
     * 根据key获取并移除存放的对象
     *
     * @param key 键
     * @return Object 值
     * @see [类、类#方法、类#成员]
     */
    public V remove(K key) {
        return indexForMap(key).remove(key);
    }

    /**
     * 根据key获取存放的对象
     *
     * @param key 键
     * @return Object 值
     * @see [类、类#方法、类#成员]
     */
    public V get(K key) {
        return indexForMap(key).get(key);
    }

    /**
     * 定位到存储数据的map
     *
     * @param key 键
     * @return 分流缓存对象
     * @see [类、类#方法、类#成员]
     */
    private Map<K, V> indexForMap(K key) {
        return mapCache[indexFor(key.hashCode(), corePoolSize)];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Cache Size [" + corePoolSize + "]";
    }


    /**
     * 默认的文件路径读取
     */
    public void defaultLoader() {
        InputStream inputStream = getFileInputStream(DEFAULT_PATH + dataStore);
        //如果为空，则跳过
        if (null == inputStream) {
            return;
        }
        //读取数据
        this.loader(inputStream);
    }


    /**
     * 文件获取,优先加载本地配制文件，未找则则从内部找文件
     *
     * @param absPath
     * @return
     * @throws IllegalArgumentException 当未加载配制时，收报错
     */
    public static InputStream getFileInputStream(String absPath) {
        // 优先加载外部配制文件
        InputStream input = getOutFileStream(absPath);
        // 当外部文件不存时，则使用内部配制文件,文件也可能存在于jar包或者普通工程中
        if (null == input) {
            input = CacheKeyValue.class.getClassLoader().getResourceAsStream(absPath);
        }
        if (null == input) {
            input = CacheKeyValue.class.getResourceAsStream(absPath);
        }
        if (null == input) {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(absPath);
        }

        return input;
    }


    /**
     * 获取外部文件的流
     *
     * @return 文件流
     */
    private static InputStream getOutFileStream(String path) {
        InputStream outFileStream = null;
        try {
            outFileStream = new FileInputStream(path);
        }
        // 当外部文件不存在时，会报出文件不存在异常，此异常需忽略，后续加载内置文件即可
        catch (FileNotFoundException e) {
            log.info("out file not exists : {}", path);
        }

        return outFileStream;
    }

    /**
     * 数据的加载操作
     */
    public void loader(InputStream inputStream) {
        try (BufferedInputStream bufferInput = new BufferedInputStream(inputStream);
             ObjectInputStream ooRead = new ObjectInputStream(bufferInput)) {
            mapCache = (ConcurrentHashMap<K, V>[]) ooRead.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("CacheKeyValue loader ClassNotFoundException", e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("CacheKeyValue loader IOException", e);
        } finally {
            StreamUtils.close(inputStream);
        }

    }

    /**
     * 默认文件的输出操作
     */
    public void defaultSave() {
        this.save(DEFAULT_PATH, dataStore);
    }


    /**
     * 指定路径的写入
     *
     * @param path     路径
     * @param fileName 文件名
     */
    public void save(String path, String fileName) {

        // 首先检查文件路径，不存在，则创建
        File basePath = new File(path);
        if (!basePath.exists()) {
            // 进行文件夹的创建
            basePath.mkdirs();
        }

        File fileRead = new File(path, fileName);
        try (FileOutputStream fileOutput = new FileOutputStream(fileRead);
             BufferedOutputStream bufferOutput = new BufferedOutputStream(fileOutput);
             ObjectOutputStream ooWrite = new ObjectOutputStream(bufferOutput)) {
            ooWrite.writeObject(mapCache);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("CacheKeyValue save ClassNotFoundException", e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("CacheKeyValue save IOException", e);
        }
    }

    public ConcurrentHashMap<K, V>[] getMapCache() {
        return mapCache;
    }

}
