import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.SwingWorker;

/**
 * BackGroundSender will send request in backgroung to avoid freezing
 */
public class BackGroundSender extends SwingWorker {
    private Request request;
    private RequestSender sender;
    private StatusObservable statusObservable;
    private Boolean followRedDirect;

    /**
     * Counstructor
     * 
     * @param request
     * @param responsePanel
     */
    public BackGroundSender(Request request, ResponsePanel responsePanel,Boolean followRedDirect) {
        this.request = request;
        this.followRedDirect=followRedDirect;
        statusObservable = new StatusObservable();
        addObserver(responsePanel);

    }

    @Override
    protected Object doInBackground() throws Exception {
        try {
            sender = new RequestSender(request, false, request.getName() + "_Body", false, false, followRedDirect, "");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Can't connect to "+request.getUrl().toString(), "!!!!",
            JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    @Override
    protected void done() {

        statusObservable.notifyObservers(request);
        super.done();
    }

    private void addObserver(Observer obs) {
        statusObservable.addObserver(obs);
    }

    private class StatusObservable extends Observable {
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    }
}