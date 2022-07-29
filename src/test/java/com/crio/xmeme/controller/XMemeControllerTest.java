package com.crio.xmeme.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.crio.xmeme.XMemeApp;
import com.crio.xmeme.DTO.MemeDto;
import com.crio.xmeme.exchanges.postRequestDto;
import com.crio.xmeme.exchanges.postResponseDto;
import com.crio.xmeme.service.XMemeService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(classes = {XMemeApp.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class XMemeControllerTest {

    public static final String MEME_ENDPOINT = "/memes";

    @MockBean
    private XMemeService xmemeService;

    @InjectMocks
    private XMemeController xmemeController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void wronglySpelledPostMemeTest() throws Exception {
        // misspelled name
        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

        String request1 = "{\"nam\": \"Dummy1\", \"url\": \"https://www.google.co/\""
                + ", \"caption\": \"Dummy1/\"}";
        MockHttpServletResponse response1 = mvc.perform(post(uri.toString())
                        .content(request1)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response1.getStatus());

        // misspelled url
        String request2 = "{\"name\": \"Dummy1\", \"ul\": \"https://www.google.co/\""
                + ", \"caption\": \"Dummy1/\"}";
        MockHttpServletResponse response2 = mvc
                .perform(post(uri.toString()).content(request2)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatus());

        // misspelled caption
        String request3 = "{\"name\": \"Dummy1\", \"url\": \"https://www.google.co/\""
                + ", \"capton\": \"Dummy1/\"}";
        MockHttpServletResponse response3 = mvc
                .perform(post(uri.toString()).content(request3)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatus());

    }

    @Test
    public void missingParamsPostMemeTest() throws Exception {
        // missing name
        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

        String request1 = "{\"url\": \"https://www.google.co/\""
                + ", \"caption\": \"Dummy1/\"}";
        MockHttpServletResponse response1 = mvc
                .perform(post(uri.toString()).content(request1)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response1.getStatus());

        // missing url
        String request2 = "{\"name\": \"Dummy1\"" + ", \"caption\": \"Dummy1/\"}";
        MockHttpServletResponse response2 = mvc
                .perform(post(uri.toString()).content(request2)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatus());

        // missing caption
        String request3 = "{\"name\": \"Dummy1\", \"url\": \"https://www.google.co/\"}";
        MockHttpServletResponse response3 = mvc
                .perform(post(uri.toString()).content(request3)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatus());

    }

    @Test
    public void nullParamsPostMemeTest() throws Exception {

        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

        // null name
        String request1 = "{\"name\": null, \"url\": \"https://www.google.co/\""
                + ", \"caption\": \"Dummy1/\"}";
        MockHttpServletResponse response1 = mvc
                .perform(post(uri.toString()).content(request1)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response1.getStatus());

        // null url
        String request2 = "{\"name\": \"Dummy1\", \"url\": null"
                + ", \"caption\": \"Dummy1/\"}";
        MockHttpServletResponse response2 = mvc
                .perform(post(uri.toString()).content(request2)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatus());

        // null caption
        String request3 = "{\"name\": \"Dummy1\", \"url\": \"https://www.google.co/\""
                + ", \"caption\": null}";
        MockHttpServletResponse response3 = mvc
                .perform(post(uri.toString()).content(request3)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatus());

        // All null
        String request4 = "{\"name\": null, \"url\": null" + ", \"caption\": null}";
        MockHttpServletResponse response4 = mvc
                .perform(post(uri.toString()).content(request4)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response4.getStatus());

    }

    @Test
    public void postMemeSuccessTest() throws Exception {

        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

        String reqBody = "{\"name\": \"Pradeep\", \"url\": \"https://www.google.com/\""
                + ", \"caption\": \"Party/\"}";

        postResponseDto postResponseDto = new postResponseDto("1");

        when(xmemeService.saveAndReturnId(any(postRequestDto.class)))
                .thenReturn(postResponseDto);
        MockHttpServletResponse response = mvc
                .perform(post(uri.toString()).content(reqBody)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        verify(xmemeService, times(1)).saveAndReturnId(any(postRequestDto.class));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"id\":\"" + postResponseDto.getId() + "\"}",
                response.getContentAsString());

    }

    @Test
    public void postDuplicateMemeTest() throws Exception {

        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

        String reqBody = "{\"name\": \"Pradeep\", \"url\": \"https://www.google.com/\""
                + ", \"caption\": \"Party/\"}";
        when(xmemeService.saveAndReturnId(any(postRequestDto.class))).thenReturn(null);
        when(xmemeService.isPresent(any(postRequestDto.class))).thenReturn(true);

        MockHttpServletResponse response = mvc
                .perform(post(uri.toString()).content(reqBody)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        verify(xmemeService, times(1)).isPresent(any(postRequestDto.class));
        verify(xmemeService, times(0)).saveAndReturnId(any(postRequestDto.class));

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    public void getMemeSuccessTest() throws Exception {
        MemeDto meme = new MemeDto("1", "kshas", "https://www.google.com", "Party");
        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT + "/" + meme.getId()).build()
                .toUri();

        when(xmemeService.getMemeById(eq(meme.getId()))).thenReturn(meme);

        MockHttpServletResponse response =
                mvc.perform(get(uri.toString()).accept(APPLICATION_JSON_VALUE))
                        .andReturn().getResponse();

        verify(xmemeService, times(1)).getMemeById(eq(meme.getId()));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(meme.getId()));
    }

    @Test
    public void getMemeNotFoundTest() throws Exception {
        URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT + "/" + "123").build()
                .toUri();

        when(xmemeService.getMemeById(anyString())).thenReturn(null);

        MockHttpServletResponse response =
                mvc.perform(get(uri.toString()).accept(APPLICATION_JSON_VALUE))
                        .andReturn().getResponse();
        verify(xmemeService, times(1)).getMemeById(anyString());

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }


}
