

import java.util.*;
import java.util.PriorityQueue;
public class countCache {


    //creates hashmap
    public static Map<String, String> map = new HashMap<String, String>();

    //creates PriorityQ
    public static PriorityQueue<Node> pq = new PriorityQueue<Node>(5, new Comparator() {
        public int compare(Object a, Object b) {
            if (!(a instanceof Node) || !(b instanceof Node))
                return 0;
            //nodes are compared based on their timeStamp
            Node n1 = (Node) a;
            Node n2 = (Node) b;
            return n1.getTimestamp().compareTo(n2.getTimestamp());
        }

    });

    private void insert(Node e) {
        //inserts node into cache
        System.out.println("Received Element information for this file: " + e.getValue()+" into cache");
        pq.offer(e);
    }

    private String remove() {
        //removes the leas used node in the cache, not this can only remove the leas used node
        Node leastUsed = pq.poll();
        if (leastUsed != null) {
            System.out.println("Removing least used element:" + leastUsed.getValue());
            System.out.println("This element was last used:" + leastUsed.getTimestamp());
            return leastUsed.getValue();
        }
        return "";
    }
    private void update(String mostRecentEleKey) {
        //updates timeStamp if the node being put is already in cache
        Iterator<Node> pqIterator = pq.iterator();
        while (pqIterator.hasNext()) {
            Node e = pqIterator.next();
            //removes older version of node
            if (e.getValue().equals(mostRecentEleKey)) {
                pqIterator.remove();
                break;
            }
        }
        Node mostRecent = new Node();
        mostRecent.setTimestamp(new Date());
        mostRecent.setValue(mostRecentEleKey);
        //inserts newer version
        insert(mostRecent);
    }

    public String get(String key) {
        String value = map.get(key);
        return value;
    }

    public void put(String key, String value) {
        System.out.println("Before put opertaion, map size:" + map.size());
        if (map.containsKey(key)) {
            //if elements is already in the cache
            System.out.println("Cache hit on key:" + key + ", nothing to insert!");
            update(key);
            //updates the old value with the new
            map.put(key,value);
        } else {
            if (map.size() >= 5) {
                //makes sure the cache stays within map size
                String leastUsedKey = remove();
                map.remove(leastUsedKey);
            }
            //adds new element

            System.out.println("Element not present in Cache: " + key);
            Node e = new Node();
            e.setValue(key);
            e.setTimestamp(new Date());
            insert(e);
            map.put(key, value);
        }
        //prints good debuggin info
        System.out.println("After put operation, following stats are generated:");
        System.out.println("Least used element:" + pq.peek().getValue() + ", last used at:" + pq.peek().getTimestamp());
        System.out.println("map size:" + map.size());
    }



    class Node {

        private String data_content;
        private Date time_stamp;


        public Date getTimestamp() {
            return time_stamp;
        }

        public void setTimestamp(Date timestamp) {
            this.time_stamp = timestamp;
        }

        public String getValue() {
            return data_content;
        }

        public void setValue(String value) {
            this.data_content = value;
        }




    }
}
