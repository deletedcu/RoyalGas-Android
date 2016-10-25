package dmg.com.rg.model;

/**
 * Created by Star on 10/20/16.
 */

public class PageInfo {

    public static final int NATIVE_TYPE = 1;
    public static final int WEB_TYPE = 2;

    private int nPagetype;
    private String strTitle;
    private String strDescription;
    private String strImageURL;
    private String strLink;
    private String strPageName;

    public PageInfo() {
        super();
    }

    public PageInfo(int pageType, String title, String description, String imageURL, String link, String pageName) {
        super();
        setnPagetype(pageType);
        setStrTitle(title);
        setStrDescription(description);
        setStrImageURL(imageURL);
        setStrLink(link);
        setStrPageName(pageName);
    }

    public int getnPagetype() {
        return nPagetype;
    }

    public void setnPagetype(int nPagetype) {
        this.nPagetype = nPagetype;
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

    public String getStrImageURL() {
        return strImageURL;
    }

    public void setStrImageURL(String strImageURL) {
        this.strImageURL = strImageURL;
    }

    public String getStrLink() {
        return strLink;
    }

    public void setStrLink(String strLink) {
        this.strLink = strLink;
    }

    public String getStrPageName() {
        return strPageName;
    }

    public void setStrPageName(String strPageName) {
        this.strPageName = strPageName;
    }
}
