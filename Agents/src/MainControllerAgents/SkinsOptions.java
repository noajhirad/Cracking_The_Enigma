package MainControllerAgents;

import javafx.scene.paint.Color;

public enum SkinsOptions {
    COOKIE_MONSTER("/resource/cookieMonster/cookieMonsterCSS.css", "/resource/cookieMonster/intro.png", 232, 223, 216),
    PEACH("/resource/peach/peachCSS.css", "/resource/peach/intro.png", 243, 238, 235),
    LION_KING("/resource/lionKing/lionKingCSS.css", "/resource/lionKing/intro.png", 207, 224, 204),
    DEFAULT("", "", 211, 211, 211);

    private String path;
    private String introImagePath;
    private int r;
    private int g;
    private int b;

    SkinsOptions(String path, String introImagePath, int r, int g, int b){
        this.path = path;
        this.introImagePath = introImagePath;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public String getCSS() {
        return this.getClass().getResource(path).toExternalForm();
    }

    public String getIntroImagePath() {
        return introImagePath;
    }

    public Color getBtnColor(){
        return Color.rgb(r,g,b);
    }
}