package groupEntity;

import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/11  9:14
 * @description TODO
 **/
//规格和规格小项组合类
public class Specification  implements Serializable {
    private TbSpecification tbSpecification;
    private List<TbSpecificationOption> tbSpecificationOptionList;

    public Specification() {
    }

    public Specification(TbSpecification tbSpecification, List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecification = tbSpecification;
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }
}
