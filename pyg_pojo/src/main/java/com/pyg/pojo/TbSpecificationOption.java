package com.pyg.pojo;

import java.io.Serializable;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/11  9:18
 * @description TODO
 **/
public class TbSpecificationOption implements Serializable {
    private Long id;
    private String optionName;
    private Long specId;
    private Integer orders;

    public TbSpecificationOption() {
    }

    public TbSpecificationOption(Long id, String optionName, Long specId, Integer orders) {
        this.id = id;
        this.optionName = optionName;
        this.specId = specId;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }
}
