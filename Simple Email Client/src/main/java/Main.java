public class Main
{
    public static void main(String[] args)
    {
        EmailHandler handler = new EmailHandler();
        EmailGUI emailGUI = new EmailGUI(handler);

        emailGUI.showGUI();
    }
}
