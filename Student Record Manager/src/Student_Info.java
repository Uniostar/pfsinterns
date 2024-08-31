public class Student_Info
{
    private String name = "";
    private int roll = 0;
    private int englishGrade = 0;
    private int mathGrade = 0;
    private int scienceGrade = 0;

    public Student_Info(String name, int roll, int englishGrade, int mathGrade, int scienceGrade)
    {
        this.name = name;
        this.roll = roll;
        this.englishGrade = englishGrade;
        this.mathGrade = mathGrade;
        this.scienceGrade = scienceGrade;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRoll() {
        return roll;
    }
    public void setRoll(int roll) {
        this.roll = roll;
    }
    public int getEnglishGrade() {
        return englishGrade;
    }
    public void setEnglishGrade(int englishGrade) {
        this.englishGrade = englishGrade;
    }
    public int getMathGrade() {
        return mathGrade;
    }
    public void setMathGrade(int mathGrade) {
        this.mathGrade = mathGrade;
    }
    public int getScienceGrade() {
        return scienceGrade;
    }
    public void setScienceGrade(int scienceGrade) {
        this.scienceGrade = scienceGrade;
    }

    @Override
    public String toString()
    {
        return STR."\{getName()}\t|\t\{getRoll()}\t|\t\{getEnglishGrade()}\t|\t\{getMathGrade()}\t|\t\{getScienceGrade()}";
    }
}