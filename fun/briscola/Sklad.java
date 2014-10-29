import java.util.*;

public class Sklad {
    final static int INC = 16;
    Object[] obj = new Object[INC];
    int size = 0;
    
    public Sklad() {}
        
    public Object push(Object item) {
        obj[size] = item;
        size++;
        if (size == obj.length)
            setCapacity(size + INC);
        return item;
    }
    
    public synchronized Object pop() {
        Object pop = peek();
        size--;
        obj[size] = null;
        return pop;
    }
    
    public synchronized Object peek() {
        if (size == 0)
            throw new EmptyStackException();
        return obj[size-1];
    }
    
    public boolean empty() {
        return size == 0;
    }
    
    public synchronized int search(Object item) {
        for (int i=0; i < size; i++)
            if (obj[size-i-1] == item)
                return i;
        return -1;
    }
    
    public int size() {
        return size;
    }
    
    public synchronized Object[] toArray() {
        Object[] array = new Object[size];
        for (int i=0; i < size; i++) 
            array[i] = obj[i];
        return array;
    }
    
    public void izprazni() {
        for (int i=0; i < size; i++) 
            obj[i] = null;
        size = 0;
    }
    
    private void setCapacity(int c) {
        Object[] old = obj;
        obj = new Object[c];
        for (int i=0; i < Math.min(c, old.length); i++)
            obj[i] = old[i];
    }
}

class KupKart extends Sklad {
    public Karta popCard() {
        return (Karta)pop();
    }
    
    public Karta peekCard() {
        return (Karta)peek();
    }
    
    public Karta bottom() {
        if (size == 0)
            throw new EmptyStackException();
        return (Karta)obj[0];
    }
    
    public synchronized Karta[] toCardArray() {
        Karta[] array = new Karta[size];
        for (int i=0; i < size; i++) 
            array[i] = (Karta)obj[i];
        return array;
    }
}