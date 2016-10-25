package dmg.com.rg.model;

/**
 * Created by Star on 10/20/16.
 */

public class Services {

    private String strImageURL;
    private String strTitle;
    private String strDescription;

    public Services() {
        super();
    }

    public Services(String url, String title, String description) {
        super();
        setStrImageURL(url);
        setStrTitle(title);
        setStrDescription(description);
    }

    public String getStrImageURL() {
        return strImageURL;
    }

    public void setStrImageURL(String strImageURL) {
        this.strImageURL = strImageURL;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }
}
