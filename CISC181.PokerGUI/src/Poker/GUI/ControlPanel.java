package Poker.GUI;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pokerAction.Action.*;
import pokerAction.Action;


//import pokerAction.Actions.*;
 
public class ControlPanel extends JPanel  implements ActionListener {

    /** The Check button. */
	private final JButton btnDeal;
	private final JButton btnStart;
	private final JButton btnEnd;
	
	private final JButton btnContinue;
	private final JButton btnLeave;
	private final JButton btnSit;
	private final JButton btnDraw;
	private final JButton btnDiscard;
	private final JButton btnFold;
	
	
    private final Object monitor = new Object();
    
	private Action selectedAction;
    
	/**
	 * Create the panel.
	 */
	public ControlPanel() {
		
		setLayout(null);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSelectedAction(Action.START);
			}
		});
		btnStart.setBounds(10, 11, 87, 23);
		add(btnStart);
		
		btnEnd = new JButton("End");
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedAction(Action.END);
			}
		});
		btnEnd.setBounds(100, 11, 87, 23);
		add(btnEnd);
		
		/*btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnContinue.setBounds(190, 11, 87, 23);
		add(btnContinue);*/
		
        setBackground(Color.blue);
        btnDeal = createActionButton(Action.DEAL);
        //btnStart = createActionButton(Action.START);
        //btnEnd = createActionButton(Action.END);
        btnContinue = createActionButton(Action.CONTINUE);
        btnLeave= createActionButton(Action.LEAVE);
        btnSit= createActionButton(Action.SIT);
        btnDraw= createActionButton(Action.DRAW);
        btnDiscard= createActionButton(Action.DISCARD);
        btnFold= createActionButton(Action.FOLD);

	}
	
    /**
     * Waits for the user to click the Continue button.
     */
	public void waitForUserInput() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(btnContinue);
                repaint();
            }
        });
        Set<Action> allowedActions = new HashSet<Action>();
        allowedActions.add(Action.CONTINUE);
        getUserInput(allowedActions);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnDeal) {
            selectedAction = Action.DEAL;
        } else if (source == btnStart) {
            selectedAction = Action.START;
        } else if (source == btnEnd) {
            selectedAction = Action.END;
        }
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
    
    
    public Action getUserInput(final Set<Action> allowedActions) {
    	System.out.println("Here");
        selectedAction = null;
        while (selectedAction == null) {
            // Show the buttons for the allowed actions.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeAll();
                    if (allowedActions.contains(Action.CONTINUE)) {
                        add(btnContinue);
                    } else {
                        if (allowedActions.contains(Action.LEAVE)) {
                            add(btnLeave);
                        }
                        if (allowedActions.contains(Action.SIT)) {
                            add(btnSit);
                        }
                        if (allowedActions.contains(Action.DRAW)) {
                            add(btnDraw);
                        }
                        if (allowedActions.contains(Action.DISCARD)) {
                            add(btnDiscard);
                        }                        
                        if (allowedActions.contains(Action.FOLD)) {
                            add(btnFold);
                        }
                    }
                    repaint();
                }
            });
            
            // Wait for the user to select an action.
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    // Ignore.
                }
            }
            

        }
        
        return selectedAction;
    }
    
    
    
    
    
    
    
    
    
    private void setSelectedAction(Action a)
    {
    	selectedAction = a;
    }
    
    
    public Action getSelectedAction()
    {
    	return selectedAction;
    }
    
    
    
    
    
    
    
    
    private JButton createActionButton(Action action) {
        String label = action.getName();
        JButton button = new JButton(label);
        button.setMnemonic(label.charAt(0));
        button.setSize(100, 30);
        button.addActionListener(this);
        return button;
    }

}
