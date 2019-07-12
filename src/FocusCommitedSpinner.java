import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

class FocusCommitedSpinner extends Spinner<Integer> {

    FocusCommitedSpinner(int min, int max, int initialValue) {
        super(min, max, initialValue);
        focusedProperty().addListener((s,ov,nv)->{
            if(nv)return;
            commitEditorText(this);
        });

        setEditable(true);


    }



    private <T> void commitEditorText(Spinner<T> spinner) {
        if (!spinner.isEditable()) return;
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

}
