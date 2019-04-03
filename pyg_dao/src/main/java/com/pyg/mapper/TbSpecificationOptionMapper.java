package com.pyg.mapper;


import com.pyg.pojo.TbSpecificationOption;

import java.util.List;

public interface TbSpecificationOptionMapper {
    List<TbSpecificationOption> finAll();

    void insert(TbSpecificationOption tbSpecificationOption);

    TbSpecificationOption selectByKey(Long id);

    void updateByKey(TbSpecificationOption tbSpecificationOption);

    void deleteByIds(Long id);

     List<TbSpecificationOption> search(TbSpecificationOption tbSpecificationOption);

    List<TbSpecificationOption> selectBySpecId(Long id);

    void deleteBySpecId(Long id);
}
