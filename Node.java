// Single linked list questions, functions based on questions from CTCI.

import java.util.*;

class Node {
    Integer value; // value stored in current node
    Node    next;  // node linked to next node

    // Constructs a node with a value
    public Node(int d) {
        value = d;
        next = null;
    }

    /***************************************************************************
     * 2.1 Write code to remove duplicates from an unsorted linked list.
     *     FOLLOW UP
     *     How would you solve this problem if a temporary buffer is not
     *     allowed?
     **************************************************************************/

    // My initial thought: Use an array to keep track whether a certain
    // value has been seen (assuming the value is int based). If so, remove the
    // node from the linked list. Otherwise just keep it.
    // This solution uses O(n) time and space.
    public static void removeDuplicates1(Node n) {
        // So we create a buffer to keep track if a value has been seen
        ArrayList<Integer> buffer = new ArrayList<Integer>();
        
        Node prev = null;

        // Go through every value in the linked list
        while (n != null) {
            if (buffer.indexOf(n.value) != -1) {
                prev.next = n.next;
            }
            else {
                buffer.add(n.value);
                prev = n;
            }

            n = n.next;
        }
    }

    // From the book, solution uses a Hashtable. Don't see a difference
    // between this and my initial answer except different types of buffers are
    // being used.
    // O(n) time and space.
    public static void removeDuplicates2(Node n) {
        Hashtable<Integer, Boolean> table = new Hashtable<Integer, Boolean>();

        Node prev = null;

        while (n != null) {
            if (table.containsKey(n.value)) {
                prev.next = n.next;
            }
            else {
                table.put(n.value, true);
                prev = n;
            }

            n = n.next;
        }
    }

    // If we can't use any temporary buffers, we can just do a nested iteration.
    // Note that while this takes O(1) space, it does take O(n^2) time due to
    // double loops.
    public static void removeDuplicates3(Node n) {
        Node curNode = n;

        while (curNode != null) {
            Node nextNode = curNode;

            while (nextNode.next != null) {
                if (curNode.value == nextNode.next.value) {
                    nextNode.next = nextNode.next.next;
                }
                else {
                    nextNode = nextNode.next;
                }
            }

            curNode = curNode.next;
        }
    }

    /***************************************************************************
    /* 2.2 Implement an algorithm to find the kth to last element of a singly
     *     linked list.
     **************************************************************************/

    // First way: We can use two pointers that are k apart. This means when the
    // pointer in front reaches the end, the second pointer will be at the kth
    // to last element.
    // This will take O(n) time and O(1) space.
    public static void kthToLast1(Node n, int k) {
        Node n1 = n;
        Node n2 = n;

        // Make 1st pointer k away from 2nd pointer
        for (int i=0; i<k-1; i++) {
            if (n1 == null) {
                System.out.println("ERROR: k is larger than the linked list.");
                return;
            }

            n1 = n1.next;
        }

        // Iterate 1st pointer to end so 2nd pointer is kth to last element
        while (n1.next != null) {
            n1 = n1.next;
            n2 = n2.next;
        }

        System.out.println("The " + k + "th to last element is " + n2.value);
    }

    // Alternate way: We can do this recurisvely with a counter. Once we reach
    // the end of the linked list, we return 0 and add to it everytime something
    // gets returned. Once counter = k, we have reached the element we want.
    // This is not as good of a method as iterating with two pointers.
    // It will take O(n) time but O(n) space since there are n function calls.
    public static int kthToLast2(Node n, int k) {
        //Base case
        if (n == null) {
            return 0;
        }

        int i = kthToLast2(n.next, k) + 1;

        if (i == k) {
            System.out.println("The " + k + "th to last element is " + n.value);
        }

        return i;
    }

    /***************************************************************************
     * 2.3 Implement an algorithm to delete a node in the middle of a
     *     singly linked list, given only access to that node.
     *     EXAMPLE
     *     Input: the node c from the linked list a->b->c->d->e
     *     Result: nothing is returned, but the new linked list looks like
     *             a->b->d->e
     **************************************************************************/

    // Replace the value and re-reference the node
    // Returns true or false if a node has been removed; node can't be removed
    // if there are no nodes or it is the last node, since the last node can't
    // become something else instead.
    public static boolean deleteNode(Node n) {
        if (n == null) {
            return false;
        }
        if (n.next == null) {
            n.value = -1;  // dummy node; assuming linked list should only be 
            n.next = null; // storing positive integers

            return true;
        }

        n.value = n.next.value;
        n.next = n.next.next;

        return true;
    }

    /***************************************************************************
     * 2.4 Write code to partition a linked list around a value x, such that all
     *     nodes less than x come before all nodes greater than or equal to x.
     **************************************************************************/

    // We can just loop through the given linked list and keep track of two
    // mini linked lists, one whose nodes are all less than value x and one
    // whose nodes are all greater than value x. Afterwards we piece the two
    // linked lists together.
    // This takes O(n) time (one iteration through list) and O(n) space (2 lists
    // being tracked).
    public static Node partition1(Node n, int x) {
        Node beforeStart = null;
        Node beforeEnd = null;
        Node afterStart = null;
        Node afterEnd = null;

        while (n != null) {
            Node toAdd = new Node(n.value);
            
            if (n.value < x) {
                // Start the before list; otherwise add to it
                if (beforeStart == null) {
                    beforeStart = toAdd;
                    beforeEnd = beforeStart;
                }
                else {
                    beforeEnd.next = toAdd;
                    beforeEnd = toAdd;
                }
            }
            else {
                // Star the after list; otherwise add to it
                if (afterStart == null) {
                    afterStart = toAdd;
                    afterEnd = afterStart;
                }
                else {
                    afterEnd.next = toAdd;
                    afterEnd = toAdd;
                }
            }

            n = n.next;
        }

        // Nothing partitioned before x
        if (beforeStart == null) {
            return afterStart;
        }

        beforeEnd.next = afterStart;

        return beforeStart;
    }

    // Alternate answer: We can make partition1 cleaner by sacrificing a bit of
    // efficiency (although that efficiency is insignificant, since it still
    // runs O(n) time.) Instead of adding to the end of the two mini-lists,
    // we can add to the beginning, so we don't have to keep track of so many
    // variables.
    public static Node partition2(Node n, int x) {
        Node beforeStart = null;
        Node afterStart = null;

        while (n != null) {
            Node toAdd = new Node(n.value);

            if(n.value < x) {
                toAdd.next = beforeStart;
                beforeStart = toAdd;
            }
            else {
                toAdd.next = afterStart;
                afterStart = toAdd;
            }

            n = n.next;
        }

        if (beforeStart == null) {
            return afterStart;
        }

        Node toReturn = beforeStart;

        while (beforeStart.next != null) {
            beforeStart = beforeStart.next;
        }

        beforeStart.next = afterStart;

        return toReturn;
    }

    /***************************************************************************
     * 2.5 You have two numbers represented by a linked list, where each
     *     node contains a single digit. The digits are stored in reverse
     *     order, such that that 1's digit is the head of the list. Write
     *     a function that adds the two numbers and returns the sum as a
     *     linked list.
     *     EXAMPLE
     *     Input: (7 -> 1 -> 6) + (5 -> 9 -> 2). That is, 617 + 295.
     *     Output: 2 -> 1 -> 9. That is, 912.
     *     FOLLOW UP
     *     Suppose the digits are stored in forward order. Repeat the
     *     above problem.
     *     EXAMPLE
     *     Input: (6 -> 1 -> 7) + (2 -> 9 -> 6). That is, 617 + 295.
     *     Output: 2 -> 1 -> 9. That is, 912.
     **************************************************************************/

    // For backwards order:
    // Assuming both numbers have the same number of digits (same size linked
    // list), we can iterate through them together and add the digits stored
    // within the node. We can mod the result by 10, giving us the digit we
    // want in the result, and we just carry a 1 to the next pair of digits (or
    // single digits). In the event that they are the not the same number of
    // digits, we can use a dummy "0" digit to add.
    // Node this is messy and repetitive code unless we use recursion. But this
    // is more efficient than recusion method since we do not have to keep
    // calling functions.
    public static Node addNumbers1(Node a, Node b) {
        Node result = null;
        Node curDigit = null;
        int resultDigit = 0;

        // Add digits side by side
        while (a != null && b != null) {
            resultDigit = resultDigit + a.value + b.value;

            Node toAdd = new Node(resultDigit % 10);

            if (result == null) {
                result = toAdd;
                curDigit = result;
            }
            else {
                curDigit.next = toAdd;
                curDigit = curDigit.next;
            }

            resultDigit = resultDigit / 10;

            a = a.next;
            b = b.next;
        }

        // Check that both numbers had equal digits
        if (a == null && b == null) {
            // Check for carryover digit
            if (resultDigit != 0) {
                Node toAdd = new Node(resultDigit);
                curDigit.next = toAdd;
            }

            return result;
        }

        Node remainingDigits = null;

        // Determine which number has more digits than the other
        if (a != null) {
            remainingDigits = a;
        }
        else {
            remainingDigits = b;
        }

        // Iterate through remaining digits
        while (remainingDigits != null) {
            resultDigit = resultDigit + remainingDigits.value;

            curDigit.next = new Node(resultDigit % 10);
            curDigit = curDigit.next;
            resultDigit = resultDigit / 10;
            remainingDigits = remainingDigits.next;
        }

        if (resultDigit != 0) {
                Node toAdd = new Node(resultDigit);
                curDigit.next = toAdd;
        }

        return result;
    }


    /***************************************************************************
     * 2.6 Given a circular linked list, implement an algorithm which
     *     returns the node at the beginning of the loop.
     *     DEFINITION
     *     Circular linked list: A (corrupt) linked list in which a node's
     *     next pointer points to an earlier node, as to make a loop in
     *     the linked list.
     *     EXAMPLE
     *     Input: A -> B -> C -> D -> E -> C [the same C as earlier]
     *     Output: C
     **************************************************************************/





    /***************************************************************************
     * 2.7 Implement a function to check if a linked list is a palindrome.
     **************************************************************************/ 

    //--------------------------------------------------------------------------

    // Prints out the linked list for testing purposes
    public static void printValues(Node n) {
        while (n != null) {
            System.out.println(n.value);
            n = n.next;
        }
    }

    

    // Test bed
    public static void main(String[] args) {
        Node node1 = new Node(20);
        Node node2 = new Node(20);
        Node node3 = new Node(10);
        Node node4 = new Node(20);
        Node node5 = new Node(50);
        Node node6 = new Node(1);
        Node node7 = new Node(2);
        Node node8 = new Node(10);
        Node node9 = new Node(0);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;
        node7.next = node8;
        node8.next = node9;

        System.out.println("Before removing duplicates: ");
        printValues(node1);

        removeDuplicates1(node1);

        System.out.println("After removing duplicates:");
        printValues(node1);

        kthToLast1(node1, 5);
        kthToLast2(node1, 10);
        kthToLast2(node1, 6);

        if(deleteNode(node1)) {
            System.out.println("First node deleted. New list: ");
            printValues(node1);
        }

        System.out.println("Partitioned nodes at 5: ");
        
        printValues(partition2(node1, 5));

        Node num1 = new Node(7);
        num1.next = new Node(1);
        num1.next.next = new Node(6);
        num1.next.next.next = new Node(8);

        Node num2 = new Node(5);
        num2.next = new Node(9);
        num2.next.next = new Node(2);
        num2.next.next.next = new Node(9);

        System.out.println("Adding 8617 and 295 should give 8912:");

        printValues(addNumbers1(num1, num2));

    }
}