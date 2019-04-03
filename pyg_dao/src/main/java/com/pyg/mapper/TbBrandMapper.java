package com.pyg.mapper;


import com.pyg.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface TbBrandMapper {
    List<TbBrand> finAll();

    void insert(TbBrand tbBrand);

    TbBrand selectByKey(Long id);

    void updateByKey(TbBrand tbBrand);

    void deleteByIds(Long id);

     List<TbBrand> search(TbBrand tbBrand);

    List<Map> findBrandList();
}
