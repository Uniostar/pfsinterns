import java.text.Format;
import java.util.*;
public class Main
{
    public static final String userName = "JavaUser";
    public static final String password = "JavaIsBest";

    public static void main(String[] args)
    {
        Scanner scn = new Scanner(System.in);
        Info_Manager manager = new Info_Manager();

        while (true) {
            if (validateInfo(scn)) {System.out.println("Entry Granted \n"); break;}
            else {System.out.println("Username or Password Incorrect");}
        }

        while(true)
        {
            System.out.println("Welcome to Student Record Manager");
            System.out.println("Choose an item to get started: \n C = Create Student \n R = Read Student List \n U = Update Student Info \n D = Delete Student Info \n E = Exit Program");
            String in = scn.nextLine();

            if (in.equals("E"))
            {
                System.out.println("Program Terminated");
                break;
            }
            else if (in.equals("C"))
            {
                manager.addInfo(createStudent(scn, manager));
                scn.nextLine();
            }
            else if (in.equals("R"))
            {
                manager.printXMLData();
            }
            else if (in.equals("U"))
            {
                updateStudent(scn, manager);
            }
            else if (in.equals("D"))
            {
                deleteStudent(scn, manager);
                scn.nextLine();
            }
            else
            {
                System.out.println("Choose Valid Operation");
            }
        }
    }

    public static boolean validateInfo(Scanner scn)
    {
        System.out.print("Enter user name: ");
        String userName = scn.nextLine();

        System.out.print("Enter password: ");
        String pass = scn.nextLine();

        return userName.equals(Main.userName) && pass.equals(Main.password);
    }

    public static Student_Info createStudent(Scanner scn, Info_Manager manager)
    {
        String name;
        int rollNo, mathGrade, englishGrade, scienceGrade;

        System.out.println("Enter New Student Info");

        name = getName("Name: ", scn);
        rollNo = getRoll(scn, manager);
        mathGrade = getInteger("Math Grade: ", scn);
        englishGrade = getInteger("English Grade: ", scn);
        scienceGrade = getInteger("Science Grade: ", scn);
        return new Student_Info(name, rollNo, englishGrade, mathGrade, scienceGrade);
    }

    public static void updateStudent(Scanner scn, Info_Manager manager)
    {
        manager.printXMLData();
        System.out.println("Enter Roll No to Change");

        int roll = Integer.MIN_VALUE;
        do
        {
            roll = getInteger("Enter Roll: ", scn);
            scn.nextLine();
            if (manager.findStudentRoll(roll) == -1) { System.out.println("Roll not found. Enter valid roll number."); }
        } while (manager.findStudentRoll(roll) == -1);

        Student_Info toChange = manager.studentInfo.get(manager.findStudentRoll(roll));

        while (true)
        {
            System.out.println("Choose Property to Change: \n N - Name \n M - Math Grade \n E - English Grade \n S - Science Grade");
            String choice = scn.nextLine();

            if (choice.equals("N"))
            {
                String newName = getName("New Name: ", scn);
                toChange.setName(newName);
                manager.deleteInfo(manager.findStudentRoll(roll));
                manager.addInfo(toChange);
                break;
            }
            else if (choice.equals("M"))
            {
                int math = getInteger("New Grade: ", scn);
                scn.nextLine();
                toChange.setMathGrade(math);
                manager.deleteInfo(manager.findStudentRoll(roll));
                manager.addInfo(toChange);
                break;
            }
            else if (choice.equals("E"))
            {
                int english = getInteger("New Grade: ", scn);
                scn.nextLine();
                toChange.setEnglishGrade(english);
                manager.deleteInfo(manager.findStudentRoll(roll));
                manager.addInfo(toChange);
                break;
            }
            else if (choice.equals("S"))
            {
                int science = getInteger("New Grade: ", scn);
                scn.nextLine();
                toChange.setScienceGrade(science);
                manager.deleteInfo(manager.findStudentRoll(roll));
                manager.addInfo(toChange);
                break;
            }
            else if (!choice.equals("\n"))
            {
                System.out.println("Choose Valid Property: ");
            }
        }
    }

    public static void deleteStudent(Scanner scn, Info_Manager manager)
    {
        manager.printXMLData();
        int roll = Integer.MIN_VALUE;
        do
        {
            roll = getInteger("Enter Roll: ", scn);
            if (manager.findStudentRoll(roll) == -1) { System.out.println("Roll not found. Enter valid roll number."); }
        } while (manager.findStudentRoll(roll) == -1);

        manager.deleteInfo(manager.studentInfo.get(manager.findStudentRoll(roll)));
    }

    public static String getName(String message, Scanner scn)
    {
        System.out.print(message);
        return scn.nextLine();
    }

    public static int getInteger(String message, Scanner scn)
    {
        int integer = -1;
        while (true) {
            System.out.print(message);
            if (scn.hasNextInt())
            {
                integer = scn.nextInt();
                break;
            }
            else
            {
                System.out.println("Invalid input. Please enter an integer.");
                scn.next();
            }
        }

        return integer;
    }

    public static int getRoll(Scanner scn, Info_Manager manager)
    {
        int roll = getInteger("Roll Number: ", scn);

        while (manager.findStudentRoll(roll) != -1)
        {
            if (manager.findStudentRoll(roll) != -1)
                System.out.println("Roll Number in use. Type new roll.");

            roll = getInteger("Roll Number: ", scn);
        }

        return roll;
    }
}
