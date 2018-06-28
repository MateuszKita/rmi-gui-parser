package client;

import rmi.api.Group;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rmi.api.Parser;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 8000);

        Parser parser = (Parser) registry.lookup("parser");

        VBox root = new VBox(5);
        Label lblNumber = new Label("Click \"Parse\" to download schedule for IT groups.");
        Button btnProcess = new Button("Parse");
        Label lblResponse = new Label();
        TextArea txt = new TextArea();
        txt.setEditable(false);

        btnProcess.setOnAction((ActionEvent t) -> {
            btnProcess.setDisable(true);
            try {
                List<Group> groups = parser.parseGroups();
                Button[] groupsButtons = new Button[groups.size()];

                lblResponse.setText("Downloaded schedules for " + groups.size() + " groups. Select group to see a plan:");

                for (int i = 0; i < groups.size(); i++) {
                    groupsButtons[i] = new Button(groups.get(i).groupName);
                    final String url = groups.get(i).groupUrl;
                    groupsButtons[i].setOnAction((ActionEvent s) -> {
                        txt.clear();
                        try {
                            List<String> schedule = parser.parseSchedule(url);
                            for (int x = 0; x < schedule.size(); x++) {
                                if ((schedule.get(x).equals("Poniedziałek") || schedule.get(x).equals("Wtorek") || schedule.get(x).equals("Środa")
                                        || schedule.get(x).equals("Czwartek") || schedule.get(x).equals("Piątek")
                                        || schedule.get(x).equals("Sobota") || schedule.get(x).equals("Niedziela")) && x != 0) {
                                    schedule.set(x, "\n" + schedule.get(x));
                                }
                                txt.appendText(schedule.get(x) + "\n");
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    root.getChildren().addAll(groupsButtons[i]);
                }
            } catch (RemoteException e) {
                System.err.println(e);
            }
        }
        );

        root.getChildren()
                .addAll(lblNumber, btnProcess, lblResponse, txt);

        stage.setScene(
                new Scene(root, 800, 1000));

        stage.setTitle(
                "RMI MZK ZGORA SCHEDULE");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
