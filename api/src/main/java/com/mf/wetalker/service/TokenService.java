package com.mf.wetalker.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mf.wetalker.infra.auth.WeChatJwtTokenHolder;
import com.mf.wetalker.infra.auth.WechatAuthObject;
import com.mf.wetalker.infra.db.entity.WeTalkUser;
import com.mf.wetalker.infra.db.mapper.UserMapper;
import com.mf.wetalker.infra.wechat.WeChatHelper;
import com.mf.wetalker.model.WeChatLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final UserMapper userMapper;
    private final WeChatHelper weChatHelper;
    private final WeChatJwtTokenHolder weChatJwtTokenGenerator;

    public String login(WeChatLoginRequest request) {

        WechatAuthObject authObject = weChatHelper.login(request.getCode());
        WeTalkUser weTalkUser = saveWeTalkUser(request, authObject);
        String token = weChatJwtTokenGenerator.generate(weTalkUser.getId());
        return token;
    }

    @NotNull
    private WeTalkUser saveWeTalkUser(WeChatLoginRequest request, WechatAuthObject authObject) {
        WeTalkUser weTalkUser = WeTalkUser.builder()
                .wechatId(authObject.getOpenid())
                .build();
        List<WeTalkUser> weTalkUsers = userMapper.selectList(Wrappers.<WeTalkUser>lambdaQuery()
                .eq(WeTalkUser::getWechatId, weTalkUser.getWechatId()));

        if (weTalkUsers == null || weTalkUsers.isEmpty()) {
            weTalkUser.setUserName("游客"+ RandomUtil.getPositiveInt());
            weTalkUser.setIcon("https://wetalk-1259114324.cos.ap-chengdu.myqcloud.com/user.png");
            userMapper.insert(weTalkUser);
        } else {
            WeTalkUser weTalkUser1 = weTalkUsers.get(0);
            weTalkUser.setId(weTalkUser1.getId());
            weTalkUser.setUserName(weTalkUser1.getUserName());
            weTalkUser.setIcon(weTalkUser1.getIcon());
            userMapper.updateById(weTalkUser);
        }
        return weTalkUser;
    }

    public WeTalkUser getProfile(BigInteger userId) {
         return userMapper.selectById(userId);
    }
}
