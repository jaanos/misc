import java.util.*;

interface NumberedSetIterator<K,V> extends Iterator<K> {
    V value();
}

public class TreeNumberedSet<K,V> implements SortedSet<K> {
    protected Comparator<K> keyComparator;
    protected Counter<V> valueCounter;
    protected Entry header;
    private int size;
    private int count;
    
    protected TreeNumberedSet() {}
    
    //O(1)
    public TreeNumberedSet(Comparator<K> keyComparator, Counter<V> valueCounter) {
        this.keyComparator = keyComparator;
        this.valueCounter = valueCounter;
        
        header = new Entry();
        header.parent = header;
        header.previous = header;
        header.next = header;
        header.lesser = header;
        header.greater = header;
        header.red = false;
        header.green = false;
        size = 0;
        count = 0;
    }
    
    protected class Subset extends TreeNumberedSet<K, V> {
        K fromKey;
        K toKey;
        
        public Subset(K fromKey, K toKey) {
            this.fromKey = fromKey;
            this.toKey = toKey;
            this.header = TreeNumberedSet.this.header;
            this.keyComparator = TreeNumberedSet.this.keyComparator;
            this.valueCounter = TreeNumberedSet.this.valueCounter;
        }
        
        //O(log n)
        public NumberedSetIterator<K,V> numberedSetIterator(final boolean dir) {
            int diff;
            final Entry c = (dir ? firstEntry().previous : lastEntry().next);
            return new NumberedSetIterator<K,V>() {
                Entry cursor = c;
                boolean init = false;
    
                //O(1)
                public boolean hasNext() {
                    return (dir ? cursor.next != header && smallEnough(cursor.next.key) : cursor.previous != header && bigEnough(cursor.next.key));
                }
                
                //O(1)
                public K next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    cursor = (dir ? cursor.next : cursor.previous);
                    init = true;
                    return cursor.key;
                }
                
                //O(log n)
                public void remove() {
                    removeFromTree(cursor);
                }
                
                //O(1)
                public V value() {
                    if (!init) throw new IllegalStateException();
                    return cursor.value;
                }
            };
        }
        
        //O(n)
        public NumberedSetIterator<K,V> numberedSetIteratorByValue(final boolean dir) {
            Entry start = (dir ? header.greater : header.lesser);
            while (start != header) {
                if (bigEnough(start.key) && smallEnough(start.key)) break;
                start = (dir ? start.greater : start.lesser);
            }
            final Entry s = start;
            return new NumberedSetIterator<K,V>() {
                Entry cursor;
                Entry next = s;
                boolean init = false;
    
                //O(1)
                public boolean hasNext() {
                    return next != header;
                }
                
                //O(n)
                public K next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    cursor = next;
                    while (next != header) {
                        if (bigEnough(next.key) && smallEnough(next.key)) break;
                        next = (dir ? next.greater : next.lesser);
                    }
                    init = true;
                    return cursor.key;
                }
                
                //O(log n)
                public void remove() {
                    removeFromTree(cursor);
                }
                
                //O(1)
                public V value() {
                    if (!init) throw new IllegalStateException();
                    return cursor.value;
                }
            };
        }
        
        //O(n)
        public int size() {
            int size = 0;
            for (K key : this) size++;
            return size;
        }
        
        //O(n)
        public int count() {
            int count = 0;
            NumberedSetIterator<K,V> it = numberedSetIteratorByValue();
            while(it.hasNext()) {
                count += valueCounter.toInt(it.value());
            }
            return count;
        }
        
        public V value(K key) {
            if (!bigEnough(key) || !smallEnough(key)) return null;
            return super.value(key);
        }
        
        public boolean add(K key) {
            if (!bigEnough(key) || !smallEnough(key)) throw new IllegalArgumentException();
            return super.add(key);
        }
        
        public boolean add(K key, V value) {
            if (!bigEnough(key) || !smallEnough(key)) throw new IllegalArgumentException();
            return super.add(key, value);
        }
        
        public boolean remove(Object o) {
            try {
                K key = (K)o;
                if (!bigEnough(key) || !smallEnough(key)) return false;
                return super.remove(key);
            } catch (ClassCastException e) {
                return false;
            }
        }
        
        public boolean remove(Object o, V value) {
            try {
                K key = (K)o;
                if (!bigEnough(key) || !smallEnough(key)) return false;
                return super.remove(key, value);
            } catch (ClassCastException e) {
                return false;
            }
        }
        
        public boolean removeAll(Object o) {
            try {
                K key = (K)o;
                if (!bigEnough(key) || !smallEnough(key)) return false;
                return super.removeAll(key);
            } catch (ClassCastException e) {
                return false;
            }
        }
        
        //O(1)
        public SortedSet<K> subSet(K fromKey, K toKey) {
            if (fromKey == null || toKey == null) throw new NullPointerException();
            return new Subset(bigEnough(fromKey) ? fromKey : this.fromKey, smallEnough(toKey) ? toKey : this.toKey);
        }
        
        //O(1)
        public SortedSet<K> headSet(K toKey) {
            if (toKey == null) throw new NullPointerException();
            return new Subset(fromKey, smallEnough(toKey) ? toKey : this.toKey);
        }
        
        //O(1)
        public SortedSet<K> tailSet(K fromKey) {
            if (fromKey == null) throw new NullPointerException();
            return new Subset(bigEnough(fromKey) ? fromKey : this.fromKey, toKey);
        }
        
        //O(log n)
        public K first() {
            Entry e = firstEntry();
            if (e == header) throw new NoSuchElementException();
            return e.key;
        }
        
        //O(log n)
        public K last() {
            Entry e = lastEntry();
            if (e == header) throw new NoSuchElementException();
            return e.key;
        }
        
        //O(log n)
        private Entry firstEntry() {
            Entry e;
            int diff;
            if (fromKey == null) {
                e = header.next;
            } else {
                e = header.parent;
                while (e != null) {
                    diff = keyComparator.compare(fromKey, e.key);
                    if (diff == 0) break;
                    if (diff < 0) {
                        if (e.left == null) break;
                        e = e.left;
                    } else {
                        if (e.right == null) {
                            e = e.next;
                            break;
                        }
                        e = e.right;
                    }
                }
            }
            return e;
        }
        
        //O(log n)
        private Entry lastEntry() {
            Entry e;
            if (toKey == null) {
                e = header.previous;
            } else {
                e = header.parent;
                while (e != null) {
                    if (keyComparator.compare(toKey, e.key) < 0) {
                        if (e.left == null) {
                            e = e.previous;
                            break;
                        }
                        e = e.left;
                    } else {
                        if (e.right == null) break;
                        e = e.right;
                    }
                }
            }
            return e;
        }
        
        //O(1)
        private boolean bigEnough(K key) {
            if (fromKey == null) return true;
            return keyComparator.compare(key, fromKey) >= 0;
        }
        
        //O(1)
        private boolean smallEnough(K key) {
            if (toKey == null) return true;
            return keyComparator.compare(key, toKey) < 0;
        }
    }
    
    protected class Entry implements Map.Entry<K, V> {
        K key;
        V value;
        boolean red;
        boolean green;
        
        Entry parent;
        Entry left;
        Entry right;
        Entry father;
        Entry son;
        Entry daughter;
        Entry previous;
        Entry next;
        Entry lesser;
        Entry greater;
        
        //O(1)
        public Entry(K key, V value, boolean red, boolean green, Entry parent, Entry left, Entry right, Entry father, Entry son, Entry daughter, Entry previous, Entry next, Entry lesser, Entry greater) {
            this.key = key;
            this.value = value;
            this.red = red;
            this.green = green;
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.father = father;
            this.son = son;
            this.daughter = daughter;
            this.previous = previous;
            this.next = next;
            this.lesser = lesser;
            this.greater = greater;
        }
        
        public Entry(K key, Entry parent, Entry father) {
            this(key, valueCounter.unit(), true, true, parent, null, null, father, null, null, null, null, null, null);
        }
        
        public Entry(K key, boolean red, boolean green) {
            this(key, valueCounter.unit(), red, green, header, null, null, header, null, null, header, header, header, header);
        }
                
        public Entry(K key, Entry parent, Entry previous, Entry next) {
            this(key, valueCounter.unit(), true, true, parent, null, null, null, null, null, previous, next, null, null);
        }
        
        public Entry(K key, V value, Entry parent, Entry previous, Entry next) {
            this(key, value, true, true, parent, null, null, null, null, null, previous, next, null, null);
        }
        
        public Entry() {}
        
        //O(1)
        public K getKey() {
            return key;
        }
        
        //O(1)
        public V getValue() {
            return value;
        }
        
        //O(1)
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
        
        //O(1)
        public void inc() {
            value = valueCounter.next(value);
        }
        
        //O(1)
        public void dec() {
            value = valueCounter.previous(value);
        }
                
        //O(1)
        public int hashCode() {
            if (key == null) return 0;
            return key.hashCode();
        }
        
        //O(n) - rekurzija!
        public String toString() {
            return (red ? '[' : '(') + (key == null ? "" : key.toString()) + ", " + (value == null ? "" : value.toString()) + ", {" + (left == null ? "()" : left.toString()) + ", " + (right == null ? "()" : right.toString()) + '}' + (red ? ']' : ')');
        }
        
        //O(n) - rekurzija!
        public String toStringByValue() {
            return (green ? '[' : '(') + (key == null ? "" : key.toString()) + ", " + (value == null ? "" : value.toString()) + ", {" + (son == null ? "()" : son.toStringByValue()) + ", " + (daughter == null ? "()" : daughter.toStringByValue()) + '}' + (green ? ']' : ')');
        }
    }
    
    //O(1)
    public Iterator<K> iterator() {
        return numberedSetIterator(true);
    }
    
    //O(1)
    public Iterator<K> iterator(boolean dir) {
        return numberedSetIterator(dir);
    }
    
    //O(1)
    public NumberedSetIterator<K,V> numberedSetIterator() {
        return numberedSetIterator(true);
    }
    
    //O(1)
    public NumberedSetIterator<K,V> numberedSetIterator(final boolean dir) {
        return new NumberedSetIterator<K,V>() {
            Entry cursor = header;
            boolean init = false;

            //O(1)
            public boolean hasNext() {
                return (dir ? cursor.next != header : cursor.previous != header);
            }
            
            //O(1)
            public K next() {
                if (!hasNext()) throw new NoSuchElementException();
                cursor = (dir ? cursor.next : cursor.previous);
                init = true;
                return cursor.key;
            }
            
            //O(log n)
            public void remove() {
                removeFromTree(cursor);
            }
            
            //O(1)
            public V value() {
                if (!init) throw new IllegalStateException();
                return cursor.value;
            }
        };
    }
    
    //enako kot prej
    public NumberedSetIterator<K,V> numberedSetIteratorByValue() {
        return numberedSetIteratorByValue(true);
    }
    
    public NumberedSetIterator<K,V> numberedSetIteratorByValue(final boolean dir) {
        return new NumberedSetIterator<K,V>() {
            Entry cursor = header;
            boolean init = false;

            public boolean hasNext() {
                return (dir ? cursor.greater != header : cursor.lesser != header);
            }
            
            public K next() {
                if (!hasNext()) throw new NoSuchElementException();
                cursor = (dir ? cursor.greater : cursor.lesser);
                init = true;
                return cursor.key;
            }
            
            public void remove() {
                removeFromTree(cursor);
            }
            
            public V value() {
                if (!init) throw new IllegalStateException();
                return cursor.value;
            }
        };
    }
    
    //O(1)
    public int size() {
        return size;
    }
    
    //O(1)
    public int count() {
        return count;
    }
    
    //O(1) (O(n) za podmnožice)
    public boolean isEmpty() {
        return size() == 0;
    }
    
    //O(log n)
    public boolean contains(Object o) {
        try {
            K key = (K)o;
            return value(key) != null;
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    //O(log n)
    public V value(K key) {
        if (size == 0) return null;
        Entry e = header.parent;
        while (e != null) {
            if (keyComparator.compare(key, e.key) == 0) return e.value;
            if (keyComparator.compare(key, e.key) < 0) {
                e = e.left;
            } else {
                e = e.right;
            }
        }
        return null;
    }
    
    //O(log n)
    public boolean add(K key) {
        if (key == null) throw new NullPointerException();
        Entry e = header.parent;
        if (e == header) {
            header.father = header.parent = header.greater = header.lesser = header.previous = header.next = new Entry(key, false, false);
            size = 1;
            count = 1;
            return true;
        }
        while (e != null) {
            if (keyComparator.compare(key, e.key) == 0) {
                e.inc();
                adjustValPos(e);    //O(log n)
                count++;
                return true;
            }
            if (keyComparator.compare(key, e.key) < 0) {
                if (e.left == null) {
                    e.previous = e.previous.next = e.left = new Entry(key, e, e.previous, e);
                    adjustValPos(e.left);   //O(log n)
                    adjustTree(e.left);     //O(log n)
                    size++;
                    count++;
                    return true;
                }
                e = e.left;
            } else {
                if (e.right == null) {
                    e.next = e.next.previous = e.right = new Entry(key, e, e, e.next);
                    adjustValPos(e.right);  //O(log n)
                    adjustTree(e.right);    //O(log n)
                    size++;
                    count++;
                    return true;
                }
                e = e.right;
            }
        }
        return false;
    }
    
    //O(log n)
    public boolean add(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        Entry e = header.parent;
        if (e == header) {
            header.father = header.parent = header.greater = header.lesser = header.previous = header.next = new Entry(key, false, false);
            header.parent.value = value;
            size = 1;
            count = valueCounter.toInt(value);
            return true;
        }
        while (e != null) {
            if (keyComparator.compare(key, e.key) == 0) {
                int diff = valueCounter.toInt(value);
                if (diff == 0) return false;
                e.value = valueCounter.add(e.value, value);
                adjustValPos(e);
                count += diff;
                return true;
            }
            if (keyComparator.compare(key, e.key) < 0) {
                if (e.left == null) {
                    e.previous = e.previous.next = e.left = new Entry(key, value, e, e.previous, e);
                    adjustValPos(e.left);
                    adjustTree(e.left);
                    size++;
                    count += valueCounter.toInt(value);
                    return true;
                }
                e = e.left;
            } else {
                if (e.right == null) {
                    e.next = e.next.previous = e.right = new Entry(key, value, e, e, e.next);
                    adjustValPos(e.right);
                    adjustTree(e.right);
                    size++;
                    count += valueCounter.toInt(value);
                    return true;
                }
                e = e.right;
            }
        }
        return false;
    }
    
    //O(log n)
    public boolean put(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        if (valueCounter.compare(value, valueCounter.zero()) == 0) {
            return removeAll(key);
        }
        Entry e = header.parent;
        if (e == header) {
            header.father = header.parent = header.greater = header.lesser = header.previous = header.next = new Entry(key, false, false);
            header.parent.value = value;
            size = 1;
            count = valueCounter.toInt(value);
            return true;
        }
        while (e != null) {
            if (keyComparator.compare(key, e.key) == 0) {
                int diff = valueCounter.compare(value, e.value);
                if (diff == 0) return false;
                e.value = value;
                adjustValPos(e);
                count += diff;
                return true;
            }
            if (keyComparator.compare(key, e.key) < 0) {
                if (e.left == null) {
                    e.previous = e.previous.next = e.left = new Entry(key, value, e, e.previous, e);
                    adjustValPos(e.left);
                    adjustTree(e.left);
                    size++;
                    count += valueCounter.toInt(value);
                    return true;
                }
                e = e.left;
            } else {
                if (e.right == null) {
                    e.next = e.next.previous = e.right = new Entry(key, value, e, e, e.next);
                    adjustValPos(e.right);
                    adjustTree(e.right);
                    size++;
                    count += valueCounter.toInt(value);
                    return true;
                }
                e = e.right;
            }
        }
        return false;
    }
    
    //O(log n)
    public boolean remove(Object o) {
        if (o == null) throw new NullPointerException();
        K key;
        try {
            key = (K)o;
        } catch (ClassCastException e) {
            return false;
        }
        Entry e = header.parent;
        if (e == header) return false;
        while (e != null) {             //O(log n)
            if (keyComparator.compare(key, e.key) == 0) {
                e.dec();
                count--;
                if (valueCounter.compare(e.value, valueCounter.zero()) == 0) {
                    removeFromTree(e);  //O(log n)
                } else {
                    adjustValPos(e);    //O(log n)
                }
                return true;
            }
            if (keyComparator.compare(key, e.key) < 0) {
                e = e.left;
            } else {
                e = e.right;
            }
        }
        return false;
    }
    
    //O(log n)
    public boolean remove(Object o, V value) {
        if (o == null) throw new NullPointerException();
        K key;
        try {
            key = (K)o;
        } catch (ClassCastException e) {
            return false;
        }
        Entry e = header.parent;
        if (e == header) return false;
        while (e != null) {
            if (keyComparator.compare(key, e.key) == 0) {
                V old = e.value;
                e.value = valueCounter.subtract(e.value, value);
                count -= valueCounter.toInt(valueCounter.subtract(old, e.value));
                if (valueCounter.compare(e.value, valueCounter.zero()) == 0) {
                    removeFromTree(e);
                } else {
                    adjustValPos(e);
                }
                return true;
            }
            if (keyComparator.compare(key, e.key) < 0) {
                e = e.left;
            } else {
                e = e.right;
            }
        }
        return false;
    }
    
    //O(log n)
    public boolean removeAll(Object o) {
        if (o == null) throw new NullPointerException();
        K key;
        try {
            key = (K)o;
        } catch (ClassCastException e) {
            return false;
        }
        Entry e = header.parent;
        if (e == header) return false;
        while (e != null) {
            if (keyComparator.compare(key, e.key) == 0) {
                count -= valueCounter.toInt(e.value);
                removeFromTree(e);
                return true;
            }
            if (keyComparator.compare(key, e.key) < 0) {
                e = e.left;
            } else {
                e = e.right;
            }
        }
        return false;
    }
    
    //O(1)
    public void clear() {
        header.parent = header;
        header.father = header;
        header.previous = header;
        header.next = header;
        header.lesser = header;
        header.greater = header;
        size = 0;
        count = 0;
    }
    
    //O(m log n)
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }
    
    //O(m log n)
    public boolean containsAll(TreeNumberedSet<K,V> tns) {
        NumberedSetIterator<K,V> it = tns.numberedSetIterator();
        V value;
        while (it.hasNext()) {
            value = value(it.next());   //O(log n)
            if (value == null || valueCounter.compare(value, it.value()) < 0) {
                return false;
            }
        }
        return true;
    }
    
    //O(m log n)
    public boolean addAll(Collection<? extends K> c) {
        if (c.size() == 0) return false;
        for (K o : c) {     //m
            add(o);         //log n
        }
        return true;
    }
    
    //O(m log n)
    public boolean addAll(TreeNumberedSet<K,V> tns) {
        if (tns.size() == 0) return false;
        NumberedSetIterator<K,V> it = tns.numberedSetIterator();
        while (it.hasNext()) {          //m
            add(it.next(), it.value()); //log n
        }
        return true;
    }
    
    //O(m n) - odvisno od implementacije vhodnega argumenta
    //če je c implementiran kot drevo, velja O(n log m)
    public boolean retainAll(Collection<?> c) {
        boolean done = false;
        for (K o : this) {              //n
            if (!c.contains(o)) {       //m ~ log m
                done |= removeAll(o);   //log n
            }
        }
        return done;
    }
    
    //O(n log m)
    public boolean retainAll(TreeNumberedSet<K,V> tns) {
        boolean done = false;
        NumberedSetIterator<K,V> it = numberedSetIterator();
        K key;
        V value;
        while (it.hasNext()) {          //n
            key = it.next();
            value = tns.value(key);     //log m
            if (value == null) {
                done |= removeAll(key); //log n
            } else if (valueCounter.compare(it.value(), value) > 0) {
                done |= put(key, value);//log n
            }
        }
        return done;
    }
    
    //O(n log m)
    public boolean intersection(TreeNumberedSet<K,V> tns) {
        boolean done = false;
        NumberedSetIterator<K,V> it = numberedSetIterator();
        K key;
        V value;
        while (it.hasNext()) {          //n
            key = it.next();
            value = tns.value(key);     //log m
            if (value == null) {
                done |= removeAll(key); //log n
            } else {
                done |= add(key, value);//log n
            }
        }
        return done;
    }
    
    //O(m log n)
    public boolean removeAll(Collection<?> c) {
        boolean done = false;
        for (Object o : c) {    //m
            done |= remove(o);  //log n
        }
        return done;
    }
    
    //O(m log n)
    public boolean removeAll(TreeNumberedSet<K,V> tns) {
        boolean done = false;
        NumberedSetIterator<K,V> it = tns.numberedSetIterator();
        while (it.hasNext()) {                      //m
            done |= remove(it.next(), it.value());  //log n
        }
        return done;
    }
    
    //O(m log n)
    public boolean removeAllAll(Collection<?> c) {
        boolean done = false;
        for (Object o : c) {        //m
            done |= removeAll(o);   //log n
        }
        return done;
    }
    
    //O(n)
    public Object[] toArray() {
        Object[] arr = new Object[count];
        NumberedSetIterator<K,V> it = numberedSetIterator();
        K obj = null;
        int n = 0;
        for (int i = 0; i < count; i++) {
            if (n == 0) {
                obj = it.next();
                n = valueCounter.toInt(it.value());
            }
            arr[i] = obj;
            n--;
        }
        return arr;
    }
    
    //O(n)
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
        }
        NumberedSetIterator<K,V> it = numberedSetIterator();
        K obj = null;
        int n = 0;
        for (int i = 0; it.hasNext(); i++) {
            if (n == 0) {
                obj = it.next();
                n = valueCounter.toInt(it.value());
            }
            try {
                a[i] = (T)obj;
            } catch (ClassCastException e) {
                throw new ArrayStoreException();
            }
            n--;
        }
        return a;
    }
    
    //O(1)
    public Comparator<? super K> comparator() {
        return keyComparator;
    }
    
    //O(1)
    public Counter<? super V> counter() {
        return valueCounter;
    }
    
    //O(1)
    public SortedSet<K> subSet(K fromKey, K toKey) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        if (keyComparator.compare(fromKey, toKey) > 0) throw new IllegalArgumentException();
        return new Subset(fromKey, toKey);
    }
    
    //O(1)
    public SortedSet<K> headSet(K toKey) {
        if (toKey == null) throw new NullPointerException();
        return new Subset(null, toKey);
    }
    
    //O(1)
    public SortedSet<K> tailSet(K fromKey) {
        if (fromKey == null) throw new NullPointerException();
        return new Subset(fromKey, null);
    }
    
    //O(1)
    public K first() {
        if (header.next == header) throw new NoSuchElementException();
        return header.next.key;
    }
    
    //O(1)
    public K last() {
        if (header.previous == header) throw new NoSuchElementException();
        return header.previous.key;
    }
    
    //O(n log n)
    public TreeNumberedSet<K,V> copy() {
        NumberedSetIterator<K,V> it = numberedSetIteratorByValue(false);
        TreeNumberedSet<K,V> tns = new TreeNumberedSet(keyComparator, valueCounter);
        K key;
        while (it.hasNext()) {              //n
            tns.add(it.next(), it.value()); //log n
        }
        return tns;
    }
    
    //O(n log n)
    public Object clone() {
        return copy();
    }
    
    //O(n)
    public String toString() {
        return toString(true);
    }
    
    //O(n)
    public String toString(boolean dir){
        StringBuilder sb = new StringBuilder("{");
        NumberedSetIterator<K,V> it = numberedSetIterator(dir);
        while (it.hasNext()) {
            sb.append('(' + it.next().toString() + ", " + it.value().toString() + ')');
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    //O(n)
    public String toStringByValue(boolean dir) {
        StringBuilder sb = new StringBuilder("{");
        NumberedSetIterator<K,V> it = numberedSetIteratorByValue(dir);
        while (it.hasNext()) {
            sb.append('(' + it.next().toString() + ", " + it.value().toString() + ')');
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    //O(n) - rekurzija!
    public String toStringByTree() {
        return header.parent == header ? "()" : header.parent.toString();
    }
    
    //O(n) - rekurzija!
    public String toStringByValTree() {
        return header.father == header ? "()" : header.father.toStringByValue();
    }
    
    //O(log n)
    private void adjustValPos(Entry f) {
        int diff;
        if (f.lesser != null) {
            diff = 0;
            if (f.lesser != header) {
                diff = valueCounter.compare(f.value, f.lesser.value);
                if (diff == 0) {
                    diff = keyComparator.compare(f.key, f.lesser.key);
                }
            }
            if (diff >= 0) {
                if (f.greater != header) {
                    diff = valueCounter.compare(f.greater.value, f.value);
                    if (diff == 0) {
                        diff = keyComparator.compare(f.greater.key, f.key);
                    }
                }
                if (diff >= 0) return;
            }
            removeValFromTree(f);   //O(log n)
            f.father = f.son = f.daughter = null;
            f.green = true;
        }
        
        Entry e = header.father;
        while (e != null) {         //O(log n)
            diff = valueCounter.compare(f.value, e.value);
            if (diff == 0) {
                diff = keyComparator.compare(f.key, e.key);
            }
            if (diff < 0) {
                if (e.son == null) {
                    f.father = f.greater = e;
                    f.lesser = e.lesser;
                    e.lesser = e.lesser.greater = e.son = f;
                    break;
                }
                e = e.son;
            } else {
                if (e.daughter == null) {
                    f.father = f.lesser = e;
                    f.greater = e.greater;
                    e.greater = e.greater.lesser = e.daughter = f;
                    break;
                }
                e = e.daughter;
            }
        }
        e = f;
        
        do {                        //O(log n)
            if (e.father == header) {
                e.green = false;
                return;
            }
            if (!e.father.green) return;
            if (e.father.father.son == e.father) {
                if (isGreen(e.father.father.daughter)) {
                    e = e.father.father;
                    recolorVal(e);
                    continue;
                }
                if (e.father.daughter == e) {
                    rotateLeftVal(e.father);
                    e = e.son;
                }
                rotateRightVal(e.father.father);
                e = e.father;
                e.green = false;
                e.daughter.green = true;
                return;
            } else {
                if (isGreen(e.father.father.son)) {
                    e = e.father.father;
                    recolorVal(e);
                    continue;
                }
                if (e.father.son == e) {
                    rotateRightVal(e.father);
                    e = e.daughter;
                }
                rotateLeftVal(e.father.father);
                e = e.father;
                e.green = false;
                e.son.green = true;
                return;
            }
        } while (e != header);
    }
    
    //O(1)
    private void rotateLeft(Entry e) {
        if (e.parent == header) {
            e.parent.parent = e.right;
        } else if (e.parent.left == e) {
            e.parent.left = e.right;
        } else {
            e.parent.right = e.right;
        }
        e.right.parent = e.parent;
        e.parent = e.right;
        e.right = e.parent.left;
        e.parent.left = e;
        if (e.right != null) {
            e.right.parent = e;
        }
    }
    
    //O(1)
    private void rotateRight(Entry e) {
        if (e.parent == header) {
            e.parent.parent = e.left;
        } else if (e.parent.left == e) {
            e.parent.left = e.left;
        } else {
            e.parent.right = e.left;
        }
        e.left.parent = e.parent;
        e.parent = e.left;
        e.left = e.parent.right;
        e.parent.right = e;
        if (e.left != null) {
            e.left.parent = e;
        }
    }
    
    //O(1)
    private void recolor(Entry e) {
        e.red = true;
        e.left.red = false;
        e.right.red = false;
    }
    
    //O(1)
    private void rotateLeftVal(Entry e) {
        if (e.father == header) {
            e.father.father = e.daughter;
        } else if (e.father.son == e) {
            e.father.son = e.daughter;
        } else {
            e.father.daughter = e.daughter;
        }
        e.daughter.father = e.father;
        e.father = e.daughter;
        e.daughter = e.father.son;
        e.father.son = e;
        if (e.daughter != null) {
            e.daughter.father = e;
        }
    }
    
    //O(1)
    private void rotateRightVal(Entry e) {
        if (e.father == header) {
            e.father.father = e.son;
        } else if (e.father.son == e) {
            e.father.son = e.son;
        } else {
            e.father.daughter = e.son;
        }
        e.son.father = e.father;
        e.father = e.son;
        e.son = e.father.daughter;
        e.father.daughter = e;
        if (e.son != null) {
            e.son.father = e;
        }
    }
    
    //O(1)
    private void recolorVal(Entry e) {
        e.green = true;
        e.son.green = false;
        e.daughter.green = false;
    }
    
    //O(log n)
    private void adjustTree(Entry e) {
        do {
            if (e.parent == header) {
                e.red = false;
                return;
            }
            if (!e.parent.red) return;
            if (e.parent.parent.left == e.parent) {
                if (isRed(e.parent.parent.right)) {
                    e = e.parent.parent;
                    recolor(e);
                    continue;
                }
                if (e.parent.right == e) {
                    rotateLeft(e.parent);
                    e = e.left;
                }
                rotateRight(e.parent.parent);
                e = e.parent;
                e.red = false;
                e.right.red = true;
                return;
            } else {
                if (isRed(e.parent.parent.left)) {
                    e = e.parent.parent;
                    recolor(e);
                    continue;
                }
                if (e.parent.left == e) {
                    rotateRight(e.parent);
                    e = e.right;
                }
                rotateLeft(e.parent.parent);
                e = e.parent;
                e.red = false;
                e.left.red = true;
                return;
            }
        } while (e != header);
    }
    
    //O(log n)
    protected void removeFromTree(Entry e) {
        e.previous.next = e.next;
        e.next.previous = e.previous;
        removeValFromTree(e);   //O(log n)
        size--;
        
        if (size == 0) {
            header.parent = header;
            return;
        }
        if (size == 1) {
            header.parent = (e.next == header ? e.previous : e.next);
            header.parent.red = false;
            header.parent.left = null;
            header.parent.right = null;
            header.parent.parent = header;
            return;
        }
        
        Entry f, g, h;
        boolean red, left = false;
        if (e.left == null || e.right == null) {
            g = e;
        } else {
            //O(1) - pri klasičnem RC drevesu O(log n) !
            g = e.next.red ? e.next : e.previous;
        }
        red = g.red;
        if (g.left == null) {
            f = g.right;
        } else {
            f = g.left;
        }
        h = g.parent;
        if (f != null) {
            f.parent = h;
        }
        if (h == header) {
            header.parent = f;
        } else if (h.left == g) {
            h.left = f;
            left = true;
        } else {
            h.right = f;
        }
        if (g != e) {
            g.parent = e.parent;
            if (e.parent == header) {
                header.parent = g;
            } else if (e.parent.left == e) {
                e.parent.left = g;
            } else {
                e.parent.right = g;
            }
            g.left = e.left;
            if (e.left != null) {
                e.left.parent = g;
            }
            g.right = e.right;
            if (e.right != null) {
                e.right.parent = g;
            }
            g.red = e.red;
            if (h == e) {
                h = g;
            }
        }
        if (red) return;
        //do tod O(1)

        //V približno 1/4 primerov
        //O(log n)
        do {
            if (left) {
                if (h.right.red) {
                    rotateLeft(h);
                    h.red = true;
                    h.parent.red = false;
                }
                if (!isRed(h.right.left) && !isRed(h.right.right)) {
                    f = h;
                    h = f.parent;
                    left = (h.left == f);
                    f.right.red = true;
                    continue;
                }
                if (!isRed(h.right.right)) {
                    rotateRight(h.right);
                    h.right.red = false;
                    h.right.right.red = true;
                }
                rotateLeft(h);
                h.parent.red = h.red;
                h.parent.right.red = false;
            } else {
                if (h.left.red) {
                    rotateRight(h);
                    h.red = true;
                    h.parent.red = false;
                }
                if (!isRed(h.left.left) && !isRed(h.left.right)) {
                    f = h;
                    h = f.parent;
                    left = (h.left == f);
                    f.left.red = true;
                    continue;
                }
                if (!isRed(h.left.left)) {
                    rotateLeft(h.left);
                    h.left.red = false;
                    h.left.left.red = true;
                }
                rotateRight(h);
                h.parent.red = h.red;
                h.parent.left.red = false;
            }
            h.red = false;
            f = header.parent;
        } while (f != header.parent && !f.red);
        f.red = false;
    }
    
    //O(log n)
    protected void removeValFromTree(Entry e) {
        e.lesser.greater = e.greater;
        e.greater.lesser = e.lesser;
        
        if (size <= 1) {
            header.father = header;
            return;
        }
        if (size == 2) {
            header.father = (e.greater == header ? e.lesser : e.greater);
            header.father.green = false;
            header.father.son = null;
            header.father.daughter = null;
            header.father.father = header;
            return;
        }
        
        Entry f, g, h;
        boolean green, son = false;
        if (e.son == null || e.daughter == null) {
            g = e;
        } else {
            //O(1) - pri klasičnem RC drevesu O(log n) !
            g = e.greater.green ? e.greater : e.lesser;
        }
        green = g.green;
        if (g.son == null) {
            f = g.daughter;
        } else {
            f = g.son;
        }
        h = g.father;
        if (f != null) {
            f.father = h;
        }
        if (h == header) {
            header.father = f;
        } else if (h.son == g) {
            h.son = f;
            son = true;
        } else {
            h.daughter = f;
        }
        if (g != e) {
            g.father = e.father;
            if (e.father == header) {
                header.father = g;
            } else if (e.father.son == e) {
                e.father.son = g;
            } else {
                e.father.daughter = g;
            }
            g.son = e.son;
            if (e.son != null) {
                e.son.father = g;
            }
            g.daughter = e.daughter;
            if (e.daughter != null) {
                e.daughter.father = g;
            }
            g.green = e.green;
            if (h == e) {
                h = g;
            }
        }
        if (green) return;
        //do tod O(1)

        //V približno 1/4 primerov
        //O(log n)
        do {
            if (son) {
                if (h.daughter.green) {
                    rotateLeftVal(h);
                    h.green = true;
                    h.father.green = false;
                }
                if (!isGreen(h.daughter.son) && !isGreen(h.daughter.daughter)) {
                    f = h;
                    h = f.father;
                    son = (h.son == f);
                    f.daughter.green = true;
                    continue;
                }
                if (!isGreen(h.daughter.daughter)) {
                    rotateRightVal(h.daughter);
                    h.daughter.green = false;
                    h.daughter.daughter.green = true;
                }
                rotateLeftVal(h);
                h.father.green = h.green;
                h.father.daughter.green = false;
            } else {
                if (h.son.green) {
                    rotateRightVal(h);
                    h.green = true;
                    h.father.green = false;
                }
                if (!isGreen(h.son.son) && !isGreen(h.son.daughter)) {
                    f = h;
                    h = f.father;
                    son = (h.son == f);
                    f.son.green = true;
                    continue;
                }
                if (!isGreen(h.son.son)) {
                    rotateLeftVal(h.son);
                    h.son.green = false;
                    h.son.son.green = true;
                }
                rotateRightVal(h);
                h.father.green = h.green;
                h.father.son.green = false;
            }
            h.green = false;
            f = header.father;
        } while (f != header.father && !f.green);
        f.green = false;
    }
    
    //O(1)
    private boolean isRed(Entry e) {
        if (e == null) return false;
        return e.red;
    }
    
    //O(1)
    private boolean isGreen(Entry e) {
        if (e == null) return false;
        return e.green;
    }
}