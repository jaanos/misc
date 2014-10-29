import java.util.*;

interface Counter<E> extends Comparator<E> {
    E next(E n);
    E previous(E n);
    E add(E n1, E n2);
    E subtract(E n1, E n2);
    E zero();
    E unit();
    int toInt(E n);
}

class StringComparator implements Comparator<String> {
    boolean caseSensitive;
    
    public StringComparator(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    public int compare(String s1, String s2) {
        if (!caseSensitive) return s1.compareToIgnoreCase(s2);
        return s1.compareTo(s2);
    }
}

class CharComparator implements Comparator<Character> {
    boolean caseSensitive;
    
    public CharComparator(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    public int compare(Character c1, Character c2) {
        if (!caseSensitive) {
            return Character.valueOf(Character.toLowerCase(c1)).compareTo(Character.toLowerCase(c2));
        }
        return c1.compareTo(c2);
    }
}

class IntCounter implements Counter<Integer> {
    public int compare(Integer n1, Integer n2) {
        return (n1 < 0 ? 0 : n1) - (n2 < 0 ? 0 : n2);
    }
    
    public Integer next(Integer n) {
        return (n < 0 ? 1 : n + 1);
    }
    
    public Integer previous(Integer n) {
        return (n <= 0 ? 0 : n - 1);
    }
    
    public Integer add(Integer n1, Integer n2) {
        int n = (n1 < 0 ? 0 : n1) + (n2 < 0 ? 0 : n2);
        return (n < 0 ? 0 : n);
    }
    
    public Integer subtract(Integer n1, Integer n2) {
        int n = (n1 < 0 ? 0 : n1) - (n2 < 0 ? 0 : n2);
        return (n < 0 ? 0 : n);
    }
    
    public Integer zero() {
        return 0;
    }
    
    public Integer unit() {
        return 1;
    }
    
    public int toInt(Integer n) {
        return (n < 0 ? 0 : n);
    }
}