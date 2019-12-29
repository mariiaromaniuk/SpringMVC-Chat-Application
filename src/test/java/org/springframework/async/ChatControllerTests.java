package org.springframework.async.chat;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

public class ChatControllerTests {

	private MockMvc mockMvc;

	private ChatRepository chatRepository;

	@Before
	public void setup() {
		this.chatRepository = EasyMock.createMock(ChatRepository.class);
		this.mockMvc = standaloneSetup(new ChatController(this.chatRepository)).build();
	}

	@Test
	public void getMessages() throws Exception {
		List<String> messages = Arrays.asList("a", "b", "c");
		expect(this.chatRepository.getMessages(9)).andReturn(messages);
		replay(this.chatRepository);

		this.mockMvc.perform(get("/mvc/chat").param("messageIndex", "9"))
				.andExpect(status().isOk())
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult(messages));

		verify(this.chatRepository);
	}

	@Test
	public void getMessagesStartAsync() throws Exception {
		expect(this.chatRepository.getMessages(9)).andReturn(Arrays.<String>asList());
		replay(this.chatRepository);

		this.mockMvc.perform(get("/mvc/chat").param("messageIndex", "9"))
				.andExpect(request().asyncStarted());

		verify(this.chatRepository);
	}

}
