import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class APriori {
    private ArrayList<String> database;
    private HashMap<Integer, HashMap<String,Integer>> itemsets;

    public APriori() {
        database = new ArrayList<>();
        itemsets = new HashMap<>();
    }

    public void loadData(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try {
            Scanner s = new Scanner(f);
            s.nextLine();
            while (s.hasNextLine()) {
                String[] line = s.nextLine().split("\t");
                database.add(line[1].replace(" ", ""));
            }
        }
        catch (Exception e) {
            System.out.println("File "+f+" not found");
        }
    }

    public void findItemsets(int n) {
        itemsets.put(n,new HashMap<>());
        HashMap<String, Integer> n_itemsets = itemsets.get(n);
        if (n==1) {
            for (String basket: database) {
                String[] items = basket.split(",");
                for (String item: items) {
                    if (n_itemsets.containsKey(item)) n_itemsets.put(item, n_itemsets.get(item) + 1);
                    else n_itemsets.put(item,1);
                }
            }
        }
        else {
            HashMap<String, Integer> n_1_itemsets = itemsets.get(n-1);
            for (String n_1_itemset_1: n_1_itemsets.keySet()) {
                for (String n_1_itemset_2: n_1_itemsets.keySet()) {
                    if (!n_1_itemset_1.equals(n_1_itemset_2)) {
                        if (n==2 || sizeOfIntersection(n_1_itemset_1, n_1_itemset_2) == n-2) {
                            String union = union(n_1_itemset_1, n_1_itemset_2);
                            int count = 0;
                            for (String basket: database) {
                                if (basketContainsItemset(basket, union)) count++;
                            }
                            n_itemsets.put(union,count);
                        }
                    }
                }
            }
        }
    }

    public void dropUnderTreshold(int n, int t) {
        HashMap<String, Integer> n_itemsets = itemsets.get(n);
        ArrayList<String> toRemove = new ArrayList<>();
        for (String item: n_itemsets.keySet()) {
            if (n_itemsets.get(item) < t) toRemove.add(item);
        }
        for (String item: toRemove) n_itemsets.remove(item);
    }

    public ArrayList<String> findAssociations(int n) {
        ArrayList<String> associations = new ArrayList<>();
        HashMap<String, Integer> n_itemsets = itemsets.get(n);
        for (String item: n_itemsets.keySet()) {
            String[] elements = item.split(",");
            for (String element: elements) {
                String complement = item.replaceAll(element+",","");
                double elementSupport = itemsets.get(1).get(element);
//                System.out.println(elementSupport);
                double associationSupport = n_itemsets.get(item);
//                System.out.println(associationSupport);
                double confidence = associationSupport/elementSupport;
                String association = element + "->" + complement + String.valueOf(confidence);
                System.out.println(association);
                associations.add(association);
            }
        }
        return associations;
    }

    public int sizeOfIntersection(String s1, String s2) {
        int count = 0;
        HashMap<String, Integer> items = new HashMap<>();
        for (String item: s1.split(",")) {
            if (items.keySet().contains(item)) items.put(item, items.get(item) + 1);
            else items.put(item, 1);
        }
        for (String item: s2.split(",")) {
            if (items.keySet().contains(item)) items.put(item, items.get(item) + 1);
            else items.put(item, 1);
        }
        for (String item: items.keySet()) {
            if (items.get(item) > 1) count++;
        }
        return count;
    }

    public String union(String s1, String s2) {
        String union = "";
        HashSet<String> items = new HashSet<>();
        for (String item: s1.split(",")) items.add(item);
        for (String item: s2.split(",")) items.add(item);
        for (String item: items) union += item + ",";
        return union;
    }

    public boolean basketContainsItemset(String basket, String itemset) {
        boolean areIn = true;
        String[] itemssetItems = itemset.split(",");
        String[] basketItems = basket.split(",");
        for (String item: itemssetItems) {
            if (!arrayContains(basketItems, item)) areIn = false;
        }
        return areIn;
    }

    public boolean arrayContains(String[] array, String s) {
        boolean isIn = false;
        for (String element: array) {
            if (element.equals(s)) isIn = true;
        }
        return isIn;
    }

    public static void main(String[] args) throws FileNotFoundException {
        APriori apriori = new APriori();
        apriori.loadData("D2.txt");
        System.out.println(apriori.database);
        apriori.findItemsets(1);
        System.out.println(apriori.itemsets);
        apriori.findItemsets(2);
        System.out.println(apriori.itemsets);
        apriori.findItemsets(3);
        System.out.println(apriori.itemsets);
        apriori.findItemsets(4);
        System.out.println(apriori.itemsets);

        apriori.findAssociations(2);
        apriori.findAssociations(3);
        apriori.findAssociations(4);

        System.out.println("------------------------");

        APriori aprioriCulled = new APriori();
        aprioriCulled.loadData("D2.txt");
        System.out.println(aprioriCulled.database);
        aprioriCulled.findItemsets(1);
        aprioriCulled.dropUnderTreshold(1,2);
        System.out.println(aprioriCulled.itemsets);
        aprioriCulled.findItemsets(2);
        aprioriCulled.dropUnderTreshold(2,2);
        System.out.println(aprioriCulled.itemsets);
        aprioriCulled.findItemsets(3);
        aprioriCulled.dropUnderTreshold(3,2);
        System.out.println(aprioriCulled.itemsets);
        aprioriCulled.findItemsets(4);
        aprioriCulled.dropUnderTreshold(4,2);
        System.out.println(aprioriCulled.itemsets);
    }

}
