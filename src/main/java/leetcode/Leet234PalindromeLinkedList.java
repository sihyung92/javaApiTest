package leetcode;

import java.util.ArrayList;

/**
 * https://leetcode.com/problems/palindrome-linked-list/description/
 * https://leetcode.com/problems/palindrome-linked-list/submissions/865498582/
 */
public class Leet234PalindromeLinkedList {

  public boolean isPalindrome(ListNode head) {
    ArrayList<Integer> list = new ArrayList<>();
    while (head.next != null) {
      head = head.next;
    }
    list.add(head.val);

    for (int i = 0; i < list.size() / 2; i++) {
      int targetIndex = list.size() - 1 - i;
      if (!list.get(i).equals(list.get(targetIndex))) {
        return false;
      }
    }

    return true;
  }

  public static class ListNode {

    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
      this.val = val;
    }

    ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }
  }
}
