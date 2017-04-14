import com.mongodb.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.LogManager;

/**
 * Created by dogboy on 4/1/2017.
 */
public class main {
    public static boolean a;
    public static ArrayList<String> CollNames;
    public static String LastName ;

    public static void main(String[] args) {
        LogManager.getLogManager().reset();

        Scanner scan = new Scanner(System.in);

        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            System.out.println("Connect to database successfully");
            DB db = mongoClient.getDB("SchoolGradeSystem");
            String ans;
            System.out.println("Are you a new Teacher?");
            ans = scan.nextLine();
            if (ans.equalsIgnoreCase("yes")) {
                String FN, LN, UN, PW;
                BasicDBObject doc = new BasicDBObject();

                System.out.println("What is your First Name?");
                FN = scan.next();
                System.out.println("What is your Last Name?");
                LN = scan.next();
                System.out.println("What will be you user name?");
                UN = scan.next();
                System.out.println("What will be you password?");
                PW = scan.next();
                doc.append("First Name", FN)
                        .append("Last Name", LN)
                        .append("Username", UN)
                        .append("Password", PW);

                db.getCollection("Logins").insert(doc);

                System.out.println("how many classes do you have? ");
                int a = scan.nextInt();

                System.out.println("1)");
                String p1 = scan.next();
                create(db,p1,LN);
                for (int i = 1; i < a; i++) {
                    System.out.println((i + 1) + ")");
                    p1 = scan.next();
                    create(db,p1,LN);
                }


            } else {
                a = login(db);
                if (a == false) {
                    return;
                }
                while (a == true) {
                    ArrayList<String> collList = new ArrayList<String>();
                    for (String coll : new ArrayList<String>(db.getCollectionNames())) {
                        collList.add(coll);
                    }
                    CollNames = new ArrayList<String>(db.getCollectionNames());

                    String wantedCol;
                    int count = 1;
                    String cont = "yes";

                    do {
                        HashMap<String, DataManager> DBmanager = new HashMap<String, DataManager>();
                        for (String name : CollNames) {
                            DBmanager.put(name, new DataManager(name, db.getCollection(name)));
                        }
                        System.out.println("What collection do you want to access?");
                        int c = ListNames(CollNames);

                        if (count == 1) {
                            wantedCol = scan.nextLine();
                        } else {
                            wantedCol = scan.nextLine();
                            wantedCol = scan.nextLine();
                        }
                        while (!CollNames.contains(wantedCol)) {
                            if (wantedCol.equalsIgnoreCase("create") || wantedCol.equalsIgnoreCase("delete") || wantedCol.equalsIgnoreCase("prevent") || wantedCol.equalsIgnoreCase("remove")) {
                                break;
                            }
                            System.out.println("Error no such thing as: " + wantedCol);
                            wantedCol = scan.nextLine();
                        }

                        for (String name : CollNames) {
                            if (wantedCol.equalsIgnoreCase(name)) {
                                for (Map.Entry<String, DataManager> entry : DBmanager.entrySet()) {
                                    String key = entry.getKey();
                                    if (wantedCol.equalsIgnoreCase(key)) {
                                        DataManager CollClass = entry.getValue();
                                        CollClass.ListOptions();
                                        break;

                                    }
                                }
                            }
                        }
                        System.out.println("Continue with DataBase mod?");
                        cont = scan.next();

                        count++;
                    } while (cont.equalsIgnoreCase("yes"));
                    if (cont.equalsIgnoreCase("no")) {
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean login(DB db) {
        Scanner scan = new Scanner(System.in);

        String UN, PW;
        System.out.println("Enter in your Username:");
        UN = scan.next();
        System.out.println("Enter in your Password");
        PW = scan.next();

        DBCollection DBC = db.getCollection("Logins");

        ArrayList<DBObject> logs = new ArrayList<DBObject>();
        DBCursor cursor = DBC.find();

        while (cursor.hasNext()) {
            DBObject a = cursor.next();
            logs.add(a);
        }

        for (DBObject dbo : logs) {
            if (dbo.get("Username").equals(UN) && dbo.get("Password").equals(PW)) {
                LastName = dbo.get("Last Name").toString();
                System.out.println("Success");
                return true;
            }
        }
        System.out.println("Error: Username or Password is incorrect");
        return false;
    }

    public static int ListNames(ArrayList<String> CollNames) {
        int count = 0;
        for (String name : CollNames) {



            if (name.length() > LastName.length() && name.substring(0,LastName.length()).indexOf(LastName) >= 0) {
                count++;
                System.out.println(count + ") " + name);
            }
        }
        return count;
    }

    public static void create(DB db, String clazz,String name) {
        Scanner scan = new Scanner(System.in);
        String collname = name+"_"+clazz;
        DBCollection coll = db.createCollection(collname, new BasicDBObject());


        System.out.println("Collection created successfully");
    }

}

