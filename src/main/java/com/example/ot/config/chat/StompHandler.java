package com.example.ot.config.chat;

import com.example.ot.config.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 웹소켓 연결 시
            String jwtToken = jwtUtils.extractJwt(accessor);
            if(jwtUtils.verify(jwtToken)) {
                String memberId = jwtUtils.getMemberIdFromToken(jwtToken);
                accessor.getSessionAttributes().put("memberId", memberId);
            }
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            // 메시지 전송 시
            String memberId = (String) accessor.getSessionAttributes().get("memberId");
            if (memberId != null) {
                accessor.setHeader("memberId", memberId);
            }
        }
        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }
}
