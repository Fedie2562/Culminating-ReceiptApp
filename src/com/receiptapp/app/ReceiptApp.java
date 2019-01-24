/*
 * Culminating receipt app
 * Ferris, Jared and Corbin.
 * January 8th 2018
 */
package com.receiptapp.app;

// Imports
import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.messaging.Message;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.util.Date;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class ReceiptApp {
   
    private Form current;
    private Resources theme;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
    }

    public void start() {
        if(current != null){
            current.show();
            return;
        }
        // Get the date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Date date = new Date();
        // Set up the UI
        Form receiptApp = new Form("Receipt App", BoxLayout.y());
        receiptApp.add(new Label("Company Name"));
        TextField CompanyName = new TextField("");
        receiptApp.add(CompanyName);
        receiptApp.add(new Label("Email"));
        TextField email = new TextField("");
        receiptApp.add(email);
        receiptApp.add(new Label("Product"));
        TextField product = new TextField("");
        receiptApp.add(product);
        receiptApp.add(new Label("Amount"));
        TextField amount = new TextField("");
        receiptApp.add(amount);
        Button send = new Button("Send");
        receiptApp.add(send);
        // What to do when the send button is pressed
        send.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e){  
            System.out.println("Sent");
            //Creating the message to be sent in the email
            Message mail = new Message("Date : "+dateFormat.format(date)+"\n"
                    +CompanyName.getText()+", this is a receipt regarding the sale of "
                    +amount.getText()+" "+product.getText());
            /*System.out.println("Date : "+dateFormat.format(date)+"\n"
                    +CompanyName.getText()+", this is a receipt regarding the sale of "
                    +amount.getText()+" "+product.getText());
                    */
            // Make a dialog confirming that the message has been sent
            Dialog dlg = new Dialog("The receipt has been sent");
            dlg.setLayout(BoxLayout.y());
            dlg.add(new SpanLabel("", ""));
            int h = Display.getInstance().getDisplayHeight();
            dlg.setDisposeWhenPointerOutOfBounds(true);
            dlg.show(h /12 * 7, 0, 0, 0);
            // Sending the actual message to the specified email
            Display.getInstance().sendMessage(new String[] {email.getText()}, product.getText(), mail);
            System.out.println("Done");
        }
        });
        receiptApp.show();
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }
}
