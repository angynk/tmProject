package com.journaldev.prime.faces.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name="sideBar")
@SessionScoped
public class SideBar implements Serializable {

    private String page;

    @PostConstruct
    public void init() {
        this.page = "/templates/tablaMaestra";

    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
