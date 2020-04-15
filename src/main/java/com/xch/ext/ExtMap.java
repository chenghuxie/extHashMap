package com.xch.ext;

import java.util.Map;

/**
 * @author xiech
 * @create 2020-04-10 11:03
 */
public interface ExtMap<K,V> {
    /**
     * 向集合中插入元素
     * @param k
     * @param v
     * @return
     */
  public V put(K k,V v);

    /**
     * 根据key查询元素
     * @param k
     * @return
     */
  public V get(K k);

    /**
     * 获取集合长度
     * @return
     */
  public int size();

    /**
     * node节点
     * @param <K>
     * @param <V>
     */
  interface  Entry<K,V>{
      K getKey();
      V getValue();
      V setValue(V v);
  }
}
