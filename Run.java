
import javax.swing.UIManager;

/**
 * Run
 */
public class Run {
    private static MainWindow mainWindow;
    private static Controller controller;
    public static void main(String[] args) {
        controller=new Controller();
        mainWindow = new MainWindow(controller);
        controller.setMainWindow(mainWindow);
        
    }
}