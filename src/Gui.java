
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Gui extends Application  {
    private Map<Skill,Spinner<Integer>> skillForm;
    private Spinner<Integer> targetCBL;
    private OsrsCharRep charRep;
    private Label displayAnswers;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        VBox pVbox = new VBox();
        Group pgroup = new Group();
        Scene pscene = new Scene(pgroup);
        primaryStage.setScene(pscene);
        pgroup.getChildren().add(pVbox);
        charRep = new OsrsCharRep();

        primaryStage.setWidth(400);
        primaryStage.setHeight(700);
        pVbox.getChildren().addAll(
                getInputFields(),
                getSubmitButton(),
                getAnswerLabel()
        );
        pVbox.setPadding(new Insets(15));
        pVbox.setMinHeight(700);
        pVbox.setStyle("-fx-background-color: linear-gradient(#ddd,#999);");

        primaryStage.setResizable(false);
        primaryStage.setTitle("Combat level calculator");

        primaryStage.show();


    }

    private HBox getInputFields(){
        skillForm = new HashMap<>();

        Spinner<Integer> defence = new FocusCommitedSpinner(1,99,1);
        skillForm.put(Skill.defence,defence);

        Spinner<Integer> hitpoints = new FocusCommitedSpinner(10,99,10);
        skillForm.put(Skill.hitpoints,hitpoints);

        Spinner<Integer> prayer = new FocusCommitedSpinner(1,99,1);
        skillForm.put(Skill.prayer,prayer);

        Spinner<Integer> attack = new FocusCommitedSpinner(1,99,1);
        skillForm.put(Skill.attack,attack);

        Spinner<Integer> strength = new FocusCommitedSpinner(1,99,1);
        skillForm.put(Skill.strength,strength);

        Spinner<Integer> ranged = new FocusCommitedSpinner(1,99,1);
        skillForm.put(Skill.ranged,ranged);

        Spinner<Integer> magic = new FocusCommitedSpinner(1,99,1);
        skillForm.put(Skill.magic,magic);

        Spinner<Integer> targetCBL = new FocusCommitedSpinner(3,126,3);
        this.targetCBL = targetCBL;

        Label defenceL = new Label("Defence: ");
        Label hitpointsL = new Label("Hitpoints: ");
        Label prayerL = new Label("Prayer: ");
        Label attackL = new Label("Attack: ");
        Label strengthL = new Label("Strength: ");
        Label rangeL = new Label("Range: ");
        Label magicL = new Label("Magic: ");
        Label targetCBLL = new Label("Target Combat Level: ");

        VBox inputLabelVbox = new VBox(30);
        inputLabelVbox.alignmentProperty().set(Pos.CENTER_RIGHT);
        VBox inputTextfieldVbox = new VBox(22);
        inputTextfieldVbox.alignmentProperty().set(Pos.CENTER_LEFT);

        VBox.setVgrow(inputLabelVbox, Priority.ALWAYS);
        VBox.setVgrow(inputTextfieldVbox, Priority.ALWAYS);

        inputLabelVbox.getChildren().addAll(
                new HBox(defenceL),
                new HBox(hitpointsL),
                new HBox(prayerL),
                new HBox(attackL),
                new HBox(strengthL),
                new HBox(rangeL),
                new HBox(magicL),
                new HBox(new Label()),
                new HBox(targetCBLL)

        );

        inputTextfieldVbox.getChildren().addAll(
                new HBox(defence),
                new HBox(hitpoints),
                new HBox(prayer),
                new HBox(attack),
                new HBox(strength),
                new HBox(ranged),
                new HBox(magic),
                new HBox(new Label()),
                new HBox(targetCBL)
        );

        HBox inputHbox = new HBox();
        inputHbox.setMinHeight(400);
        inputHbox.setMinWidth(400);
        inputHbox.setAlignment(Pos.CENTER);
        inputHbox.getChildren().addAll(inputLabelVbox,inputTextfieldVbox);

        return inputHbox;
    }

    private HBox getSubmitButton(){

        Button submit = new Button("Submit");

        submit.setStyle("" +
                "-fx-color: #ff0000");

        EventHandler<ActionEvent> submitAction = (e-> {


            for(Map.Entry<Skill,Spinner<Integer>> entry: skillForm.entrySet()){
                Skill skill = entry.getKey();
                Integer level = entry.getValue().getValue();

                charRep.setSkillStat(skill,level);
            }
            double preCombatLevel = UtilMath.calcCombatLevel(charRep);
            Map<Skill,Integer> changeReq = charRep.calcCheapesWayToGoal(targetCBL.getValue());
            StringBuilder sb = new StringBuilder();

            sb.append("Current combat level: ").append(round(preCombatLevel)).append("\n");

            for (Map.Entry<Skill,Integer> entry:changeReq.entrySet()){
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            displayAnswers.setText(sb.toString());
        });
        submit.setOnAction(submitAction);

        HBox buttonHbox = new HBox();
        buttonHbox.setPadding(new Insets(50));
        buttonHbox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonHbox,Priority.ALWAYS);
        buttonHbox.getChildren().add(submit);
        return buttonHbox;
    }

    private HBox getAnswerLabel(){
        Label answerL = new Label();
        answerL.setWrapText(false);
        answerL.setMinWidth(400);
        displayAnswers = answerL;
        return new HBox(answerL);
    }


    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
