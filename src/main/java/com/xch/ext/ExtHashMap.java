package com.xch.ext;

/**
 * @author xiech
 * @create 2020-04-10 11:08
 */
public class ExtHashMap<K,V> implements ExtMap<K,V> {
    //定义table 存放hashmap数字元素
    Node<K,V>[] tables=null;
    //实际用到table的容量
    int size;
    //负载因子  默认0.75F 负载因子越小，hash冲突越小（根据链表的长度判断）
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * table初始大小
     */
    static final int DEFAULT_INITIAL_CAPACITY=16;


    public V put(K key, V value) {
        //判断table 数组大小是否为空 初始化
        if(tables==null){
            tables=new Node[DEFAULT_INITIAL_CAPACITY];
        }
        //判断是否需要扩容  实际存储容量=负载因子*初始容量
        if(size>=DEFAULT_LOAD_FACTOR*DEFAULT_INITIAL_CAPACITY){
            resize();
        }
        //计算hash值指定下标位置
        int index =getIndex(key,tables.length);
        //获取对应节点
        Node<K,V> node=tables[index];
        if(node==null){
            //没有发生hash 冲突
            node=new Node<K, V>(key,value,null);
            size++;
        }else{
            //产生hash冲突
            Node<K,V> newNode=node;
            //循环链接，循环到最后一个元素，检查hash是否相同
            while (newNode!=null){
                //此key已存在在链表中，覆盖value值
                if(newNode.getKey().equals(key)||newNode.getKey()==key){
                    return newNode.setValue(value);
                }else{
                    //不存在，将其添加到链表的最前面，基于后进先出原则
                    if(newNode.next==null){
                        //新node的next是原来的next
                        node=new Node<K, V>(key,value,node);
                        size++;
                    }
                }
                //将下个节点复制给newNode，以判断是否继续循环
                newNode=newNode.next;
            }
        }
        tables[index]=node;
        return null;
    }

    /**
     * 获取hash取模值
     * @param key
     * @param length
     * @return
     */
    private int getIndex(K key, int length) {
        int index =key==null?0:key.hashCode()%length;
        return index;
    }

    /**
     * 扩容
     */
    private void resize() {
        //定义新的数组元素
        Node<K,V>[] newTables=new Node[tables.length<<1];
        //将老的tables 的key hash取模值 循环重新
        for(int i=0;i<tables.length;i++){
            Node<K,V> oldNode=tables[i];
            //循环链表  一直循环到链表最后一个元素
            while (oldNode!=null){
                tables[i]=null;
                //重新计算key的hash取模
                K oldKey=oldNode.key;
                int index=getIndex(oldKey,newTables.length);
                //获取当前节点的下一个节点
                Node<K,V> oldNext=oldNode.next;
                //后进先出原则，相同hash取模后加入的排在链表的前面，所以这里将后加入的节点的next设置为当前节点
                oldNode.next=newTables[index];
                //把原来的node赋值给新的node
                newTables[index]=oldNode;
                //获取下一节点，判断是否继续循环
                oldNode=oldNext;
            }
        }
        //将处理完毕的新tabels赋值给全局tabel
        tables=newTables;
        //设置为不可达
        newTables=null;
    }

    public V get(K key) {
        Node<K,V> node=getNode(tables[getIndex(key,tables.length)],key);
        return node==null?null:node.value;
    }

    public Node<K,V> getNode(Node<K,V> node,K key){
        while (node!=null){
            if(node.getKey()==key||(node.getKey()!=null&&node.getKey().equals(key))){
                return node;
            }
            node=node.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    class Node<K, V> implements Entry<K,V>{
        private K key;
        private V value;

        // 下一个节点Node
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            super();
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V v) {
            //返回原来的值
            V oldValue=this.value;
            this.value=v;
            return oldValue;
        }
    }

    public static void main(String[] args) {
        ExtMap<String,String> map=new ExtHashMap<String, String>();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        map.put("4","4");
        map.put("5","5");
        map.put("6","6");
        map.put("7","7");
        map.put("8","8");
        map.put("9","9");
        map.put("10","10");
        map.put("11","11");
        map.put("12","12");
        map.put("1","62");
        map.put(null,"空");
        System.out.println(map.get(null));
    }
}
