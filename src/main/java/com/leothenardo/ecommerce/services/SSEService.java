package com.leothenardo.ecommerce.services;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SSEService {
	private static final Logger log = LoggerFactory.getLogger(SSEService.class);
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	public SseEmitter addSubscriber(String id) {
		SseEmitter emitter = new SseEmitter();
		this.emitters.put(id, emitter);
		return emitter;
	}

	public void removeSubscriber(String id) {
		this.emitters.remove(id);
	}

	public void sendToSubscriber(String id, Object data) {
		SseEmitter emitter = this.emitters.get(id);
		try {
			SseEmitter.SseEventBuilder builder = SseEmitter.event()
							.id(id)
							.name("message") // default
							.data(data);
			emitter.send(builder);

		} catch (ClientAbortException e) { // quando o cliente fecha a aba do navegador/conexão
			log.warn("Client closed connection");
			removeSubscriber(id);

		} catch (Exception e) {
			log.error("Error sending alert to client via SSE " + id, e);
			removeSubscriber(id);
		}
	}

}
