import java.util.*;
import java.io.*;
public class Graph_M{
    public class Vertex{
        HashMap<String,Integer> nbrs=new HashMap<>();
    }
    static HashMap<String,Vertex> vtces;
    public Graph_M(){
        vtces=new HashMap<>();
    }
    public int numVertex(){
        return this.vtces.size();
    }
    public boolean containsVertex(String vname){
        return this.vtces.containsKey(vname);
    }
    public void addVertex(String vname){
        Vertex vtx=new Vertex();
        vtces.put(vname,vtx);
    }
    public void removeVertex(String vname){
        Vertex vtx=vtces.get(vname);
        ArrayList<String> keys=new ArrayList<>(vtx.nbrs.keySet());
        for(String key:keys){
            Vertex nbrVtx=vtces.get(key);
            nbrVtx.nbrs.remove(vname);
        }
        vtces.remove(vname);
    }
    public int numEdges(){
        ArrayList<String> keys=new ArrayList<>(vtces.keySet());
        int count=0;
        for(String key:keys){
            Vertex vtx=vtces.get(key);
            count+=vtx.nbrs.size();
        }
        return count/2;
    }
    public boolean containsEdge(String vname1,String vname2){
        Vertex vtx1=vtces.get(vname1);
        Vertex vtx2=vtces.get(vname2);
        if(vtx1==null || vtx2==null || !vtx1.nbrs.containsKey(vname2)){
            return false;
        }
        return true;
    }
    public void addEdge(String vname1,String vname2,int value){
        Vertex vtx1=vtces.get(vname1);
        Vertex vtx2=vtces.get(vname2);
        if(vtx1==null || vtx2==null || vtx1.nbrs.containsKey(vname2)){
            return;
        }
        vtx1.nbrs.put(vname2,value);
        vtx2.nbrs.put(vname1,value);
    }
    public void removeEdge(String vname1,String vname2){
        Vertex vtx1=vtces.get(vname1);
        Vertex vtx2=vtces.get(vname2);
        if(vtx1==null || vtx2==null || !vtx1.nbrs.containsKey(vname2)){
            return;
        }
        vtx1.nbrs.remove(vname2);
        vtx2.nbrs.remove(vname1);
    }
    public void display_Map(){
        System.out.println("\t West Bengal Local Train App");
        System.out.println("\t -----------------------");
        System.out.println("-------------------------------\n");
        ArrayList<String> keys=new ArrayList<>(vtces.keySet());
        for(String key:keys) {
            String str=key+"=>\n";
            Vertex vtx=vtces.get(key);
            ArrayList<String> vtxnbrs=new ArrayList<>(vtx.nbrs.keySet());
            for (String nbr : vtxnbrs) {
                str = str + "\t" + nbr + "\t";
                if (nbr.length() < 16)
                    str = str + "\t";
                if (nbr.length() < 8)
                    str = str + "\t";
                str = str + vtx.nbrs.get(nbr) + "\n";
            }
            System.out.println(str);
        }
        System.out.println("\t--------------------");
        System.out.println("-------------------------------------\n");
    }
    public void display_Stations(){
        System.out.println("\n******************************\n");
        ArrayList<String> keys=new ArrayList<>(vtces.keySet());
        int i=1;
        for(String key:keys){
            System.out.println(i+"."+key);
            i++;
        }
        System.out.println("\n*********************************\n");
    }
    public boolean hasPath(String vname1,String vname2,HashMap<String,Boolean> processed){
        if(containsEdge(vname1,vname2)){
            return true;
        }
        processed.put(vname1,true);
        Vertex vtx=vtces.get(vname1);
        ArrayList<String> nbrs=new ArrayList<>(vtx.nbrs.keySet());
        //TRAVERSE THE NEIGHBOURS OF THE VERTEX
        for(String nbr:nbrs){
            if(!processed.containsKey(nbr)) {
                if (hasPath(nbr, vname2, processed))
                    return true;
            }
        }
        return false;
    }
    private class DijkstraPair implements Comparable<DijsktraPair>{
        String vname;
        String psf;
        int cost;
        DijkstraPair(){
            this.vname=vname;
            this.psf=psf;
            this.cost=cost;
        }
        @Override
                public int compareTo(DijkstraPair o){
                    return o.cost - this.cost;
                }
        }
    public int dijkstra(String src, String des, boolean nan)
    {
        int val = 0;
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, DijkstraPair> map = new HashMap<>();

        Heap<DijkstraPair> heap = new Heap<>();

        for (String key : vtces.keySet())
        {
            DijkstraPair np = new DijkstraPair();
            np.vname = key;
            //np.psf = "";
            np.cost = Integer.MAX_VALUE;

            if (key.equals(src))
            {
                np.cost = 0;
                np.psf = key;
            }

            heap.add(np);
            map.put(key, np);
        }

        //keep removing the pairs while heap is not empty
        while (!heap.isEmpty())
        {
            DijkstraPair rp = heap.remove();

            if(rp.vname.equals(des))
            {
                val = rp.cost;
                break;
            }

            map.remove(rp.vname);

            ans.add(rp.vname);

            Vertex v = vtces.get(rp.vname);
            for (String nbr : v.nbrs.keySet())
            {
                if (map.containsKey(nbr))
                {
                    int oc = map.get(nbr).cost;
                    Vertex k = vtces.get(rp.vname);
                    int nc;
                    if(nan)
                        nc = rp.cost + 120 + 40*k.nbrs.get(nbr);
                    else
                        nc = rp.cost + k.nbrs.get(nbr);

                    if (nc < oc)
                    {
                        DijkstraPair gp = map.get(nbr);
                        gp.psf = rp.psf + nbr;
                        gp.cost = nc;

                        heap.updatePriority(gp);
                    }
                }
            }
        }
        return val;
    }

    private class Pair
    {
        String vname;
        String psf;
        int min_dis;
        int min_time;
    }

    public String Get_Minimum_Distance(String src, String dst)
    {
        int min = Integer.MAX_VALUE;
        //int time = 0;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> queue= new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in stack
        queue.addFirst(sp);

        // while stack is not empty keep on doing the work
        while (!queue.isEmpty())
        {
            // remove a pair from stack
            Pair rp = queue.removeFirst();

            if (processed.containsKey(rp.vname))
            {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst))
            {
                int temp = rp.min_dis;
                if(temp<min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = vtces.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

            for(String nbr : nbrs)
            {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
                    //np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr);
                    queue.addFirst(np);
                }
            }
        }
        ans = ans + Integer.toString(min);
        return ans;
    }


    public String Get_Minimum_Time(String src, String dst)
    {
        int min = Integer.MAX_VALUE;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> queue = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in queue
        queue.addFirst(sp);

        // while queue is not empty keep on doing the work
        while (!queue.isEmpty()) {

            // remove a pair from queue
            Pair rp = queue.removeFirst();

            if (processed.containsKey(rp.vname))
            {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst))
            {
                int temp = rp.min_time;
                if(temp<min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = vtces.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

            for (String nbr : nbrs)
            {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    //np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
                    np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr);
                    queue.addFirst(np);
                }
            }
        }
        Double minutes = Math.ceil((double)min / 60);
        ans = ans + Double.toString(minutes);
        return ans;
    }

    public ArrayList<String> get_Interchanges(String str)
    {
        ArrayList<String> arr = new ArrayList<>();
        String res[] = str.split("  ");
        arr.add(res[0]);
        int count = 0;
        for(int i=1;i<res.length-1;i++)
        {
            int index = res[i].indexOf('~');
            String s = res[i].substring(index+1);

            if(s.length()==2)
            {
                String prev = res[i-1].substring(res[i-1].indexOf('~')+1);
                String next = res[i+1].substring(res[i+1].indexOf('~')+1);

                if(prev.equals(next))
                {
                    arr.add(res[i]);
                }
                else
                {
                    arr.add(res[i]+" ==> "+res[i+1]);
                    i++;
                    count++;
                }
            }
            else
            {
                arr.add(res[i]);
            }
        }
        arr.add(Integer.toString(count));
        arr.add(res[res.length-1]);
        return arr;
    }
    public static void Create_Metro_Map(Graph_M g)
    {
        g.addVertex("Jadavpur~JDP");
        g.addVertex("Barasat~BT");
        g.addVertex("Lake Gardens~LG");
        g.addVertex("Kankurgachi~KG");
        g.addVertex("Sodepur~SP");
        g.addVertex("Eden Gardens~EG");
        g.addVertex("Barrackpore~BP");
        g.addVertex("Dakshineshwar~DS");
        g.addVertex("Kalyani~KL");
        g.addVertex("Shantipur~SP");
        g.addVertex("Sealdah~SEA");
        g.addVertex("DumDum~DD");
        g.addVertex("GEDE~GE");
        g.addVertex("Howrah~HWH");
        g.addVertex("Krishnanagar~KN");
        g.addVertex("Barasat~BS");
        g.addVertex("Jaynagar~JN");
        g.addVertex("Bangaon~BN");
        g.addVertex("Ballygunge~BE");

        g.addEdge("Lake Gardens~LG", "Kankurgachi~KG", 3);
        g.addEdge("Lake Gardens~LG", "Jadavpur~JDP", 7);
        g.addEdge("Barrackpore~BP", "Lake Gardens~LG", 12);
        g.addEdge("Kankurgachi~KG", "Ballygunge~BE", 6);
        g.addEdge("Ballygunge~BE", "Sealdah~SEA", 6);
        g.addEdge("Sealdah~SEA", "DumDum~DD", 7);
        g.addEdge("DumDum~DD", "Howrah~HWH", 8);
        g.addEdge("Howrah~HWH", "Bangaon~BN", 11);
        g.addEdge("Bangaon~BN", "GEDE", 10);
        g.addEdge("Howrah~HWH", "Kalyani~KL", 10);
        g.addEdge("Howrah~HWH", "Krishnanagar~KN", 7);
        g.addEdge("Krishnanagar~KN", "Barasat", 13);
        g.addEdge("Howrah~HWH", "Barrackpore~BP", 5);
        g.addEdge("Barrackpore~BP", "Sealdah~SEA", 9);
        g.addEdge("Eden Gardens~EG", "Kankurgachi~KG", 5);
        g.addEdge("Barrackpore~BP", "DumDum~DD", 11);
        g.addEdge("Shantipur~SP", "DumDum~DD", 5);
        g.addEdge("Krishnanagar~KN", "Jaynagar~JN", 2);
        g.addEdge("Sodepur~SP", "Dakshineshwar~DS", 9);
    }
    public static String[] printCodeList() {
        System.out.println("List of station along with their codes:\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int i = 1, j = 0, m = 1;
        StringTokenizer stname;
        String temp = "";
        String codes[] = new String[keys.size()];
        char c;
        for (String key : keys) {
            stname = new StringTokenizer(key);
            codes[i - 1] = "";
            j = 0;
            while (stname.hasMoreTokens()) {
                temp = stname.nextToken();
                c = temp.charAt(0);
                while (c > 47 && c < 58) {
                    codes[i - 1] += c;
                    j++;
                    c = temp.charAt(j);
                }
                if ((c < 48 || c > 57) && c < 123)
                    codes[i - 1] += c;
            }
            if (codes[i - 1].length() < 2)
                codes[i - 1] += Character.toUpperCase(temp.charAt(1));

            System.out.print(i + ". " + key + "\t");
            return
        }
    }
    public static void main(String[] args) {
        Graph_M g=new Graph_M();
        Create_Metro_Map(g);
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        while(true){
            System.out.println("\n\t\t\t**WELCOME TO THE KOLKATA BUS ROUTE APP**");
            System.out.println("1.LIST ALL THE PLACES IN THE MAP");
            System.out.println("2.SHOW THE KOLKATA BUS ROUTE MAP");
            System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("5. GET SHORTEST PATH(DISTANCE WISE) TO REACH FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("6. GET SHORTEST PATH(TIME WISE) TO REACH FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("\n ENTER YOUR CHOICE FROM THE ABOVE LIST :");
            int choice=-1;
            try{
                choice=Integer.parseInt(br.readLine());
            }
            catch(Exception e){

            }
            System.out.print("\n ***************************************\n");
            if(choice==7)
                System.exit(0);
            switch(choice){
                case 1:
                    g.display_Stations();
                    break;
                case 2:
                    g.display_Map();
                    break;
                case 3:
                    ArrayList<String> keys=new ArrayList<>(vtces.keySet());
                    String codes[]=printCodeList();
                    System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
                    System.out.println("ENTER YOUR CHOICE:");
                    int ch = Integer.parseInt(br.readLine());
                    int j;

                    String st1 = "", st2 = "";
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    if (ch == 1)
                    {
                        st1 = keys.get(Integer.parseInt(br.readLine())-1);
                        st2 = keys.get(Integer.parseInt(br.readLine())-1);
                    }
                    else if (ch == 2)
                    {
                        String a,b;
                        a = (br.readLine()).toUpperCase();
                        for (j=0;j<keys.size();j++)
                            if (a.equals(codes[j]))
                                break;
                        st1 = keys.get(j);
                        b = (br.readLine()).toUpperCase();
                        for (j=0;j<keys.size();j++)
                            if (b.equals(codes[j]))
                                break;
                        st2 = keys.get(j);
                    }
                    else if (ch == 3)
                    {
                        st1 = br.readLine();
                        st2 = br.readLine();
                    }
                    else
                    {
                        System.out.println("Invalid choice");
                        System.exit(0);
                    }

                    HashMap<String, Boolean> processed = new HashMap<>();
                    if(!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                        System.out.println("SHORTEST DISTANCE FROM "+st1+" TO "+st2+" IS "+g.dijkstra(st1, st2, false)+"KM\n");
                    break;
                case 4:
                    System.out.print("Enter the Source Station:");
                    String sat1=br.readLine();
                    System.out.print("Enter the destination station:");
                    String sat2=br.readLine();
                    HashMap<String,Boolean> processed1=new HashMap<>();
                    System.out.println("Shortest time from ("+sat1+") to ("+sat2+") IS "+g.dijkstra(sat1,sat2,true)/60+"MINUTES\n\n");
                    break;
                case 5:
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    String s1 = br.readLine();
                    String s2 = br.readLine();

                    HashMap<String, Boolean> processed2 = new HashMap<>();
                    if(!g.containsVertex(s1) || !g.containsVertex(s2) || !g.hasPath(s1, s2, processed2))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                    {
                        ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Distance(s1, s2));
                        int len = str.size();
                        System.out.println("SOURCE STATION : " + s1);
                        System.out.println("SOURCE STATION : " + s2);
                        System.out.println("DISTANCE : " + str.get(len-1));
                        System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
                        //System.out.println(str);
                        System.out.println("~~~~~~~~~~~~~");
                        System.out.println("START  ==>  " + str.get(0));
                        for(int i=1; i<len-3; i++)
                        {
                            System.out.println(str.get(i));
                        }
                        System.out.print(str.get(len-3) + "   ==>    END");
                        System.out.println("\n~~~~~~~~~~~~~");
                    }
                    break;

                case 6:
                    System.out.print("ENTER THE SOURCE STATION: ");
                    String ss1 = br.readLine();
                    System.out.print("ENTER THE DESTINATION STATION: ");
                    String ss2 = br.readLine();

                    HashMap<String, Boolean> processed3 = new HashMap<>();
                    if(!g.containsVertex(ss1) || !g.containsVertex(ss2) || !g.hasPath(ss1, ss2, processed3))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                    {
                        ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Time(ss1, ss2));
                        int len = str.size();
                        System.out.println("SOURCE STATION : " + ss1);
                        System.out.println("DESTINATION STATION : " + ss2);
                        System.out.println("TIME : " + str.get(len-1)+" MINUTES");
                        System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
                        //System.out.println(str);
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        System.out.print("START  ==>  " + str.get(0) + " ==>  ");
                        for(int i=1; i<len-3; i++)
                        {
                            System.out.println(str.get(i));
                        }
                        System.out.print(str.get(len-3) + "   ==>    END");
                        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }
                    break;
                default:  //If switch expression does not match with any case,
                    //default statements are executed by the program.
                    //No break is needed in the default case
                    System.out.println("Please enter a valid option! ");
                    System.out.println("The options you can choose are from 1 to 6. ");

            }
        }
    }
}
