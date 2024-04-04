package com.tune_fun.v1.account.adapter.output.persistence.oauth2;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
class OAuth2AuthorizationRequestPersistenceAdapterTest {
    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#loadAuthorizationRequest(HttpServletRequest)}
     */
    @Test
    void testLoadAuthorizationRequest() {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();

        // Act and Assert
        assertNull(
                OAuth2AuthorizationRequestPersistenceAdapter.loadAuthorizationRequest(new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#loadAuthorizationRequest(HttpServletRequest)}
     */
    @Test
    void testLoadAuthorizationRequest2() {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Name", "https://example.org/example"));

        // Act and Assert
        assertNull(OAuth2AuthorizationRequestPersistenceAdapter.loadAuthorizationRequest(request));
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#saveAuthorizationRequest(OAuth2AuthorizationRequest, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testSaveAuthorizationRequest() throws IOException {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();

        // Act
        OAuth2AuthorizationRequestPersistenceAdapter.saveAuthorizationRequest(null, request, response);

        // Assert that nothing has changed
        HttpServletResponse response2 = response.getResponse();
        assertInstanceOf(ResponseFacade.class, response2);
        assertInstanceOf(DelegatingServletInputStream.class, request.getInputStream());
        assertInstanceOf(MockHttpServletMapping.class, request.getHttpServletMapping());
        assertInstanceOf(MockHttpSession.class, request.getSession());
        assertEquals("", request.getContextPath());
        assertEquals("", request.getMethod());
        assertEquals("", request.getRequestURI());
        assertEquals("", request.getServletPath());
        assertEquals("HTTP/1.1", request.getProtocol());
        assertEquals("http", request.getScheme());
        assertEquals("localhost", request.getLocalName());
        assertEquals("localhost", request.getRemoteHost());
        assertEquals("localhost", request.getServerName());
        assertEquals(80, request.getLocalPort());
        assertEquals(80, request.getRemotePort());
        assertEquals(80, request.getServerPort());
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
        assertFalse(request.isAsyncStarted());
        assertFalse(request.isAsyncSupported());
        assertFalse(request.isRequestedSessionIdFromURL());
        assertTrue(request.isActive());
        assertTrue(request.isRequestedSessionIdFromCookie());
        assertTrue(request.isRequestedSessionIdValid());
        ServletOutputStream expectedOutputStream = response.getOutputStream();
        assertSame(expectedOutputStream, response2.getOutputStream());
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#saveAuthorizationRequest(OAuth2AuthorizationRequest, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testSaveAuthorizationRequest2() throws IOException {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("Name", "https://example.org/example");

        request.setCookies(cookie);
        Response response = new Response();

        // Act
        OAuth2AuthorizationRequestPersistenceAdapter.saveAuthorizationRequest(null, request, response);

        // Assert that nothing has changed
        HttpServletResponse response2 = response.getResponse();
        assertInstanceOf(ResponseFacade.class, response2);
        assertInstanceOf(DelegatingServletInputStream.class, request.getInputStream());
        assertInstanceOf(MockHttpServletMapping.class, request.getHttpServletMapping());
        assertInstanceOf(MockHttpSession.class, request.getSession());
        assertEquals("", request.getContextPath());
        assertEquals("", request.getMethod());
        assertEquals("", request.getRequestURI());
        assertEquals("", request.getServletPath());
        assertEquals("HTTP/1.1", request.getProtocol());
        assertEquals("http", request.getScheme());
        assertEquals("localhost", request.getLocalName());
        assertEquals("localhost", request.getRemoteHost());
        assertEquals("localhost", request.getServerName());
        assertEquals(80, request.getLocalPort());
        assertEquals(80, request.getRemotePort());
        assertEquals(80, request.getServerPort());
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
        assertFalse(request.isAsyncStarted());
        assertFalse(request.isAsyncSupported());
        assertFalse(request.isRequestedSessionIdFromURL());
        assertTrue(request.isActive());
        assertTrue(request.isRequestedSessionIdFromCookie());
        assertTrue(request.isRequestedSessionIdValid());
        assertSame(cookie, request.getCookies()[0]);
        ServletOutputStream expectedOutputStream = response.getOutputStream();
        assertSame(expectedOutputStream, response2.getOutputStream());
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#removeAuthorizationRequest(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemoveAuthorizationRequest() {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act and Assert
        assertNull(
                OAuth2AuthorizationRequestPersistenceAdapter.removeAuthorizationRequest(request, new Response()));
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#removeAuthorizationRequest(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemoveAuthorizationRequest2() {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Name", "https://example.org/example"));

        // Act and Assert
        assertNull(
                OAuth2AuthorizationRequestPersistenceAdapter.removeAuthorizationRequest(request, new Response()));
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#delete(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemove() throws IOException {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();

        // Act
        OAuth2AuthorizationRequestPersistenceAdapter.delete(request, response);

        // Assert that nothing has changed
        HttpServletResponse response2 = response.getResponse();
        assertInstanceOf(ResponseFacade.class, response2);
        assertInstanceOf(DelegatingServletInputStream.class, request.getInputStream());
        assertInstanceOf(MockHttpServletMapping.class, request.getHttpServletMapping());
        assertInstanceOf(MockHttpSession.class, request.getSession());
        assertEquals("", request.getContextPath());
        assertEquals("", request.getMethod());
        assertEquals("", request.getRequestURI());
        assertEquals("", request.getServletPath());
        assertEquals("HTTP/1.1", request.getProtocol());
        assertEquals("http", request.getScheme());
        assertEquals("localhost", request.getLocalName());
        assertEquals("localhost", request.getRemoteHost());
        assertEquals("localhost", request.getServerName());
        assertEquals(80, request.getLocalPort());
        assertEquals(80, request.getRemotePort());
        assertEquals(80, request.getServerPort());
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
        assertFalse(request.isAsyncStarted());
        assertFalse(request.isAsyncSupported());
        assertFalse(request.isRequestedSessionIdFromURL());
        assertTrue(request.isActive());
        assertTrue(request.isRequestedSessionIdFromCookie());
        assertTrue(request.isRequestedSessionIdValid());
        ServletOutputStream expectedOutputStream = response.getOutputStream();
        assertSame(expectedOutputStream, response2.getOutputStream());
    }

    /**
     * Method under test:
     * {@link OAuth2AuthorizationRequestPersistenceAdapter#delete(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemove2() throws IOException {
        // Arrange
        OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter = new OAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("Name", "https://example.org/example");

        request.setCookies(cookie);
        Response response = new Response();

        // Act
        OAuth2AuthorizationRequestPersistenceAdapter.delete(request, response);

        // Assert that nothing has changed
        HttpServletResponse response2 = response.getResponse();
        assertInstanceOf(ResponseFacade.class, response2);
        assertInstanceOf(DelegatingServletInputStream.class, request.getInputStream());
        assertInstanceOf(MockHttpServletMapping.class, request.getHttpServletMapping());
        assertInstanceOf(MockHttpSession.class, request.getSession());
        assertEquals("", request.getContextPath());
        assertEquals("", request.getMethod());
        assertEquals("", request.getRequestURI());
        assertEquals("", request.getServletPath());
        assertEquals("HTTP/1.1", request.getProtocol());
        assertEquals("http", request.getScheme());
        assertEquals("localhost", request.getLocalName());
        assertEquals("localhost", request.getRemoteHost());
        assertEquals("localhost", request.getServerName());
        assertEquals(80, request.getLocalPort());
        assertEquals(80, request.getRemotePort());
        assertEquals(80, request.getServerPort());
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
        assertFalse(request.isAsyncStarted());
        assertFalse(request.isAsyncSupported());
        assertFalse(request.isRequestedSessionIdFromURL());
        assertTrue(request.isActive());
        assertTrue(request.isRequestedSessionIdFromCookie());
        assertTrue(request.isRequestedSessionIdValid());
        assertSame(cookie, request.getCookies()[0]);
        ServletOutputStream expectedOutputStream = response.getOutputStream();
        assertSame(expectedOutputStream, response2.getOutputStream());
    }
}
