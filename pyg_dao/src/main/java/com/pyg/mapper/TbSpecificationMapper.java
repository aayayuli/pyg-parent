package com.pyg.mapper;


import com.pyg.pojo.TbSpecification;

import java.util.List;
import java.util.Map;

public interface TbSpecificationMapper {
    List<TbSpecification> finAll();

    void insert(TbSpecification tbspecification);

    TbSpecification selectByKey(Long id);

    void updateByKey(TbSpecification tbspecification);

    void deleteByIds(Long id);

     List<TbSpecification> search(TbSpecification tbSpecification);


    List<Map> findSpecList();

}
