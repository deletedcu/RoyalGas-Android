package dmg.com.rg.model;

/**
 * Created by Star on 10/20/16.
 */

public class MyMenu {

    public static final String TITLE = "title";
    public static final String PATH = "path";
    public static final String ICON = "icon";
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String NATIVE_TYPE = "native-contact";
    public static final String WEBVIEW_TYPE = "webview";

    private String strIconURL;
    private String strTitle;
    private String strPath;
    private String strImagePath;
    private String strType;
    private String strDescription;

    public MyMenu(String title, String path, String icon, String image, String type) {
        super();
        setStrTitle(title);
        setStrPath(path);
        setStrIconURL(icon);
        setStrImagePath(image);
        setStrType(type);
    }

    public MyMenu(String title, String description, String path, String icon, String image, String type) {
        super();
        setStrTitle(title);
        setStrDescription(description);
        setStrPath(path);
        setStrIconURL(icon);
        setStrImagePath(image);
        setStrType(type);
    }

    public String getStrIconURL() {
        return strIconURL;
    }

    public void setStrIconURL(String strIconURL) {
        this.strIconURL = strIconURL;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrPath() {
        return strPath;
    }

    public void setStrPath(String strPath) {
        this.strPath = strPath;
    }

    public String getStrImagePath() {
        return strImagePath;
    }

    public void setStrImagePath(String strImagePath) {
        this.strImagePath = strImagePath;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }
}
