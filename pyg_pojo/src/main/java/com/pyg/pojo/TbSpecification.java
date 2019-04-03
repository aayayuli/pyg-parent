package com.pyg.pojo;

import java.io.Serializable;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/11  9:16
 * @description TODO
 **/
public class TbSpecification implements Serializable {
    private  Long id ;
    private  String specName;

    public TbSpecification() {
    }

    public TbSpecification(Long id, String specName) {
        this.id = id;
        this.specName = specName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

}
