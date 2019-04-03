package com.pyg.user.service;

import com.pyg.pojo.TbUser;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/28  9:40
 * @description TODO
 **/
public interface UserService {

    void sendCode(String phone);

    void register(TbUser user, String code);
}
