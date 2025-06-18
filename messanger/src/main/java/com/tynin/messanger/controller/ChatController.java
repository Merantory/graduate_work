package com.tynin.messanger.controller;

import com.tynin.messanger.model.Message;
import com.tynin.messanger.model.User;
import com.tynin.messanger.service.ChatRoomService;
import com.tynin.messanger.service.MessageService;
import com.tynin.messanger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ChatController {
	private final MessageService messageService;
	private final ChatRoomService chatRoomService;
	private final UserService userService;

	@Autowired
	public ChatController(MessageService messageService, ChatRoomService chatRoomService, UserService userService) {
		this.messageService = messageService;
		this.chatRoomService = chatRoomService;
		this.userService = userService;
	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload Map<String, String> payload) {
		String senderUsername = payload.get("sender");
		String content = payload.get("content");
		String receiverUsername = payload.get("receiver");

		Optional<User> senderOpt = userService.findByName(senderUsername);
		Optional<User> receiverOpt = userService.findByName(receiverUsername);

		if (senderOpt.isPresent() && receiverOpt.isPresent()) {
			User sender = senderOpt.get();
			User receiver = receiverOpt.get();
			messageService.saveMessage(content, sender, receiver);
		}
	}

	@MessageMapping("/chat.addUser")
	public void addUser(SimpMessageHeaderAccessor headerAccessor, @Payload Map<String, String> payload) {
		headerAccessor.getSessionAttributes().put("username", payload.get("username"));
	}

	@GetMapping("/api/messages/{chat_room_id}")
	public ResponseEntity<List<Message>> getChatHistory(@PathVariable("chat_room_id") Long chatRoomId) {
		var messages = messageService.getChatHistory(chatRoomId);
		return ResponseEntity.ok(messages);
	}

	@GetMapping("/api/chat")
	public ResponseEntity<Long> getChatRoomIdForUsers(@RequestParam("first") Long firstUserId,
													  @RequestParam("second") Long secondUserId) {
		var chatOptional = chatRoomService.findByIds(firstUserId, secondUserId);
		if (chatOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(chatOptional.get().getId());
	}

	@PostMapping("/api/chat")
	public ResponseEntity<Long> createChatRoomForUsers(@RequestParam("first") Long firstUserId,
													   @RequestParam("second") Long secondUserId) {
		var chatOptional = chatRoomService.save(firstUserId, secondUserId);
		return ResponseEntity.ok(chatOptional.get().getId());
	}
}