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
class HttpCookieOAuth2AuthorizationRequestPersistenceAdapterTest {
    /**
     * Method under test:
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#loadAuthorizationRequest(HttpServletRequest)}
     */
    @Test
    void testLoadAuthorizationRequest() {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();

        // Act and Assert
        assertNull(
                httpCookieOAuth2AuthorizationRequestPersistenceAdapter.loadAuthorizationRequest(new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#loadAuthorizationRequest(HttpServletRequest)}
     */
    @Test
    void testLoadAuthorizationRequest2() {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Name", "https://example.org/example"));

        // Act and Assert
        assertNull(httpCookieOAuth2AuthorizationRequestPersistenceAdapter.loadAuthorizationRequest(request));
    }

    /**
     * Method under test:
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#saveAuthorizationRequest(OAuth2AuthorizationRequest, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testSaveAuthorizationRequest() throws IOException {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();

        // Act
        httpCookieOAuth2AuthorizationRequestPersistenceAdapter.saveAuthorizationRequest(null, request, response);

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
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#saveAuthorizationRequest(OAuth2AuthorizationRequest, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testSaveAuthorizationRequest2() throws IOException {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("Name", "https://example.org/example");

        request.setCookies(cookie);
        Response response = new Response();

        // Act
        httpCookieOAuth2AuthorizationRequestPersistenceAdapter.saveAuthorizationRequest(null, request, response);

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
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#removeAuthorizationRequest(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemoveAuthorizationRequest() {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act and Assert
        assertNull(
                httpCookieOAuth2AuthorizationRequestPersistenceAdapter.removeAuthorizationRequest(request, new Response()));
    }

    /**
     * Method under test:
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#removeAuthorizationRequest(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemoveAuthorizationRequest2() {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Name", "https://example.org/example"));

        // Act and Assert
        assertNull(
                httpCookieOAuth2AuthorizationRequestPersistenceAdapter.removeAuthorizationRequest(request, new Response()));
    }

    /**
     * Method under test:
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#remove(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemove() throws IOException {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();

        // Act
        httpCookieOAuth2AuthorizationRequestPersistenceAdapter.remove(request, response);

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
     * {@link HttpCookieOAuth2AuthorizationRequestPersistenceAdapter#remove(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRemove2() throws IOException {
        // Arrange
        HttpCookieOAuth2AuthorizationRequestPersistenceAdapter httpCookieOAuth2AuthorizationRequestPersistenceAdapter = new HttpCookieOAuth2AuthorizationRequestPersistenceAdapter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("Name", "https://example.org/example");

        request.setCookies(cookie);
        Response response = new Response();

        // Act
        httpCookieOAuth2AuthorizationRequestPersistenceAdapter.remove(request, response);

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
