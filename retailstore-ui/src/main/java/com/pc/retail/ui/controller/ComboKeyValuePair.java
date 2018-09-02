package com.pc.retail.ui.controller;

/**
 * Created by pavanc on 8/25/18.
 */
public class ComboKeyValuePair {
    String displayName;
    String id;

    public ComboKeyValuePair(String id, String displayName) {
        this.displayName = displayName;
        this.id = id;
    }

    public ComboKeyValuePair() {
        this.displayName = displayName;
        this.id = id;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComboKeyValuePair that = (ComboKeyValuePair) o;

        if (getDisplayName() != null ? !getDisplayName().equals(that.getDisplayName()) : that.getDisplayName() != null)
            return false;
        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        int result = getDisplayName() != null ? getDisplayName().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  displayName == null ? "" : displayName;
    }
}
