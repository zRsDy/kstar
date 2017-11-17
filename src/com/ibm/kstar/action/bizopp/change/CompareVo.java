package com.ibm.kstar.action.bizopp.change;

import java.io.Serializable;

/**
 * Created by wangchao on 2017/3/17.
 */
public class CompareVo implements Serializable{

    private String name;
    private String oldCol;
    private String newCol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldCol() {
        return oldCol;
    }

    public void setOldCol(String oldCol) {
        this.oldCol = oldCol;
    }

    public String getNewCol() {
        return newCol;
    }

    public void setNewCol(String newCol) {
        this.newCol = newCol;
    }
}
