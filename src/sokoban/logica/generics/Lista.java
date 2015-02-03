
package sokoban.logica.generics;

import java.util.ListIterator;

public class Lista<E extends Comparable<E>> implements Iterable<E> {
    
    private Object elements[];
    private int size;
    
    public Lista() {
        this.elements = new Object[10];
        this.size = 0;
    }
    
    private void growInnerArray() {
        Object[] auxiliaryArray = new Object[this.elements.length * 2];
        System.arraycopy(this.elements, 0, auxiliaryArray, 0, this.elements.length);
        this.elements = auxiliaryArray;
    }
    
    public int size() {
        return this.size;
    }
    
    public E add(E newElement) {
        if (this.size == this.elements.length) {
            this.growInnerArray();
        }
        this.elements[size] = newElement;
        this.size++;
        return newElement;
    }
    
    public E add(E newElement, int i) {
        if (this.size == this.elements.length) {
            this.growInnerArray();
        }
        if (i == this.size) {
            this.elements[i] = newElement;
        }
        if (i >= 0 && i < this.size) {
            System.arraycopy(this.elements, i, this.elements, i + 1, size - i);
            this.elements[i] = newElement;
            this.size++;
        }
        return newElement;
    }
    
    public E get(int i) {
        if (i < 0 || i >= this.size) {
            throw new IndexOutOfBoundsException("Index " + i + " is out of bounds.");
        }else{
            return (E)this.elements[i];
        }
    }
    
    public E remove(int i) {
        if (i < 0 || i >= this.size) {
            throw new IndexOutOfBoundsException("Index " + i + " is out of bounds.");
        }
        E oldValue = this.get(i);
        int numbersMoved = this.size - i  - 1;
        if (numbersMoved > 0) {
            System.arraycopy(this.elements, i + 1, this.elements, i, numbersMoved);
        }
        this.elements[--size] = null;
        return oldValue;
    }
    
    public E removeLast() {
        Object removedElement = this.elements[size - 1];
        this.elements[this.size--] = null;
        return (E)removedElement;
    }
    
    public void removeAll() {
        this.elements = new Object[10];
        this.size = 0;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    private void swap(int indexA, int indexB) {
        if (this.size <= indexA || this.size <= indexB || indexA < 0 || indexB < 0){
            throw new IndexOutOfBoundsException("Index a or b has gone out of its bounds.");
        }else{
            Object aux = this.elements[indexA];
            this.elements[indexA] = this.elements[indexB];
            this.elements[indexB] = aux;
        }
    }
    
    public void bubbleSort(){
        for (int i = this.size-1; i >= 0 ; i--) {
            for (int j = 0; j < i; j++) {
                if (((E)this.elements[j]).compareTo((E)this.elements[i]) > 0) {
                    this.swap(j, i);
                }
            }
        }
    }
    
    @Override
    public ListIterator<E> iterator(){
        return new ListIterator<E>() {
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return !isEmpty() && currentIndex < size() && elements[currentIndex] != null;
            }

            @Override
            public E next() {
                return get(currentIndex++);
            }

            @Override
            public boolean hasPrevious() {
                return !isEmpty() && currentIndex > 0;
            }

            @Override
            public E previous() {
                return hasPrevious()? (E)elements[currentIndex--] :  null;
            }

            @Override
            public int nextIndex() {
                if (currentIndex == size - 1) {
                    return size;
                }else if (hasNext()){
                    return currentIndex + 1;
                }else{
                    return -1;
                }
            }

            @Override
            public int previousIndex() {
                return hasPrevious()? currentIndex - 1 : -1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void set(E e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };    
    }
}
