package com.xat.dataStructure;


import java.util.LinkedList;
import java.util.Stack;

/**
 * @author xuantao
 */
public class LinkList {


    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.add("甲");
        linkedList.add("乙");
        linkedList.add("丙");
        while (!linkedList.isEmpty()) {
            System.out.println(linkedList.pollLast());
        }

    }


}
