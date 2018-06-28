package rmi.api;

import java.io.Serializable;

public class Group implements Serializable {

    public String groupName = "";
    public String groupUrl = "";

    public Group(String name, String url) {
        this.groupName = name;
        this.groupUrl = url;
    }
}
