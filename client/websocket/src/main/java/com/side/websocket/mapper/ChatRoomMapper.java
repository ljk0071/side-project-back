package com.side.websocket.mapper;

import com.side.websocket.dto.ChatRoomResponseDto;
import com.side.websocket.model.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChatRoomMapper {

    ChatRoomMapper ChatRoomMapper = Mappers.getMapper(ChatRoomMapper.class);

    ChatRoomResponseDto toResponse(ChatRoom chatRoom);

    List<ChatRoomResponseDto> toResponseList(List<ChatRoom> chatRooms);
}