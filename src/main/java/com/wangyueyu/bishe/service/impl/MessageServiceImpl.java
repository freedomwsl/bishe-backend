package com.wangyueyu.bishe.service.impl;

import com.wangyueyu.bishe.entity.Message;
import com.wangyueyu.bishe.mapper.MessageMapper;
import com.wangyueyu.bishe.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyueyu
 * @since 2021-01-11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
