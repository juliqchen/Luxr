package com.example.luxr;

import java.util.LinkedList;

/**
 * Created by juliachen on 7/24/17.
 */

class PixelQueue {

    LinkedList<HashObject> list;

    PixelQueue() {
        list = new LinkedList<>();
    }

    public void enqueue(HashObject item) {
        list.addLast(item);
    }

    public HashObject dequeue() {
        return list.poll();
    }

    public boolean hasItems() {
        return !list.isEmpty();
    }

    public int size() {
        return list.size();
    }


}
