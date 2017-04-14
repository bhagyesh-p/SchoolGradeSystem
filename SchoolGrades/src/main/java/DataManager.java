import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.*;

/**
 * Created by dogboy on 3/25/2017.
 */
public class DataManager {
    private String name;
    private DBCollection DBC;
    private Scanner scan = new Scanner(System.in);

    public DataManager(String name, DBCollection DBC) {
        this.name = name;
        this.DBC = DBC;
    }


    public void ListOptions() {
        String ans;
        do {
            System.out.println("What would you like to do?(Enter corresponding number) ");
            System.out.println("1) List students in the class");
            System.out.println("2) Add a student to the class");
            System.out.println("3) Delete a student in the class");
            System.out.println("4) Update a student in the class");
            System.out.println("5) Add a grade");
            System.out.println("6) Add a grade to all students");

            int a = scan.nextInt();
            switch (a) {
                case 1:
                    ListDocs();
                    break;
                case 2:
                    CreateDoc();
                    break;
                case 3:
                    DeleteDoc();
                    break;
                case 4:
                    UpdateGrade();
                    break;
                case 5:
                    AddGrade();
                    break;
                case 6:
                    AddGradeForAllStudents();
                    break;
            }
            System.out.println("Continue with collection mod?");
            ans = scan.next();
        } while (ans.equalsIgnoreCase("yes"));
    }

    public void CreateDoc() {
        BasicDBObject doc;
        ArrayList<Map> assignments = new ArrayList();
        Map<Object, Object> assignment = new HashMap<Object, Object>();
        String nameFirst = scan.next();
        String nameSecond = scan.next();
        assignments.add(assignment);


        doc = new BasicDBObject("First Name", nameFirst)
                .append("Second Name", nameSecond)
                .append("Assignments", assignments);
        DBC.insert(doc);

    }
    public void DeleteDoc() {
        Scanner scan = new Scanner(System.in);
        System.out.println("What document do you want to delete?");
        ArrayList<DBObject> list = ListDocs();

        int num = scan.nextInt();
        DBC.remove(list.get(num - 1));

        System.out.println("Document deleted successfully");


    }

    public void UpdateGrade() {
        System.out.println("What student do you want the assignment and grade to be added");
        System.out.println("First Name");
        String FirstName = scan.next();
        System.out.println("Last Name");
        String LastName = scan.next();
        ArrayList<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = DBC.find();

        while (cursor.hasNext()) {
            DBObject a = cursor.next();
            list.add(a);
        }

        for (DBObject dbo : list) {
            if (dbo.get("First Name").equals(FirstName) && dbo.get("Second Name").equals(LastName)) {
                HashMap<Object, Object> assignment = new HashMap<Object, Object>();
                ArrayList<Map<Object, Object>> assignmentsOld = (ArrayList) dbo.get("Assignments");

                ArrayList<Map<Object, Object>> assignmentsNew = (ArrayList) dbo.get("Assignments");
                System.out.println("Whats the assignment name?");
                System.out.println("Assignment Name");
                String assignmentName = scan.next();
                System.out.println("Assignment Grade");
                Integer grade = scan.nextInt();
                for (int i = 0; i < assignmentsOld.size(); i++) {
                    Map<Object, Object> temp = assignmentsOld.get(i);
                    for (Map.Entry<Object, Object> entry : temp.entrySet()) {
                        Object key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.toString().equalsIgnoreCase(assignmentName)) {
                            entry.setValue(grade);
                        }
                    }
                }

                this.UpdateDeleteDoc(FirstName, LastName);
                this.UpdateCreateDoc(FirstName, LastName, assignmentsNew);
            }
        }
    }
    public void AddGrade() {
        System.out.println("What student do you want the assignment and grade to be added");
        System.out.println("First Name");
        String FirstName = scan.next();
        System.out.println("Last Name");
        String LastName = scan.next();
        ArrayList<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = DBC.find();

        while (cursor.hasNext()) {
            DBObject a = cursor.next();
            list.add(a);
        }

        for (DBObject dbo : list) {
            if (dbo.get("First Name").equals(FirstName) && dbo.get("Second Name").equals(LastName)) {
                HashMap<Object, Object> assignment = new HashMap<Object, Object>();
                ArrayList<Map<Object, Object>> assignmentsOld = (ArrayList) dbo.get("Assignments");

                ArrayList<Map<Object, Object>> assignmentsNew = (ArrayList) dbo.get("Assignments");
                System.out.println("1" + assignmentsOld);

                System.out.println("Whats the assignment name?");
                System.out.println("Assignment Name");

                String assignmentName = scan.next();


                System.out.println("Assignment Grade");
                Integer grade = scan.nextInt();


                assignment.put(assignmentName, grade);


                assignmentsNew.add(assignment);


                this.UpdateDeleteDoc(FirstName, LastName);
                this.UpdateCreateDoc(FirstName, LastName, assignmentsNew);
            }
        }


    }
    public void AddGradeForAllStudents() {
        String FirstName;
        String LastName;
        ArrayList<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = DBC.find();

        System.out.println("Whats the assignment name?");
        System.out.println("Assignment Name");
        String assignmentName = scan.next();

        while (cursor.hasNext()) {
            DBObject a = cursor.next();
            list.add(a);
        }

        for (DBObject dbo : list) {
            FirstName = dbo.get("First Name").toString();
            LastName = dbo.get("Second Name").toString();
            HashMap<Object, Object> assignment = new HashMap<Object, Object>();
            ArrayList<Map<Object, Object>> assignmentsNew = (ArrayList) dbo.get("Assignments");
            System.out.println("=============["+FirstName+" "+LastName+"}=============");
            System.out.println("Assignment Grade");
            Integer grade = scan.nextInt();

            assignment.put(assignmentName, grade);
            assignmentsNew.add(assignment);


            this.UpdateDeleteDoc(FirstName, LastName);
            this.UpdateCreateDoc(FirstName, LastName, assignmentsNew);

        }
    }

    public void UpdateDeleteDoc(String FirstName, String LastName) {
        ArrayList<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = DBC.find();

        while (cursor.hasNext()) {
            DBObject a = cursor.next();
            list.add(a);
        }

        for (DBObject dbo : list) {
            if (dbo.get("First Name").equals(FirstName) && dbo.get("Second Name").equals(LastName)) {
                DBC.remove(dbo);
                return;
            }
        }

    }
    public void UpdateCreateDoc(String nameFirst, String nameSecond, ArrayList assignments) {
        BasicDBObject doc;


        doc = new BasicDBObject("First Name", nameFirst)
                .append("Second Name", nameSecond)
                .append("Assignments", assignments);
        DBC.insert(doc);

    }

    public ArrayList ListDocs() {
        String FirstName,LastName;
        ArrayList<DBObject> list = new ArrayList<DBObject>();
        ArrayList<Map<Object,Object>> assigns= new ArrayList<Map<Object,Object>>();
        DBCursor cursor = DBC.find();
        int count = 1;

        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            FirstName = dbo.get("First Name").toString();
            LastName = dbo.get("Second Name").toString();
            assigns = (ArrayList<Map<Object, Object>>) dbo.get("Assignments");

            System.out.println("=============["+FirstName+" "+LastName+"}=============");
            for(int i = 0; i<assigns.size()-1;i++){
                Map<Object,Object> temp = assigns.get(i);

                for (Map.Entry<Object,Object> entry : temp.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(count+")"+key.toString()+" "+ value.toString());
                    count++;
                }
            }
            count =1;
            list.add(dbo);
        }
        return list;
    }
}
