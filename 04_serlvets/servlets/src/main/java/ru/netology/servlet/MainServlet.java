package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String API_PATH = "/api/posts";
  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_DELETE = "DELETE";

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(METHOD_GET) && path.equals(API_PATH)) {
        controller.all(resp);
        return;
      }
      if (method.equals(METHOD_GET) && path.matches(API_PATH + "/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(METHOD_POST) && path.equals(API_PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(METHOD_DELETE) && path.matches(API_PATH + "/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

