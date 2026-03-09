package main;


class ListNode
{
    int val;
    ListNode next;

    ListNode()
    {
    }

    ListNode(int val)
    {
        this.val = val;
    }

    ListNode(int val, ListNode next)
    {
        this.val = val;
        this.next = next;
    }
}


public class Main
{

    public ListNode reverseList(ListNode head)
    {
        return __recurseDown(head);
    }

    public ListNode __recurseDown(ListNode node)
    {
        if(node==null || node.next==null)
            return node;

        ListNode nodeNext = __recurseDown(node.next);

        nodeNext.next = node;
        node.next = null;
        return node;
    }

}
