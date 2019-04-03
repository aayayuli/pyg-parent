package groupEntity;

import com.pyg.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/30  9:44
 * @description TODO
 **/
public class Cart implements Serializable {
    private  String  sellerName;
    private  String  sellerId;
    private List<TbOrderItem>   orderItemList;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
