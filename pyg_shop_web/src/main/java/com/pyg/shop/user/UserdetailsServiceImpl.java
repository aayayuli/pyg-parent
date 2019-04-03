package com.pyg.shop.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbSeller;
import com.pyg.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

//自定义的认证类
public class UserdetailsServiceImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbSeller seller = sellerService.findOne(username);
        if (seller == null) {  //意味着用户名输入错误
            return null;
        } else {
            if (!seller.getStatus().equals("1")) {
                return null;
            }
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
            authorities.add(grantedAuthority);
            User user = new User(username, seller.getPassword(), authorities);
            return user;

        }
    }
}
