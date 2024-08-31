import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import java.util.*;

public class Info_Manager
{
    ArrayList<Student_Info> studentInfo;

    public Info_Manager()
    {
        //Only needed once
//        createXMLFile();

        studentInfo = new ArrayList<>();
        initiateXML();
    }
    private void initiateXML()
    {
        try {
            // Load and parse the XML file
            File xmlFile = new File("students.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the document
            doc.getDocumentElement().normalize();

            // Get all Student elements
            NodeList nList = doc.getElementsByTagName("Student");

            // Loop through the Student nodes
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    // Extract student data
                    int roll = Integer.parseInt(eElement.getElementsByTagName("Roll").item(0).getTextContent());
                    String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                    int math = Integer.parseInt(eElement.getElementsByTagName("Math_Grade").item(0).getTextContent());
                    int english = Integer.parseInt(eElement.getElementsByTagName("English_Grade").item(0).getTextContent());
                    int science = Integer.parseInt(eElement.getElementsByTagName("Science_Grade").item(0).getTextContent());

                    // Create a new Student object
                    Student_Info student = new Student_Info(name, roll, english, math, science);

                    // Add the Student object to the ArrayList
                    studentInfo.add(student);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int findStudentRoll(int roll)
    {
        for (int i = 0; i < studentInfo.size(); i++)
        {
            if (studentInfo.get(i).getRoll() == roll)
                return i;
        }

        return -1;
    }
    public void addInfo(Student_Info studentInfo)
    {
        this.studentInfo.add(studentInfo);
        try {
            saveStudent(studentInfo);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void deleteInfo(Student_Info studentInfo) {
        if (this.studentInfo.contains(studentInfo)) {
            removeStudentFromXML(Integer.toString(studentInfo.getRoll()));
            this.studentInfo.remove(studentInfo);
        }
        else {
            System.err.println("Error 1: Student not found");
        }
    }
    public void deleteInfo(int idx)
    {
        removeStudentFromXML(Integer.toString(this.studentInfo.get(idx).getRoll()));
        this.studentInfo.remove(idx);
    }
    private void saveStudent(Student_Info studentInfo) throws ParserConfigurationException {
        try {
            File xmlFile = new File("students.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();

            Element student = doc.createElement("Student");

            Element roll = doc.createElement("Roll");
            roll.appendChild(doc.createTextNode(Integer.toString(studentInfo.getRoll())));
            student.appendChild(roll);

            Element name1 = doc.createElement("Name");
            name1.appendChild(doc.createTextNode((studentInfo.getName())));
            student.appendChild(name1);

            Element math = doc.createElement("Math_Grade");
            math.appendChild(doc.createTextNode(String.valueOf(studentInfo.getMathGrade())));
            student.appendChild(math);

            Element eng = doc.createElement("English_Grade");
            eng.appendChild(doc.createTextNode(String.valueOf(studentInfo.getEnglishGrade())));
            student.appendChild(eng);

            Element sci = doc.createElement("Science_Grade");
            sci.appendChild(doc.createTextNode(String.valueOf(studentInfo.getScienceGrade())));
            student.appendChild(sci);

            rootElement.appendChild(student);

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("New student data saved!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void removeStudentFromXML(String roll) {
        try {
            // Load the existing XML file
            File xmlFile = new File("students.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            // Get all Employee elements
            NodeList nList = doc.getElementsByTagName("Student");

            boolean removed = false;

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String currentId = eElement.getElementsByTagName("Roll").item(0).getTextContent();

                    if (currentId.equals(roll)) {
                        eElement.getParentNode().removeChild(eElement);
                        removed = true;
                        break;  // Remove only the first matching Roll
                    }
                }
            }

            if (removed) {
                // Write the updated XML back to the file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(xmlFile);
                transformer.transform(source, result);

                System.out.println("Student with Roll " + roll + " removed successfully!");
            } else {
                System.out.println("Student with Roll " + roll + " not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printXMLData() {
        try {
            File xmlFile = new File("students.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Student");

            if (nList.getLength() > 0) {
                System.out.printf("%-5s %-15s %-5s %-5s %-5s%n", "Roll", "Name", "Math ", "English ", "Science ");
                System.out.println("--------------------------------------------");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        String id = eElement.getElementsByTagName("Roll").item(0).getTextContent();
                        String name = (eElement.getElementsByTagName("Name").item(0).getTextContent());
                        name = (name.length() <= 15) ? name : name.substring(0, 15);
                        String math = eElement.getElementsByTagName("Math_Grade").item(0).getTextContent();
                        String english = eElement.getElementsByTagName("English_Grade").item(0).getTextContent();
                        String science = eElement.getElementsByTagName("Science_Grade").item(0).getTextContent();

                        System.out.printf("%-5s %-15s %-5s %-8s %-8s%n", id, name, math, english, science);

                    }
                }
            }
            else
            {
                System.out.println("No data available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createXMLFile()
    {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Students");
            doc.appendChild(rootElement);

            Element student = doc.createElement("Student");
            rootElement.appendChild(student);

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("students.xml"));

            transformer.transform(source, result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}