import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tester.*;

class Discussion10 {
    List<Integer> merge(List<Integer> lst1, List<Integer> lst2) {
        List<Integer> rs = new ArrayList<>();

        int index1 = 0; // The next element from lst1 that we haven't added yet
        int index2 = 0; // The next element from lst2 that we haven't added yet

        for (int i = 0; i < (lst1.size() + lst2.size()); i += 1) {
            System.out.println( i + ": Comparing lst1[" + index1 + "] with lst2[" + index2 + "].");
            if (index1 >= lst1.size()) {
                // We know that we've added all the elements from lst1,
                // and we only need to add elements from lst2
                System.out.println("All from lst1 were added. Adding " + index2 + " from lst2.");
                rs.add(lst2.get(index2));
                index2 += 1;
            }
            else if (index2 >= lst2.size()) {
                // same, but for lst2
                System.out.println("All from lst2 were added. Adding " + index1 + " from lst1.");
                rs.add(lst1.get(index1));
                index1 += 1;
            }
            // At this point, we know it's safe to call
            // .get on both lists
            else if (lst1.get(index1) < lst2.get(index2)) {
                // lst1.get(index1) should be added!
                System.out.println("lst1[" + index1 + "] < lst2[" + index2 + "], so adding from lst1.");
                rs.add(lst1.get(index1));
                index1 += 1;
            }
            else {
                System.out.println("lst2[" + index2 + "] <= lst1[" + index1 + "], so adding from lst2.");
                rs.add(lst2.get(index2));
                index2 += 1;
            }
        }

        return rs;
    }

    void testMerge(Tester t) {
        t.checkExpect(
            this.merge(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)),
            Arrays.asList(1, 2, 3, 4, 5, 6)
        );
        t.checkExpect(
            this.merge(Arrays.asList(), Arrays.asList(4, 5, 6)),
            Arrays.asList(4, 5, 6)
        );
        t.checkExpect(
            this.merge(Arrays.asList(1, 3, 5), Arrays.asList(2, 4, 6)),
            Arrays.asList(1, 2, 3, 4, 5, 6)
        );
    }
}