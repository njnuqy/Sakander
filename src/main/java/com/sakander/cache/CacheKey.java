package com.sakander.cache;

import com.sakander.utils.ArrayUtil;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CacheKey implements Cloneable, Serializable {
    // 用于版本控制的唯一标识符，当对象被序列化时，确保序列化和反序列化过程中版本的一致性
    private static final long serialVersionUID = 1146682552656046210L;

    public static final CacheKey NULL_CACHE_KEY = new CacheKey(){
        private static final long serialVersionUID = 1L;
    };
    // 用作生成hash码时的乘数，会和对象的某些属性结合使用，以减少哈希冲突并提高哈希表的性能
    private static final int DEFAULT_MULTIPLIER = 37;
    // 用作hash码的初始值，在构建CacheKey的哈希表示时，这个初始值会和后续的属性值通过特定的算法结合，以生成唯一的哈希码
    private static final int DEFAULT_HASHCODE = 17;

    private final int multiplier;
    private int hashcode;
    // 用于存储校验和，检测数据在传输和存储过程中是否发生变化。用于确保缓存键的唯一性和完整性
    private long checksum;
    // 记录添加到updateList中的元素数量，优化哈希码的计算过程，或者在需要的时候提供有关CacheKey状态的细腻系
    private int count;
    // 存储构成CacheKey的元素列表，可能是查询参数，SQL语句片段，或者其他能够唯一标识缓存条目的信息。
    private List<Object> updateList;
    public CacheKey(){
        this.hashcode = DEFAULT_HASHCODE;
        this.multiplier = DEFAULT_MULTIPLIER;
        this.count = 0;
        this.updateList = new ArrayList<>();
    }
    public CacheKey(Object[] objects){
        this();
        updateAll(objects);
    }

    public int getUpdateCount(){
        return updateList.size();
    }

    public void update(Object object){
        int baseHashCode = object == null ? 1 : ArrayUtil.hashcode(object);

        count++;
        checksum += baseHashCode;
        baseHashCode = count;

        hashcode = multiplier * hashcode + baseHashCode;

        updateList.add(object);
    }

    public void updateAll(Object[] objects){
        for (Object o : objects) {
            update(o);
        }
    }

    @Override
    public boolean equals(Object object) {
        if(this == object){
            return true;
        }

        if(!(object instanceof CacheKey)){
            return false;
        }
        final CacheKey cacheKey = (CacheKey) object;

        if((hashcode != cacheKey.hashcode) || (checksum != cacheKey.checksum) || (count != cacheKey.count)){
            return false;
        }

        for(int i = 0 ; i < updateList.size() ; i++){
            Object thisObject = updateList.get(i);
            Object thatObject = cacheKey.updateList.get(i);
            if(!ArrayUtil.equals(thisObject,thatObject)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        StringJoiner returnValue = new StringJoiner(":");
        returnValue.add(String.valueOf(hashcode));
        returnValue.add(String.valueOf(checksum));
        updateList.stream().map(ArrayUtil::toString).forEach(returnValue::add);
        return returnValue.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        CacheKey clonedCacheKey = (CacheKey) super.clone();
        clonedCacheKey.updateList = new ArrayList<>(updateList);
        return clonedCacheKey;
    }
}
