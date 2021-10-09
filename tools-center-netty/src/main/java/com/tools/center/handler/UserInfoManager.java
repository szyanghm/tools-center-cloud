package com.tools.center.handler;


import com.tools.center.entty.ChatProto;
import com.tools.center.entty.UserInfo;
import com.tools.center.entty.UserInfoVO;
import com.tools.center.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Channel的管理器
 */
@Slf4j
public class UserInfoManager {

    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();

    private static AtomicInteger userCount = new AtomicInteger(0);

    public static void addChannel(Channel channel) {
        String remoteAddr = NettyUtil.parseChannelRemoteAddr(channel);
        if (!channel.isActive()) {
            log.error("channel is not active, address: {}", remoteAddr);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAddr(remoteAddr);
        userInfo.setChannel(channel);
        userInfo.setTime(System.currentTimeMillis());
        userInfos.put(channel, userInfo);
    }

    public static boolean saveUser(Channel channel, UserInfoVO vo) {
        UserInfo userInfo = userInfos.get(channel);
        if (userInfo == null) {
            return false;
        }
        if (!channel.isActive()) {
            log.error("channel is not active, address: {}, nick: {}", userInfo.getAddr(), vo.getNickName());
            return false;
        }
        // 增加一个认证用户
        userCount.incrementAndGet();
        userInfo.setNickName(vo.getNickName());
        userInfo.setAuth(true);
        userInfo.setUserId(vo.getId());
        userInfo.setAvatarUrl(vo.getAvatarUrl());
        userInfo.setTime(System.currentTimeMillis());
        return true;
    }

    /**
     * 从缓存中移除Channel，并且关闭Channel
     *
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        try {
            log.warn("channel will be remove, address is :{}", NettyUtil.parseChannelRemoteAddr(channel));
            rwLock.writeLock().lock();
            channel.close();
            UserInfo userInfo = userInfos.get(channel);
            if (userInfo != null) {
                UserInfo tmp = userInfos.remove(channel);
                if (tmp != null) {
                    // 减去一个认证用户
                    userCount.decrementAndGet();
                }
            }
        } finally {
            rwLock.writeLock().unlock();
        }

    }

    /**
     * 广播普通消息
     *
     * @param message
     */
    public static void broadcastMess(String id,String uid, String nick, String avatarUrl,String message) {
        if (!StringUtils.isBlank(message)) {
            try {
                rwLock.readLock().lock();
                Set<Channel> keySet = userInfos.keySet();
                log.info("用户数量:"+keySet.size());
                for (Channel ch : keySet) {
                    UserInfo userInfo = userInfos.get(ch);
                    if (userInfo == null || !userInfo.isAuth()) continue;
                    ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildMessProto(id,uid, nick,avatarUrl, message)));
                }
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    /**
     * 广播系统消息
     */
    public static void broadCastInfo(int code, Object mess) {
        try {
            rwLock.readLock().lock();
            Set<Channel> keySet = userInfos.keySet();
            for (Channel ch : keySet) {
                UserInfo userInfo = userInfos.get(ch);
                if (userInfo == null || !userInfo.isAuth()) continue;
                ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildSystProto(code, mess)));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public static void broadCastPing() {
        try {
            rwLock.readLock().lock();
            log.info("broadCastPing userCount: {}", userCount.intValue());
            Set<Channel> keySet = userInfos.keySet();
            for (Channel ch : keySet) {
                UserInfo userInfo = userInfos.get(ch);
                if (userInfo == null || !userInfo.isAuth()) continue;
                ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildPingProto()));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 发送系统消息
     *
     * @param code
     * @param mess
     */
    public static void sendInfo(Channel channel, int code, Object mess) {
        channel.writeAndFlush(new TextWebSocketFrame(ChatProto.buildSystProto(code, mess)));
    }

    public static void sendPong(Channel channel) {
        channel.writeAndFlush(new TextWebSocketFrame(ChatProto.buildPongProto()));
    }

    /**
     * 扫描并关闭失效的Channel
     */
    public static void scanNotActiveChannel() {
        Set<Channel> keySet = userInfos.keySet();
        for (Channel ch : keySet) {
            UserInfo userInfo = userInfos.get(ch);
            if (userInfo == null) continue;
            log.info("open:{},active:{}",ch.isOpen(),ch.isActive());
            if (!ch.isOpen() || !ch.isActive() || (!userInfo.isAuth() &&
                    (System.currentTimeMillis() - userInfo.getTime()) > 10000)) {
                log.info("删除失效连接");
                removeChannel(ch);
            }
        }
    }


    public static UserInfo getUserInfo(Channel channel) {
        return userInfos.get(channel);
    }

    public static ConcurrentMap<Channel, UserInfo> getUserInfos() {
        return userInfos;
    }

    public static int getAuthUserCount() {
        return userCount.get();
    }

    public static void updateUserTime(Channel channel) {
        UserInfo userInfo = getUserInfo(channel);
        if (userInfo != null) {
            userInfo.setTime(System.currentTimeMillis());
        }
    }
}
