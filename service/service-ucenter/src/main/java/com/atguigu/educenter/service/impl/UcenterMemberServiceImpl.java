package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.vo.LoginVo;
import com.atguigu.educenter.vo.RegisterVo;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-09-29
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

   @Autowired
   private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(LoginVo loginVo) {
//        判断用户输入是否为空
        if (StringUtils.isEmpty(loginVo.getMobile()) || StringUtils.isEmpty(loginVo.getPassword())){
            throw new GuliException(ResultCodeEnum.NOTWORK_ERROR);
        }
//        构建查询条件
        UcenterMember member = new UcenterMember();
        BeanUtils.copyProperties(loginVo,member);
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",loginVo.getMobile());
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
//        验证账号
        if (StringUtils.isEmpty(ucenterMember)){
            throw new GuliException(ResultCodeEnum.NOTWORK_ERROR);
        }
//        密码加密在验证
        if (!MD5.encrypt(loginVo.getPassword()).equals(ucenterMember.getPassword())){
            throw new GuliException(ResultCodeEnum.PWD_ERROR);
        }
        if (ucenterMember.getIsDisabled()){
            throw new GuliException(ResultCodeEnum.USER_LOCK);
        }
//      生成token
        String token = JwtUtils.createJwtToken(ucenterMember.getId(), ucenterMember .getNickname());

        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
//        获取数据
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

//        验证参数
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)){
            throw new GuliException(ResultCodeEnum.USERINFO_ERROR);
        }

//        判断用户是否已经注册
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 1){
            throw new GuliException(ResultCodeEnum.IS_MOBILE);
        }

//        校验验证码
        String verification =  redisTemplate.opsForValue().get(mobile);
         if (StringUtils.isEmpty(verification) || !verification.equals(code)){
            throw new GuliException(ResultCodeEnum.VERIFICATION_ERROR);
        }

//        注册账号
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setIsDisabled(false);
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS 4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        baseMapper.insert(ucenterMember);
    }

    @Override
    public UcenterMember getMemberId(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }
}
