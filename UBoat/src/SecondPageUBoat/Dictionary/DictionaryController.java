package SecondPageUBoat.Dictionary;

import SecondPageUBoat.SecondPageController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DictionaryController {

    private SecondPageController secondPageController;
    private DictionaryTrie trie;
    private Set<String> dictionary;

    @FXML private VBox wordsListComponent;
    @FXML private TextField searchInputField;

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    private void buildElement(){
        destroyElement();

        dictionary = secondPageController.getDictionary();
        for (String word : dictionary) {
            wordsListComponent.getChildren().add(createNewWordLabel(word));
            trie.insert(word);
        }
        bindSearchFieldToTrie();
    }

    private Label createNewWordLabel(String word){
        Label res = new Label(word);
        res.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    secondPageController.setChoosenDictionaryWord(word);
                }
            }
        });
        return res;
    }

    private void destroyElement(){
        trie = new DictionaryTrie();
        wordsListComponent.getChildren().clear();
        searchInputField.textProperty().unbind();
    }

    public void bindToXMLPath(SimpleStringProperty xmlPathProperty) {
        xmlPathProperty.addListener(e -> {
            buildElement();
        });
    }

    private void bindSearchFieldToTrie(){
        searchInputField.textProperty().addListener(e ->{
            buildDictionaryElement();
        });
    }

    public void insertWordsToComponent(Collection<String> toDisplay) {
        for (String word : toDisplay)
            wordsListComponent.getChildren().add(createNewWordLabel(word));
    }

    @FXML
    public void searchInputFieldKeyTyped(KeyEvent event) { }

    private void buildDictionaryElement() {
        wordsListComponent.getChildren().clear();

        String toSearch = searchInputField.getText();
        if(toSearch.equals(""))
            insertWordsToComponent(dictionary);
        else {
            List<String> toDisplay = trie.wordsFinderTraversal(toSearch.toUpperCase());
            insertWordsToComponent(toDisplay);
        }
    }

    public void clearAllFields() {
        searchInputField.clear();
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
    }
}
