package jtranslate.parser;

public enum TokenType
{
    SPECIAL_BLOCK("@"),
    MAP("->");







    private String image;

    TokenType(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
