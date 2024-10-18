package com.example.silkroad.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.example.silkroad.utils.ThymeLeafUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet(name = "HomeController", value = "/sum")
public class HomeController extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ThymeLeafUtil thymeleafUtil = new ThymeLeafUtil(getServletContext());
        templateEngine = ThymeLeafUtil.templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("message", "Hello from Thymeleaf in JEE!");

        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process("home", ctx, resp.getWriter());
    }
}
