package org.platform.modules.abstr.storage;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICache {
    
    public String set(String key, Object o) throws IOException;

    public List<String> set(final String[] keys, final Object[] os) throws IOException;
    
    public String set(String key, Object o, int expireTime) throws IOException;
    
    public Long setnx(String key, Object o) throws IOException;
    
    public void delete(final String key) throws IOException;

    public <T> T get(String key) throws IOException;

    public <T> List<T> get(final String[] keys) throws IOException;

    public Long incrby(String key, long increment) throws IOException;
    
    public Long decrby(String key, long increment) throws IOException;

    public long zAdd(String key, double score, Object o) throws IOException;

    public long zAdd(String key, double score, Object o, long len) throws IOException;

    public long zAdd(String key, Map<Double, Object> map, long len) throws IOException;

    public long zCard(final String key) throws IOException;

    List<Long> zAdd(String[] keys, Double[] scores, Object[] os) throws IOException;

    public Double zScore(String key, Object o) throws IOException;

    /**
     * 在区间min, max中的数量, including items whose score equals min or max.
     * @param key
     * @param min
     * @param max
     * @return
     * @throws java.io.IOException
     */
    public long zCount(String key, double min, double max) throws IOException;

    public long zRemByValue(String key, Object value) throws IOException;
    
    public List<Long> zRemByValue(String[] keys, Object[] os) throws IOException;

    long zRemRangeByRank(String key, int start, int end) throws IOException;

    long zRemRangeByScore(String key, double start, double end) throws IOException;

    /**
     * redis原生操作，支持类似0 -1返回所有对象操作，返回从start到end的所有值（包含end）
     * @param <T>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public <T> Set<T> zRevRange(String key, int start, int end) throws IOException;

    /**
     * 模拟List根据index取值，start不可以<0，返回从start到end-1的所有值
     * @param <T>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public <T> Set<T> zRevRangeByOffset(final String key, final int start, final int end) throws IOException;

    public Set<Tuple> zRevWithScores(final String key, final int start, final int end) throws IOException;

    /**
     * get items by range, 左闭右开
     * @param <T>
     * @param key
     * @param min
     * @param max
     * @param rev
     * @return
     */
    public <T> Set<T> zRangeByScore(String key, double min, double max, boolean rev) throws IOException;

    /**
     * get items by range, results not including items whose score equals min or max. 开区间
     * @param <T>
     * @param key
     * @param min
     * @param max
     * @param rev
     * @param offset
     * @param length
     * @return
     */
    public <T> Set<T> zRangeByScore(String key, double min, double max,
            boolean rev, int offset, int length) throws IOException;

    /**
     * get items by range, results not including items whose score equals min or max. 开区间
     * @param key
     * @param min
     * @param max
     * @param rev
     * @param offset
     * @param length
     * @return
     */
    public Set<Tuple> zRangeByScoreWithScores(String key, double min,
            double max, boolean rev, int offset, int length) throws IOException;

    /**
     * zRangeByScoreWithScores using pipeline, not tested
     * @param keys
     * @param min
     * @param max
     * @param rev
     * @param offset
     * @param length
     * @return
     * @throws IOException
     */
    List<Set<Tuple>> zRangeByScoreWithScores(String[] keys, double min,
            double max, boolean rev, int offset, int length) throws IOException;

    /**
     * merge sort using pipeline, not tested
     * @param keys
     * @param min
     * @param max
     * @param rev
     * @param offset
     * @param length
     * @return
     * @throws IOException
     */
    List<Tuple> zMergeRangeByScoreWithScores(String[] keys, double min,
            double max, boolean rev, int offset, int length) throws IOException;

    public Void hmSet(final String[] keys, final Object[] os, final int expireTime) throws IOException;

    public Void hmSet(final String[] keys, final Object[] os) throws IOException;

    Void hmSet(String key, Object o, int expireTime) throws IOException;

    public String hmSet(String key, Object o) throws IOException;

    public void hmSet(String key, Map<String, Object> map) throws IOException;

    void hmSetCreateKeyIfNotExists(String key, Map<String, Object> map) throws IOException;

    public long hSet(String key, String field, String value) throws IOException;
    
    public long hSet(String key, String field, Object object) throws IOException;

    public long hSetCreateKeyIfNotExists(String key, String field, String value) throws IOException;
    
    public long hSetCreateKeyIfNotExist(final String key, final String field, final Object object) throws IOException;

    public String hGet(String key, String field) throws IOException;

    public Object hmGet(String key, Class<?> clazz) throws IOException;

    public long hDel(String key) throws IOException;

    public Long hIncrBy(String key, String field, int increment) throws IOException;

    public Long hIncrBy(final String key, final String field,
            final int increment, final long defaultValue) throws JedisException, IOException;

    public boolean hExists(String key) throws IOException;

    Long listLength(String key) throws IOException;

    <T> long  listAppend(String key, T[] objs) throws IOException;

    <T> long listAppend(final String key, T objs) throws IOException;

    <T> List<T> listRange(String key, int start, int end) throws IOException;

    public boolean expire(String key, int seconds) throws IOException;

    public boolean exists(String key) throws IOException;

    /**
     * api push set map
     * @param key
     * @param map
     * @throws java.io.IOException
     */
    public void hmSetForPush(final String key, final Map<Long, Object> map) throws IOException;

    /**
     * api push get map
     * @param key
     * @return
     * @throws java.io.IOException
     */
    public Object hmGetForPush(final String key) throws IOException;
    
    /**
     * delete value(s) from list
     * @param <T>
     * @param key
     * @param objs
     * @throws java.io.IOException
     */
    <T> void listDel(String key, T[] objs) throws IOException;

    <T> void listDel(final String key, final T obj) throws IOException;

    void hmSetMap(String key, Map<String, String> map) throws IOException;

    Map<String, String> hmGetMap(String key) throws IOException;

    public <T> List<T> hmGet(final String[] keys, final Class<?> clazz) throws IOException;

    /**
     * @param key
     * @return
     * @throws java.io.IOException
     */
    <T> T rPop(String key) throws IOException;

    /**
     * @param key
     * @param increment
     * @return
     * @throws java.io.IOException
     */
    Double zIncrby(String key, long increment, Object member) throws IOException;

    /**
     * @param key
     * @param map
     * @param len
     * @return
     * @throws java.io.IOException
     */
    long zAddWithMap(String key, Map<Object, Double> map, long len) throws IOException;

}
