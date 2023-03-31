package MainControllerUBoat.Intro;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IntroController {
    @FXML private ImageView intoImageView;

    public void setIntroImage(String introImagePath) {
        if(introImagePath.equals(""))
            intoImageView.setImage(null);
        else
            intoImageView.setImage(new Image(introImagePath));
    }
}
